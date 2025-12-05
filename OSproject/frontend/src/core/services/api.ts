export interface FileDto {
    fileId: number;
    fileName: string;
    fileFolderId?: number;
    type: "TEXT" | "IMAGE" | "OTHER";
    size: number;
    content: string;
    fileCreatedAt: string;
    fileDeleted: boolean;
}

export interface FolderDto {
    folderId: number;
    folderName: string;
    parentId: number | null;
    folderCreatedAt: string;
    itemCount: number;
    folderDeleted: boolean;
}

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

export async function fetchFiles(): Promise<FileDto[]> {
    const response = await fetch(`${API_URL}/files`);
    if (!response.ok) {
        throw new Error("Erreur lors du fetch des fichiers");
    }
    return response.json();
}

export async function fetchFolders(): Promise<FolderDto[]> {
    const response = await fetch(`${API_URL}/folders`);
    if (!response.ok) {
        throw new Error("Erreur lors du fetch des dossiers");
    }
    return response.json();
}

export async function fetchFolderContent(folderId?: number): Promise<{ files: FileDto[]; folders: FolderDto[] }> {
    const url = folderId ? `${API_URL}/folders/${folderId}/content` : `${API_URL}/folders/root`;
    const response = await fetch(url);
    if (!response.ok) throw new Error("Erreur lors du fetch du contenu du dossier");

    return response.json();
}

export async function resolveFolderPath(path: string): Promise<number> {
    const response = await fetch(`${API_URL}/folders/by-path?path=${encodeURIComponent(path)}`);
    if (!response.ok) throw new Error("Chemin introuvable");
    return response.json();
}

// Déplacer un fichier dans la corbeille (soft delete)
export async function moveFileToTrash(fileId: number): Promise<void> {
    const response = await fetch(
        `${import.meta.env.VITE_API_URL}/Files/${fileId}`,
        { method: "DELETE" }
    );
    if (!response.ok) {
        throw new Error("Erreur lors du déplacement du fichier vers la corbeille");
    }
}

// Restaurer un fichier
export async function restoreFile(fileId: number): Promise<void> {
    const response = await fetch(
        `${import.meta.env.VITE_API_URL}/Files/Restore/${fileId}`,
        { method: "PUT" }
    );
    if (!response.ok) {
        throw new Error("Erreur lors de la restauration du fichier");
    }
}

// Supprimer définitivement un fichier (purge)
export async function purgeFile(fileId: number): Promise<void> {
    const response = await fetch(
        `${import.meta.env.VITE_API_URL}/Files/Purge/${fileId}`,
        { method: "DELETE" }
    );
    if (!response.ok) {
        throw new Error("Erreur lors de la suppression définitive du fichier");
    }
}

// Déplacer un fichier dans la corbeille (soft delete)
export async function moveFolderToTrash(folderId: number): Promise<void> {
    const response = await fetch(
        `${import.meta.env.VITE_API_URL}/Folders/${folderId}`,
        { method: "DELETE" }
    );
    if (!response.ok) {
        throw new Error("Erreur lors du déplacement du dossier vers la corbeille");
    }
}

// Restaurer un dossier
export async function restoreFolder(folderId: number): Promise<void> {
    const response = await fetch(
        `${import.meta.env.VITE_API_URL}/Folders/Restore/${folderId}`,
        { method: "PUT" }
    );
    if (!response.ok) {
        throw new Error("Erreur lors de la restauration du dossier");
    }
}

// Supprimer définitivement un fichier (purge)
export async function purgeFolder(folderId: number): Promise<void> {
    const response = await fetch(
        `${import.meta.env.VITE_API_URL}/Folders/Purge/${folderId}`,
        { method: "DELETE" }
    );
    if (!response.ok) {
        throw new Error("Erreur lors de la suppression définitive du dossier");
    }
}

export async function createFolder(folderName: string, parentId: number | null): Promise<FolderDto> {
    const response = await fetch(`${API_URL}/Folders/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            folderName,
            parentId,
            folderCreatedAt: new Date().toISOString()
        }),
    });

    if (!response.ok) {
        throw new Error("Erreur lors de la création du dossier");
    }

    return response.json();
}

export async function createFile(
    fileName: string,
    fileFolderId: number | null,
    content: string = ""
): Promise<FileDto> {
    const response = await fetch(`${API_URL}/Files/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            fileName,
            fileFolderId,
            type: "TEXT", // pour l’instant on fixe TEXT
            content,
            fileCreatedAt: new Date().toISOString()
        }),
    });

    if (!response.ok) {
        throw new Error("Erreur lors de la création du fichier");
    }

    return response.json();
}

export async function updateFileContent(fileId: number, content: string): Promise<void> {
    const response = await fetch(`${API_URL}/Files/${fileId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ content }),
    });

    if (!response.ok) {
        throw new Error("Erreur lors de la mise à jour du fichier");
    }
}

export async function fetchFileById(id: number): Promise<FileDto> {
    const response = await fetch(`${API_URL}/files/${id}`);
    if (!response.ok) throw new Error("Erreur lors du fetch du fichier");
    return response.json();
}
