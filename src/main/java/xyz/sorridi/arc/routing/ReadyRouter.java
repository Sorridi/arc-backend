package xyz.sorridi.arc.routing;

import spark.Request;
import spark.Response;
import xyz.sorridi.arc.responses.RouteBuilder;

public class ReadyRouter extends RouteBuilder
{
    @Override
    public Object handle(Request request, Response response)
    {
        logRequest(request);
        logResponse(request);

        return "OK";
    }
}
