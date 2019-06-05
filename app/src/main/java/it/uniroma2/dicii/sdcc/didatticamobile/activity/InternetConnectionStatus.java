package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/* Utility class for Internet Connection Status checking*/
public class InternetConnectionStatus {

    /**
     * @param context the application context
     * @return {@code true} if the device is able to connect to the Internet. Otherwise, the method
     * returns {@code false}. To check the connectivity either Wifi and Mobile Data are tested.
     * */
    public boolean isDeviceConnectedToInternet(Context context){

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return mWifi.isConnected() || mMobile.isConnected();
        }
        return false;

    }

}
