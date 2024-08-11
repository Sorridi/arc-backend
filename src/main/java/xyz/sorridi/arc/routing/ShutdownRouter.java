package xyz.sorridi.arc.routing;

import spark.Request;
import spark.Response;
import xyz.sorridi.arc.responses.RouteBuilder;

import java.util.Timer;
import java.util.TimerTask;

public class ShutdownRouter extends RouteBuilder
{
    private static final String SHUTTING_DOWN = "Shutting down server...";

    @Override
    public Object handle(Request request, Response response)
    {
        logRequest(request);

        // Delay of 1 second to allow the response to be sent.
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                logResponse(request);
                System.exit(0);
            }
        }, 1000);

        return SHUTTING_DOWN;
    }
}
