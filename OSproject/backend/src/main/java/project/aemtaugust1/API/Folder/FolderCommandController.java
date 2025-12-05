package project.aemtaugust1.API.Folder;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.aemtaugust1.Application.Commands.Create.FolderCreateCommand;
import project.aemtaugust1.Application.Commands.Create.FolderCreateOutput;
import project.aemtaugust1.Application.Commands.FolderCommandProcessor;

@RestController
@RequestMapping("/Folders")
public class FolderCommandController {
    private final FolderCommandProcessor folderCommandProcessor;

    public FolderCommandController(FolderCommandProcessor folderCommandProcessor) {
        this.folderCommandProcessor = folderCommandProcessor;
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<FolderCreateOutput> create(@RequestBody FolderCreateCommand folderCreateCommand) {
        return new ResponseEntity<>(folderCommandProcessor.create(folderCreateCommand), HttpStatus.CREATED);
    }

    // Soft delete
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Folder moved to trash"),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public ResponseEntity<Void> deleteFolder(@PathVariable long id) {
        folderCommandProcessor.deleteFolder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Restore
    @PutMapping("/Restore/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder restored"),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public ResponseEntity<Void> restoreFolder(@PathVariable long id) {
        folderCommandProcessor.restoreFolder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Hard delete
    @DeleteMapping("/Purge/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Folder permanently deleted"),
            @ApiResponse(responseCode = "404", description = "Folder not found")
    })
    public ResponseEntity<Void> purgeFolder(@PathVariable long id) {
        folderCommandProcessor.purgeFolder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
