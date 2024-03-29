package pe.com.glup.session;

import android.content.Context;
import android.content.SharedPreferences;

import pe.com.glup.beans.Usuario;

/**
 * Created by Gantz on 23/06/14.
 */
public class Session_Manager {

    private static final String PREFERENCE_NAME = "glup_preferences";
    private int PRIVATE_MODE = 0;

    public static final String USER_CODE = "userCodigo";
    public static final String USER_LOGIN = "userLogin";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Session_Manager(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public String getCurrentUserCode() {
        return preferences.getString(USER_CODE, "-1");
    }

    public boolean isLogin() {
        return preferences.getBoolean(USER_LOGIN, false);
    }

    public void openSession(Usuario usuario) {
        editor.putBoolean(USER_LOGIN, true);
        editor.putString(USER_CODE, usuario.getCodUser());
        editor.commit();
    }

    public void closeSession() {
        editor.putBoolean(USER_LOGIN, false);
        editor.putString(USER_CODE, "-1");
        editor.commit();
    }
}