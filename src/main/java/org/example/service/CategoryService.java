package org.example.service;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.apache.http.HttpStatus;
import org.example.constants.Endpoints;
import org.example.constants.QueryParam;
import org.example.model.category.Category;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

public class CategoryService {

    /**
     *
     * @param loadImage if the value is true, categories are returned with pictures, otherwise without pictures.
     * @return list of all categories
     */
    public static List<Category> getAllCategories(boolean loadImage) {
        //@formatter:off
        return given()
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
    }

}
