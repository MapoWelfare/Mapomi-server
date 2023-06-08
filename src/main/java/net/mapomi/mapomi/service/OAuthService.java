package net.mapomi.mapomi.service;

import net.mapomi.mapomi.common.PropertyUtil;
import net.mapomi.mapomi.dto.request.OauthDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OAuthService {
    private static final OkHttpClient client = new OkHttpClient();
    private String KAKAO_ID = "ad91fa142c08bdf6f047dfb348f53b10";

    public JSONObject getOauthInfo(String token) throws IOException, ParseException {
        return getKakaoInfo(token);
    }

    public JSONObject getKakaoAccessToken(String redirect_uri, String code) throws IOException, ParseException {
        String url = "https://kauth.kakao.com/oauth/token"
                + "?client_id="+KAKAO_ID
                + "&redirect_uri="+ redirect_uri
                + "&grant_type=authorization_code"
                + "&code=" + code;
        Request.Builder builder = new Request.Builder().header("Content-type", " application/x-www-form-urlencoded")
                .url(url);
        JSONObject postObj = new JSONObject();
        RequestBody requestBody = RequestBody.create(postObj.toJSONString().getBytes());
        builder.post(requestBody);
        Request request = builder.build();

        Response responseHTML = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(responseHTML.body().string());
        JSONObject response = new JSONObject();
        response.put("access_token",obj.get("access_token").toString());
        return PropertyUtil.response(response);
    }
    public JSONObject getKakaoInfo(String accessToken) throws IOException, ParseException {
        String url = "https://kapi.kakao.com/v2/user/me";
        Request.Builder builder = new Request.Builder()
                .header("Authorization","Bearer "+accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .url(url);
        Request request = builder.build();

        Response responseHTML = client.newCall(request).execute();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(responseHTML.body().string());
    }
}
