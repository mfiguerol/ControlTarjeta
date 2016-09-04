package com.botsydroid.controltarjeta;

/**
 * Created by jofl on 19/05/2016.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
        import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import static android.support.v4.app.ActivityCompat.startActivity;

public class receptor  extends BroadcastReceiver {

    /*private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
*/
private String Numero;
    /*public receptor(String numero) {
        Numero=numero;
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
       String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
        String ACTION_SMS_DELIVER= "android.provider.Telephony.SMS_DELIVER";

        SharedPreferences prefe=context.getSharedPreferences("Numero", Context.MODE_PRIVATE);
        Numero=prefe.getString("num","nada");
      /*  if(action.equals("my.action.string")){
            Numero= intent.getExtras().getString("state");
            //do your stuff+

            Toast toast = Toast.makeText(context,
                    "numero fijado"+Numero,
                    Toast.LENGTH_LONG);
            toast.show();
        }*/
        Toast.makeText(context,
                "senderNum: " + "antes de " + ", message: ",
                Toast.LENGTH_LONG).show();
        if (action.equals(ACTION_SMS_RECEIVED) || action.equals(ACTION_SMS_DELIVER)) {
           try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage =
                                SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber =
                                currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();


                         //
//Aqui filtra el numero

                        Toast.makeText(context,
                                "senderNum: " + senderNum + ", message: ",
                                Toast.LENGTH_LONG).show();
                        if (currentMessage.getOriginatingAddress().equals(Numero)) {


                            //AQUI se llama la otra actividad status
                            String tipoM=message.substring(0,1);//obtengo la primera letra del mensaje

                            /* toast = Toast.makeText(context,
                                    "senderNum: " + senderNum + ", message: " + tipoM,
                                    Toast.LENGTH_LONG);*/
                            //toast.show();
                            if(tipoM.equals("S"))//Si la primera letra del mensaje es S piden el estatus
                            {
                                Intent intentone = new Intent(context.getApplicationContext(), MainActivity.class);
                                intentone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentone.putExtra("tam", phoneNumber);
                                intentone.putExtra("Texto", message);
                                intentone.putExtra("Tipo","Status");
                                context.startActivity(intentone);
                            }if(tipoM.equals("M"))//Si la primera letra del mensaje es S piden el estatus
                            {
                                Intent intentone = new Intent(context.getApplicationContext(), MainActivity.class);
                                intentone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentone.putExtra("tam", phoneNumber);
                                intentone.putExtra("Texto", message);
                                intentone.putExtra("Tipo","Coordendas");
                                context.startActivity(intentone);
                            }


                           // abortBroadcast();
                            // Show Alert
                            Toast.makeText(context,
                                    "senderNum: " + senderNum + ", message: " + tipoM,
                                    Toast.LENGTH_LONG).show();

                        }
                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);
            }
        }
    }
}