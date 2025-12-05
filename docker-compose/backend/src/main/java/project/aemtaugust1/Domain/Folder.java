package project.aemtaugust1.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Folder {
    private long folderId;
    private String folderName;
    private Long parentId;
    private LocalDateTime folderCreatedAt;
    private boolean folderDeleted;
}
