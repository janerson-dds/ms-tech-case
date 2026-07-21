package com.ebanx.techcase.infrastructure.repository.impl;

import com.ebanx.techcase.infrastructure.entity.AccountEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@DisplayName("In memory repository layer tests")
class InMemoryAccountRepositoryImplTest {

    private static final String ACCOUNT_ID = "100";

    private InMemoryAccountRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepositoryImpl();
    }

    @Test
    @DisplayName("Given an existing account when finding by id then it should return the account")
    void givenExistingAccountWhenFindByIdShouldReturnAccount() {
        repository.save(buildAccount(ACCOUNT_ID, BigDecimal.TEN));

        Optional<AccountEntity> result = repository.findById(ACCOUNT_ID);

        assertThat(result)
                .as("Should return the previously saved account")
                .isPresent();
        assertThat(result.get().getBalance())
                .as("The returned balance should match the saved balance")
                .isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test
    @DisplayName("Given a non-existing account when finding by id then it should return empty")
    void givenNonExistingAccountWhenFindByIdShouldReturnEmpty() {
        Optional<AccountEntity> result = repository.findById("999");

        assertThat(result)
                .as("Looking up a non-existing id should return no account")
                .isEmpty();
    }

    @Test
    @DisplayName("Given a new account when saving then it should persist and be retrievable")
    void givenNewAccountWhenSaveShouldPersistAndBeRetrievable() {
        repository.save(buildAccount(ACCOUNT_ID, BigDecimal.TEN));

        assertThat(repository.findById(ACCOUNT_ID))
                .as("The saved account should be available for lookup")
                .isPresent();
    }

    @Test
    @DisplayName("Given an already existing account when saving with the same id then it should update the balance without duplicating")
    void givenExistingAccountWhenSaveWithSameIdShouldUpdateBalance() {
        repository.save(buildAccount(ACCOUNT_ID, BigDecimal.TEN));
        repository.save(buildAccount(ACCOUNT_ID, BigDecimal.valueOf(20)));

        assertThat(repository.findById(ACCOUNT_ID).orElseThrow().getBalance())
                .as("Saving with the same id should overwrite the previous balance")
                .isEqualByComparingTo(BigDecimal.valueOf(20));
    }

    @Test
    @DisplayName("Given a populated store when resetting then it should remove all accounts")
    void givenPopulatedStoreWhenResetShouldRemoveAllAccounts() {
        repository.save(buildAccount(ACCOUNT_ID, BigDecimal.TEN));
        repository.save(buildAccount("200", BigDecimal.valueOf(50)));

        repository.reset();

        assertThat(repository.findById(ACCOUNT_ID))
                .as("After reset the first account should no longer exist")
                .isEmpty();
        assertThat(repository.findById("200"))
                .as("After reset the second account should no longer exist")
                .isEmpty();
    }

    private AccountEntity buildAccount(String id, BigDecimal balance) {
        return AccountEntity.builder()
                .id(id)
                .balance(balance)
                .build();
    }
}
