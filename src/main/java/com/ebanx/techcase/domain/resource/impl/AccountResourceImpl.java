package com.ebanx.techcase.domain.resource.impl;

import com.ebanx.techcase.domain.resource.AccountResource;
import com.ebanx.techcase.domain.service.AccountService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;

@ApplicationScoped
public class AccountResourceImpl implements AccountResource {

    private final AccountService accountService;

    @Inject
    public AccountResourceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Response reset() {
        accountService.reset();
        return Response.ok().build();
    }

    @Override
    public Response getBalance(String accountId) {
        return accountService.getBalance(accountId)
                .map(balance -> Response.ok(balance).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).entity(BigDecimal.ZERO).build());
    }
}
