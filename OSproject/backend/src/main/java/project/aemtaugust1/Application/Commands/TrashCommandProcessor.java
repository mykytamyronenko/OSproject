package project.aemtaugust1.Application.Commands;

import org.springframework.stereotype.Service;
import project.aemtaugust1.Application.Commands.Delete.TrashDeleteHandler;

@Service
public class TrashCommandProcessor {
    private final TrashDeleteHandler trashDeleteHandler;

    public TrashCommandProcessor(TrashDeleteHandler trashDeleteHandler) {
        this.trashDeleteHandler = trashDeleteHandler;
    }

    public void emptyTrash() {
        trashDeleteHandler.emptyTrash();
    }
}
