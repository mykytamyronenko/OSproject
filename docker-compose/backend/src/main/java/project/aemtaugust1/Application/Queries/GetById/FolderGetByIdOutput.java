package project.aemtaugust1.Application.Queries.GetById;

import java.time.LocalDateTime;

public class FolderGetByIdOutput {
    public long folderId;
    public String folderName;
    public String parentId;
    public LocalDateTime folderCreatedAt;
    public boolean folderDeleted;
}
