package com.ebanx.techcase.domain.resource.impl;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@DisplayName("Account resource layer tests")
class AccountResourceImplTest {

    private static final String RESET_ENDPOINT = "/reset";
    private static final String BALANCE_ENDPOINT = "/balance";

    @Test
    @DisplayName("Given a reset request when posting reset then it should return 200")
    void givenResetRequestWhenPostResetShouldReturn200() {
        given()
                .when().post(RESET_ENDPOINT)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Given a non-existing account when getting balance then it should return 404 with body 0")
    void givenNonExistingAccountWhenGetBalanceShouldReturn404() {
        String body = given()
                .queryParam("account_id", "1234")
                .when().get(BALANCE_ENDPOINT)
                .then().statusCode(404)
                .extract().asString();

        assertThat(body)
                .as("Balance body for a non-existing account should be 0")
                .isEqualTo("0");
    }
}