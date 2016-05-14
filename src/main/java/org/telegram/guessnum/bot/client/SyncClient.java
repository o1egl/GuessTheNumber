package org.telegram.guessnum.bot.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.apache.log4j.Logger;
import org.telegram.guessnum.bot.HttpLoggingInterceptor;
import org.telegram.guessnum.bot.UpdatesReceiver;
import org.telegram.guessnum.bot.request.Request;
import org.telegram.guessnum.bot.response.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SyncClient implements Client {

    private final String apiHost;
    private String accessToken;
    private OkHttpClient client;
    private Gson gson;

    private static Logger log = Logger.getLogger(UpdatesReceiver.class.getName());

    public SyncClient(String apiHost, String accessToken, int timeout) {
        this.apiHost = apiHost;
        this.accessToken = accessToken;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
        gson = new GsonBuilder().serializeNulls().setLenient().create();
    }

    @Override
    public <T extends Request, R extends Response> R execute(T req, Class<R> clazz) {

        HttpUrl.Builder urlBuilder = new HttpUrl.Builder().scheme("https").host("api.telegram.org")
                .addPathSegment("bot"+accessToken)
                .addPathSegment(req.getMethod());
        for(Map.Entry<String, Object> entry : req.getParameters().entrySet()){
            urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
        }

        okhttp3.Request request = new okhttp3.Request.Builder().url(urlBuilder.build()).build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            //final TypeToken<R> typeToken = new TypeToken<R>(getClass()) { };
            //log.info(typeToken.getType().toString());
            return gson.fromJson(response.body().string(), clazz);
        } catch (IOException e) {
            log.error("Request execution error", e);
        } catch (Exception e) {
            log.error("Json deserialization error", e);
        }
        return null;
    }

}
