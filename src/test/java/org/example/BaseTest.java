package org.example;

import org.example.util.PropertyUtil;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

public class BaseTest {

    static {
        RestAssured.baseURI = PropertyUtil.getProperty("base.url");
        RestAssured.urlEncodingEnabled = true;
        RestAssured.replaceFiltersWith(new ResponseLoggingFilter(), new RequestLoggingFilter());
    }

}
