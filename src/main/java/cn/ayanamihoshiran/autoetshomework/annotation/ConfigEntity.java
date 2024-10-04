package cn.ayanamihoshiran.autoetshomework.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigEntity {
    String name();
    String filePath();
}
