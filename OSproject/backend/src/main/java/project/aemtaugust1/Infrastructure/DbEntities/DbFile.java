package project.aemtaugust1.Infrastructure.DbEntities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.aemtaugust1.Domain.File;

import java.time.LocalDateTime;

@Entity
@Table(name = "file")
@Data
@NoArgsConstructor
public class DbFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private long fileId;
    @Column(name = "file_name")
    private String fileName;
    @Column (name = "file_folder_id")
    private Long fileFolderId;
    @Enumerated(EnumType.STRING)
    private File.FileType type;
    private long size;
    private String content;
    @Column(name = "file_created_at")
    private LocalDateTime fileCreatedAt;
    @Column(name="is_deleted_file")
    private boolean isDeletedFile = false;

    public Boolean getIsDeletedFile() {
        return isDeletedFile;
    }

    public void setIsDeletedFile(Boolean isDeletedFile) {
        this.isDeletedFile = isDeletedFile;
    }
}
