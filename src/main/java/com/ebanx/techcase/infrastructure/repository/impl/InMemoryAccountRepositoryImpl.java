package com.ebanx.techcase.infrastructure.repository.impl;

import com.ebanx.techcase.infrastructure.repository.AccountRepository;
import com.ebanx.techcase.infrastructure.entity.AccountEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemoryAccountRepositoryImpl implements AccountRepository {

    private final Map<String, AccountEntity> store = new ConcurrentHashMap<>();

    @Override
    public Optional<AccountEntity> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        store.put(account.getId(), account);
        return store.get(account.getId());
    }

    @Override
    public void reset() {
        store.clear();
    }
}
