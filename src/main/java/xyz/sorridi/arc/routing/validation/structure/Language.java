package xyz.sorridi.arc.routing.validation.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language
{
    IJVM("IJVM"),
    a8088("8088");

    private final String name;

    /**
     * Check if the given value is a valid language.
     *
     * @param value The value to check.
     * @return True if the value is a valid language, false otherwise.
     */
    public static boolean contains(String value)
    {
        for (var language : values())
        {
            if (language.getName().equals(value))
            {
                return true;
            }
        }

        return false;
    }

}
