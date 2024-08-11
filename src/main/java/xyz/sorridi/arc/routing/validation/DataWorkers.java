package xyz.sorridi.arc.routing.validation;

import lombok.Getter;
import xyz.sorridi.arc.ServerConfiguration;
import xyz.sorridi.arc.routing.validation.structure.ValidatorThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataWorkers
{
    @Getter
    private final DataValidator validator;
    @Getter
    private final DataPusher pusher;

    private final ExecutorService validatorService, pusherService;

    private final int valThreads;

    public DataWorkers(ServerConfiguration configuration)
    {
        this.valThreads = configuration.getValThreads();

        this.pusher = new DataPusher();
        this.pusherService = Executors.newSingleThreadExecutor();

        this.validator = new DataValidator(pusher);
        this.validatorService = Executors.newFixedThreadPool(valThreads, new ValidatorThreadFactory());
    }

    public void setup()
    {
        this.pusher.setPerpetual(true);
        this.validator.setPerpetual(true);

        this.pusherService.execute(pusher);

        for (int i = 0; i < valThreads; i++)
        {
            this.validatorService.execute(validator);
        }
    }

}
