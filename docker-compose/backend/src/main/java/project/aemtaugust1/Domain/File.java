package project.aemtaugust1.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class File {
    private long fileId;
    private String fileName;
    private Long fileFolderId;
    private FileType type;
    private long size;
    private String content;
    private LocalDateTime fileCreatedAt;
    private boolean fileDeleted;

    public enum FileType {
        TEXT,
        IMAGE,
        OTHER
    }
}
