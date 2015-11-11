package pe.com.glup.notifications.logging;

/**
 * Created by Glup on 10/11/15.
 */

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

public class ACRAReportSender implements ReportSender {

    private String emailUsername ;
    private String emailPassword ;
    private final String LINE_SEPARATOR = "\n";




    public ACRAReportSender(String emailUsername, String emailPassword) {
        super();
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
    }



    @Override
    public void send(Context context, CrashReportData report)
            throws ReportSenderException {

        // Extract the required data out of the crash report.
        String reportBody = createCrashReport(report);

        // instantiate the email sender
        GMailSender gMailSender = new GMailSender(emailUsername, emailPassword);

        try {
            // specify your recipients and send the email
            gMailSender.sendMail("CRASH REPORT", reportBody, emailUsername, "xtofer16@gmail.com, glupmovil@gmail.com");
        } catch (Exception e) {
            Log.d("Error Sending email", e.toString());
        }
    }


    /** Extract the required data out of the crash report.*/
    private String createCrashReport(CrashReportData report) {

        // I've extracted only basic information.
        // U can add loads more data using the enum ReportField. See below.
        StringBuilder body = new StringBuilder();
        body
            /*.append("Device : " + report.getProperty(ReportField.BRAND) + "-" + report.getProperty(ReportField.PHONE_MODEL))
            .append("\n")
            .append("Android Version :" + report.getProperty(ReportField.ANDROID_VERSION))
            .append("\n")
            .append("App Version : " + report.getProperty(ReportField.APP_VERSION_CODE))
            .append("\n")
            .append("STACK TRACE : \n" + report.getProperty(ReportField.STACK_TRACE))*/

            .append("************ CAUSE OF ERROR ************\n\n")
            .append(report.getProperty(ReportField.STACK_TRACE)+"")
            .append("\n************ DEVICE INFORMATION ***********\n")
            .append("Brand: ")
            .append(Build.BRAND)
            .append(LINE_SEPARATOR)
            .append("Device: ")
            .append(Build.DEVICE)
            .append(LINE_SEPARATOR)
            .append("Model: ")
            .append(Build.MODEL)
            .append(LINE_SEPARATOR)
            .append("Id: ")
            .append(Build.ID)
            .append(LINE_SEPARATOR)
            .append("Product: ")
            .append(Build.PRODUCT)
            .append(LINE_SEPARATOR)
            .append("\n************ FIRMWARE ************\n")
            .append("SDK: ")
            .append(Build.VERSION.SDK)
            .append(LINE_SEPARATOR)
            .append("Release: ")
            .append(Build.VERSION.RELEASE)
            .append(LINE_SEPARATOR)
            .append("Incremental: ")
            .append(Build.VERSION.INCREMENTAL)
            .append(LINE_SEPARATOR)
            .append("\n************ VERSION ************\n")
            .append("Android Version :" + report.getProperty(ReportField.ANDROID_VERSION))
            .append(LINE_SEPARATOR)
            .append("App Version : " + report.getProperty(ReportField.APP_VERSION_CODE));
            
        return body.toString();
    }
}
