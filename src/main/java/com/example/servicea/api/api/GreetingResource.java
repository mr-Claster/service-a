package com.example.servicea.api.api;

import com.example.NameClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.UUID;


@Path("/greeting")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static final Logger LOG = Logger.getLogger(GreetingResource.class.getName());

    @Inject
    @RestClient
    NameClient nameClient;

    @GET
    public Response getGreeting(@HeaderParam("X-Correlation-ID") String correlationId) {
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        try {
            String name = nameClient.getName(correlationId).name();
            var greeting = new GreetingResponse(name, "Привіт! Твоє ім’я — " + name + ".");
            return Response.ok(greeting)
                    .header("X-Correlation-ID", correlationId)
                    .build();
        } catch (ProcessingException e) {
            LOG.warnf("service-b unavailable | correlationId=%s", correlationId);
            var error = new ErrorResponse("Не вдалося отримати відповідь від service-b.");
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .header("X-Correlation-ID", correlationId)
                    .entity(error)
                    .build();
        }
    }

    public record GreetingResponse(String name, String message) {}

    public record ErrorResponse(String error) {}
}