package project.aemtaugust1.Application.Commands.Create;

import java.time.LocalDateTime;

public class FolderCreateOutput {
    public long folderId;
    public String folderName;
    public Long parentId;
    public LocalDateTime folderCreatedAt;
    public boolean folderDeleted;
}
