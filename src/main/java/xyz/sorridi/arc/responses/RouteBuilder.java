package xyz.sorridi.arc.responses;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.ResponseTransformer;
import spark.Route;
import spark.Spark;
import xyz.sorridi.arc.Main;
import xyz.sorridi.arc.ServerConfiguration;
import xyz.sorridi.arc.responses.transformers.JsonTransformer;
import xyz.sorridi.arc.utils.Clip;
import xyz.sorridi.arc.utils.logs.LogMessageBuilder;

import static xyz.sorridi.arc.utils.logs.elem.LogLevel.*;
import static xyz.sorridi.arc.utils.logs.elem.LogTitle.*;

public abstract class RouteBuilder implements Route
{
    private final String CLASS_NAME;
    private String REQUEST_PATH, RESPONSE_PATH;

    protected final ServerConfiguration configuration;

    @Getter
    protected String path;
    @Getter
    protected MimeType mimeType;
    @Getter
    protected RouteType routeType;
    @Getter
    protected boolean allowsRemote = true;

    protected ResponseTransformer transformer;

    public RouteBuilder()
    {
        this.CLASS_NAME = getClass().getSimpleName();
        this.configuration = Main.getConfig();
    }

    public RouteBuilder path(@NotNull String path)
    {
        this.path = path;
        this.REQUEST_PATH = Clip.quote(path) + " as ";
        this.RESPONSE_PATH = Clip.quote(path) + " to ";
        return this;
    }

    public RouteBuilder mimeType(@NotNull MimeType mimeType)
    {
        if (routeType == RouteType.SOCKET)
        {
            throw new IllegalArgumentException("Cannot set MIME type for a socket route");
        }

        this.mimeType = mimeType;
        return this;
    }

    public RouteBuilder routeType(@NotNull RouteType routeType)
    {
        this.routeType = routeType;
        return this;
    }

    public RouteBuilder allowsRemote(boolean allowsRemote)
    {
        this.allowsRemote = allowsRemote;
        return this;
    }

    public RouteBuilder transformer(@NotNull ResponseTransformer transformer)
    {
        this.transformer = transformer;
        return this;
    }

    private void sanityCheck() throws IllegalArgumentException
    {
        if (path == null)
        {
            throw new IllegalArgumentException("Path must be set");
        }

        if (routeType == null)
        {
            throw new IllegalArgumentException("Route type must be set");
        }

        boolean isSocket = routeType == RouteType.SOCKET;

        if (mimeType == null && !isSocket)
        {
            throw new IllegalArgumentException("MIME type must be set");
        }

        if (transformer != null && isSocket)
        {
            throw new IllegalArgumentException("Cannot set a transformer for a socket route");
        }
    }

    public void build() throws IllegalArgumentException
    {
        sanityCheck();

        if (mimeType != null)
        {
            Spark.before(path, (request, response) -> response.type(mimeType.getType()));

            if (transformer == null)
            {
                if (mimeType == MimeType.JSON)
                {
                    transformer = new JsonTransformer();
                }
            }
        }

        this.routeType = configuration.isRemote() && !allowsRemote ? RouteType.NONE : routeType;

        switch (this.routeType)
        {
            case NONE ->
            {
                logRemoteAccessWarning();
                return;
            }
            case GET ->
            {
                if (transformer == null)
                {
                    Spark.get(path, this);
                    return;
                }

                Spark.get(path, this, transformer);
            }
            case POST ->
            {
                if (transformer == null)
                {
                    Spark.post(path, this);
                    return;
                }

                Spark.post(path, this, transformer);
            }
            case SOCKET -> Spark.webSocket(path, this);
        }

        logRouteSetUp();
    }

    /**
     * Logs a message with the request path.
     *
     * @param request The message to log.
     */
    protected void logRequest(Request request)
    {
        String hash = Clip.getIdentityHash(request);
        String quote = Clip.quote(hash);

        new LogMessageBuilder()
                .level(INFO)
                .title(REQUEST)
                .message(REQUEST_PATH + quote)
                .write();
    }

    /**
     * Logs a message with the response path.
     *
     * @param toRequest The request this response is for.
     */
    protected void logResponse(Request toRequest)
    {
        String hash = Clip.getIdentityHash(toRequest);

        new LogMessageBuilder()
                .level(INFO)
                .title(RESPONSE)
                .message(RESPONSE_PATH + Clip.quote(hash))
                .write();
    }

    private void logRouteSetUp()
    {
        new LogMessageBuilder()
                .level(INFO)
                .title(ROUTE)
                .message("Set up " + Clip.quote(CLASS_NAME) + " route for " + Clip.quote(path) + " " + Clip.parenthesize(
                        routeType))
                .write();
    }

    private void logRemoteAccessWarning()
    {
        new LogMessageBuilder()
                .level(INFO)
                .title(ROUTE)
                .message("Server will not route " + Clip.quote(CLASS_NAME) + " since it does not allow remote access")
                .write();
    }

    @Override
    public String toString()
    {
        return "SparkRoute{" +
                "path='" + path + '\'' +
                ", routeType=" + routeType +
                '}';
    }
}
