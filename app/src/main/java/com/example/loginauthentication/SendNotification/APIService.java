package com.example.loginauthentication.SendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:sender=1077991553061"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
