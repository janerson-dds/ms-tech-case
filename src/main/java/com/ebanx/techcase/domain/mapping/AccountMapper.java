package com.ebanx.techcase.domain.mapping;

import com.ebanx.techcase.domain.dto.response.AccountResponse;
import com.ebanx.techcase.domain.dto.response.EventResponse;
import com.ebanx.techcase.infrastructure.entity.AccountEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccountMapper {

    public EventResponse mapToDepositResponse(AccountEntity entity) {
        return EventResponse.builder()
                .destination(
                        AccountResponse.builder()
                                .id(entity.getId())
                                .balance(entity.getBalance())
                                .build()
                )
                .build();
    }
}
