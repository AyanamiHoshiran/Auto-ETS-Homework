package cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil;

import cn.ayanamihoshiran.autoetshomework.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger logger = LogManager.getLogger();

    public static final String LAUNCH = "/Launch:";
    public static final String FILE = "/File:";
    public static final String DOWNLOAD = "/Download:";
    public static final String UI_ACTION = "/UI: ";
    private static final String INFO = "/Info: ";
    private static final String WARN = "/Warn: ";
    private static final String ERROR = "/Error: ";
    private static final String DEBUG = "/Debug: ";


    public static void info(Object msg) {
        logger.info(INFO + " " + msg);
    }

    public static void warn(Object msg) {
        logger.warn(WARN + " " + msg);
    }

    public static void error(Object msg) {
        logger.error(ERROR + " " + msg);
    }

    public static void debug(Object msg) {
        logger.debug(DEBUG + " " + msg);
    }

    public static void stereoLogo() {
        info("""
                                
                                _                                    \s
                     /\\        | |                                   \s
                    /  \\  _   _| |_ ___                              \s
                   / /\\ \\| | | | __/ _ \\                             \s
                  / ____ \\ |_| | || (_) |                            \s
                 /_/    \\_\\__,_|\\__\\___/                             \s
                  ______ _______ _____                               \s
                 |  ____|__   __/ ____|                              \s
                 | |__     | | | (___                                \s
                 |  __|    | |  \\___ \\                               \s
                 | |____   | |  ____) |                              \s
                 |______|  |_| |_____/                               \s
                  _    _                                         _   \s
                 | |  | |                                       | |  \s
                 | |__| | ___  _ __ ___   _____      _____  _ __| | __
                 |  __  |/ _ \\| '_ ` _ \\ / _ \\ \\ /\\ / / _ \\| '__| |/ /
                 | |  | | (_) | | | | | |  __/\\ V  V / (_) | |  |   <\s
                 |_|  |_|\\___/|_| |_| |_|\\___| \\_/\\_/ \\___/|_|  |_|\\_\\
                                                                     \s
                                                            版本:\s"""
                + Application.version + "v" +
                "\n                                           By Ayanami Hoshiran");
    }


    public static void info(Object msg, String status) {
        logger.info(status + " " + msg);
    }

    public static void warn(Object msg, String status) {
        logger.warn(status + " " + msg);
    }

    public static void error(Object msg, String status) {
        logger.error(status + " " + msg);
    }

    public static void error(Object msg, Exception e) {
        logger.error(ERROR + " " + msg, e);
    }

    public static void debug(Object msg, String status) {
        logger.debug(status + " " + msg);
    }


    public static void logTest(Object msg) {
        info(msg, "/TEST:");
        warn(msg, "/TEST:");
        error(msg, "/TEST:");
        debug(msg, "/TEST:");
    }

}
