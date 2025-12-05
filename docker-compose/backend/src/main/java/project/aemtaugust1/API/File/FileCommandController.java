package project.aemtaugust1.API.File;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.aemtaugust1.Application.Commands.Create.FileCreateCommand;
import project.aemtaugust1.Application.Commands.Create.FileCreateOutput;
import project.aemtaugust1.Application.Commands.FileCommandProcessor;
import project.aemtaugust1.Application.Commands.Update.FileUpdateCommand;

@RestController
@RequestMapping("/Files")
public class FileCommandController {
    private final FileCommandProcessor fileCommandProcessor;

    public FileCommandController(FileCommandProcessor fileCommandProcessor) {
        this.fileCommandProcessor = fileCommandProcessor;
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<FileCreateOutput> create(@RequestBody FileCreateCommand fileCreateCommand) {
        return new ResponseEntity<>(fileCommandProcessor.handle(fileCreateCommand), HttpStatus.CREATED);
    }

    // Soft delete → corbeille
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File moved to trash"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<Void> deleteFile(@PathVariable long id) {
        fileCommandProcessor.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Restore
    @PutMapping("/Restore/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File restored"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<Void> restoreFile(@PathVariable long id) {
        fileCommandProcessor.restoreFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Hard delete → purge
    @DeleteMapping("/Purge/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File permanently deleted"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<Void> purgeFile(@PathVariable long id) {
        fileCommandProcessor.purgeFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Void> updateFile(@PathVariable long id, @RequestBody FileUpdateCommand fileUpdateCommand) {
        fileUpdateCommand.fileId = id;
        fileCommandProcessor.update(fileUpdateCommand);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
