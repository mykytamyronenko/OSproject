import { useEffect, useState } from "react";
import { fetchFiles, type FileDto } from "../services/api";

export function useFiles() {
    const [files, setFiles] = useState<FileDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchFiles()
            .then((data) => setFiles(data))
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, []);

    return { files, loading, error };
}
