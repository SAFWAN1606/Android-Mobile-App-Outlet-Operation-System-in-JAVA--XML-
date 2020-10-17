package com.system.outletoperationsystemserver.Common;

import android.content.Context;
import android.net.ConnectivityManager;

import com.system.outletoperationsystemserver.Model.User;


public class Common {
    public static User currentUser;
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

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
}
