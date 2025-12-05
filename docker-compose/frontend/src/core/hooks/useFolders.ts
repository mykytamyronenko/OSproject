import { useEffect, useState } from "react";
import {fetchFolders, type FolderDto} from "../services/api";

export function useFolders() {
    const [folders, setFolders] = useState<FolderDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchFolders()
            .then((data) => setFolders(data))
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, []);

    return { folders, loading, error };
}
