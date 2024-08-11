package xyz.sorridi.arc.utils.logs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.sorridi.arc.utils.logs.elem.LogLevel;
import xyz.sorridi.arc.utils.logs.elem.LogTitle;

@AllArgsConstructor
@Getter
public class LogMessageBuilder
{
    private LogLevel level;
    private LogTitle title;
    private String message;

    public LogMessageBuilder()
    {
    }

    public LogMessageBuilder level(LogLevel level)
    {
        this.level = level;
        return this;
    }

    public LogMessageBuilder title(String title)
    {
        this.title = LogTitle.valueOf(title);
        return this;
    }

    public LogMessageBuilder title(LogTitle title)
    {
        this.title = title;
        return this;
    }

    public LogMessageBuilder message(@NotNull String message)
    {
        this.message = this.message != null ? this.message + "\n" + message : message;
        return this;
    }

    public String build()
    {
        sanityCheck();

        return (title != null ? "[" + title.name() + "] " : "") + message;
    }

    public String[] multiBuild()
    {
        sanityCheck();

        String[] split = message.split("\n");

        for (int i = 0; i < split.length; i++)
        {
            split[i] = (title != null ? "[" + title.name() + "] " : "") + split[i];
        }

        return split;
    }

    public void write()
    {
        Logs.write(this);
    }

    private void sanityCheck()
    {
        if (level == null)
        {
            throw new IllegalArgumentException("Level must be set");
        }
    }

}