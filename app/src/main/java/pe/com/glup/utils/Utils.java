package pe.com.glup.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by usuario on 29/04/15.
 */
public class Utils {

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
