package org.example.category;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.example.constants.Endpoints;
import org.example.constants.QueryParam;
import org.example.model.category.Category;
import org.example.util.SoftAssertWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Method;

public class GetAllCategoriesTest extends BaseTest {

    @ParameterizedTest(name = "Get category with image state {0}")
    @ValueSource(booleans = {true, false})
    void getAllCategoriesTest(boolean loadImage) {
        //@formatter:off
        List<Category> categories = given()
                    .accept(ContentType.JSON)
                    .queryParam(QueryParam.LOAD_IMAGE, loadImage)
                .when()
                    .request(Endpoints.PATH_ALL_CATEGORIES_GET.getMethod(), Endpoints.PATH_ALL_CATEGORIES_GET.getPath())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                .extract()
                    .as(new TypeRef<>() {});
        //@formatter:on
        assertThat(categories).isNotEmpty();
        for (Category user : categories) {
            SoftAssertWrapper.get().assertThat(user.getId()).isNotNull();
            SoftAssertWrapper.get().assertThat(user.getName()).isNotNull();
            SoftAssertWrapper.get().assertThat(user.getDescription()).isNotNull();
            if (loadImage) {
                SoftAssertWrapper.get().assertThat(user.getImage()).isNotNull();
            } else {
                SoftAssertWrapper.get().assertThat(user.getImage()).isNull();
            }
        }
        SoftAssertWrapper.get().assertAll();
    }

    @Test
    void getAllCategoriesWithoutRequiredParam() {
        //@formatter:off
        given()
                    .accept(ContentType.JSON)
                .when()
                    .request(Endpoints.PATH_ALL_CATEGORIES_GET.getMethod(), Endpoints.PATH_ALL_CATEGORIES_GET.getPath())
                .then()
                    .statusCode(HttpStatus.SC_BAD_REQUEST)
                    .contentType(ContentType.JSON);
        //@formatter:on
    }

    @ParameterizedTest(name = "Check request with content type ''{0}''")
    @EnumSource(value = ContentType.class, names = {"TEXT", "HTML", "XML"})
    void incorrectAcceptTypeTest(ContentType contentType) {
        //@formatter:off
        given()
                    .accept(contentType)
                    .queryParam(QueryParam.LOAD_IMAGE, false)
                .when()
                    .request(Endpoints.PATH_ALL_CATEGORIES_GET.getMethod(), Endpoints.PATH_ALL_CATEGORIES_GET.getPath())
                .then()
                    .statusCode(HttpStatus.SC_NOT_ACCEPTABLE);
        //@formatter:on
    }

    @ParameterizedTest(name = "Check request with http method {0}")
    @EnumSource(value = Method.class, names = {"POST", "DELETE", "PUT"})
    void notAllowedRequestTest(Method method) {
        //@formatter:off
        given()
                    .accept(ContentType.JSON)
                    .queryParam(QueryParam.LOAD_IMAGE, false)
                .when()
                    .request(method, Endpoints.PATH_ALL_CATEGORIES_GET.getPath())
                .then()
                    .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
                    .contentType(ContentType.JSON);
        //@formatter:on
    }
}
