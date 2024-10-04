package cn.ayanamihoshiran.autoetshomework.manager;

import cn.ayanamihoshiran.autoetshomework.annotation.ConfigEntity;
import cn.ayanamihoshiran.autoetshomework.globalUtils.config.Config;
import cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil.Log;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class ConfigManager {
    public static Object loadConfig(Object config) {
        // 判断配置类是否被@ConfigEntity注解修饰
        if (!config.getClass().isAnnotationPresent(ConfigEntity.class)) {
            Log.error("Config class must be annotated with @ConfigEntity");
            return null;
        }

        // 获取配置文件路径
        String filePath = config.getClass().getAnnotation(ConfigEntity.class).filePath();
        // 获取配置文件名
        String fileName = config.getClass().getAnnotation(ConfigEntity.class).name() + ".yml";

        // 创建配置文件对象
        File file = new File(filePath + File.separator + fileName);

        // 创建Yaml对象，设置Representer
        Yaml yaml = createYaml();

        try {
            // 如果配置文件不存在，则创建配置文件
            if (!file.exists()) {

                new File(filePath).mkdirs();
                // 创建配置对象
                Config newConfig = Config.builder()
                        .record_coordinates(new ArrayList<>())
                        .build();

                // 将配置对象写入配置文件
                yaml.dump(newConfig, new FileWriter(file));
            }

            // 从配置文件中加载配置对象
            return yaml.loadAs(new FileReader(file), Config.class);

        } catch (Exception e) {
            Log.error("Failed to load config: " + e.getMessage());
        }
        return null;
    }


    // 保存配置文件
    public static boolean saveConfig(Object config) {

        // 获取配置文件路径
        String filePath = config.getClass().getAnnotation(ConfigEntity.class).filePath();
        // 获取配置文件名
        String fileName = config.getClass().getAnnotation(ConfigEntity.class).name()+".yml";

        // 创建配置文件对象
        File file = new File(filePath + File.separator + fileName);

        // 创建Yaml对象，设置Representer
        Yaml yaml = createYaml();

        try {
            // 如果配置文件不存在，则创建配置文件
            yaml.dump(config, new FileWriter(file));

            return true;

        } catch (Exception e) {
            Log.error("Failed to load config: " + e.getMessage());
        }
        return false;
    }


    private static Yaml createYaml() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setPrettyFlow(true);

        Representer representer = new Representer(dumperOptions);
        representer.addClassTag(Config.class, Tag.MAP);

        return new Yaml(representer, dumperOptions);
    }
}
