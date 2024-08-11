package xyz.sorridi.arc.routing.validation.structure.payloads;

import lombok.Getter;

import java.util.List;

@Getter
public class NeedValidationPayload
{
    private Long id;
    private String language;
    private List<String> code;

    @Override
    public String toString()
    {
        return "NeedValidationPayload{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", code=" + code +
                '}';
    }
}
