package com.ebanx.techcase.domain.resource.impl;

import com.ebanx.techcase.domain.resource.AccountResource;
import com.ebanx.techcase.domain.service.AccountService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

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
}
