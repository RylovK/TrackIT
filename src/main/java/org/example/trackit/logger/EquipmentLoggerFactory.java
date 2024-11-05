package org.example.trackit.logger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class EquipmentLoggerFactory {

    public Logger getLogger(String partNumber, String serialNumber) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d %p [%c] - %m%n");
        encoder.start();

        String fileName = String.format("equipment_%s_%s.log", partNumber, serialNumber);
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(context);
        fileAppender.setName("fileLogger-" + partNumber + "_" + serialNumber);
        fileAppender.setFile(Paths.get("logs", fileName).toString());
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        Logger logger = context.getLogger("EquipmentLogger-" + partNumber + "_" + serialNumber);
        logger.addAppender(fileAppender);
        logger.setAdditive(false);

        return logger;
    }
}
