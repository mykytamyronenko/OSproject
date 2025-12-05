package project.aemtaugust1.API.Folder;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.aemtaugust1.Application.Queries.FileQueryProcessor;
import project.aemtaugust1.Application.Queries.FolderQueryProcessor;
import project.aemtaugust1.Application.Queries.GetAll.FolderGetAllOutput;
import project.aemtaugust1.Application.Queries.GetById.FolderGetByIdOutput;
import project.aemtaugust1.Application.Queries.ResolvePath.FolderPathResolver;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderQueryController {
    public final FolderQueryProcessor folderQueryProcessor;
    private final FolderPathResolver folderPathResolver;

    public FolderQueryController(FolderQueryProcessor folderQueryProcessor, FolderPathResolver folderPathResolver) {
        this.folderQueryProcessor = folderQueryProcessor;
        this.folderPathResolver = folderPathResolver;
    }

    @GetMapping()
    public Iterable<FolderGetAllOutput.Folder> getAll() {return folderQueryProcessor.getAll(null).folderList;}

    @GetMapping(value = "{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })

    public FolderGetByIdOutput getById(@PathVariable("id") Long id) { return folderQueryProcessor.getById(id); }

    @GetMapping("/by-path")
    public ResponseEntity<Long> getFolderIdByPath(@RequestParam String path) {
        return folderPathResolver.resolve(path)
                .map(f -> ResponseEntity.ok(f.getFolderId()))
                .orElse(ResponseEntity.notFound().build());
    }
}
