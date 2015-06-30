package pe.com.glup.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import pe.com.glup.R;
import pe.com.glup.glup.Glup;
import pe.com.glup.glup.Principal;
import pe.com.glup.interfaces.OnSearchListener;

/**
 * Created by Glup on 23/06/15.
 */
public class Header extends LinearLayout implements Principal.OnChangeTab {

    private View view;

    private LinearLayout frame_buscar;
    private LinearLayout frame_detalle;

    private ImageButton btnbuscar;
    private ImageButton btncancelar;
    private EditText edtbuscar;

    private Context mcontext;

    private OnSearchListener onSearchListener;

    public Header(Context context) {
        super(context);
        this.mcontext = context;
    }

    public Header(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mcontext = context;
    }

    public Header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mcontext = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Header(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mcontext = context;
    }

    public void initView(Principal principal) {
        principal.setOnChangeTab(this);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.header, this, true);

        btnbuscar = (ImageButton) view.findViewById(R.id.btnbuscar);
        edtbuscar = (EditText) view.findViewById(R.id.edtbuscar);
        btncancelar = (ImageButton) view.findViewById(R.id.btncancel);
        frame_buscar = (LinearLayout) view.findViewById(R.id.frame_buscar);
        frame_detalle = (LinearLayout) view.findViewById(R.id.frame_detalle);

        edtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onSearchListener.onSearchListener(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnbuscar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(GONE);
                frame_buscar.setVisibility(VISIBLE);
                showSoftKeyboard(edtbuscar);
                edtbuscar.setText("");
            }
        });

        btncancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                btnbuscar.setVisibility(VISIBLE);
                frame_buscar.setVisibility(GONE);
            }
        });
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mcontext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(((Glup) mcontext).getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(Header.class.getName(), "Window token @null");
        }
    }

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) mcontext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            view.requestFocus();
            inputMethodManager.showSoftInput(view, 0);
        } catch (Exception e) {
            Log.e(Header.class.getName(), "Window token @null");
        }
    }

    @Override
    public void onChangeTab(int position) {
        updateMenu(position);
    }

    @Override
    public void currentTab(int current) {
        updateMenu(current);
    }

    private void updateMenu(int poscur) {
        switch (poscur) {
            case 0:
                showOptionsHome();
                break;
            case 1:
                showOptionsCloset();
                break;
            case 2:
                showOptionsProbador();
                break;
            case 3:
                showOptionsCamera();
                break;
        }
    }

    public void showOptionsHome() {
        btnbuscar.setVisibility(VISIBLE);
        frame_detalle.setVisibility(GONE);
    }

    public void showOptionsCloset() {
        btnbuscar.setVisibility(GONE);
    }

    public void showOptionsProbador() {
        btnbuscar.setVisibility(GONE);
    }

    public void showOptionsCamera() {
        btnbuscar.setVisibility(GONE);
    }

    public View showOptionsDetail() {
        frame_detalle.setVisibility(VISIBLE);

        btnbuscar.setVisibility(GONE);
        frame_buscar.setVisibility(GONE);

        return frame_detalle;
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }
}
