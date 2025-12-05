import React, { useEffect, useState } from "react";
import { useExplorer } from "../../core/hooks/useExplorer";
import { FolderIcon, FileIcon } from "../../shared/icons";
import { Breadcrumb } from "../../shared/ui/Breadcrumb";
import {
    resolveFolderPath,
    moveFolderToTrash,
    moveFileToTrash,
    type FolderDto,
    type FileDto, createFolder, createFile, updateFileContent, fetchFileById,
} from "../../core/services/api.ts";
import { Notification } from "../../shared/ui/Notification";

type ExplorerProps = {
    onFileOpenChange?: (isOpen: boolean) => void;
    folders: FolderDto[];
    files: FileDto[];
    onDeleteFolder: (id: number) => void;
    onDeleteFile: (id: number) => void;
    reloadKey?: number;
};

export const Explorer: React.FC<ExplorerProps> = ({onDeleteFolder, onDeleteFile, onFileOpenChange, reloadKey = 0}) => {
    // ---------- Navigation / erreurs ----------
    const [path, setPath] = useState<{ id: number | null; name: string }[]>([
        { id: null, name: "Racine" },
    ]);
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const [notification, setNotification] = useState<{ message: string; type: "success" | "error" } | null>(null);

    useEffect(() => {
        if (!errorMessage) return;
        const t = setTimeout(() => setErrorMessage(null), 2000);
        return () => clearTimeout(t);
    }, [errorMessage]);

    const [creatingFile, setCreatingFile] = useState(false);
    const [creatingFolder, setCreatingFolder] = useState(false);
    const [newName, setNewName] = useState("");


    // ---------- Fichier ouvert (preview) ----------
    const [openedFile, setOpenedFile] = useState<FileDto | null>(null);

    const currentFolder = path[path.length - 1].id ?? undefined;
    const { files, folders, loading, error, refresh } = useExplorer(currentFolder, reloadKey);

    const [editedContent, setEditedContent] = useState<string | null>(null);

    useEffect(() => {
        if (openedFile?.type === "TEXT") {
            setEditedContent(openedFile.content); // init avec le contenu
        }
    }, [openedFile]);

    // ---------- Cache local : masquer ce qui part à la corbeille ----------
    const [hiddenFileIds, setHiddenFileIds] = useState<number[]>([]);
    const [hiddenFolderIds, setHiddenFolderIds] = useState<number[]>([]);
    const [deletingFileIds, setDeletingFileIds] = useState<number[]>([]);
    const [deletingFolderIds, setDeletingFolderIds] = useState<number[]>([]);

    // Réagir uniquement aux fichiers
    useEffect(() => {
        setHiddenFileIds(prev => prev.filter(id => {
            const f = files.find(x => x.fileId === id);
            return !!(f && !f.fileDeleted);
        }));
    }, [files]);

    // Réagir uniquement aux dossiers
    useEffect(() => {
        setHiddenFolderIds(prev => prev.filter(id => {
            const d = folders.find(x => x.folderId === id);
            return !!(d && !d.folderDeleted);
        }));
    }, [folders]);

    const [tooltipData, setTooltipData] = useState<Record<number, FileDto>>({});

    const ensureTooltip = async (f: FileDto) => {
        if (f.type !== "IMAGE") return;
        if (tooltipData[f.fileId]) return;
        try {
            const data = await fetchFileById(f.fileId); // JSON brut de Swagger
            setTooltipData(prev => ({
                ...prev,
                [f.fileId]: data,
            }));
        } catch (e) {
            console.error("Erreur lors du fetch du fichier", e);
        }
    };

    const visibleFiles = files.filter((f) => !hiddenFileIds.includes(f.fileId));
    const visibleFolders = folders.filter(
        (d) => !hiddenFolderIds.includes(d.folderId)
    );

    // ---------- Actions ----------
    const handleOpenFolder = (folderId: number, folderName: string) => {
        setPath([...path, { id: folderId, name: folderName }]);
    };

    const handleNavigateBreadcrumb = (_id: number | null, index: number) => {
        setPath(path.slice(0, index + 1));
        setOpenedFile(null);
        onFileOpenChange?.(false);
    };

    const handleNavigateByPath = async (fullPath: string) => {
        const raw = fullPath.trim();

        if (raw === "Racine" || raw === "Racine/") {
            setPath([{ id: null, name: "Racine" }]);
            return;
        }

        try {
            const cleanPath = raw.startsWith("Racine/")
                ? raw.replace(/^Racine\//, "")
                : raw;

            const segments = cleanPath.split("/").filter(Boolean);
            const newPath: { id: number | null; name: string }[] = [
                { id: null, name: "Racine" },
            ];

            let parentPath = "";
            for (let i = 0; i < segments.length; i++) {
                parentPath = i === 0 ? segments[i] : `${parentPath}/${segments[i]}`;
                const id = await resolveFolderPath(parentPath);
                newPath.push({ id, name: segments[i] });
            }

            setPath(newPath);
        } catch {
            setErrorMessage("Chemin introuvable");
        }
    };

    const handleOpenFile = (file: FileDto) => {
        setOpenedFile(file);
        onFileOpenChange?.(true);
    };

    const handleCloseFile = () => {
        setOpenedFile(null);
        onFileOpenChange?.(false);
    };

    const handleDeleteFile = async (fileId: number) => {
        if (deletingFileIds.includes(fileId)) return;
        try {
            setDeletingFileIds((p) => [...p, fileId]);
            await moveFileToTrash(fileId);
            setHiddenFileIds((prev) => (prev.includes(fileId) ? prev : [...prev, fileId]));
            refresh();
        } catch {
            setErrorMessage("Impossible de déplacer le fichier dans la corbeille");
        } finally {
            setDeletingFileIds((p) => p.filter((id) => id !== fileId));
        }
    };

    const handleDeleteFolder = async (folderId: number) => {
        if (deletingFolderIds.includes(folderId)) return;
        try {
            setDeletingFolderIds((p) => [...p, folderId]);
            await moveFolderToTrash(folderId);
            setHiddenFolderIds((prev) => prev.includes(folderId) ? prev : [...prev, folderId]
            );
            refresh();
        } catch {
            setErrorMessage("Impossible de déplacer le dossier dans la corbeille");
        } finally {
            setDeletingFolderIds((p) => p.filter((id) => id !== folderId));
        }
    };

    const formatSize = (size: number) => {
        if (size < 1024) return `${size} o`;
        if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} Ko`;
        if (size < 1024 * 1024 * 1024) return `${(size / 1024 / 1024).toFixed(1)} Mo`;
        return `${(size / 1024 / 1024 / 1024).toFixed(1)} Go`;
    };
    
    // ---------- Rendu ----------
    if (loading) return <p className="explorer__loading">Chargement…</p>;
    if (error) return <p className="explorer__error">Erreur&nbsp;: {error}</p>;

    return (
        <div className="explorer">
            {/* Fil d’Ariane */}
            <div className="breadcrumb">
                <Breadcrumb
                    path={path}
                    onNavigate={handleNavigateBreadcrumb}
                    onNavigateByPath={handleNavigateByPath}
                />
            </div>

            {errorMessage && (
                <div
                    className="mt-2 flex items-center gap-2 bg-red-100 border border-red-400 text-red-700 px-3 py-2 rounded-lg shadow-sm animate-pulse">
                    <span className="font-medium">❌</span>
                    <span>{errorMessage}</span>
                </div>
            )}

            {notification && (
                <Notification
                    message={notification.message}
                    type={notification.type}
                    onClose={() => setNotification(null)}
                />
            )}


            {creatingFolder ? (
                <div className="flex items-center gap-2 mt-2">
                    <input
                        type="text"
                        value={newName}
                        onChange={(e) => setNewName(e.target.value)}
                        placeholder="Nom du dossier"
                        className="border px-2 py-1 rounded"
                        autoFocus
                    />
                    <button
                        className="btn btn--primary"
                        onClick={async () => {
                            if (!newName.trim()) return;
                            try {
                                await createFolder(newName.trim(), currentFolder ?? null);
                                refresh();
                                setNotification({ message: "Dossier créé ✅", type: "success" });
                            } catch {
                                setNotification({ message: "Erreur lors de la création du dossier ❌", type: "error" });
                            } finally {
                                setNewName("");
                                setCreatingFolder(false);
                            }
                        }}
                    >
                        ✔
                    </button>
                    <button
                        className="btn btn--secondary"
                        onClick={() => {
                            setCreatingFolder(false);
                            setNewName("");
                        }}
                    >
                        ✖
                    </button>
                </div>
            ) : (
                <button
                    className="btn btn--primary mt-2"
                    onClick={() => {
                        setCreatingFolder(true);
                        setNewName("");
                    }}
                >
                    + Nouveau dossier
                </button>
            )}


            {creatingFile ? (
                <div className="flex items-center gap-2 mt-2">
                    <input
                        type="text"
                        value={newName}
                        onChange={(e) => setNewName(e.target.value)}
                        placeholder="Nom du fichier"
                        className="border px-2 py-1 rounded"
                        autoFocus
                    />
                    <button
                        className="btn btn--primary"
                        onClick={async () => {
                            if (!newName.trim()) return;
                            try {
                                await createFile(newName.trim(), currentFolder ?? null, "");
                                refresh();
                                setNotification({ message: "Fichier créé ✅", type: "success" });
                            } catch {
                                setNotification({ message: "Erreur lors de la création du fichier ❌", type: "error" });
                            } finally {
                                setNewName("");
                                setCreatingFile(false);
                            }
                        }}
                    >
                        ✔
                    </button>
                    <button
                        className="btn btn--secondary"
                        onClick={() => {
                            setCreatingFile(false);
                            setNewName("");
                        }}
                    >
                        ✖
                    </button>
                </div>
            ) : (
                <button
                    className="btn btn--primary mt-2"
                    onClick={() => {
                        setCreatingFile(true);
                        setNewName("");
                    }}
                >
                    + Nouveau fichier
                </button>
            )}



            {/* Si un fichier est ouvert */}
            {openedFile ? (
                <div className="file-viewer">
                    <div className="file-viewer__overlay">
                        <div className="file-viewer__content">
                            <button
                                onClick={handleCloseFile}
                                className="file-viewer__close"
                                aria-label="Fermer"
                            >
                                ✖
                            </button>

                            {openedFile.type === "TEXT" ? (
                                <div className="file-viewer__editor">
                                    <textarea
                                        value={editedContent ?? ""}
                                        onChange={(e) => setEditedContent(e.target.value)}
                                        className="file-viewer__textarea"
                                    />

                                    <button
                                        className="file-viewer__save"
                                        onClick={async () => {
                                            try {
                                                await updateFileContent(openedFile.fileId, editedContent ?? "");
                                                setNotification({message: "Fichier enregistré ✅", type: "success"});
                                                refresh();
                                            } catch {
                                                setErrorMessage("Impossible d'enregistrer le fichier");
                                                setNotification({
                                                    message: "Erreur lors de l'enregistrement",
                                                    type: "error"
                                                });
                                            }
                                        }}
                                    >
                                        💾 Enregistrer
                                    </button>
                                </div>
                            ) : openedFile.type === "IMAGE" ? (
                                <img
                                    src={`${import.meta.env.VITE_API_URL}${openedFile.content}`}
                                    alt={openedFile.fileName}
                                    className="file-viewer__image"
                                />
                            ) : (
                                <p>Type de fichier non supporté.</p>
                            )}

                        </div>
                    </div>
                </div>
            ) : (
                // Grille dossiers/fichiers
                <div className="explorer-grid">
                    {/* Dossiers */}
                    {visibleFolders
                        .filter(folder => !folder.folderDeleted)
                        .map((folder) => {
                            const deleting = deletingFolderIds.includes(folder.folderId);
                            return (
                                <div
                                    key={folder.folderId}
                                    className="tile tile--folder"
                                    onDoubleClick={() =>
                                        handleOpenFolder(folder.folderId, folder.folderName)
                                    }
                                    title={`Nom : ${folder.folderName}\nNombre d'éléments : ${folder.itemCount}`}
                                    role="button"
                                >
                                    <div className="tile__icon">
                                        <FolderIcon/>
                                    </div>
                                    <span className="tile__label">{folder.folderName}</span>

                                    <button
                                        className="btn btn--danger"
                                        onClick={(e) => {
                                            e.stopPropagation(); // évite d'ouvrir le dossier
                                            void handleDeleteFolder(folder.folderId);
                                            onDeleteFolder(folder.folderId);
                                        }}
                                        disabled={deleting}
                                    >
                                        {deleting ? "Suppression…" : "Supprimer"}
                                    </button>
                                </div>
                            );
                        })}

                    {/* Fichiers */}
                    {visibleFiles
                        .filter(file => !file.fileDeleted)
                        .map((file) => {
                            const deleting = deletingFileIds.includes(file.fileId);
                            return (
                                <div
                                    key={file.fileId}
                                    className="tile tile--file"
                                    onDoubleClick={() => handleOpenFile(file)}
                                    onMouseEnter={() => ensureTooltip(file)}
                                    title={
                                        file.type === "IMAGE"
                                            ? tooltipData[file.fileId]
                                                ? `Nom : ${tooltipData[file.fileId].fileName}\nPoids : ${formatSize(tooltipData[file.fileId].size)}\nCréé le : ${new Date(tooltipData[file.fileId].fileCreatedAt).toLocaleString()}`
                                                : "Chargement des infos…"
                                            : `Nom : ${file.fileName}\nPoids : ${formatSize(file.size)}\nCréé le : ${new Date(file.fileCreatedAt).toLocaleString()}`
                                    }
                                    role="button"
                                >


                                    <div className="tile__icon">
                                        <FileIcon/>
                                    </div>
                                    <span className="tile__label">{file.fileName}</span>

                                    <button
                                        className="btn btn--danger"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            void handleDeleteFile(file.fileId);
                                            onDeleteFile(file.fileId);
                                        }}
                                        disabled={deleting}
                                    >
                                        {deleting ? "Suppression…" : "Supprimer"}
                                    </button>
                                </div>
                            );
                        })}
                </div>
            )}
        </div>
    );
};
