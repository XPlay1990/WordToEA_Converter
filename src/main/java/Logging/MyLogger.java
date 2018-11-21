/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jan.adamczyk
 */
public class MyLogger {

    private static Logger logger;

    /**
     *
     */
    public MyLogger() {
        logger = LogManager.getLogger(MyLogger.class.getName());
    }

    private static Logger getLogger() {
        if (logger == null) {
            new MyLogger();
        }
        return logger;
    }

    /**
     *
     * @param level
     * @param msg
     */
    public static void log(Level level, String msg) {
        getLogger().log(level, msg);
        System.out.println(msg);
    }
}
