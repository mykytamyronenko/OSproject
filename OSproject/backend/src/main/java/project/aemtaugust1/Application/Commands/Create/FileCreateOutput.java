package project.aemtaugust1.Application.Commands.Create;

import project.aemtaugust1.Domain.File;

import java.time.LocalDateTime;

public class FileCreateOutput {
    public String fileName;
    public Long fileFolderId;
    public File.FileType type;
    public String content;
    public LocalDateTime fileCreatedAt;

}
