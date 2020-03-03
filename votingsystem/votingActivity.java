package vs.android.votingsystem;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Observable;
import java.util.Observer;

import static vs.android.votingsystem.MySmsReceiver.*;


public class votingActivity extends AppCompatActivity implements Observer {


    private boolean mVotingEnabled = false;

    private Button mbuttonStart;
    private Button mbuttonEndVote;
    private Button mbuttonSendReport;

    private TextView poster1Count;
    private TextView poster2Count;
    private TextView poster3Count;
    private TextView poster4Count;
    private TextView totalVotesCount;

    int poster1 = 0;
    int poster2 = 0;
    int poster3 = 0;
    int poster4 = 0;
    int totalVotes = 0;

    MySmsReceiver listener;

    private static int VT_CAPACITY = 1000;
    String voterTable[] = new String[VT_CAPACITY];
    String posterTable[] = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        poster1Count = (TextView) findViewById(R.id.textView_poster1_count);
        poster2Count = (TextView) findViewById(R.id.textView_poster2_count);
        poster3Count = (TextView) findViewById(R.id.textView_poster3_count);
        poster4Count = (TextView) findViewById(R.id.textView_poster4_count);
        totalVotesCount = (TextView) findViewById(R.id.textView_total_votes_count);

        poster1Count.setText(String.valueOf(poster1));
        poster2Count.setText(String.valueOf(poster2));
        poster3Count.setText(String.valueOf(poster3));
        poster4Count.setText(String.valueOf(poster4));
        totalVotesCount.setText(String.valueOf(totalVotes));

        ObservableObject.getInstance().addObserver(this);


        posterTable[0] = "Gaming";
        posterTable[1] = "AI";
        posterTable[2] = "Computer Vision";
        posterTable[3] = "Data Base";

        for(int i = 0; i < VT_CAPACITY; i++){
            voterTable[i] = "";
        }


    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void update(Observable observable, Object data) {

        //Toast.makeText(this, String.valueOf("activity observer " + data), Toast.LENGTH_LONG).show();

        Intent intent = (Intent)data;
        boolean isVersionM = false;

        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            /*
            System.out.println("bundle not null");
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("UPDATE", String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }*/

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

                phoneNo += msgs[i].getOriginatingAddress();
                strMessage += msgs[i].getMessageBody() + "\n";

                //System.out.println("no str:" + phoneNo);
                //System.out.println("msg str:" + strMessage);
                checkNumber(phoneNo, strMessage);

            }
        }

    }

    public void checkNumber(String number, String msg) {

        String testNo = number;
        String posterNo = msg;

        System.out.println("checknumber function");

        String tempSanStr = testNo;
        String sanitizedNoStr = tempSanStr.replaceFirst("\\+", "");


        System.out.println("from "+ sanitizedNoStr);
        System.out.println(msg);

        //sendSMS(sanitizedNoStr, "test");


        if(msg.contains("1")){
            System.out.println(poster1);
            poster1++;
            totalVotes++;
            poster1Count.setText(String.valueOf(poster1));
            totalVotesCount.setText(String.valueOf(totalVotes));
            System.out.println(poster1);

        } else if(msg.contains("2")){
            System.out.println(poster1);
            poster2++;
            totalVotes++;
            poster2Count.setText(String.valueOf(poster2));
            totalVotesCount.setText(String.valueOf(totalVotes));
            System.out.println(poster2);

        } else if (msg.contains("3")) {
            System.out.println(poster3);
            poster3++;
            totalVotes++;
            poster3Count.setText(String.valueOf(poster3));
            totalVotesCount.setText(String.valueOf(totalVotes));
            System.out.println(poster3);

        } else if (msg.contains("4")) {
            System.out.println(poster4);
            poster4++;
            totalVotes++;
            poster4Count.setText(String.valueOf(poster4));
            totalVotesCount.setText(String.valueOf(totalVotes));
            System.out.println(poster4);

        } else{
            ;
        }


    }

    public void updateViews(){
        ;
    }

    public boolean checkVoterTable(String phone_num){

        boolean inTable = false;

        for(int i = 0; i < VT_CAPACITY; i++ ){

            String tableNo = voterTable[i];

                if(tableNo.equals(phone_num)){
                    inTable = true;
                }
            }
        return inTable;
    }

    public void addNumber(String number){

        for(int i = 0; i < VT_CAPACITY; i++){

            if(number.equals("")){
                voterTable[i] = number;
                System.out.println("number added");
                break;
            }
        }
    }


    public void enableVoting(View view){
        mVotingEnabled = true;
        System.out.println(mVotingEnabled);
    }


    public void sendReport(View view){

        String report = "Poster 1: " + posterTable[0] + " Vote Count:   " + poster1 + "\n" +
                        "Poster 2: " + posterTable[1] + " Vote Count:   " + poster2 + "\n" +
                        "Poster 3: " + posterTable[2] + " Vote Count:   " + poster3 + "\n" +
                        "Poster 4: " + posterTable[3] + " Vote Count:   " + poster4 + "\n";

        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Voting Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, report);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}


