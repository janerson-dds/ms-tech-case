package com.ebanx.techcase.domain.resource.impl;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@DisplayName("Account resource layer tests")
class AccountResourceImplTest {

    private static final String RESET_ENDPOINT = "/reset";

    @Test
    @DisplayName("Given a reset request when posting reset then it should return 200")
    void givenResetRequestWhenPostResetShouldReturn200() {
        given()
                .when().post(RESET_ENDPOINT)
                .then().statusCode(200);
    }
}