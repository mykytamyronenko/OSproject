package project.aemtaugust1.Application.Commands.Delete;

import org.springframework.stereotype.Service;
import project.aemtaugust1.Infrastructure.IFileRepository;
import project.aemtaugust1.Infrastructure.IFolderRepository;

@Service
public class TrashDeleteHandler {
    private final IFileRepository fileRepository;
    private final IFolderRepository folderRepository;

    public TrashDeleteHandler(IFileRepository fileRepository, IFolderRepository folderRepository) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
    }

    public void emptyTrash() {
        fileRepository.deleteAll(fileRepository.findAllByIsDeletedFileTrue());
        folderRepository.deleteAll(folderRepository.findAllByIsDeletedFolderTrue());
    }
}
