package com.ebanx.techcase.infrastructure.repository;

import com.ebanx.techcase.infrastructure.entity.AccountEntity;

import java.util.Optional;

public interface AccountRepository {
    Optional<AccountEntity> findById(String id);
    AccountEntity save(AccountEntity account);
    void reset();
}
