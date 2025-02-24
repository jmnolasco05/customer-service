package com.jdev.customers.service.client;

import com.jdev.customers.model.CountryDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "com.jdev.customers.service.client.IRestCountryAPIClient")
public interface IRestCountryAPIClient {

    @GET
    @Path("/{countryCode}")
    @Produces(MediaType.APPLICATION_JSON)
    CountryDTO[] getCountry(@PathParam("countryCode") String countryCode);
}
