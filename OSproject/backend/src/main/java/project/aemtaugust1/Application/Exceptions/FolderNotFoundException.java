package project.aemtaugust1.Application.Exceptions;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(String id) {
        super("Folder with id " + id + " not found");
    }
}
