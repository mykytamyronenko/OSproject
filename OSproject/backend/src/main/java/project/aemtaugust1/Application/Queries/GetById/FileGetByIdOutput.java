package project.aemtaugust1.Application.Queries.GetById;

import java.time.LocalDateTime;

public class FileGetByIdOutput {
    public long fileId;
    public String fileName;
    public Long fileFolderId;
    public project.aemtaugust1.Domain.File.FileType type;
    public long size;
    public String content;
    public LocalDateTime fileCreatedAt;
    public boolean fileDeleted;
}
