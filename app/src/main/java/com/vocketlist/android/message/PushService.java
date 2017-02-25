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

	private List<String> list = new ArrayList<>();


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
			String title = remoteMessage.getNotification().getTitle();
			int badgeCount = 1;
			String pushMessage = datas[1];
			String notiType = "watchList"; //서버에게 푸시 타입 알려줘야함

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(pushMessage);
			JsonArray jsonArray = element.getAsJsonArray();
			for (int loop = 0; loop < jsonArray.size(); loop++) {
				list.add(jsonArray.get(loop).getAsJsonObject().get("content").toString());
			}

			sendBadgeUpdateIntent(badgeCount);
			sendNotification(title, list, notiType);

		}
	}

	private void sendNotification(String title, List<String> list, String notiType) {

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		if (Notitype.FAVORITE.equals(notiType)) {
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
			Notification.Builder mBuilder = new Notification.Builder(this);
			mBuilder.setSmallIcon(R.mipmap.ic_launcher);
			mBuilder.setTicker("알람 도착");
			mBuilder.setWhen(System.currentTimeMillis());
			mBuilder.setContentTitle(title);
			mBuilder.setContentText("관심 스케쥴 일정");
			mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
			mBuilder.setContentIntent(pendingIntent);
			mBuilder.setAutoCancel(true);

			Notification.InboxStyle style = new Notification.InboxStyle(mBuilder);
			if (list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					style.addLine(list.get(i));
				}
			} else {
				style.addLine("없네요 관심 스케쥴을 설정해보세요.");
			}
			style.setSummaryText("더보기");
			mBuilder.setStyle(style);

			nm.notify(555, mBuilder.build());

		} else if (Notitype.NOTICE.equals(notiType)) {
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
			Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setContentTitle("")
					.setContentText("")
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
					.setContentTitle("")
					.setContentText("")
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
