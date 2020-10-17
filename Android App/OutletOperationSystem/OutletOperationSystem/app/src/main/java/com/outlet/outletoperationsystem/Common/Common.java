package com.outlet.outletoperationsystem.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.outlet.outletoperationsystem.Model.Request;
import com.outlet.outletoperationsystem.Model.User;
import com.outlet.outletoperationsystem.Remote.IGeoCoordinates;
import com.outlet.outletoperationsystem.Remote.RetrofitClient;

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public static final String baseUrl = "https://maps.googleapis.com";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static boolean isConnectedToInternet(Context context)
    {
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(baseUrl).create(IGeoCoordinates.class);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap,int newWidth,int newHeight)
    {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);
        float scaleX = newWidth / (float)bitmap.getWidth();
        float scaleY = newHeight / (float)bitmap.getHeight();

        float pivotX = 0,pivotY = 0;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;

    }
}
