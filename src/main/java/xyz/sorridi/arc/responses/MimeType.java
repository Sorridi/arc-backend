package xyz.sorridi.arc.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumerates the MIME types.
 */
@Getter
@AllArgsConstructor
public enum MimeType
{
    JSON("application/json"),
    HTML("text/html"),
    TEXT("text/plain"),
    NONE("none");

    private final String type;
}
