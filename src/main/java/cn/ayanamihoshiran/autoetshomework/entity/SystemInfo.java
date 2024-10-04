package cn.ayanamihoshiran.autoetshomework.entity;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.FileSystems;

@Setter
@Getter
public class SystemInfo {
    private final String systemVersion;
    private final String fileSeparator;
    private final String javaHome;
    private final String javaVersion;

    public SystemInfo() {
        this.systemVersion = System.getProperty("os.name");
        this.fileSeparator = FileSystems.getDefault().getSeparator();
        this.javaHome = System.getProperty("java.home");
        this.javaVersion = System.getProperty("java.version");
    }
}
