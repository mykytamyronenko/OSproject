package project.aemtaugust1.Application.Utils;

import project.aemtaugust1.Domain.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSizeCalculator {

    private static final String IMAGE_BASE_PATH = "src/main/resources/static/images/";

    public static long calculateSize(File.FileType type, String content, String fileName) {
        try {
            switch (type) {
                case TEXT:
                    if (content == null) return 0;
                    return content.getBytes(StandardCharsets.UTF_8).length;

                case IMAGE:
                    Path imagePath = Path.of(IMAGE_BASE_PATH + fileName);
                    if (Files.exists(imagePath)) {
                        return Files.size(imagePath);
                    }
                    return 0;

                default:
                    return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
