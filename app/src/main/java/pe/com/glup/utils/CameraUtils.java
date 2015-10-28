package pe.com.glup.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import pe.com.glup.bus.BusHolder;
import pe.com.glup.events.Enfocar;

////


/**
 * Created by usuario on 29/04/15.
 */
public class CameraUtils {

    public CameraUtils() {
        BusHolder.getInstance().register(this);
    }

    public void enfocar(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        String focusMode = parameters.getFocusMode();

        Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                BusHolder.getInstance().post(new Enfocar());
            }
        };
        camera.autoFocus(autoFocusCallback);
    }

    /**
     * Comprueba si el Dispositivo tiene Flash
     * @param context
     * @return
     */
    public static boolean isFlash(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
