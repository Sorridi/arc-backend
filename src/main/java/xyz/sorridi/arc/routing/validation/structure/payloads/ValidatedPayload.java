package xyz.sorridi.arc.routing.validation.structure.payloads;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ValidatedPayload
{
    private Long id;
    private String language;
    private List<String> validatedCode;

    // todo parsing time taken
    // todo exec time taken

    @Override
    public String toString()
    {
        return "ValidatedPayload{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", validatedCode=" + validatedCode +
                '}';
    }
}
