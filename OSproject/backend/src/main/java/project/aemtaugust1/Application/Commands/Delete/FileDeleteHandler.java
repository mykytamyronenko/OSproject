package project.aemtaugust1.Application.Commands.Delete;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.aemtaugust1.API.File.Exceptions.FileNotFoundException;
import project.aemtaugust1.Application.Utils.IEmptyOutputCommandHandler;
import project.aemtaugust1.Infrastructure.DbEntities.DbFile;
import project.aemtaugust1.Infrastructure.IFolderRepository;
import project.aemtaugust1.Infrastructure.IFileRepository;

import java.util.Optional;

@Service
@Qualifier("FileDeleteHandler")
public class FileDeleteHandler implements IEmptyOutputCommandHandler<Long> {
    private final IFileRepository fileRepository;
    private final IFolderRepository folderRepository; // 🔥 ajout

    public FileDeleteHandler(IFileRepository fileRepository, IFolderRepository folderRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public void handle(Long id) {
        Optional<DbFile> fileOpt = fileRepository.findById(id);
        if (fileOpt.isPresent()) {
            DbFile file = fileOpt.get();
            file.setIsDeletedFile(true);
            fileRepository.save(file);
        } else {
            throw new FileNotFoundException("File with id " + id + " not found");
        }
    }

    public void restore(Long id) {
        Optional<DbFile> fileOpt = fileRepository.findById(id);
        if (fileOpt.isPresent()) {
            DbFile file = fileOpt.get();

            // 🔥 Vérification du dossier parent
            if (file.getFileFolderId() != null &&
                    !folderRepository.existsById(file.getFileFolderId())) {
                file.setFileFolderId(null); // placer en racine
            }

            file.setIsDeletedFile(false);
            fileRepository.save(file);
        } else {
            throw new FileNotFoundException("File with id " + id + " not found");
        }
    }

    public void purge(Long id) {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
        } else {
            throw new FileNotFoundException("File with id " + id + " not found");
        }
    }
}
