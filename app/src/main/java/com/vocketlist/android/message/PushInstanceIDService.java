package com.vocketlist.android.message;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.vocketlist.android.net.ServiceManager;
import com.vocketlist.android.preference.FCMPreference;
import com.vocketlist.android.roboguice.log.Ln;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class PushInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Ln.d("Refreshed token: " + refreshedToken);

        FCMPreference.getInstance().saveToken(refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

    }

    private void saveToken(String token) {

    }

    private void sendRegistrationToServer(String refreshedToken) {
        // todo send token to server
        ServiceManager manager = new ServiceManager();
        manager.registerFcmToken(refreshedToken);
    }
}
