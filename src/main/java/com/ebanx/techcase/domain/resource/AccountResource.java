package com.ebanx.techcase.domain.resource;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
}
