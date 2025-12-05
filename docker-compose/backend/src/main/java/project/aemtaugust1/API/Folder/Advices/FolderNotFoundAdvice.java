package project.aemtaugust1.API.Folder.Advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import project.aemtaugust1.Application.Exceptions.FolderNotFoundException;

@RestControllerAdvice
public class FolderNotFoundAdvice {
    @ExceptionHandler(FolderNotFoundException.class)
        public ResponseStatusException handle(FolderNotFoundException e) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
