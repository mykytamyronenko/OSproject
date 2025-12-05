package project.aemtaugust1.Application.Queries.GetById;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Exceptions.FolderNotFoundException;
import project.aemtaugust1.Application.Utils.IQueryHandler;
import project.aemtaugust1.Infrastructure.DbEntities.DbFolder;
import project.aemtaugust1.Infrastructure.IFolderRepository;

@Service
public class FolderGetByIdHandler implements IQueryHandler<Long, FolderGetByIdOutput> {
    private final IFolderRepository folderRepository;
    private final ModelMapper modelMapper;

    public FolderGetByIdHandler(IFolderRepository folderRepository, ModelMapper modelMapper) {
        this.folderRepository = folderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FolderGetByIdOutput handle(Long id) throws FolderNotFoundException {
        DbFolder dbFolder = folderRepository.findById(id).orElseThrow(() -> new FolderNotFoundException("Folder not found! " + id));
        return modelMapper.map(dbFolder, FolderGetByIdOutput.class);
    }
}
