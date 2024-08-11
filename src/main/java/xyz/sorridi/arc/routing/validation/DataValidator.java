package xyz.sorridi.arc.routing.validation;

import org.jetbrains.annotations.NotNull;
import xyz.sorridi.arc.routing.validation.structure.payloads.NeedValidationPayload;
import xyz.sorridi.arc.routing.validation.structure.payloads.ValidatedPayload;
import xyz.sorridi.arc.utils.Clip;
import xyz.sorridi.arc.utils.logs.LogMessageBuilder;

import static xyz.sorridi.arc.utils.logs.elem.LogLevel.*;
import static xyz.sorridi.arc.utils.logs.elem.LogTitle.*;

public class DataValidator extends DataNode<NeedValidationPayload>
{
    private final DataPusher pusher;

    public DataValidator(DataPusher pusher)
    {
        super();
        this.pusher = pusher;
    }

    @Override
    public void execute(@NotNull NeedValidationPayload payload)
    {
        String threadName = Thread.currentThread().getName();

        var id = payload.getId();
        var language = payload.getLanguage();
        var code = payload.getCode();

        // Validate the payload
        new LogMessageBuilder()
                .level(INFO)
                .title(VALIDATION)
                .message("Validating payload in " + Clip.quote(threadName))
                .message("Language: " + language + ", Code: " + code + ", ID: " + id)
                .write();

        pusher.add(new ValidatedPayload(id, language, code));
    }
}
