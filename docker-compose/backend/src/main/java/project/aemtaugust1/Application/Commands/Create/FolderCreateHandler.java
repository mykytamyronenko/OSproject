package project.aemtaugust1.Application.Commands.Create;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Utils.ICommandsHandler;
import project.aemtaugust1.Domain.Folder;
import project.aemtaugust1.Infrastructure.DbEntities.DbFolder;
import project.aemtaugust1.Infrastructure.IFolderRepository;

@Service
public class FolderCreateHandler implements ICommandsHandler<FolderCreateCommand, FolderCreateOutput> {
    private final IFolderRepository folderRepository;
    private final ModelMapper modelMapper;

    public FolderCreateHandler(IFolderRepository folderRepository, ModelMapper modelMapper) {
        this.folderRepository = folderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FolderCreateOutput handle(FolderCreateCommand command) {
        Folder folder = new Folder();
        folder.setFolderName(command.folderName);
        folder.setParentId(command.parentId);
        folder.setFolderCreatedAt(command.folderCreatedAt);
        folder.setFolderDeleted(false);

        DbFolder dbFolder = modelMapper.map(folder, DbFolder.class);
        DbFolder parentDbFolder = folderRepository.save(dbFolder);

        return modelMapper.map(dbFolder, FolderCreateOutput.class);
    }
}
