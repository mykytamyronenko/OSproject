package project.aemtaugust1.Application.Commands.Delete;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.aemtaugust1.API.Folder.Exceptions.FolderNotFoundException;
import project.aemtaugust1.Application.Utils.IEmptyOutputCommandHandler;
import project.aemtaugust1.Infrastructure.DbEntities.DbFolder;
import project.aemtaugust1.Infrastructure.IFolderRepository;

import java.util.Optional;

@Service
@Qualifier("FolderDeleteHandler")
public class FolderDeleteHandler implements IEmptyOutputCommandHandler<Long> {
    private final IFolderRepository folderRepository;

    public FolderDeleteHandler(IFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public void handle(Long id) {
        Optional<DbFolder> folderOpt = folderRepository.findById(id);
        if (folderOpt.isPresent()) {
            DbFolder folder = folderOpt.get();
            folder.setIsDeletedFolder(true);
            folderRepository.save(folder);
        } else {
            throw new FolderNotFoundException("Folder with id " + id + " not found");
        }
    }

    public void restore(Long id) {
        Optional<DbFolder> folderOpt = folderRepository.findById(id);
        if (folderOpt.isPresent()) {
            DbFolder folder = folderOpt.get();

            // 🔥 Vérification du parent
            if (folder.getParentId() != null &&
                    !folderRepository.existsById(folder.getParentId())) {
                folder.setParentId(null); // placer en racine
            }

            folder.setIsDeletedFolder(false);
            folderRepository.save(folder);
        } else {
            throw new FolderNotFoundException("Folder with id " + id + " not found");
        }
    }

    public void purge(Long id) {
        if (folderRepository.existsById(id)) {
            folderRepository.deleteById(id);
        } else {
            throw new FolderNotFoundException("Folder with id " + id + " not found");
        }
    }
}
