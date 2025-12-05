package project.aemtaugust1.Application.Commands.Update;

import project.aemtaugust1.Domain.File;

import java.time.LocalDateTime;

public class FileUpdateCommand {
    public long fileId;
    public String fileName;
    public long size;
    public String content;
}
