package com.venturessoft.human.utils;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FakeInterceptor implements Interceptor {
    private final static String TAG = FakeInterceptor.class.getSimpleName();
    private static int index = 0;

    public static  Boolean IS_ENABLE = false;
    static ArrayList codesList = new ArrayList<Integer>(){
        {
            /*
            add(100);
            add(102);
            add(103);
            add(200);
            add(201);
            add(202);
            add(203);
            add(204);
            add(205);
            add(206);
            add(300);
            add(302);
            add(303);
            add(304);
            add(305);
            add(306);
            add(307);
            add(308);

             */
            add(400);
            add(401);
            add(402);
            add(403);
            add(404);
            add(405);
            add(406);
            add(407);
            add(408);
            add(409);
            add(410);
            add(411);
            add(412);
            add(413);
            add(414);
            add(415);
            add(416);
            add(417);
            add(418);
            add(421);
            add(422);
            add(423);
            add(425);
            add(426);
            add(428);
            add(429);
            add(431);
            add(451);
            add(500);
            add(501);
            add(502);
            add(503);
            add(504);
            add(505);
            add(506);
            add(507);
            add(511);
            add(520);
            add(522);
            add(524);
        }
    };

    // FAKE RESPONSES.
    private final static String ET2016 = "{\n" + "  \"codigo\" : \"ET216\"\n" + "}";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        if(IS_ENABLE) {
            String responseString = ET2016;

            response = new Response.Builder()
                    .code((int)codesList.get(index))
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                    .addHeader("content-type", "application/json")
                    .build();
            index++;
            if(index == codesList.size()){
                index = 0;
            }
        }
        else {
            response = chain.proceed(chain.request());
        }
        return response;
    }
}