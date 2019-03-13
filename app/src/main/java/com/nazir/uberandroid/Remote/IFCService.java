package com.nazir.uberandroid.Remote;

import com.nazir.uberandroid.Model.FCMResponse;
import com.nazir.uberandroid.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAAROZWkk:APA91bFBN4jpKFfYkNCIZtqQTp2pzU5tUd7dcxlbI9GZiYI0whv_MlF269Q99oIUeNyJSUzb_BVM1Va36TEyNvT3TYI7YiYgTNhN_m0DAPmOdhi2kB85f6CzWuD6j4Jhc3ZGlirxuajK"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
