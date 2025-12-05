package project.aemtaugust1.Infrastructure.DbEntities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "folder")
@Data
@NoArgsConstructor
public class DbFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private long folderId;
    @Column(name = "folder_name")
    private String folderName;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "folder_created_at")
    private LocalDateTime folderCreatedAt;
    @Column(name="is_deleted_folder")
    private boolean isDeletedFolder = false;

    public Boolean getIsDeletedFolder() {
        return isDeletedFolder;
    }

    public void setIsDeletedFolder(Boolean isDeletedFolder) {
        this.isDeletedFolder = isDeletedFolder;
    }
}
