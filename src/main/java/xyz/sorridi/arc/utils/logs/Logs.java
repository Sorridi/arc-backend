package xyz.sorridi.arc.utils.logs;

import xyz.sorridi.arc.utils.logs.elem.LogLevel;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logs
{
    private static final Logger LOGGER = Logger.getLogger(Logs.class.getName());

    static
    {
        ConsoleHandler handler = new ConsoleHandler();

        handler.setFormatter(new SimpleFormatter()
        {
            private static final String FORMAT = "[%1$tF %1$tT.%1$tL] [%2$s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr)
            {
                return String.format(FORMAT,
                                     new Date(lr.getMillis()),
                                     lr.getLevel().getLocalizedName(),
                                     lr.getMessage()
                );
            }
        });

        LOGGER.addHandler(handler);
        LOGGER.setUseParentHandlers(false);
    }

    public static void write(LogMessageBuilder builder)
    {
        String[] messages = builder.multiBuild();
        LogLevel level = builder.getLevel();

        for (String row : messages)
        {
            switch (level)
            {
                case INFO -> LOGGER.info(row);
                case WARNING -> LOGGER.warning(row);
                case ERROR -> LOGGER.severe(row);
                case DEBUG -> LOGGER.fine(row);
            }
        }
    }

}
