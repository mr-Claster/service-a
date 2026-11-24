package com.example;


import com.example.servicea.client.NameResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/name")
@RegisterRestClient(configKey = "service-b")
public interface NameClient {

    @GET
    NameResponse getName(@HeaderParam("X-Correlation-ID") String correlationId);
}
