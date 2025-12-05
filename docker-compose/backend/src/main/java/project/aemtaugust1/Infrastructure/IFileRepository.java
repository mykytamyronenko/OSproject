package project.aemtaugust1.Infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.aemtaugust1.Infrastructure.DbEntities.DbFile;

import java.util.List;

public interface IFileRepository extends JpaRepository<DbFile, Long> {
    List<DbFile> findByFileFolderId(Long folderId);
    List<DbFile> findByFileFolderIdIsNull();
    long countByFileFolderId(Long folderId);
    List<DbFile> findAllByIsDeletedFileTrue();

}
