package com.vocketlist.android.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vocketlist.android.R;
import com.vocketlist.android.activity.FavoriteActivity;
import com.vocketlist.android.activity.MainActivity;
import com.vocketlist.android.message.notitype.Notitype;
import com.vocketlist.android.roboguice.log.Ln;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SeungTaek.Lim on 2017. 2. 2..
 */

public class PushService extends FirebaseMessagingService {


	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		// TODO(developer): Handle FCM messages here.
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		Ln.d("From: " + remoteMessage.getFrom());

		// Check if message contains a data payload.
		if (remoteMessage.getData().size() > 0) {
			Ln.d("Message data payload: " + remoteMessage.getData());
		}

		// Check if message contains a notification payload.
		if (remoteMessage.getNotification() != null) {
			Ln.d("Message Notification Body: " + remoteMessage.getNotification().getBody());
		}

		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.

		Map data = remoteMessage.getData();

		if (data != null) {

			String[] datas = (String[]) data.values().toArray(new String[]{});
			int badgeCount = 1;
			String pushMessage = datas[0];
			String notiType = datas[1];
			String title = datas[2];


			sendBadgeUpdateIntent(badgeCount);
			sendNotification(pushMessage, title, notiType);

		}
	}

	private void sendNotification(String title, String list, String notiType) {

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if (Notitype.FAVORITE.equals(notiType)) {
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, FavoriteActivity.class), 0);
			Notification.Builder mBuilder = new Notification.Builder(this);
			mBuilder.setSmallIcon(R.mipmap.ic_launcher);
			mBuilder.setTicker("관심 알림이 도착했습니다.");
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setContentTitle(title);
			mBuilder.setContentText(list);
			mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
			mBuilder.setContentIntent(pendingIntent);
			mBuilder.setAutoCancel(true);


			nm.notify(555, mBuilder.build());

		} else if (Notitype.NOTICE.equals(notiType)) {
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
			Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setTicker("공지 사항이 도착했습니다.")
					.setContentTitle(title)
					.setContentText(list)
					.setAutoCancel(true)
					.setSound(defaultSoundUri)
					.setPriority(android.app.Notification.PRIORITY_MAX)
					.setContentIntent(pendingIntent);

			NotificationManager notificationManager =
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(0, notificationBuilder.build());

		} else if (Notitype.WISDOM.equals(notiType)) {
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
			Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setTicker("오늘의 명언이 도착했습니다.")
					.setContentTitle(title)
					.setContentText(list)
					.setAutoCancel(true)
					.setSound(defaultSoundUri)
					.setPriority(android.app.Notification.PRIORITY_MAX)
					.setContentIntent(pendingIntent);

			NotificationManager notificationManager =
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(0, notificationBuilder.build());

		}

	}

	private void sendBadgeUpdateIntent(int badgeCount) {
		Intent intent = new Intent("badgeCount");
		intent.putExtra("badgeCount", badgeCount);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

}
