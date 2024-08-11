package xyz.sorridi.arc.responses;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import spark.Response;
import xyz.sorridi.arc.utils.Clip;
import xyz.sorridi.arc.utils.HTTPStatus;

@AllArgsConstructor
@Getter
public class Outcome
{
    private transient final RouteBuilder route;
    private transient final Response response;
    private transient final HTTPStatus status;

    private final Object data;

    public void applyJsonPartial()
    {
        response.type("application/json");
        response.status(status.getCode());
    }

    public void applyJsonFull(@Nullable Gson gson)
    {
        var json = gson == null ? Clip.getJson(data) : Clip.getJson(gson, data);
        response.body(json);

        applyJsonPartial();
    }

    public void applyJsonFull()
    {
        applyJsonFull(null);
    }

    public String asJson()
    {
        applyJsonPartial();
        return Clip.getJson(data);
    }

}
