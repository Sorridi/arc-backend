package xyz.sorridi.arc.routing.validation;

import org.jetbrains.annotations.NotNull;
import xyz.sorridi.arc.routing.validation.structure.payloads.ValidatedPayload;
import xyz.sorridi.arc.utils.logs.LogMessageBuilder;
import xyz.sorridi.arc.utils.logs.elem.LogTitle;

import static xyz.sorridi.arc.utils.logs.elem.LogLevel.*;

public class DataPusher extends DataNode<ValidatedPayload>
{
    public DataPusher()
    {
        super();
    }

    @Override
    public void execute(@NotNull ValidatedPayload payload)
    {
        // Push the payload to the websocket
        new LogMessageBuilder()
                .level(INFO)
                .title(LogTitle.SOCKET)
                .message("Pushing payload to the websocket")
                .write();
    }
}
