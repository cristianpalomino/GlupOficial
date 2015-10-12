package pe.com.glup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import pe.com.glup.R;

/**
 * Created by Glup on 9/10/15.
 */
public class FragmentEntrarDefault extends Fragment {
	private ToggleButton facebook,twitter,instagram;
	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
	}
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
							 Bundle savedInstance){
		return inflater.inflate(R.layout.fragment_entrar_default,container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstance){
		super.onActivityCreated(savedInstance);
		facebook = (ToggleButton) getView().findViewById(R.id.btn_fb);
		twitter = (ToggleButton) getView().findViewById(R.id.btn_tw);
		instagram = (ToggleButton) getView().findViewById(R.id.btn_ins);

	}
}
