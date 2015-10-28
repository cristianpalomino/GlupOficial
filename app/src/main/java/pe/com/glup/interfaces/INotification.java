package pe.com.glup.interfaces;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Glup on 21/10/15.
 */
public interface INotification {
	void createNotification(Context context);
	void setProgress(int max, int progress);
	void onFinish();
	void vibrate();
	void invalidate();

	NotificationCompat.Builder getBuilder();
	NotificationManager getManager();
}
