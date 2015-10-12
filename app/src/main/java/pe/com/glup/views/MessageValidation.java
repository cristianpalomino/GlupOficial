package pe.com.glup.views;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import pe.com.glup.R;

/**
 * Created by Glup on 12/10/15.
 */
public class MessageValidation  extends Toast {

	/**
	 * Construct an empty Toast object.  You must call {@link #setView} before you
	 * can call {@link #show}.
	 *
	 * @param context The context to use.  Usually your {@link Application}
	 *                or {@link Activity} object.
	 */
	public MessageValidation(Context context,String msg, int duration) {
		super(context);
		this.setDuration(duration);
		LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		try{
			View view = inflater.inflate(R.layout.dialog_validation_register, (ViewGroup) ((AppCompatActivity) context).findViewById(R.id.toast));
			this.setView(view);
			TextView textView = (TextView) view.findViewById(R.id.tv_message);
			textView.setText(msg);
		}catch (ClassCastException c){
			Log.e(null, c.toString());
		}




	}

}