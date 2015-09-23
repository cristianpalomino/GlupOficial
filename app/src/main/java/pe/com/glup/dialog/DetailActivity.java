package pe.com.glup.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import pe.com.glup.R;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            crearFullScreenDialog();
        }
    }

    private void crearFullScreenDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FullScreenDialog newFragment = new FullScreenDialog();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment, "FullScreenFragment")
                .commit();
    }



}
