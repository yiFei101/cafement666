package com.example.loginauthentication.SendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-v1NuCU:APA91bHwgCJgDzZ7nCOT_o7IWJg39wIH3fj74uKhXOiSiGTu51PzZ61pDvZL3jHniWZvlPsWEFSJlM1q5QNYUEa5ZIGFkbZfFo5X3HWcG45Hrj7ApD7lPMPpTa5myxyqRBzp25HswS94"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
