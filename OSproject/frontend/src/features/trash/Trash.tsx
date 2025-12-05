/*
import React from "react";
import type { FileDto, FolderDto } from "../../core/services/api";
import { FileIcon, FolderIcon } from "lucide-react";

interface TrashProps {
    files: FileDto[];
    folders: FolderDto[];
    onRestoreFile: (id: number) => void;
    onRestoreFolder: (id: number) => void;
}

export const Trash: React.FC<TrashProps> = ({
                                                files,
                                                folders,
                                                onRestoreFile,
                                                onRestoreFolder,
                                            }) => {
    return (
        <div className="trash flex flex-wrap gap-4 p-4">
            {/!* --- Dossiers supprimés --- *!/}
            {folders.map((folder) => (
                <div key={folder.folderId} className="tile tile--folder">
                    <div className="tile__icon">
                        <FolderIcon size={40} />
                    </div>
                    <span className="tile__label text-center">{folder.folderName}</span>
                    <button
                        className="mt-2 text-xs px-2 py-1 rounded bg-green-600 text-white"
                        onClick={() => onRestoreFolder(folder.folderId)}
                    >
                        Restaurer
                    </button>
                </div>
            ))}

            {/!* --- Fichiers supprimés --- *!/}
            {files.map((file) => (
                <div key={file.fileId} className="tile tile--file">
                    <div className="tile__icon">
                        <FileIcon size={40} />
                    </div>
                    <span className="tile__label text-center">{file.fileName}</span>
                    <button
                        className="mt-2 text-xs px-2 py-1 rounded bg-green-600 text-white"
                        onClick={() => onRestoreFile(file.fileId)}
                    >
                        Restaurer
                    </button>
                </div>
            ))}
        </div>
    );
};
*/
