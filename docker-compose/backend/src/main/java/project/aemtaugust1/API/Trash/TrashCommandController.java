package project.aemtaugust1.API.Trash;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.aemtaugust1.Application.Commands.TrashCommandProcessor;

@RestController
@RequestMapping("/Trash")
public class TrashCommandController {
    private final TrashCommandProcessor trashCommandProcessor;

    public TrashCommandController(TrashCommandProcessor trashCommandProcessor) {
        this.trashCommandProcessor = trashCommandProcessor;
    }

    @DeleteMapping("/Empty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trash emptied")
    })
    public ResponseEntity<Void> emptyTrash() {
        trashCommandProcessor.emptyTrash();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
