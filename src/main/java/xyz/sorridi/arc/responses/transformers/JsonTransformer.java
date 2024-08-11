package xyz.sorridi.arc.responses.transformers;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import spark.ResponseTransformer;

/**
 * Transformer of responses in JSON format.
 *
 * @see ResponseTransformer
 */
@AllArgsConstructor
public class JsonTransformer implements ResponseTransformer
{
    private final Gson gson;

    public JsonTransformer()
    {
        this(new Gson());
    }

    @Override
    public String render(Object model)
    {
        return gson.toJson(model);
    }
}