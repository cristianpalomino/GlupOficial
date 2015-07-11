package pe.com.glup.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import pe.com.glup.R;

/**
 * Created by cristian on 8/07/15.
 */
public class GlupDialog extends AlertDialog {


    public GlupDialog(Context context) {
        super(context);
        initDialog();
    }

    protected GlupDialog(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    protected GlupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.glup_dialog, null);
        setView(view);

        getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
    }
}