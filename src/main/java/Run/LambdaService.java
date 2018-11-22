package Run;

import LambdaBox.TestRequest;
import LambdaBox.TestResponse;
import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface LambdaService {
    @LambdaFunction(functionName="meLTest")
    TestResponse lambdaService(TestRequest input);
}
