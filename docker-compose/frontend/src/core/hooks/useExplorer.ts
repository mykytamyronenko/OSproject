import { useEffect, useState, useCallback } from "react";
import { fetchFolderContent, type FileDto, type FolderDto } from "../services/api";

export function useExplorer(folderId?: number, reloadKey: number = 0) {
    const [files, setFiles] = useState<FileDto[]>([]);
    const [folders, setFolders] = useState<FolderDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const loadData = useCallback(() => {
        setLoading(true);
        fetchFolderContent(folderId)
            .then((data) => {
                setFiles(data.files);
                setFolders(data.folders);
            })
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [folderId]);

    useEffect(() => {
        loadData();
    }, [loadData, reloadKey]);

    return { files, folders, loading, error, refresh: loadData };
}
