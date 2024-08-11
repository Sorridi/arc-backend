package xyz.sorridi.arc.routing;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Request;
import spark.Response;
import xyz.sorridi.arc.Main;
import xyz.sorridi.arc.responses.RouteBuilder;
import xyz.sorridi.arc.routing.validation.DataWorkers;
import xyz.sorridi.arc.utils.Clip;
import xyz.sorridi.arc.utils.logs.LogMessageBuilder;

import java.util.ArrayList;
import java.util.Collection;

import static xyz.sorridi.arc.utils.logs.elem.LogLevel.*;
import static xyz.sorridi.arc.utils.logs.elem.LogTitle.*;

@WebSocket
public class WaterfallRouter extends RouteBuilder
{
    private final Collection<Session> sessions;
    private final DataWorkers dataWorkers;

    public WaterfallRouter()
    {
        this.sessions = new ArrayList<>();
        this.dataWorkers = Main.getDataWorkers();
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        String address = Clip.getAddressAndPort(session);

        new LogMessageBuilder()
                .level(INFO)
                .title(SOCKET)
                .message("Connected " + Clip.quote(address))
                .write();

        sessions.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason)
    {
        String address = Clip.getAddressAndPort(session);
        boolean noReason = Clip.areEmpty(reason);

        String message = "Disconnected " + Clip.quote(address);

        if (!noReason)
        {
            message += " - reason: " + Clip.quote(reason);
        }

        new LogMessageBuilder()
                .level(INFO)
                .title(SOCKET)
                .message(message)
                .write();

        sessions.remove(session);
    }

    /* unused */
    @Override
    public Object handle(Request request, Response response) throws Exception
    {
        return null;
    }
}
