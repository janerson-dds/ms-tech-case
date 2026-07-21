package com.ebanx.techcase.domain.service;

import com.ebanx.techcase.domain.dto.request.EventRequest;
import com.ebanx.techcase.domain.dto.response.EventResponse;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    void reset();
    Optional<BigDecimal> getBalance(String accountId);
    Optional<EventResponse> manageEvent(EventRequest event);
}
