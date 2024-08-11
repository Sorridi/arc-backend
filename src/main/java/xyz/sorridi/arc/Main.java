package xyz.sorridi.arc;

import lombok.Getter;
import lombok.Setter;
import xyz.sorridi.arc.routing.validation.DataWorkers;
import xyz.sorridi.arc.utils.logs.LogMessageBuilder;

import static xyz.sorridi.arc.utils.logs.elem.LogLevel.*;
import static xyz.sorridi.arc.utils.logs.elem.LogTitle.*;

public class Main
{
    @Getter
    @Setter
    private static ServerConfiguration config;

    @Getter
    @Setter
    private static DataWorkers dataWorkers;

    public static void main(String[] args)
    {
        Boot boot = new Boot(args);

        try
        {
            boot.ignite();

            new LogMessageBuilder()
                    .level(INFO)
                    .title(SERVER)
                    .message("Server started!")
                    .write();
        }
        catch (Exception e)
        {
            new LogMessageBuilder()
                    .level(ERROR)
                    .title(SERVER)
                    .message("Server failed to start :(")
                    .write();

            e.printStackTrace();

            System.exit(1);
        }
    }

}