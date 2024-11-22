package cn.ayanamihoshiran.autoetshomework.tools.getExamAnswer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class LatestModifiedFolder {

    public static String getLatestModifiedFolder(Path directoryPath) {
        File directory = directoryPath.toFile();
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory");
        }

return Arrays.stream(Objects.requireNonNull(directory.listFiles(File::isDirectory)))
    .filter(file -> !file.getName().equals("common"))
    .max(Comparator.comparingLong(file -> {
        try {
            return Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime().toMillis();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }))
    .map(File::getName)
    .orElse(null);
    }


}