import React, { useState, useEffect } from "react";

interface BreadcrumbProps {
    path: { id: number | null; name: string }[];
    onNavigate: (id: number | null, index: number) => void;
    onNavigateByPath?: (folderName: string) => void;
}

export const Breadcrumb: React.FC<BreadcrumbProps> = ({ path, onNavigate, onNavigateByPath }) => {
    const [editing, setEditing] = useState(false);
    const [inputValue, setInputValue] = useState(path.map(p => p.name).join("/"));

    useEffect(() => {
        setInputValue(path.map(p => p.name).join("/"));
    }, [path]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setEditing(false);

        if (onNavigateByPath) {
            onNavigateByPath(inputValue.trim());
        }
    };

    return (
        <div className="flex space-x-2 text-sm text-gray-700 mb-4">
            {editing ? (
                <form onSubmit={handleSubmit} className="flex w-full">
                    <input
                        className="border rounded px-2 py-1 w-full"
                        value={inputValue}
                        onChange={(e) => setInputValue(e.target.value)}
                        autoFocus
                        onBlur={() => setEditing(false)}
                    />
                </form>
            ) : (
                path.map((segment, index) => (
                    <span key={segment.id ?? "root"} className="flex items-center">
                        <button
                            onClick={() => onNavigate(segment.id, index)}
                            onDoubleClick={() => {
                                if (index === path.length - 1) {
                                    setEditing(true);
                                }
                            }}
                            className="hover:underline"
                        >
                            {segment.name}
                        </button>
                        {index < path.length - 1 && <span className="mx-1">→</span>}
                    </span>
                ))
            )}
        </div>
    );
};
