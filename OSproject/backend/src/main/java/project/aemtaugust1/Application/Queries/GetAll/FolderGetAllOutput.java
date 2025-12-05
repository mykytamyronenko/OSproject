package project.aemtaugust1.Application.Queries.GetAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FolderGetAllOutput {
    public List<FolderGetAllOutput.Folder> folderList = new ArrayList<>();

    public static class Folder {
        public long folderId;
        public String folderName;
        public String parentId;
        public LocalDateTime folderCreatedAt;
        public boolean folderDeleted;
    }
}
