package project.aemtaugust1.API.File;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.aemtaugust1.Application.Queries.FileQueryProcessor;
import project.aemtaugust1.Application.Queries.GetAll.FileGetAllOutput;
import project.aemtaugust1.Application.Queries.GetById.FileGetByIdOutput;

@RestController
@RequestMapping("/files")
public class FileQueryController {
    public final FileQueryProcessor fileQueryProcessor;

    public FileQueryController(FileQueryProcessor fileQueryProcessor) {
        this.fileQueryProcessor = fileQueryProcessor;
    }

    @GetMapping()
    public Iterable<FileGetAllOutput.File> getAll() {return fileQueryProcessor.getAll(null).fileList; }

    @GetMapping(value = "{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    public FileGetByIdOutput getById(@PathVariable("id") long id) { return fileQueryProcessor.getById(id); }
}
