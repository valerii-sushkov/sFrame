package common;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import dto.TestRequest;
import dto.TestResponse;

public interface LambdaService {
    @LambdaFunction(functionName="meLTest")
    TestResponse lambdaService(TestRequest input);
}
