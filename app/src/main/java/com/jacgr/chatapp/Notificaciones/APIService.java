package com.jacgr.chatapp.Notificaciones;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAArbhRt0s:APA91bGuEGW1CPs6o93qMmmmZ0OsZ_WtTtJyCFQvYsD4FamYkJXQtwzBp7n_2Zzr-cbbcMglwCvDDaYBdYLUrJGXcqXTfjyMSnuwGjFBSK3B82D1kHbQjAccjTBtU_7pmhkeIM_3ZPdJ"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
