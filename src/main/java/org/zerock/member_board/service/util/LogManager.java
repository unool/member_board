package org.zerock.member_board.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {

    static Logger logger = LoggerFactory.getLogger(LogManager.class);

    public static void log(String log){
        logger.debug("&&===================&&"+log+"&&===================&&");
    }
}
