package xyz.sorridi.arc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import spark.Spark;
import xyz.sorridi.arc.exceptions.structure.ServerException;
import xyz.sorridi.arc.exceptions.structure.ServerExceptionType;
import xyz.sorridi.arc.responses.MimeType;
import xyz.sorridi.arc.responses.RouteType;
import xyz.sorridi.arc.routing.ReadyRouter;
import xyz.sorridi.arc.routing.ShutdownRouter;
import xyz.sorridi.arc.routing.ValidationRouter;
import xyz.sorridi.arc.routing.WaterfallRouter;
import xyz.sorridi.arc.routing.validation.DataWorkers;
import xyz.sorridi.arc.utils.Clip;
import xyz.sorridi.arc.utils.SecureLayer;
import xyz.sorridi.arc.utils.logs.LogMessageBuilder;
import xyz.sorridi.arc.utils.logs.elem.LogLevel;
import xyz.sorridi.arc.utils.logs.elem.LogTitle;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

@Getter
public class Boot
{
    private final String[] mainArgs;

    private final Options options;
    private final ServerConfiguration serverConfiguration;

    public Boot(String[] mainArgs)
    {
        this.mainArgs = mainArgs;

        this.options = new Options();
        this.serverConfiguration = new ServerConfiguration();
    }

    public void ignite() throws Exception
    {
        initOptions();
        parseOptions(mainArgs);

        var dataWorkers = new DataWorkers(serverConfiguration);
        dataWorkers.setup();

        Main.setDataWorkers(dataWorkers);

        if (serverConfiguration.isRemote())
        {
            setupHTTPS();
        }

        setupPort();
        setupExceptionHandlers();
        setupRouting();

        Spark.awaitInitialization();
    }

    private void initOptions()
    {
        options.addOption("port", true, "The port number");
        options.addOption("val_threads", true, "The number of threads to use for validation");
        options.addOption("remote", false, "Whether the server is remote");
    }

    private void parseOptions(String[] args) throws ParseException, NumberFormatException
    {
        serverConfiguration.parseMainArgs(options, args);
    }

    private void setupExceptionHandlers()
    {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Spark.exception(RuntimeException.class, (exception, request, response) ->
        {
            throw new ServerException(ServerExceptionType.UNHANDLED, exception.getMessage());
        });

        Spark.exception(ServerException.class, (exception, request, response) ->
        {
            exception.makeJsonResponse(exception, response, gson);

            String exceptionName = exception.getType().name();
            String hash = Clip.getIdentityHash(request);

            new LogMessageBuilder()
                    .level(LogLevel.WARNING)
                    .title(LogTitle.EXCEPTION)
                    .message(Clip.quote(exceptionName) + " to " + Clip.quote(hash))
                    .write();
        });
    }

    private void setupHTTPS() throws Exception
    {
        var keyPair = SecureLayer.generateKeyPair();
        var keyPassword = SecureLayer.generateRandomPassword(16).toCharArray();
        var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        var certificate = SecureLayer.generateCertificateAndKey(keyPair);

        keyStore.load(null, keyPassword);
        keyStore.setKeyEntry("server", keyPair.getPrivate(), keyPassword, new Certificate[]{certificate});

        try (var stream = new FileOutputStream("arc-backend-key.jks"))
        {
            keyStore.store(stream, keyPassword);
        }

        Spark.secure("arc-backend-key.jks", new String(keyPassword), null, null);
    }

    private void setupPort()
    {
        Spark.port(serverConfiguration.getPort());
    }

    private void setupRouting()
    {
        // SOCKET
        new WaterfallRouter()
                .path("/waterfall")
                .routeType(RouteType.SOCKET)
                .build();

        // GET
        new ReadyRouter()
                .path("/ready")
                .mimeType(MimeType.JSON)
                .routeType(RouteType.GET)
                .build();

        new ShutdownRouter()
                .path("/shutdown")
                .mimeType(MimeType.JSON)
                .routeType(RouteType.GET)
                .allowsRemote(false)
                .build();

        // POST
        new ValidationRouter()
                .path("/validation")
                .mimeType(MimeType.JSON)
                .routeType(RouteType.POST)
                .build();
    }
}
