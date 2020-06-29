package com.sedadurmus.yenivavi.Fragments;

import com.sedadurmus.yenivavi.notifications.MyReponse;
import com.sedadurmus.yenivavi.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAJ0MZ1Nk:APA91bHnw4H_QWg2UIxcbThA-mYWwWTpmSNviaefSGsQl1NxsXio3FbuCaWAlaZ0vfOl19ChQnrmB2Ab_0UupQx9ozfRBlqxSBaBeAJa3mfUxkiXmiay-JbV9xH1hwG-2mo8zRKaBxKB"
            }
    )

    @POST("fcm/send")
    Call<MyReponse> sendNotification(@Body Sender body);
}
