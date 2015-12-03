package pe.com.glup.managers.session;

import android.content.Context;
import android.content.SharedPreferences;

import pe.com.glup.models.Usuario;

/**
 * Created by Gantz on 23/06/14.
 */
public class Session_Manager {

    private static final String PREFERENCE_NAME = "glup_preferences";
    private int PRIVATE_MODE = 0;

    public static final String USER_CODE = "userCodigo";
    public static final String USER_SEXO = "sexCodigo";
    public static final String FLAG_RELOAD = "flagReload";

    public static final String NUM_PAGES_TODOS = "numPages";
    public static final String NUM_PAGES_GENM = "numPagesM";
    public static final String NUM_PAGES_GENH = "numPagesH";

    public static final String TOTAL_PREND_TODOS= "numPrendas";
    public static final String TOTAL_PREND_GENM= "numPrendasM";
    public static final String TOTAL_PREND_GENH= "numPrendasH";


    public static final String USER_LOGIN = "userLogin";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Session_Manager(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public String getCurrentUserCode() {return preferences.getString(USER_CODE, "-1");}

    public String getCurrentUserSexo() { return preferences.getString(USER_SEXO, "-1"); }

    public int getCurrentNumPages() { return preferences.getInt(NUM_PAGES_TODOS, 1);}

    public int getCurrentNumPagesMujer() { return preferences.getInt(NUM_PAGES_GENM,1);}

    public int getCurrentNumPagesHombre() { return preferences.getInt(NUM_PAGES_GENH,1);}

    public int getTotalPrendas() { return preferences.getInt(TOTAL_PREND_TODOS,-1);}
    public int getTotalPrendasMujer() { return preferences.getInt(TOTAL_PREND_GENM,-1);}
    public int getTotalPrendasHombre() { return preferences.getInt(TOTAL_PREND_GENH,-1);}


    public boolean isLogin() {
        return preferences.getBoolean(USER_LOGIN, false);
    }

    public boolean isLoad() { return  preferences.getBoolean(FLAG_RELOAD,false);}

    public void openSession(Usuario usuario) {
        editor.putBoolean(USER_LOGIN, true);
        editor.putString(USER_CODE, usuario.getCodUser());
        editor.putString(USER_SEXO, usuario.getSexoUser());
        editor.putBoolean(FLAG_RELOAD, true);
        editor.putInt(NUM_PAGES_TODOS, 1);
        editor.putInt(NUM_PAGES_GENM, 1);
        editor.putInt(NUM_PAGES_GENH,1);
        editor.putInt(TOTAL_PREND_TODOS,0);
        editor.putInt(TOTAL_PREND_GENM,0);
        editor.putInt(TOTAL_PREND_GENH,0);
        editor.commit();
    }

    public void setFlagReload(Boolean flag){
        editor.putBoolean(FLAG_RELOAD, flag);
        editor.commit();
    }
    public void setNumPages(int numPages){
        editor.putInt(NUM_PAGES_TODOS, numPages);
        editor.commit();
    }
    public void setNumPagesMujer(int numPages){
        editor.putInt(NUM_PAGES_GENM,numPages);
        editor.commit();
    }
    public void setNumPagesHombre(int numPages){
        editor.putInt(NUM_PAGES_GENH,numPages);
        editor.commit();
    }
    public void setTotalPrendTodos(int totalPrendTodos){
        editor.putInt(TOTAL_PREND_TODOS,totalPrendTodos);
        editor.commit();
    }
    public void setTotalPrendGenm(int totalPrendGenm){
        editor.putInt(TOTAL_PREND_GENM,totalPrendGenm);
        editor.commit();
    }
    public void setTotalPrendGenh(int totalPrendGenh){
        editor.putInt(TOTAL_PREND_GENH,totalPrendGenh);
        editor.commit();
    }

    public void closeSession() {
        editor.putBoolean(USER_LOGIN, false);
        editor.putString(USER_CODE, "-1");
        editor.putString(USER_SEXO, "-1");
        editor.putBoolean(FLAG_RELOAD, false);
        editor.putInt(NUM_PAGES_TODOS, 1);
        editor.putInt(NUM_PAGES_GENM, 1);
        editor.putInt(NUM_PAGES_GENH,1);
        editor.putInt(TOTAL_PREND_TODOS,-1);
        editor.putInt(TOTAL_PREND_GENM,-1);
        editor.putInt(TOTAL_PREND_GENH,-1);
        editor.commit();
    }
}