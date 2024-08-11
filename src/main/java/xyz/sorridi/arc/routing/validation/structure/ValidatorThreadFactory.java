package xyz.sorridi.arc.routing.validation.structure;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

public class ValidatorThreadFactory implements ThreadFactory
{
    private int count = 0;

    @Override
    public Thread newThread(@NotNull Runnable runnable)
    {
        return new Thread(runnable, "val_thread-" + ++count);
    }
}
