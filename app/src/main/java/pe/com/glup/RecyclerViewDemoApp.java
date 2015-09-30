package pe.com.glup;

/**
 * Created by Glup on 30/09/15.
 */
import android.app.Application;
import android.util.SparseArray;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pe.com.glup.beans.DemoModel;

public class RecyclerViewDemoApp extends Application {

    private static List<DemoModel> demoData;
    private static SparseArray<DemoModel> demoMap;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Random r = new Random();
        demoData = new ArrayList<DemoModel>();
        demoMap = new SparseArray<DemoModel>();
        for (int i = 0; i < 20; i++) {
            DemoModel model = new DemoModel();
            DateTime dateTime = new DateTime();
            dateTime = dateTime.minusDays(r.nextInt(30));
            model.setDateTime( dateTime.toDate() );
            model.setLabel("Test Label No. " + i);
            demoData.add(model);
            demoMap.put(model.getId(), model);
        }
    }

    public static final List<DemoModel> getDemoData() {
        return new ArrayList<DemoModel>(demoData);
    }

    public static final List<DemoModel> addItemToList(DemoModel model, int position) {
        demoData.add(position, model);
        demoMap.put(model.getId(), model);
        return new ArrayList<DemoModel>(demoData);
    }

    public static final List<DemoModel> removeItemFromList(int position) {
        demoData.remove(position);
        demoMap.remove(demoData.get(position).getId());
        return new ArrayList<DemoModel>(demoData);
    }

    public static DemoModel findById(int id) {
        return demoMap.get(id);
    }

}
