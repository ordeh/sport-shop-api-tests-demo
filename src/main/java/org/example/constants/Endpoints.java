package org.example.constants;

import io.restassured.http.Method;
import lombok.AllArgsConstructor;
import lombok.Data;

public class Endpoints {

    public static Endpoint PATH_ALL_CATEGORIES_GET = new Endpoint("categories", Method.GET);
    public static Endpoint PATH_PRODUCTS_BY_ID_GET = new Endpoint("catalog/products", Method.GET);

    @Data
    @AllArgsConstructor
    public static class Endpoint {

        private String path;

        private Method method;
    }

}
