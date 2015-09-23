package pe.com.glup.utils;

import android.content.Context;
import android.widget.Toast;

import pe.com.glup.views.Message;

/**
 * Created by Glup on 21/09/15.
 */
public class MessageUtil {
    public static void showToast(Context activityContext, String message) {
            Message toast = new Message(activityContext, message, Toast.LENGTH_SHORT);
            toast.show();
    }
}