package project.aemtaugust1.Application.Queries.GetAll;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Utils.IQueryHandler;
import project.aemtaugust1.Infrastructure.DbEntities.DbFolder;
import project.aemtaugust1.Infrastructure.IFolderRepository;

@Service
public class FolderGetAllHandler implements IQueryHandler<FolderGetAllQuery, FolderGetAllOutput> {
    private final IFolderRepository folderRepository;
    private final ModelMapper modelMapper;

    public FolderGetAllHandler(IFolderRepository folderRepository, ModelMapper modelMapper) {
        this.folderRepository = folderRepository;
        this.modelMapper = modelMapper;
    }

    public FolderGetAllOutput handle(FolderGetAllQuery query) {
        Iterable<DbFolder> dbFolders = folderRepository.findAll();
        FolderGetAllOutput output = new FolderGetAllOutput();

        for (DbFolder dbFolder : dbFolders) {
            FolderGetAllOutput.Folder folder = modelMapper.map(dbFolder, FolderGetAllOutput.Folder.class);

            output.folderList.add(folder);
        }
        return output;
    }
}
