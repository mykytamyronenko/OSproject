package project.aemtaugust1.Application.Queries.ResolvePath;

import org.springframework.stereotype.Service;
import project.aemtaugust1.Infrastructure.DbEntities.DbFolder;
import project.aemtaugust1.Infrastructure.IFolderRepository;

import java.util.Optional;

@Service
public class FolderPathResolver {
    private final IFolderRepository folderRepository;

    public FolderPathResolver(IFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public Optional<DbFolder> resolve(String fullPath) {
        if (fullPath == null || fullPath.isBlank()) return Optional.empty();
        String normalized = fullPath.trim().replaceAll("^/+", "").replaceAll("/+$", "");
        if (normalized.equals("Racine")) return Optional.empty(); // la racine n’a pas d’ID

        String[] parts = normalized.split("/");

        int i = (parts.length > 0 && parts[0].equals("Racine")) ? 1 : 0;

        Long parentId = null;
        DbFolder current = null;

        for (; i < parts.length; i++) {
            String name = parts[i];
            Optional<DbFolder> next =
                    folderRepository.findByFolderNameAndParentId(name, parentId);
            if (next.isEmpty()) return Optional.empty();
            current = next.get();
            parentId = current.getFolderId();
        }
        return Optional.ofNullable(current);
    }
}
