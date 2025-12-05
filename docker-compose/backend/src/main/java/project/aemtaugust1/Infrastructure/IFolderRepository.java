package project.aemtaugust1.Infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.aemtaugust1.Infrastructure.DbEntities.DbFolder;

import java.util.List;

public interface IFolderRepository extends JpaRepository<DbFolder, Long> {
    List<DbFolder> findByParentId(Long parentId);
    List<DbFolder> findByParentIdIsNull();
    long countByParentId(Long parentId);
    Optional<DbFolder> findByFolderNameAndParentId(String folderName, Long parentId);
    List<DbFolder> findAllByIsDeletedFolderTrue();
}
