package project.aemtaugust1.Application.Commands.Create;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Utils.ICommandsHandler;
import project.aemtaugust1.Domain.File;
import project.aemtaugust1.Infrastructure.DbEntities.DbFile;
import project.aemtaugust1.Infrastructure.IFileRepository;

import java.time.LocalDateTime;

@Service
public class FileCreateHandler implements ICommandsHandler<FileCreateCommand, FileCreateOutput> {
    private final IFileRepository fileRepository;
    private final ModelMapper modelMapper;

    public FileCreateHandler(IFileRepository fileRepository, ModelMapper modelMapper) {
        this.fileRepository = fileRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FileCreateOutput handle(FileCreateCommand command) {
        File file = new File();

        String normalizedName = command.fileName;
        if (!normalizedName.toLowerCase().endsWith(".txt")) {
            normalizedName += ".txt";
        }
        file.setFileName(normalizedName);

        file.setType(File.FileType.TEXT);

        file.setFileFolderId(command.fileFolderId); // peut être null → racine

        file.setContent(command.content);
        file.setFileCreatedAt(
                command.fileCreatedAt != null
                        ? command.fileCreatedAt.plusHours(2)
                        : LocalDateTime.now().plusHours(2) // fallback si jamais null
        );


        DbFile dbFile = modelMapper.map(file, DbFile.class);
        DbFile dbFileCreated = fileRepository.save(dbFile);

        return modelMapper.map(dbFileCreated, FileCreateOutput.class);
    }

}
