package io.micronaut.gcp.function.http;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.env.Environment;
import io.micronaut.function.http.ServerlessExchange;
import io.micronaut.function.http.ServerlessHttpHandler;

import javax.annotation.Nonnull;

/**
 * Entry point into the Micronaut + GCP integration.
 *
 * @author graemerocher
 * @since 1.2.0
 */
public class HttpServerFunction extends ServerlessHttpHandler<HttpRequest, HttpResponse> implements HttpFunction {


    @Override
    protected ServerlessExchange createExchange(HttpRequest request, HttpResponse response) {
        final GoogleFunctionHttpResponse<Object> res = new GoogleFunctionHttpResponse<>(response, getMediaTypeCodecRegistry());
        final GoogleFunctionHttpRequest<Object> req = new GoogleFunctionHttpRequest<>(request, res);

        return new ServerlessExchange(req, res);
    }

    @Override
    protected ServerlessExchange createExchange(io.micronaut.http.HttpRequest<? super Object> request, HttpResponse response) {
        return new ServerlessExchange(request, new GoogleFunctionHttpResponse<>(response, getMediaTypeCodecRegistry()));
    }

    @Nonnull
    @Override
    protected ApplicationContextBuilder newApplicationContextBuilder() {
        final ApplicationContextBuilder builder = super.newApplicationContextBuilder();
        builder.deduceEnvironment(false);
        builder.environments(Environment.GOOGLE_COMPUTE);
        return builder;
    }
}