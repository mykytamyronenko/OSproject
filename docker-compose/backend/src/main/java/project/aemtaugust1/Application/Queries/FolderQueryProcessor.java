package project.aemtaugust1.Application.Queries;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Queries.GetAll.FolderGetAllOutput;
import project.aemtaugust1.Application.Queries.GetAll.FolderGetAllQuery;
import project.aemtaugust1.Application.Queries.GetById.FolderGetByIdOutput;
import project.aemtaugust1.Application.Utils.IQueryHandler;

@Service
public class FolderQueryProcessor {
    final IQueryHandler<FolderGetAllQuery, FolderGetAllOutput> folderGetAllHandler;
    private final IQueryHandler<Long, FolderGetByIdOutput> folderGetByIdHandler;

    public FolderQueryProcessor(
            IQueryHandler<FolderGetAllQuery, FolderGetAllOutput> folderGetAllHandler,
            IQueryHandler<Long, FolderGetByIdOutput> folderGetByIdHandler
    ) {
        this.folderGetAllHandler = folderGetAllHandler;
        this.folderGetByIdHandler = folderGetByIdHandler;
    }

    @SneakyThrows
    public FolderGetAllOutput getAll(FolderGetAllQuery query) {
        return folderGetAllHandler.handle(query);
    }

    @SneakyThrows
    public FolderGetByIdOutput getById(Long id) {
        return folderGetByIdHandler.handle(id);
    }
}
