package com.botsydroid.controltarjeta;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements Principal.OnFragmentInteractionListener, FragmentAlert.OnFragmentInteractionListener, StatusFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    TextView titulo;
    String numero_disp;
    String desc_disp;
    Button Auxiliar;
    private static String SENT = "SMS_SENT";
    private static String DELIVERED = "SMS_DELIVERED";
    private static int MAX_SMS_MESSAGE_LENGTH = 160;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences prefe = getSharedPreferences("Numero", Context.MODE_PRIVATE);
        numero_disp = prefe.getString("num", "nada");
        desc_disp = prefe.getString("desc", "nada");
        if (numero_disp.equals("nada")) {
            Agregar_numero();
        } else {
            Colocar_numero(desc_disp, numero_disp);
        }


        Fragment fragment = Principal.newInstance("1", "2");
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.addToBackStack(null);
        FT.replace(R.id.Contenedor, fragment).commit();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void Seleccionar_dispositivo() {
        final Cursor auxTodos;
        Record auxR = new Record(getBaseContext());
        auxTodos = auxR.datos();
    /*String a;
     String b;*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione");
        builder.
                setSingleChoiceItems(auxTodos, 1, "nombre", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        auxTodos.moveToPosition(item);
                        numero_disp = auxTodos.getString(1);
                        desc_disp = auxTodos.getString(2);

                    }
                })
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Colocar_numero(desc_disp, numero_disp);
                        SharedPreferences prefs = getSharedPreferences("Numero", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.putString("num", numero_disp);
                        editor.putString("desc", desc_disp);

                        editor.commit();


                    }

                })
        ;
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void Colocar_numero(String a, String b) {

        getSupportActionBar().setTitle(b + " - " + a);
   /* Intent in = new Intent("my.action.string");
    in.putExtra("state", numero_disp);
    sendBroadcast(in);*/
    /*titulo=(TextView)findViewById(R.id.tvTitulo);
    titulo.setText(b+" - "+a);*/
    }

    public void Agregar_numero() {
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.agregar_numero, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Ingresa el numero");
        alert.setView(inflator);

        final EditText et1 = (EditText) inflator.findViewById(R.id.etDescripcion);
        final EditText et2 = (EditText) inflator.findViewById(R.id.etNumero);

        alert.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String s1 = et1.getText().toString();
                String s2 = et2.getText().toString();
                //String aux=inputstreet.getText().toString();
                    /*Bundle args = new Bundle();
                    args.putString("nombre1", s1);
                    args.putString("nombre2", s2);*/
                SharedPreferences prefs = getSharedPreferences("Numero", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.putString("num", s2);
                editor.putString("desc", s1);

                editor.commit();

                Record R1 = new Record(getBaseContext());
                ContentValues values = new ContentValues();
                values.put("numero", s2);
                values.put("nombre", "" + s1);
                R1.insertar(values);
                R1.close();

                Colocar_numero(s1, s2);

                //do operations using s1 and s2 here...
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Inicio) {
            // Handle the camera action
            fragemntoprincipal();

        } else if (id == R.id.Nuevo_numero) {

            Agregar_numero();

        } else if (id == R.id.Status) {
            TraerStatus();

        } else if (id == R.id.Seleccionar_numero) {
            Seleccionar_dispositivo();

        } else if (id == R.id.alertas) {
            llamar_alertas();

        } else if(id ==R.id.set_desfault)
        {
            colocar();
        }
        else if(id ==R.id.nav_borrar)
        {
            borrar();
        }
        else if(id==R.id.mapa)
        {
            montar_mapa();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void montar_mapa() {

        startActivity(new Intent(this, Mapas.class));

    }

    private void llamar_alertas() {

        Fragment fragment = FragmentAlert.newInstance("1", "2"); //el fragment a llamar
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.addToBackStack(null);
        FT.replace(R.id.Contenedor, fragment).commit();

    }

    private void fragemntoprincipal() {

        Fragment fragment = Principal.newInstance("1", "2");
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.addToBackStack(null);
        FT.replace(R.id.Contenedor, fragment).commit();
    }

    private void TraerStatus() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirma")
                .setMessage("Desea Obtener el  status de " + desc_disp)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        enviar("STATUS");
/*
                        Fragment fragment = StatusFragment.newInstance("1","2");
                        FragmentTransaction FT=getSupportFragmentManager().beginTransaction();
                        FT.addToBackStack(null);
                        FT.replace(R.id.Contenedor,fragment).commit();*/

                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }

                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent myIntent = getIntent();
        Bundle a = myIntent.getExtras();
        if (a != null) {
            String tamanio = a.getString("tam");
            String men = a.getString("Texto");
            String tipo = a.getString("Tipo");

            if (tipo.equals("Status")) {
                Bundle args = new Bundle();
                args.putString("nombre1", men);
                args.putString("nombre2", tamanio);


                Fragment fragment = StatusFragment.newInstance(men, men);
                fragment.setArguments(args);

                FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
                FT.addToBackStack(null);
                FT.replace(R.id.Contenedor, fragment).commit();
            }
            if(tipo.equals("Coordenadas"))
            {
                Bundle args = new Bundle();
                args.putString("nombre1", men);
                args.putString("nombre2", tamanio);

                Fragment fragment =StatusFragment.newInstance(men,men);
                fragment.setArguments(args);

                FragmentTransaction FT=getSupportFragmentManager().beginTransaction();
                FT.addToBackStack(null);
                FT.replace(R.id.Contenedor,fragment).commit();

            }
            //.setText("prueba "+tamanio);
            //Mensaje.setText(men);
        }
    }

    public void enviar(String mensaje, Button aux) {
        Auxiliar = aux;
        PendingIntent piSent = PendingIntent.getBroadcast(getApplicationContext(),
                0, new Intent(SENT), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(getApplicationContext(),
                0, new Intent(DELIVERED), 0);
        final BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Auxiliar.setBackgroundResource(R.drawable.bordes3);//coloca en amarillo el boton porque solo fue envieado
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                unregisterReceiver(this);
            }
        };
        registerReceiver(myReceiver, new IntentFilter(SENT));
        //unregisterReceiver(myReceiver );

        //---when the SMS has been delivered---
        final BroadcastReceiver myReceiverDev = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Auxiliar.setBackgroundResource(R.drawable.selector2);//coloca en verde eñl boton porque ya fue entregado
                        Auxiliar.setGravity(Gravity.RIGHT);

                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                unregisterReceiver(this);
            }
        };

        registerReceiver(myReceiverDev, new IntentFilter(DELIVERED));
        //unregisterReceiver(myReceiverDev );
        String phoneNo = numero_disp;
        String sms = mensaje;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, piSent, piDelivered);
            //smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            /*Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();*/
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void enviar(String mensaje) {
        PendingIntent piSent = PendingIntent.getBroadcast(getApplicationContext(),
                0, new Intent(SENT), 0);
        PendingIntent piDelivered = PendingIntent.getBroadcast(getApplicationContext(),
                0, new Intent(DELIVERED), 0);
        final BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                unregisterReceiver(this);
            }
        };
        registerReceiver(myReceiver, new IntentFilter(SENT));
        //unregisterReceiver(myReceiver );

        //---when the SMS has been delivered---
        final BroadcastReceiver myReceiverDev = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();

                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                unregisterReceiver(this);
            }
        };

        registerReceiver(myReceiverDev, new IntentFilter(DELIVERED));
        //unregisterReceiver(myReceiverDev );


        String phoneNo = numero_disp;
        String sms = mensaje;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, piSent, piDelivered);
            //smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            /*Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();*/
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void colocar() {

        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.getPackageName());
        startActivity(intent);
        //Intent sendIntent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        //sendIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.botsydroid.controltarjeta/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    private void borrar() {
        try {

            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor c = this.getContentResolver().query(uriSms,
                    new String[] { "_id", "thread_id", "address",
                            "person", "date", "body" }, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    long id = c.getLong(0);
                    String address = c.getString(2);
                    Toast.makeText(getBaseContext(), "borrando"+id+numero_disp+address,
                            Toast.LENGTH_SHORT).show();
                    if ( address.equals(numero_disp)) {
                        this.getContentResolver().delete(
                                Uri.parse("content://sms/" + id), null, null);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
        }
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.botsydroid.controltarjeta/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
