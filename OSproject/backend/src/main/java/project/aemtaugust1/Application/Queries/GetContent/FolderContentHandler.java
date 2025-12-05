package project.aemtaugust1.Application.Queries.GetContent;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Infrastructure.IFileRepository;
import project.aemtaugust1.Infrastructure.IFolderRepository;
import project.aemtaugust1.Infrastructure.DbEntities.DbFile;
import project.aemtaugust1.Infrastructure.DbEntities.DbFolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FolderContentHandler {
    private final IFileRepository fileRepository;
    private final IFolderRepository folderRepository;
    private final ModelMapper modelMapper;

    public FolderContentHandler(IFolderRepository folderRepository, IFileRepository fileRepository, ModelMapper modelMapper) {
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.modelMapper = modelMapper;
    }

    public FolderContentOutput getRootContent() {
        Iterable<DbFolder> folders = folderRepository.findByParentIdIsNull();
        Iterable<DbFile> files = fileRepository.findByFileFolderIdIsNull();

        return buildOutput(folders, files);
    }

    public FolderContentOutput getFolderContent(Long folderId) {
        Iterable<DbFolder> folders = folderRepository.findByParentId(folderId);
        Iterable<DbFile> files = fileRepository.findByFileFolderId(folderId);

        return buildOutput(folders, files);
    }

    private FolderContentOutput buildOutput(Iterable<DbFolder> folders, Iterable<DbFile> files) {
        FolderContentOutput output = new FolderContentOutput();

        for (DbFolder dbFolder : folders) {
            FolderContentOutput.Folder dto = modelMapper.map(dbFolder, FolderContentOutput.Folder.class);

            long subFoldersCount = folderRepository.countByParentId(dbFolder.getFolderId());
            long subFilesCount = fileRepository.countByFileFolderId(dbFolder.getFolderId());
            dto.itemCount = subFoldersCount + subFilesCount;

            output.folders.add(dto);
        }

        for (DbFile dbFile : files) {
            FolderContentOutput.File dto = new FolderContentOutput.File();
            dto.fileId = dbFile.getFileId();
            dto.fileName = dbFile.getFileName();
            dto.type = String.valueOf(dbFile.getType());
            dto.fileCreatedAt = dbFile.getFileCreatedAt();

            if ("TEXT".equals(dto.type)) {
                dto.size = dbFile.getContent().getBytes(StandardCharsets.UTF_8).length;
                dto.content = dbFile.getContent();
            } else if ("IMAGE".equals(dto.type)) {
                Path path = Paths.get("src/main/resources/static/images/" + dbFile.getFileName());
                try {
                    dto.size = Files.size(path);
                } catch (IOException e) {
                    dto.size = 0L;
                }
                dto.content = "/images/" + dbFile.getFileName();
            } else {
                dto.size = 0L;
            }

            output.files.add(dto);
        }



        return output;
    }
}

