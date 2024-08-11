package xyz.sorridi.arc.utils;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Clip
{

    /**
     * Get the JSON representation of an object.
     *
     * @param object The object to get the JSON representation of.
     * @return The JSON representation of the object.
     */
    public static String getJson(@Nullable Object object)
    {
        return new Gson().toJson(object);
    }

    /**
     * Get the JSON representation of an object.
     *
     * @param gson   The Gson instance to use.
     * @param object The object to get the JSON representation of.
     */
    public static String getJson(@NotNull Gson gson, @Nullable Object object)
    {
        return gson.toJson(object);
    }

    /**
     * Check if any of the given parameters is empty.
     *
     * @param params The parameters to check.
     * @return True if any of the parameters is empty, false otherwise.
     */
    public static boolean areEmpty(@Nullable String... params)
    {
        if (params == null)
        {
            return true;
        }

        for (String param : params)
        {
            if (param == null || param.isBlank())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if any of the given parameters is empty.
     *
     * @param objects The parameters to check.
     * @return True if any of the parameters is empty, false otherwise.
     */
    public static boolean areEmpty(@Nullable Object... objects)
    {
        if (objects == null)
        {
            return true;
        }

        for (Object object : objects)
        {
            if (object == null)
            {
                return true;
            }

            if (object instanceof String string && string.isEmpty())
            {
                return true;
            }

            if (object instanceof String[] strings && strings.length == 0)
            {
                return true;
            }

            if (object instanceof List<?> list && list.isEmpty())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Quote a string.
     *
     * @param object The object to quote.
     * @return The quoted string.
     */
    public static String quote(@NotNull Object object)
    {
        return "`" + object + "`";
    }

    /**
     * Parenthesize a string.
     *
     * @param object The object to parenthesize.
     * @return The parenthesized string.
     */
    public static String parenthesize(@NotNull Object object)
    {
        return "(" + object + ")";
    }

    /**
     * Get the identity hash of an object.
     *
     * @param object The object to get the identity hash of.
     * @return The identity hash of the object.
     */
    public static String getIdentityHash(@NotNull Object object)
    {
        return "@" + Integer.toHexString(System.identityHashCode(object));
    }

    /**
     * Get the address and port of a session.
     *
     * @param session The session to get the address and port of.
     * @return The address and port of the session.
     */
    public static String getAddressAndPort(@NotNull Session session)
    {
        var address = session.getRemoteAddress();
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

}
