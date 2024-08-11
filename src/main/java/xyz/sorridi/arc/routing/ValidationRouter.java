package xyz.sorridi.arc.routing;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import xyz.sorridi.arc.Main;
import xyz.sorridi.arc.exceptions.PayloadException;
import xyz.sorridi.arc.exceptions.structure.ServerExceptionType;
import xyz.sorridi.arc.responses.RouteBuilder;
import xyz.sorridi.arc.routing.validation.DataValidator;
import xyz.sorridi.arc.routing.validation.structure.Language;
import xyz.sorridi.arc.routing.validation.structure.payloads.NeedValidationPayload;
import xyz.sorridi.arc.utils.Clip;

public class ValidationRouter extends RouteBuilder
{
    private final DataValidator dataValidator;

    public ValidationRouter()
    {
        this.dataValidator = Main.getDataWorkers().getValidator();
    }

    @Override
    public Object handle(Request request, Response response)
    {
        logRequest(request);

        NeedValidationPayload payload;

        try
        {
            payload = new Gson().fromJson(request.body(), NeedValidationPayload.class);
        }
        catch (Exception e)
        {
            throw new PayloadException(ServerExceptionType.PAYLOAD_MALFORMED, "JSON request structure");
        }

        if (payload == null)
        {
            throw new PayloadException(ServerExceptionType.PAYLOAD_NULL);
        }

        var id = payload.getId();
        var language = payload.getLanguage();
        var code = payload.getCode();

        if (Clip.areEmpty(language, code, id))
        {
            throw new PayloadException(ServerExceptionType.PAYLOAD_INVALID, "`id` or `language` or `code` are empty");
        }

        if (!Language.contains(language))
        {
            throw new PayloadException(ServerExceptionType.PAYLOAD_INVALID, "`language` is unknown");
        }

        dataValidator.add(payload);

        logResponse(request);

        return "OK";
    }
}
