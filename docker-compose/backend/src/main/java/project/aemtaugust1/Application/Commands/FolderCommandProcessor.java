package project.aemtaugust1.Application.Commands;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.aemtaugust1.API.Folder.Exceptions.FolderNotFoundException;
import project.aemtaugust1.Application.Commands.Create.FolderCreateCommand;
import project.aemtaugust1.Application.Commands.Create.FolderCreateOutput;
import project.aemtaugust1.Application.Commands.Delete.FolderDeleteHandler;
import project.aemtaugust1.Application.Utils.ICommandsHandler;
import project.aemtaugust1.Application.Utils.IEmptyOutputCommandHandler;
import project.aemtaugust1.Infrastructure.IFolderRepository;

@Service
public class FolderCommandProcessor {
    private final ICommandsHandler<FolderCreateCommand, FolderCreateOutput> folderCreateHandler;
    private final IEmptyOutputCommandHandler<Long> folderDeleteCommand;
    private final FolderDeleteHandler folderDeleteHandler;

    public FolderCommandProcessor(ICommandsHandler<FolderCreateCommand, FolderCreateOutput> folderCreateHandler,
            @Qualifier("FolderDeleteHandler") IEmptyOutputCommandHandler<Long> folderDeleteCommand,
            FolderDeleteHandler folderDeleteHandler
    ) {
        this.folderCreateHandler = folderCreateHandler;
        this.folderDeleteCommand = folderDeleteCommand;
        this.folderDeleteHandler = folderDeleteHandler;
    }

    public FolderCreateOutput create(FolderCreateCommand folderCreateCommand) {
        return folderCreateHandler.handle(folderCreateCommand);
    }

    // Soft delete
    public void deleteFolder(Long folderId) {
        folderDeleteCommand.handle(folderId);
    }

    // Restore
    public void restoreFolder(Long folderId) {
        folderDeleteHandler.restore(folderId);
    }

    // Hard delete
    public void purgeFolder(Long folderId) {
        folderDeleteHandler.purge(folderId);
    }
}
