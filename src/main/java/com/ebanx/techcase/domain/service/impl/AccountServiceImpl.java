package com.ebanx.techcase.domain.service.impl;

import com.ebanx.techcase.domain.dto.request.EventRequest;
import com.ebanx.techcase.domain.dto.response.EventResponse;
import com.ebanx.techcase.domain.mapping.AccountMapper;
import com.ebanx.techcase.domain.service.AccountService;
import com.ebanx.techcase.infrastructure.entity.AccountEntity;
import com.ebanx.techcase.infrastructure.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.Optional;

@ApplicationScoped
public class AccountServiceImpl implements AccountService {

    private static final String DEPOSIT = "deposit";
    private static final String WITHDRAW = "withdraw";
    private static final String TRANSFER = "transfer";

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Inject
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public void reset() {
        accountRepository.reset();
    }

    @Override
    public Optional<BigDecimal> getBalance(String accountId) {
        return accountRepository.findById(accountId).map(AccountEntity::getBalance);
    }

    @Override
    public Optional<EventResponse> manageEvent(EventRequest event) {
        return switch (event.type()) {
            case DEPOSIT -> processDeposit(event);
            case WITHDRAW ->  processWithdraw(event);
            case TRANSFER -> processTransfer(event);
            default -> throw new IllegalStateException();
        };
    }

    private Optional<EventResponse> processDeposit(EventRequest depositEvent) {
        var account = accountRepository.findById(depositEvent.destination())
                .orElseGet(() ->
                        AccountEntity.builder()
                        .id(depositEvent.destination())
                        .balance(BigDecimal.ZERO)
                        .build()
                );

        account.setBalance(account.getBalance().add(depositEvent.amount()));

        return Optional.ofNullable(accountMapper.mapToDepositResponse(accountRepository.save(account)));
    }

    private Optional<EventResponse> processWithdraw(EventRequest withdrawEvent) {
        var existingAccount = accountRepository.findById(withdrawEvent.origin());
        if (existingAccount.isEmpty()) {
            return Optional.empty();
        }

        var account = existingAccount.get();
        if (account.getBalance().compareTo(withdrawEvent.amount()) < 0) {
            return Optional.empty();
        }

        account.setBalance(account.getBalance().subtract(withdrawEvent.amount()));

        return Optional.of(accountMapper.mapToWithdrawResponse(accountRepository.save(account)));
    }

    private Optional<EventResponse> processTransfer(EventRequest transferEvent) {
        var existingOriginAccount = accountRepository.findById(transferEvent.origin());
        if (existingOriginAccount.isEmpty()) {
            return Optional.empty();
        }

        var originAccount = existingOriginAccount.get();
        if (originAccount.getBalance().compareTo(transferEvent.amount()) < 0) {
            return Optional.empty();
        }

        originAccount.setBalance(originAccount.getBalance().subtract(transferEvent.amount()));

        var destinationAccount = accountRepository.findById(transferEvent.destination())
                .orElseGet(() ->
                        AccountEntity.builder()
                                .id(transferEvent.destination())
                                .balance(BigDecimal.ZERO)
                                .build()
                );
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferEvent.amount()));

        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        return Optional.of(accountMapper.mapToTransferResponse(originAccount, destinationAccount));
    }
}
