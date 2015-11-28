package pe.com.glup.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Util_Fonts {
    public static Typeface setBold(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/m_bold.otf");
    }

    public static Typeface setRegular(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/m_regular.otf");
    }

    public static Typeface setLight(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/geosanslight.ttf");
    }
    public static Typeface setLatoRegular(Context paramContext){
        return Typeface.createFromAsset(paramContext.getAssets(),"fonts/LatoRegular.ttf");
    }
}