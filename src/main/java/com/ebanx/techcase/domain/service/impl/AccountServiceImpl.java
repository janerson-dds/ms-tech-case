package com.ebanx.techcase.domain.service.impl;

import com.ebanx.techcase.domain.service.AccountService;
import com.ebanx.techcase.infrastructure.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Inject
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void reset() {
        accountRepository.reset();
    }
}
