package com.ebanx.techcase.domain.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    void reset();
    Optional<BigDecimal> getBalance(String accountId);
}
