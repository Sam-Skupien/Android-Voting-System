package vs.android.votingsystem;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

// from : https://google-developer-training.github.io/android-developer-phone-sms-course/
// Lesson%202/2_p_2_sending_sms_messages.html

public class MySmsReceiver extends BroadcastReceiver {

    /*
    // create interface
    public interface OnSmsReceivedListener {
        public void onSmsReceived(String sender, String message);
    }*/

    //private OnSmsReceivedListener listener;

    /*
    public MySmsReceiver(){
        this.listener = null;
        System.out.println("MySmsReceiver()");
    }*/

    /*public void setOnSmsReceivedListener(OnSmsReceivedListener listener) {
        this.listener = listener;
        System.out.println("MySMSClass new listener init");
    }*/

    /************************************************************************/
    // end listener interface

    private static final String TAG = MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    boolean isVersionM;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        ObservableObject.getInstance().updateValue(intent);
    }

    /*
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String phoneNo = "";
        String strMessage = "";
        String format = bundle.getString("format");


        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);

        if (pdus != null) {
            // Check the Android version.
            isVersionM = (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.M);

        }


        msgs = new SmsMessage[pdus.length];
        for (int i = 0; i < msgs.length; i++) {


            // Check Android version and use appropriate createFromPdu.

            if (isVersionM) {

                // If Android version M or newer:
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);

            } else {

                // If Android version L or older:
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            phoneNo += "SMS from " + msgs[i].getOriginatingAddress();
            strMessage += " :" + msgs[i].getMessageBody() + "\n";

            System.out.println("from rec:"+ phoneNo);
            System.out.println("msg rec:"+ strMessage);

            if (listener == null){
                System.out.println("listener null");
            }


            if(listener != null) {
                System.out.println("listener not null if statment");
                listener.onSmsReceived(phoneNo, strMessage);
            }

        }

    } */// end onReceive

}
