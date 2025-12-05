package project.aemtaugust1.API.File.Advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import project.aemtaugust1.Application.Exceptions.FileNotFoundException;

@RestControllerAdvice
public class FileNotFoundAdvice {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseStatusException handle(FileNotFoundException e) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
