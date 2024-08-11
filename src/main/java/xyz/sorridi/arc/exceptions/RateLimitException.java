package xyz.sorridi.arc.exceptions;

import xyz.sorridi.arc.exceptions.structure.ServerException;
import xyz.sorridi.arc.exceptions.structure.ServerExceptionType;

/**
 * Exception thrown when the rate limit has been exceeded.
 *
 * @see ServerException
 */
public class RateLimitException extends ServerException
{
    public RateLimitException(ServerExceptionType type)
    {
        super(type);
    }

    public RateLimitException(ServerExceptionType type, String message)
    {
        super(type, message);
    }
}
