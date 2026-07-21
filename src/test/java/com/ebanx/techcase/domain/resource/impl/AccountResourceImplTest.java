package com.ebanx.techcase.domain.resource.impl;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@DisplayName("Account resource layer tests")
class AccountResourceImplTest {

    private static final String RESET_ENDPOINT = "/reset";
    private static final String BALANCE_ENDPOINT = "/balance";
    private static final String EVENT_ENDPOINT = "/event";

    @BeforeEach
    void resetState() {
        given()
                .when().post(RESET_ENDPOINT)
                .then().statusCode(200);
    }

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

    @Test
    @DisplayName("Given a deposit to a non-existing account when posting event then it should create it with the amount")
    void givenDepositToNonExistingAccountWhenPostEventShouldCreateAccount() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("type", "deposit", "destination", "1", "amount", 10))
                .when().post(EVENT_ENDPOINT)
                .then().statusCode(201)
                .body("destination.id", is("1"))
                .body("destination.balance", is(10));
    }

    @Test
    @DisplayName("Given an existing account when depositing again then it should accumulate the balance")
    void givenExistingAccountWhenDepositAgainShouldAccumulateBalance() {
        deposit("1", 10);

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("type", "deposit", "destination", "1", "amount", 10))
                .when().post(EVENT_ENDPOINT)
                .then().statusCode(201)
                .body("destination.balance", is(20));
    }

    @Test
    @DisplayName("Given an existing account when getting balance then it should return 200 with the balance")
    void givenExistingAccountWhenGetBalanceShouldReturnBalance() {
        deposit("2", 20);

        String body = given()
                .queryParam("account_id", "2")
                .when().get(BALANCE_ENDPOINT)
                .then().statusCode(200)
                .extract().asString();

        assertThat(body)
                .as("Balance body should reflect the deposited amount")
                .isEqualTo("20");
    }

    @Test
    @DisplayName("Given a withdraw from a non-existing account when posting event then it should return 404 with body 0")
    void givenWithdrawFromNonExistingAccountWhenPostEventShouldReturn404() {
        String body = given()
                .contentType(ContentType.JSON)
                .body(Map.of("type", "withdraw", "origin", "2", "amount", 10))
                .when().post(EVENT_ENDPOINT)
                .then().statusCode(404)
                .extract().asString();

        assertThat(body)
                .as("Withdraw from a non-existing account should return body 0")
                .isEqualTo("0");
    }

    @Test
    @DisplayName("Given an existing account when withdrawing then it should reduce the balance")
    void givenExistingAccountWhenWithdrawShouldReduceBalance() {
        deposit("3", 20);

        given()
                .contentType(ContentType.JSON)
                .body(Map.of("type", "withdraw", "origin", "3", "amount", 5))
                .when().post(EVENT_ENDPOINT)
                .then().statusCode(201)
                .body("origin.id", is("3"))
                .body("origin.balance", is(15));
    }

    private void deposit(String accountId, int amount) {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("type", "deposit", "destination", accountId, "amount", amount))
                .when().post(EVENT_ENDPOINT)
                .then().statusCode(201);
    }
}