package demowebshop;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemowebshopTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://demowebshop.tricentis.com";
    }

    String cookieValue = "20DAA0AE59284183060E668BF6F760AF9F1E005D5A356E2EC627F49" +
            "8805783EFBE9EEF9D0F0FC60A74594833E8350AB4575D6579606C550D74A4FC0" +
            "F0F119BBBFAE1EE4518AB47EC794017792E348E6CB3F5178A1D8CCD3EFA36378D" +
            "C0B36528451EAF72267F420EAA9F2F1C2AA9425BAB6D1D46D31FD4AC9D292C306" +
            "B68A7E66F3850FF5CD4523EAFDCCB7578FEF1D7F2DE325CF9DC6E3F3DE250220B0170CF";
    String answerId = "pollAnswerId=1";
    String email = "email=test111@test111.ru";
    String emailInvalid = "email=test111";

    @Test
    @DisplayName("Проверка голосования для не зарегестрированного пользователя")
    void voteUnregisteredUserTest() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body(answerId)
                .when()
                .post("/poll/vote")
                .then().log().all()
                .statusCode(200)
                .body("error", is("Only registered users can vote."));
    }

    @Test
    @DisplayName("Проверка статус кода ответа в голосовании для зарегестрированного пользователя")
    void voteRegisteredUserTest() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", cookieValue)
                .body(answerId)
                .when()
                .post("/poll/vote")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка подписки на новости")
    void checkSubscribeTest() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", cookieValue)
                .body(email)
                .when()
                .post("/subscribenewsletter")
                .then().log().all()
                .statusCode(200)
                .body("Success", is(true))
                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));
    }

    @Test
    @DisplayName("Проверка подписки на новости с невалидным email")
    void checkSubscribeWithInvalidEmailTest() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", cookieValue)
                .body(emailInvalid)
                .when()
                .post("/subscribenewsletter")
                .then().log().all()
                .statusCode(200)
                .body("Success", is(false))
                .body("Result", is("Enter valid email"));
    }
}
