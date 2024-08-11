package xyz.sorridi.arc.exceptions.structure;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumerates the server exception types.
 */
@Getter
@AllArgsConstructor
public enum ServerExceptionType
{
    UNHANDLED("A non-handled exception occurred"),

    /*
     * Payload exceptions
     */

    // When the payload is null
    PAYLOAD_NULL("Payload is null"),

    // When the payload has invalid fields (null, empty, etc.)
    PAYLOAD_INVALID("Payload is invalid"),

    // When the payload has missing fields
    PAYLOAD_MALFORMED("Payload is malformed"),

    // When the maximum number of requests has been reached
    RATE_LIMIT_EXCEEDED("Rate limit exceeded");

    private final String message;
}
