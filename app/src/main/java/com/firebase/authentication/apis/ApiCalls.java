package com.firebase.authentication.apis;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiCalls {

    @POST("/notification/token")
    Call <PushNotificationResponse> sendToken(@Body PushNotificationRequest pushNotificationRequest);

}
