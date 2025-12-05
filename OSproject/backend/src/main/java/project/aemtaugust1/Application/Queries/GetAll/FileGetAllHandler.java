package project.aemtaugust1.Application.Queries.GetAll;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Utils.FileSizeCalculator;
import project.aemtaugust1.Application.Utils.IQueryHandler;
import project.aemtaugust1.Infrastructure.DbEntities.DbFile;
import project.aemtaugust1.Infrastructure.IFileRepository;

@Service
public class FileGetAllHandler implements IQueryHandler<FileGetAllQuery, FileGetAllOutput> {
    private final IFileRepository fileRepository;
    private final ModelMapper modelMapper;

    public FileGetAllHandler(IFileRepository fileRepository, ModelMapper modelMapper) {
        this.fileRepository = fileRepository;
        this.modelMapper = modelMapper;
    }

    public FileGetAllOutput handle(FileGetAllQuery query) {
        Iterable<DbFile> dbFiles = fileRepository.findAll();
        FileGetAllOutput output = new FileGetAllOutput();

        for (DbFile dbFile : dbFiles) {
            if (dbFile.getType() == project.aemtaugust1.Domain.File.FileType.TEXT) {
                long realSize = FileSizeCalculator.calculateSize(
                        dbFile.getType(),
                        dbFile.getContent(),
                        dbFile.getFileName()
                );
                dbFile.setSize(realSize);
            }
            FileGetAllOutput.File file = modelMapper.map(dbFile, FileGetAllOutput.File.class);
            output.fileList.add(file);
        }
        return output;
    }
}

