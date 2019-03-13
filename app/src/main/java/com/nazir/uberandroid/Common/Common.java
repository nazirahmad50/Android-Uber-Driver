package com.nazir.uberandroid.Common;

import android.location.Location;

import com.nazir.uberandroid.Remote.FCMClient;
import com.nazir.uberandroid.Remote.IFCService;
import com.nazir.uberandroid.Remote.IGoogleAPI;
import com.nazir.uberandroid.Remote.RetrofitClient;

public class Common {

    public static final String baseURL = "https://maps.googleapi.com";
    public static final String fcmURL = "https://fcm.googleapis.com/";

    public static final String TOKEN_TBL = "Token";

    public static IGoogleAPI googleAPI(){

        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }

    public static IFCService getFCMService(){

        return FCMClient.getClient(fcmURL).create(IFCService.class);
    }

    public static final String DRIVER_TBL = "Drivers";
    public static final String DRIVER_INFO_TBL = "DriverInformation";
    public static final String RIDERS_INFO_TBL = "RidersInformation";
    public static final String PICKUP_REQUEST_TBL = "PickupRequest";


    public static Location mLastLocation = null;

}
