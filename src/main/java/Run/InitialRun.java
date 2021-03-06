package Run;

import LambdaBox.TestRequest;
import LambdaBox.TestResponse;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import helpers.Config;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

public class InitialRun {
    private static final Logger LOGGER = Logger.getLogger(InitialRun.class.getCanonicalName());

    private static ConcurrentHashMap<String, TestResponse> allResults;

    public static void main(final String[] args) {
        new InitialRun().runAllRemote();
    }

    public void runAllRemote() {
        Config.readProperties();
        final LambdaService myService = prepareLambdaService();
        Map<String, List<String>> allTc = collectAllTestCases();
        long testCount = allTc.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .count();
        ExecutorService ex = Executors.newFixedThreadPool(toIntExact(testCount));
        AtomicInteger counter = new AtomicInteger(0);
        allResults = new ConcurrentHashMap(toIntExact(testCount));
        allResults.clear();
        allTc.entrySet().stream()
                .flatMap(entry ->
                    entry.getValue().stream()
                            .map(test -> new TestRequest(counter.incrementAndGet(), entry.getKey(), test))
                )
                .parallel()
                .forEach(req -> requestTestCase2(req, myService, ex));

        stopExecutor(ex);
        ReportHandler.writeReport(allTc, allResults);
    }

    private static void requestTestCase(final TestRequest req, final LambdaService myService,
                                        final ExecutorService ex) {
        LOGGER.info("Request test case:" + req.getTestCaseClass() + "." + req.getTestCaseName() + "()");
        LOGGER.info("Request test case id:" + req.getTestId());
        CompletableFuture.supplyAsync(() -> myService.lambdaService(req), ex).thenAccept(res -> {
            LOGGER.info("Response received for test case:" +
                    req.getTestCaseClass() + "." + req.getTestCaseName() + "() " + req.getTestId());
            allResults.put(req.getTestCaseClass() + "." + req.getTestCaseName(), res);
        });
    }

    private static void requestTestCase2(final TestRequest req, final LambdaService myService,
                                         final ExecutorService ex) {
        LOGGER.info("Request test case:" + req.getTestCaseClass() + "." + req.getTestCaseName() + "()");
        LOGGER.info("Request test case id:" + req.getTestId());
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000 + new Random().nextInt(5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new TestResponse(req.getTestId(), req.getTestCaseName(), 0, new ArrayList<>(
                    Arrays.asList("Buenos Aires", "Cordoba")), null);
        }, ex).thenAccept(res -> {
            LOGGER.info("Response received for test case:" +
                    req.getTestCaseClass() + "." + req.getTestCaseName() + "() " + req.getTestId());
            allResults.put(req.getTestCaseClass() + "." + req.getTestCaseName(), res);
        });
    }



    private static LambdaService prepareLambdaService() {
        BasicAWSCredentials credentials = new
                BasicAWSCredentials(PROPERTY.L_KEY.getValue(), PROPERTY.L_PASS.getValue());
        final LambdaService myService = LambdaInvokerFactory.builder()
                .lambdaClient(AWSLambdaClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion("us-east-1")
                        .build())
                .build(LambdaService.class);
        return myService;
    }

    private static void stopExecutor(final ExecutorService ex) {
        ex.shutdown();
        try {
            if (!ex.awaitTermination(3, TimeUnit.MINUTES)) {
                LOGGER.info("Still waiting...");
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error closing executor!", e);
        }
    }

    private static Map<String, List<String>> collectAllTestCases() {
        List<String> suitesList = new ArrayList<>();
        suitesList.add("tests.Test1");

        return suitesList.stream().collect(Collectors.toMap(name -> name, suite -> {
            List<Method> methods;
            try {
                Class<?> c = Class.forName(suite);
                methods = Arrays.asList(c.getMethods());
            } catch (ClassNotFoundException e) {
                LOGGER.severe("Test class not found:" + suite);
                methods = new ArrayList<>();
            }
            return methods.stream()
                    .filter(m -> Arrays.asList(m.getDeclaredAnnotations()).stream()
                            .anyMatch(ann -> ann instanceof Test))
                    .map(m -> m.getName())
                    .collect(Collectors.toList());
        }));

    }
}
