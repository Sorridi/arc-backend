package xyz.sorridi.arc.routing.validation;

import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public abstract class DataNode<T> implements Runnable
{
    private final Queue<T> queue;
    private final Semaphore semaphore;

    @Setter
    private boolean perpetual;

    public DataNode()
    {
        this.queue = new ConcurrentLinkedQueue<>();
        this.semaphore = new Semaphore(0);
        this.perpetual = false;
    }

    public DataNode(Queue<T> queue, Semaphore semaphore)
    {
        this.queue = queue;
        this.semaphore = semaphore;
        this.perpetual = false;
    }

    /**
     * Acquire the semaphore.
     */
    @SneakyThrows
    protected void acquire()
    {
        semaphore.acquire();
    }

    /**
     * Release the semaphore.
     */
    protected void release()
    {
        semaphore.release();
    }

    /**
     * Add a payload to the deque.
     *
     * @param payload the payload to add
     */
    public void add(@NotNull T payload)
    {
        queue.add(payload);
        release();
    }

    /**
     * Remove a payload from the deque.
     *
     * @return the payload removed
     */
    protected @Nullable T poll()
    {
        return queue.poll();
    }

    public abstract void execute(@NotNull T payload);

    @Override
    public void run()
    {
        do
        {
            acquire();

            var payload = poll();

            if (payload != null)
            {
                execute(payload);
            }
        }
        while (perpetual);
    }
}
