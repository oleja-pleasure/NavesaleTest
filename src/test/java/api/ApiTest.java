package api;

import api.models.PetInfo;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

public class ApiTest {
    @Test
    void test02() {
        int delay = 3;
        Instant start = Instant.now();
        String date = given()
                .post(format("https://httpbin.org/delay/%d", delay))
                .then()
                .statusCode(200)
                .extract().header("date");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd LLL yyyy HH:mm:ss z", Locale.US);
        Instant end = ZonedDateTime.parse(date, inputFormatter).toInstant();
        assertTrue(Duration.between(start, end).getSeconds() >= delay);
    }

    @Test
    void test03() {
        String status = "sold";
        String resp = given()
                .get(format("https://petstore.swagger.io/v2/pet/findByStatus?status=%s",status))
                .then()
                .statusCode(200)
                .extract().response().asString();

        List<PetInfo> pets = from(resp).getList(".", PetInfo.class);

        Set<String> names = new HashSet<>();
        for (PetInfo pet: pets) {
            names.add(pet.getName());
        }

        for (Object value : names) {
            System.out.println(value);
        }
    }
}
