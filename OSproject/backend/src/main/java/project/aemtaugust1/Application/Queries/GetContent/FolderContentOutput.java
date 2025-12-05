package project.aemtaugust1.Application.Queries.GetContent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FolderContentOutput {
    public List<Folder> folders = new ArrayList<>();
    public List<File> files = new ArrayList<>();

    public static class Folder {
        public long folderId;
        public String folderName;
        public LocalDateTime folderCreatedAt;
        public  boolean folderDeleted;

        public long itemCount;
    }

    public static class File {
        public long fileId;
        public String fileName;
        public long size;
        public LocalDateTime fileCreatedAt;
        public String type;
        public String content;
        public boolean fileDeleted;
    }
}
