package pe.com.glup;

/**
 * Created by Glup on 30/09/15.
 */
import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import pe.com.glup.notifications.logging.ACRAReportSender;


@ReportsCrashes()
public class GlupApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this);

        // instantiate the report sender with the email credentials.
        // these will be used to send the crash report
        ACRAReportSender reportSender = new ACRAReportSender("glupmovil@gmail.com", "glupmovil123");

        // register it with ACRA.
        ACRA.getErrorReporter().setReportSender(reportSender);
    }
}
