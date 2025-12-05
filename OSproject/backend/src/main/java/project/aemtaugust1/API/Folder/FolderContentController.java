package project.aemtaugust1.API.Folder;

import org.springframework.web.bind.annotation.*;
import project.aemtaugust1.Application.Queries.GetContent.FolderContentHandler;
import project.aemtaugust1.Application.Queries.GetContent.FolderContentOutput;

@RestController
@RequestMapping("/folders")
public class FolderContentController {
    private final FolderContentHandler folderContentHandler;

    public FolderContentController(FolderContentHandler folderContentHandler) {
        this.folderContentHandler = folderContentHandler;
    }

    @GetMapping("/root")
    public FolderContentOutput getRootContent() {
        return folderContentHandler.getRootContent();
    }

    @GetMapping("/{id}/content")
    public FolderContentOutput getFolderContent(@PathVariable("id") Long id) {
        return folderContentHandler.getFolderContent(id);
    }
}
