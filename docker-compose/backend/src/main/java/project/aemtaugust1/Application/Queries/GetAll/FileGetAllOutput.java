package project.aemtaugust1.Application.Queries.GetAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileGetAllOutput {
    public List<FileGetAllOutput.File> fileList = new ArrayList<>();

    public static class File {
        public long fileId;
        public String fileName;
        public Long fileFolderId;
        public project.aemtaugust1.Domain.File.FileType type;
        public long size;
        public String content;
        public LocalDateTime fileCreatedAt;
        public boolean fileDeleted;
    }
}
