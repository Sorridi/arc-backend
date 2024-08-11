package xyz.sorridi.arc;

import lombok.Getter;
import org.apache.commons.cli.*;
import xyz.sorridi.arc.utils.Clip;
import xyz.sorridi.arc.utils.logs.LogMessageBuilder;

import static xyz.sorridi.arc.utils.logs.elem.LogLevel.*;
import static xyz.sorridi.arc.utils.logs.elem.LogTitle.*;

@Getter
public class ServerConfiguration
{
    private int port, valThreads;
    private boolean remote;

    /**
     * Parse the command-line arguments.
     *
     * @param options The options to parse.
     * @param args    The arguments to parse.
     * @throws ParseException        If the parsing fails.
     * @throws NumberFormatException If the port or thread number is invalid.
     */
    public void parseMainArgs(Options options, String[] args) throws ParseException, NumberFormatException
    {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        remote = cmd.hasOption("remote");

        String portValue = cmd.getOptionValue("port");

        if (portValue != null)
        {
            try
            {
                port = Integer.parseInt(portValue);
            }
            catch (NumberFormatException e)
            {
                throw new NumberFormatException("Invalid port number: " + Clip.quote(portValue));
            }
        }
        else
        {
            throw new MissingArgumentException("Missing required argument: " + Clip.quote("port"));
        }

        String valThreadsValue = cmd.getOptionValue("val_threads");

        try
        {
            valThreads = Integer.parseInt(valThreadsValue);
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("Invalid thread number: " + Clip.quote(valThreadsValue));
        }

        if (valThreads < 1 || valThreads > Runtime.getRuntime().availableProcessors())
        {
            throw new NumberFormatException("Invalid thread number: " + Clip.quote(valThreadsValue));
        }

        new LogMessageBuilder()
                .level(INFO)
                .title(CONFIG)
                .message("Mode: " + (remote ? "remote" : "local"))
                .message("Port: " + port)
                .message("Validation threads: " + valThreads)
                .write();

        Main.setConfig(this);
    }

    @Override
    public String toString()
    {
        return "ServerConfiguration{" +
                "port=" + port +
                ", threads=" + valThreads +
                ", remote=" + remote +
                '}';
    }
}
