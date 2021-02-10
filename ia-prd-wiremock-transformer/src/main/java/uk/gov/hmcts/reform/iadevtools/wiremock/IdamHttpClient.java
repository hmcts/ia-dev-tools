package uk.gov.hmcts.reform.iadevtools.wiremock;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.*;

public class IdamHttpClient {

    OkHttpClient client = new OkHttpClient();

    public String getUserDetails(String userToken) throws IOException {

        Request request = new Request.Builder()
            .url("http://sidam-api:5000/o/userinfo")
            .addHeader("Authorization", userToken)
            .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


