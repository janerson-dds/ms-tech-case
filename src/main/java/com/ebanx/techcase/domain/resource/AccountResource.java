package com.ebanx.techcase.domain.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;

@Tag(name = "AccountResource", description = "Set of endpoints related to account transactions")
@Path("/")
public interface AccountResource {

    @POST
    @Operation(
            summary = "Reset the information on completed transactions.",
            description = "Resets transaction information for the accounts to default values."
    )
    @APIResponse(
        responseCode = "200",
        description = "The accounts information have been successfully reset to default values."
    )
    @Path("/reset")
    Response reset();

    @GET
    @Operation(
            summary = "Get balance from an account.",
            description = "Retrieve the balance amount from an account."
    )
    @Parameter(
            name = "account_id",
            description = "Account identification",
            example = "1"
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Retrieved successfully the account balance amount.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = BigDecimal.class),
                                    example = "1"
                            )
                    ),
                    @APIResponse(
                            responseCode = "404",
                            description = "Account does not exist for the identification specified.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = BigDecimal.class)
                            )
                    )
            }
    )
    @Path("/balance")
    Response getBalance(@QueryParam("account_id") String accountId);
}
