package project.aemtaugust1.Application.Queries.GetById;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Utils.FileSizeCalculator;
import project.aemtaugust1.Application.Utils.IQueryHandler;
import project.aemtaugust1.Infrastructure.DbEntities.DbFile;
import project.aemtaugust1.Infrastructure.IFileRepository;

import java.io.FileNotFoundException;

@Service
public class FileGetByIdHandler implements IQueryHandler<Long, FileGetByIdOutput> {
    private final IFileRepository fileRepository;
    private final ModelMapper modelMapper;

    public FileGetByIdHandler(IFileRepository fileRepository, ModelMapper modelMapper) {
        this.fileRepository = fileRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FileGetByIdOutput handle(Long id) throws FileNotFoundException {
        DbFile dbFile = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + id));

        // recalcul uniquement pour les images
        if (dbFile.getType() == project.aemtaugust1.Domain.File.FileType.TEXT) {
            long realSize = FileSizeCalculator.calculateSize(
                    dbFile.getType(),
                    dbFile.getContent(),
                    dbFile.getFileName()
            );
            dbFile.setSize(realSize);
        }

        return modelMapper.map(dbFile, FileGetByIdOutput.class);
    }
}
