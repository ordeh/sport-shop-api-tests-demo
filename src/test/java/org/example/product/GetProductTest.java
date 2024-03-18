package org.example.product;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.http.HttpStatus;
import org.example.BaseTest;
import org.example.constants.Endpoints;
import org.example.constants.ProductStatus;
import org.example.constants.QueryParam;
import org.example.model.category.Category;
import org.example.model.product.Product;
import org.example.model.product.ProductContainer;
import org.example.service.CategoryService;
import org.example.util.SoftAssertWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.restassured.http.ContentType;
import io.restassured.http.Method;

public class GetProductTest extends BaseTest {

    private static final int pageSize = 6;
    private static final int page = 1;
    private static final List<Category> categories = new ArrayList<>();
    private static Category anyCategory;

    @BeforeAll
    static void prepareData() {
        categories.addAll(CategoryService.getAllCategories(false));
        anyCategory = categories.stream().findAny().orElseThrow(() -> new RuntimeException("Category was not found"));
    }

    private static List<Category> getAllCategories() {
        return categories;
    }

    private static Stream<Arguments> testParams() {
        return Stream.of(Arguments.of(QueryParam.CATEGORY_ID, anyCategory.getId()),
                    Arguments.of(QueryParam.PAGE, page), Arguments.of(QueryParam.SIZE, pageSize));
    }

    @ParameterizedTest(name = "Get product by category {0}")
    @MethodSource("getAllCategories")
    void getProductByCategoryIdTest(Category category) {
        //@formatter:off
        ProductContainer productContainer = given()
                    .accept(ContentType.JSON)
                    .queryParam(QueryParam.CATEGORY_ID, category.getId())
                    .queryParam(QueryParam.PAGE, page)
                    .queryParam(QueryParam.SIZE, pageSize)
                .when()
                    .request(Endpoints.PATH_PRODUCTS_BY_ID_GET.getMethod(), Endpoints.PATH_PRODUCTS_BY_ID_GET.getPath())
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .contentType(ContentType.JSON)
                .extract()
                    .as(ProductContainer.class);
        //@formatter:on
        assertThat(productContainer.getProducts().size()).isLessThanOrEqualTo(pageSize);
        assertThat(productContainer.getTotalCount()).isGreaterThanOrEqualTo(productContainer.getProducts().size());
        assertThat(productContainer.getDefaultCategoryId()).isGreaterThan(0);
        for (Product product : productContainer.getProducts()) {
            SoftAssertWrapper.get().assertThat(product.getId()).isNotNull();
            SoftAssertWrapper.get().assertThat(product.getName()).isNotNull();
            SoftAssertWrapper.get().assertThat(product.getPrice()).isGreaterThan(0);
            SoftAssertWrapper.get().assertThat(product.getDiscountPercent()).isGreaterThanOrEqualTo(0);
            SoftAssertWrapper.get().assertThat(product.getLastModified()).isNotNull();
            SoftAssertWrapper.get().assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
            SoftAssertWrapper.get().assertThat(product.getCatalogImageData()).isNotNull();
            SoftAssertWrapper.get().assertThat(product.getCategory()).isEqualTo(category);
        }
        SoftAssertWrapper.get().assertAll();
    }

    @ParameterizedTest
    @MethodSource("testParams")
    void getProductByIdWithoutRequiredParam(String name, Object value) {
        //@formatter:off
        given()
                    .accept(ContentType.JSON)
                    .queryParam(name, value)
                .when()
                    .request(Endpoints.PATH_PRODUCTS_BY_ID_GET.getMethod(), Endpoints.PATH_PRODUCTS_BY_ID_GET.getPath())
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
                    .queryParam(QueryParam.CATEGORY_ID, anyCategory.getId())
                    .queryParam(QueryParam.PAGE, page)
                    .queryParam(QueryParam.SIZE, pageSize)
                .when()
                    .request(Endpoints.PATH_PRODUCTS_BY_ID_GET.getMethod(), Endpoints.PATH_PRODUCTS_BY_ID_GET.getPath())
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
                    .queryParam(QueryParam.CATEGORY_ID, anyCategory.getId())
                    .queryParam(QueryParam.PAGE, page)
                    .queryParam(QueryParam.SIZE, pageSize)
                .when()
                    .request(method, Endpoints.PATH_PRODUCTS_BY_ID_GET.getPath())
                .then()
                    .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
                    .contentType(ContentType.JSON);
        //@formatter:on
    }

}
