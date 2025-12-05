package project.aemtaugust1.Application.Queries;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Queries.GetAll.FileGetAllOutput;
import project.aemtaugust1.Application.Queries.GetAll.FileGetAllQuery;
import project.aemtaugust1.Application.Queries.GetById.FileGetByIdOutput;
import project.aemtaugust1.Application.Utils.IQueryHandler;

import java.io.FileNotFoundException;

@Service
public class FileQueryProcessor {
    final IQueryHandler<FileGetAllQuery, FileGetAllOutput> fileGetAllHandler;
    private final IQueryHandler<Long, FileGetByIdOutput> fileGetByIdHandler;

    public FileQueryProcessor(IQueryHandler<FileGetAllQuery, FileGetAllOutput> fileGetAllHandler, IQueryHandler<Long, FileGetByIdOutput> fileGetByIdHandler) {
        this.fileGetAllHandler = fileGetAllHandler;
        this.fileGetByIdHandler = fileGetByIdHandler;
    }

    @SneakyThrows
    public FileGetAllOutput getAll(FileGetAllQuery query) { return fileGetAllHandler.handle(query); }

    @SneakyThrows
    public FileGetByIdOutput getById(Long id) { return fileGetByIdHandler.handle(id); }
}
