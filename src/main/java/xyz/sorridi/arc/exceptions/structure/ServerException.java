package xyz.sorridi.arc.exceptions.structure;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spark.Response;
import xyz.sorridi.arc.responses.Outcome;
import xyz.sorridi.arc.utils.Clip;
import xyz.sorridi.arc.utils.HTTPStatus;

@Getter
public class ServerException extends RuntimeException
{
    @SerializedName("exceptionType")
    @Expose
    private final ServerExceptionType type;

    @SerializedName("exceptionMessage")
    @Expose
    private String message;

    public ServerException(@NotNull ServerExceptionType type, @Nullable String message)
    {
        super(type.getMessage());

        this.type = type;
        this.message = type.getMessage();

        if (!Clip.areEmpty(message))
        {
            this.message += ": " + message;
        }
    }

    public ServerException(@NotNull ServerExceptionType type)
    {
        this(type, null);
    }

    /**
     * Make a JSON response with the exception message.
     *
     * @param originException The exception that originated this exception
     * @param response        The response object
     * @param gson            The Gson object
     */
    public void makeJsonResponse(Exception originException, Response response, Gson gson)
    {
        this.message = originException.getMessage();

        var outcome = new Outcome(null, response, HTTPStatus.INTERNAL_SERVER_ERROR, message);
        outcome.applyJsonFull(gson);
    }

}
