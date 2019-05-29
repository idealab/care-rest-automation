package ca.freedommobile.api.framework.apis;

import ca.freedommobile.api.framework.config.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.client.methods.RequestBuilder;


import java.util.Map;

public class APIManger {

    private static APIManger apiManger;

    private APIManger() {
        RestAssured.basePath = Config.getProperty("host");
    }

    public Response doGet(String path, Map<String, Object> pathParams, Map<String, Object> queryParams){
        RequestSpecBuilder specBuilder = new RequestSpecBuilder().setBaseUri(Config.getProperty("host"));;
        specBuilder.setContentType(ContentType.JSON);

        if(null!=pathParams){
            pathParams.forEach(specBuilder::addPathParam);
        }
        if(null!=queryParams){
            queryParams.forEach(specBuilder::addQueryParam);
        }
        return RestAssured.given(specBuilder.build()).contentType(ContentType.JSON).get(path);
    }


    public  Response doPost(String url, Object body, Map<String, Object> pathParams ){

        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setContentType(ContentType.JSON).setBaseUri(Config.getProperty("host"));
        if(null!=pathParams) {
            pathParams.forEach(specBuilder::addPathParam);
            specBuilder.setBody(body);

        }
        return RestAssured.given(specBuilder.build()).post(url);

    }

    public  Response doPut(String url, Object body, Map<String, Object> pathParams ){

        RequestSpecBuilder specBuilder = new RequestSpecBuilder();
        specBuilder.setContentType(ContentType.JSON).setBaseUri(Config.getProperty("host"));
        if(null!=pathParams) {
            pathParams.forEach(specBuilder::addPathParam);
            specBuilder.setBody(body);

        }
        return RestAssured.given(specBuilder.build()).put(url);
    }


    public  Response doDelete(String path, Map<String, Object> pathParams, Map<String, Object> queryParams){
        RequestSpecBuilder specBuilder = new RequestSpecBuilder().setBaseUri(Config.getProperty("host"));
        specBuilder.setContentType(ContentType.JSON);

        if(null!=pathParams){
            pathParams.forEach(specBuilder::addPathParam);
        }
        if(null!=queryParams){
            queryParams.forEach(specBuilder::addQueryParam);
        }
        return RestAssured.given(specBuilder.build()).contentType(ContentType.JSON).delete(path);
     }

    public static APIManger getInstance(){
        synchronized (apiManger){
            if(apiManger==null){
                apiManger = new APIManger();
            }
        }
        return apiManger;
    }
}
