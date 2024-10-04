package cn.ayanamihoshiran.autoetshomework.globalUtils.loggerUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger logger = LogManager.getLogger();

    public static String LAUNCH = "/Launch:";
    public static String FILE = "/File:";
    public static String ERR = "/Err:";
    public static String DOWNLOAD = "/Download:";
    public static String UI_ACTION = "/UI: ";


    public static void info(Object msg) {
        logger.info(" " + msg);
    }

    public static void warn(Object msg) {
        logger.warn(" " + msg);
    }

    public static void error(Object msg) {
        logger.error(" " + msg);
    }

    public static void debug(Object msg) {
        logger.debug(" " + msg);
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
