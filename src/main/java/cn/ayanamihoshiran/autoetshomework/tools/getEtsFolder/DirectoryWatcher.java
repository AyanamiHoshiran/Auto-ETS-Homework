package cn.ayanamihoshiran.autoetshomework.tools.getEtsFolder;

import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;

import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcher {
    public static Path watcherEtsFolder() throws Exception {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Path.of(System.getProperty("user.home")).resolve("AppData/Roaming"); // Replace with the directory you want to monitor
        path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        Log.info("Monitoring directory for changes...");

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                Log.info("Event kind: " + event.kind() + ". File affected: " + event.context() + ".");
                if (event.kind().equals(ENTRY_MODIFY) && path.resolve(event.context().toString()).toFile().isDirectory()) {
                    return path.resolve(event.context().toString());
                }
            }
            key.reset();
        }
        return null;
    }
}