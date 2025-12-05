import React, { useEffect, useState } from "react";
import { Explorer } from "../explorer/Explorer";
import {
    fetchFiles,
    fetchFolders,
    type FileDto,
    type FolderDto,
    restoreFile,
    restoreFolder,
    purgeFolder,
    purgeFile
} from "../../core/services/api";

import {FileIcon, FolderIcon} from "lucide-react";

export const Desktop: React.FC = () => {
    // --- États globaux ---
    const [files, setFiles] = useState<FileDto[]>([]);
    const [folders, setFolders] = useState<FolderDto[]>([]);

    const [explorerReloadKey, setExplorerReloadKey] = useState(0);
    const bumpExplorer = () => setExplorerReloadKey(k => k + 1);

    useEffect(() => {
        fetchFiles().then(setFiles);
        fetchFolders().then(setFolders);
    }, []);

    // --- Suppression (soft delete) ---
    const handleDeleteFolder = (id: number) => {
        setFolders((prev) =>
            prev.map((f) =>
                f.folderId === id ? { ...f, folderDeleted: true } : f
            )
        );
    };

    const handleDeleteFile = (id: number) => {
        setFiles((prev) =>
            prev.map((f) =>
                f.fileId === id ? { ...f, fileDeleted: true } : f
            )
        );
    };

    // --- Fenêtres ---
    const [isExplorerOpen, setIsExplorerOpen] = useState(true);
    const [isExplorerMin, setIsExplorerMin] = useState(false);
    const [isExplorerMax, setIsExplorerMax] = useState(false);
    const [hasOpenedFile, setHasOpenedFile] = useState(false);

    const [isTrashOpen, setIsTrashOpen] = useState(false);
    const [isTrashMin, setIsTrashMin] = useState(false);
    const [isTrashMax, setIsTrashMax] = useState(false);

    const bothOpen = isExplorerOpen && isTrashOpen;
    const bothMax = bothOpen && isExplorerMax && isTrashMax;
    const bothOpenBase = bothOpen && !isExplorerMax && !isTrashMax;

    const handleExplorerClick = () => {
        setIsExplorerOpen(true);
        setIsExplorerMin(false);
        setIsExplorerMax(false);
    };

    const handleTrashClick = () => {
        setIsTrashOpen(true);
        setIsTrashMin(false);
        setIsTrashMax(false);
    };

    const handleExplorerMax = () => {
        if (isExplorerOpen && isTrashOpen) {
            if (isExplorerMax && isTrashMax) {
                setIsExplorerMax(false);
                setIsTrashMax(false);
            } else {
                setIsExplorerMax(true);
                setIsTrashMax(true);
            }
        } else {
            setIsExplorerMax(!isExplorerMax);
        }
    };

    const handleTrashMax = () => {
        if (isExplorerOpen && isTrashOpen) {
            if (isExplorerMax && isTrashMax) {
                setIsExplorerMax(false);
                setIsTrashMax(false);
            } else {
                setIsExplorerMax(true);
                setIsTrashMax(true);
            }
        } else {
            setIsTrashMax(!isTrashMax);
        }
    };

    const handleRestoreFile = async (fileId: number) => {
        try {
            await restoreFile(fileId);
            setFiles((prev) => prev.filter((f) => f.fileId !== fileId));
            bumpExplorer();
        } catch {
            console.error("Impossible de restaurer le fichier");
        }
    };

    const handleRestoreFolder = async (folderId: number) => {
        try {
            await restoreFolder(folderId);
            setFolders((prev) => prev.filter((f) => f.folderId !== folderId));
            bumpExplorer();
        } catch {
            console.error("Impossible de restaurer le dossier");
        }
    };

    const handlePurgeFile = async (fileId: number) => {
        try {
            await purgeFile(fileId);
            setFiles((prev) => prev.filter((f) => f.fileId !== fileId));
            bumpExplorer();
        } catch {
            console.error("Impossible de supprimer définitivement le fichier");
        }
    };

    const handlePurgeFolder = async (folderId: number) => {
        try {
            await purgeFolder(folderId);
            setFolders((prev) => prev.filter((f) => f.folderId !== folderId));
            bumpExplorer();
        } catch {
            console.error("Impossible de supprimer définitivement le dossier");
        }
    };


    return (
        <div
            className="desktop"
            style={{ backgroundImage: "url('/wallpaper.jpg')" }}
        >
            {/* Icône Explorateur */}
            <div
                className="desktop-icon"
                onDoubleClick={handleExplorerClick}
                title="Ouvrir l'Explorateur"
            >
                <img src="/folder.png" alt="Explorateur" className="desktop-icon__img" />
                <span className="desktop-icon__label">Explorateur</span>
            </div>

            {/* Icône Corbeille */}
            <div
                className="desktop-icon"
                style={{ marginTop: "150px" }}
                onDoubleClick={handleTrashClick}
                title="Ouvrir la Corbeille"
            >
                <img
                    src="/recycle-bin.png"
                    alt="Corbeille"
                    className="desktop-icon__img"
                />
                <span className="desktop-icon__label">Corbeille</span>
            </div>

            {/* Fenêtre Explorateur */}
            {isExplorerOpen && (
                <div
                    className={`window ${bothOpenBase ? "window--split-left" : ""} ${
                        isExplorerMax ? "window--max" : ""
                    }`}
                    aria-label="Explorateur"
                    style={
                        isExplorerMax
                            ? bothMax
                                ? {
                                    width: "50%",
                                    position: "absolute",
                                    top: 0,
                                    bottom: 0,
                                    left: 0,
                                }
                                : {
                                    width: "100%",
                                    position: "absolute",
                                    top: 0,
                                    bottom: 0,
                                    left: 0,
                                }
                            : {}
                    }
                >
                    <div className="titlebar">
                        <span className="titlebar__title">Explorateur</span>
                        <div className="titlebar__controls">
                            <button
                                className={`winbtn winbtn--min ${
                                    hasOpenedFile ? "winbtn--disabled" : ""
                                }`}
                                aria-label="Réduire"
                                onClick={() => !hasOpenedFile && handleExplorerMax()}
                                disabled={hasOpenedFile}
                            />
                            <button
                                className={`winbtn winbtn--max ${
                                    hasOpenedFile ? "winbtn--disabled" : ""
                                }`}
                                aria-label="Agrandir"
                                onClick={() => !hasOpenedFile && handleExplorerMax()}
                                disabled={hasOpenedFile}
                            />
                            <button
                                className={`winbtn winbtn--close ${
                                    hasOpenedFile ? "winbtn--disabled" : ""
                                }`}
                                aria-label="Fermer"
                                onClick={() => !hasOpenedFile && setIsExplorerOpen(false)}
                                disabled={hasOpenedFile}
                            />
                        </div>
                    </div>

                    {!isExplorerMin && (
                        <div className="window__content">
                            <Explorer
                                folders={folders.filter((f) => !f.folderDeleted)}
                                files={files.filter((f) => !f.fileDeleted)}
                                onDeleteFolder={handleDeleteFolder}
                                onDeleteFile={handleDeleteFile}
                                onFileOpenChange={setHasOpenedFile}
                                reloadKey={explorerReloadKey}
                            />
                        </div>
                    )}
                </div>
            )}

            {/* Fenêtre Corbeille */}
            {isTrashOpen && (
                <div
                    className={`window ${bothOpenBase ? "window--split-right" : ""} ${
                        isTrashMax ? "window--max" : ""
                    }`}
                    aria-label="Corbeille"
                    style={
                        isTrashMax
                            ? bothMax
                                ? {
                                    width: "50%",
                                    position: "absolute",
                                    top: 0,
                                    bottom: 0,
                                    left: "50%",
                                }
                                : {
                                    width: "100%",
                                    position: "absolute",
                                    top: 0,
                                    bottom: 0,
                                    left: 0,
                                }
                            : {}
                    }
                >
                    <div className="titlebar">
                        <span className="titlebar__title">Corbeille</span>
                        <div className="titlebar__controls">
                            <button
                                className="winbtn winbtn--min"
                                aria-label="Réduire"
                                onClick={() => !hasOpenedFile && handleExplorerMax()}
                            />
                            <button
                                className="winbtn winbtn--max"
                                aria-label="Agrandir"
                                onClick={handleTrashMax}
                            />
                            <button
                                className="winbtn winbtn--close"
                                aria-label="Fermer"
                                onClick={() => setIsTrashOpen(false)}
                            />
                        </div>
                    </div>

                    {!isTrashMin && (
                        <div className="window__content flex items-center justify-center text-gray-500 italic">
                            <Trash
                                folders={folders.filter((f) => f.folderDeleted)}
                                files={files.filter((f) => f.fileDeleted)}
                                onRestoreFile={handleRestoreFile}
                                onRestoreFolder={handleRestoreFolder}
                                onPurgeFile={handlePurgeFile}
                                onPurgeFolder={handlePurgeFolder}
                            />
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

type CorbeilleProps = {
    folders: FolderDto[];
    files: FileDto[];
    onRestoreFolder: (id: number) => void;
    onRestoreFile: (id: number) => void;
    onPurgeFolder: (id: number) => void;
    onPurgeFile: (id: number) => void;
};


export const Trash: React.FC<CorbeilleProps> = ({
                                                    folders,
                                                    files,
                                                    onRestoreFolder,
                                                    onRestoreFile,
                                                    onPurgeFile,
                                                    onPurgeFolder
                                                }) => {
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [restoringFileIds, setRestoringFileIds] = useState<number[]>([]);
    const [restoringFolderIds, setRestoringFolderIds] = useState<number[]>([]);
    const [purgingFileIds, setPurgingFileIds] = useState<number[]>([]);
    const [purgingFolderIds, setPurgingFolderIds] = useState<number[]>([]);

    const [hiddenAfterPurgeFileIds, setHiddenAfterPurgeFileIds] = useState<number[]>([]);
    const [hiddenAfterPurgeFolderIds, setHiddenAfterPurgeFolderIds] = useState<number[]>([]);


    useEffect(() => {
        if (!errorMessage) return;
        const t = setTimeout(() => setErrorMessage(null), 2000);
        return () => clearTimeout(t);
    }, [errorMessage]);

    const handleRestoreFile = async (fileId: number) => {
        if (restoringFileIds.includes(fileId)) return;
        try {
            setRestoringFileIds((p) => [...p, fileId]);
            await restoreFile(fileId);
            onRestoreFile(fileId);
        } catch {
            setErrorMessage("Impossible de restaurer le fichier");
        } finally {
            setRestoringFileIds((p) => p.filter((id) => id !== fileId));
        }
    };

    const handleRestoreFolder = async (folderId: number) => {
        if (restoringFolderIds.includes(folderId)) return;
        try {
            setRestoringFolderIds((p) => [...p, folderId]);
            await restoreFolder(folderId);
            onRestoreFolder(folderId);
        } catch {
            setErrorMessage("Impossible de restaurer le dossier");
        } finally {
            setRestoringFolderIds((p) => p.filter((id) => id !== folderId));
        }
    };

    const VANISH_MS = 1;

    const handlePurgeFile = async (fileId: number) => {
        if (purgingFileIds.includes(fileId)) return;
        try {
            setPurgingFileIds(p => [...p, fileId]);
            setTimeout(() => {
                setHiddenAfterPurgeFileIds(p => p.includes(fileId) ? p : [...p, fileId]);
            }, VANISH_MS);

            await purgeFile(fileId);
            onPurgeFile(fileId);
        } catch {
            setErrorMessage("Impossible de supprimer définitivement le fichier");
        } finally {
            setPurgingFileIds(p => p.filter(id => id !== fileId));
        }
    };

    const handlePurgeFolder = async (folderId: number) => {
        if (purgingFolderIds.includes(folderId)) return;
        try {
            setPurgingFolderIds(p => [...p, folderId]);
            setTimeout(() => {
                setHiddenAfterPurgeFolderIds(p => p.includes(folderId) ? p : [...p, folderId]);
            }, VANISH_MS);

            await purgeFolder(folderId);
            onPurgeFolder(folderId);
        } catch {
            setErrorMessage("Impossible de supprimer définitivement le dossier");
        } finally {
            setPurgingFolderIds(p => p.filter(id => id !== folderId));
        }
    };

    return (
        <div className="explorer">
            {errorMessage && (
                <div className="mt-2 flex items-center gap-2 bg-red-100 border border-red-400 text-red-700 px-3 py-2 rounded-lg shadow-sm animate-pulse">
                    <span className="font-medium">❌</span>
                    <span>{errorMessage}</span>
                </div>
            )}

            <div className="explorer-grid">
                {/* Dossiers supprimés */}
                {folders
                    .filter(f => !hiddenAfterPurgeFolderIds.includes(f.folderId))
                    .map((folder) => {
                        const restoring = restoringFolderIds.includes(folder.folderId);
                        const purging = purgingFolderIds.includes(folder.folderId);
                        return (
                            <div
                                key={folder.folderId}
                                className={`tile tile--folder ${purging ? "purged" : ""}`}
                                title={`Nom : ${folder.folderName}`}
                            >
                            <div className="tile__icon">
                                <FolderIcon/>
                            </div>
                            <span className="tile__label">{folder.folderName}</span>

                            <button
                                className="btn btn--primary"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    void handleRestoreFolder(folder.folderId);
                                }}
                                disabled={restoring}
                            >
                                {restoring ? "Restauration…" : "Restaurer"}
                            </button>

                            <button
                                className="btn btn--danger"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    void handlePurgeFolder(folder.folderId);
                                }}
                                disabled={purging}
                            >
                                {purging ? "Suppression…" : "Supprimer"}
                            </button>
                        </div>
                    );
                })}

                {/* Fichiers supprimés */}
                {files
                    .filter(f => !hiddenAfterPurgeFileIds.includes(f.fileId))
                    .map((file) => {
                        const restoring = restoringFileIds.includes(file.fileId);
                        const purging = purgingFileIds.includes(file.fileId);
                        return (
                            <div
                                key={file.fileId}
                                className={`tile tile--file ${purging ? "purged" : ""}`}
                                title={`Nom : ${file.fileName}`}
                            >
                            <div className="tile__icon">
                                <FileIcon/>
                            </div>
                            <span className="tile__label">{file.fileName}</span>

                            <button
                                className="btn btn--primary"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    void handleRestoreFile(file.fileId);
                                }}
                                disabled={restoring}
                            >
                                {restoring ? "Restauration…" : "Restaurer"}
                            </button>

                            <button
                                className="btn btn--danger"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    void handlePurgeFile(file.fileId);
                                }}
                                disabled={purging}
                            >
                                {purging ? "Suppression…" : "Supprimer"}
                            </button>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};
