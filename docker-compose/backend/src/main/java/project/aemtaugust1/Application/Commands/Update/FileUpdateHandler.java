package project.aemtaugust1.Application.Commands.Update;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.API.File.Exceptions.FileNotFoundException;
import project.aemtaugust1.Application.Commands.FileCommandProcessor;
import project.aemtaugust1.Application.Utils.IEmptyOutputCommandHandler;
import project.aemtaugust1.Domain.File;
import project.aemtaugust1.Infrastructure.DbEntities.DbFile;
import project.aemtaugust1.Infrastructure.IFileRepository;

@Service
public class FileUpdateHandler implements IEmptyOutputCommandHandler<FileUpdateCommand> {
    private final IFileRepository fileRepository;
    private final ModelMapper modelMapper;

    public FileUpdateHandler(IFileRepository fileRepository, ModelMapper modelMapper) {
        this.fileRepository = fileRepository;
        this.modelMapper = modelMapper;
    }

     @Override
     public void handle(FileUpdateCommand input) {
         DbFile entity = fileRepository.findById(input.fileId)
                 .orElseThrow(() -> new FileNotFoundException("File not found!"));

         // on met juste à jour le champ voulu
         entity.setContent(input.content);

         fileRepository.save(entity);
     }
}
