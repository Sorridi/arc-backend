package xyz.sorridi.arc.exceptions;

import xyz.sorridi.arc.exceptions.structure.ServerException;
import xyz.sorridi.arc.exceptions.structure.ServerExceptionType;

/**
 * Exception thrown when the payload is: null, invalid, malformed, etc.
 *
 * @see ServerException
 */
public class PayloadException extends ServerException
{
    public PayloadException(ServerExceptionType type)
    {
        super(type);
    }

    public PayloadException(ServerExceptionType type, String message)
    {
        super(type, message);
    }
}
