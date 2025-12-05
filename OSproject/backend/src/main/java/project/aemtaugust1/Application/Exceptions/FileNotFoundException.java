package project.aemtaugust1.Application.Exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(int id) {
        super("File with ID " + id + " not found");
    }
}
