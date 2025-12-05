import { useEffect } from "react";

type NotificationProps = {
    message: string;
    type?: "success" | "error";
    onClose: () => void;
};

export const Notification = ({ message, type = "success", onClose }: NotificationProps) => {
    useEffect(() => {
        const timer = setTimeout(onClose, 3000);
        return () => clearTimeout(timer);
    }, [onClose]);

    return (
        <div
            className={`fixed bottom-4 right-4 px-4 py-2 rounded-lg shadow-lg text-white ${
                type === "success" ? "bg-green-500" : "bg-red-500"
            }`}
        >
            {message}
        </div>
    );
};
