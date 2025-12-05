package project.aemtaugust1.Application.Commands;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Commands.Create.FileCreateCommand;
import project.aemtaugust1.Application.Commands.Create.FileCreateOutput;
import project.aemtaugust1.Application.Commands.Delete.FileDeleteHandler;
import project.aemtaugust1.Application.Commands.Update.FileUpdateCommand;
import project.aemtaugust1.Application.Utils.ICommandsHandler;
import project.aemtaugust1.Application.Utils.IEmptyOutputCommandHandler;

@Service
public class FileCommandProcessor {
    private final ICommandsHandler<FileCreateCommand, FileCreateOutput> createOutputHandler;
    private final IEmptyOutputCommandHandler<Long> fileDeleteCommand;
    private final FileDeleteHandler fileDeleteHandler;
    private final IEmptyOutputCommandHandler<FileUpdateCommand> fileUpdateCommand;

    public FileCommandProcessor(ICommandsHandler<FileCreateCommand, FileCreateOutput> createOutputHandler,
            @Qualifier("FileDeleteHandler") IEmptyOutputCommandHandler<Long> fileDeleteCommand,
            FileDeleteHandler fileDeleteHandler, IEmptyOutputCommandHandler<FileUpdateCommand> fileUpdateCommand
    ) {
        this.createOutputHandler = createOutputHandler;
        this.fileDeleteCommand = fileDeleteCommand;
        this.fileDeleteHandler = fileDeleteHandler;
        this.fileUpdateCommand = fileUpdateCommand;
    }

    public void update(FileUpdateCommand command) {
        fileUpdateCommand.handle(command);
    }

    public FileCreateOutput handle(FileCreateCommand fileCreateCommand) {
        return createOutputHandler.handle(fileCreateCommand);
    }

    // Soft delete → corbeille
    public void deleteFile(Long fileId) {
        fileDeleteCommand.handle(fileId);
    }

    // Restore
    public void restoreFile(Long fileId) {
        fileDeleteHandler.restore(fileId);
    }

    // Hard delete → purge
    public void purgeFile(Long fileId) {
        fileDeleteHandler.purge(fileId);
    }
}
