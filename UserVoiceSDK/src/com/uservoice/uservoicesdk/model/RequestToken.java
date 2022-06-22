package com.uservoice.uservoicesdk.model;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import com.uservoice.uservoicesdk.rest.Callback;
import com.uservoice.uservoicesdk.rest.RestTaskCallback;

public class RequestToken extends BaseModel {

    private String key;
    private String secret;

    public static void getRequestToken(Context context, final Callback<RequestToken> callback) {
        doGet(context, apiPath("/oauth/request_token.json"), new RestTaskCallback(callback) {
            @Override
            public void onComplete(JSONObject result) throws JSONException {
                callback.onModel(deserializeObject(result, "token", RequestToken.class));
            }
        });
    }

    @Override
    public void load(JSONObject object) throws JSONException {
        key = getString(object, "oauth_token");
        secret = getString(object, "oauth_token_secret");
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }
}
