package com.example.prabodhaharankahadeniya.smartremote;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.nio.charset.Charset;

public class RemoteActivity extends AppCompatActivity {

    private static final String TAG="RemoteAvtivity";
    ImageButton increase;
    ImageButton decrease;
    ImageButton forward;
    ImageButton backward;
    ImageButton on_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        increase=(ImageButton)findViewById(R.id.btn_increase);
        decrease=(ImageButton)findViewById(R.id.btn_decrease);
        forward=(ImageButton)findViewById(R.id.btn_forward);
        backward=(ImageButton)findViewById(R.id.btn_backward);
        on_off=(ImageButton)findViewById(R.id.btn_on_off);


    }
    @Override
    protected void onResume(){
        super.onResume();
        Cursor cursor=MainActivity.getDatabaseHelper().getAllDevices();
        if(cursor==null){
            Log.d(TAG,"null");
        }
        else {
            Log.d(TAG,"else");
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                boolean finished=MainActivity.getDatabaseHelper().confirmRegistration(ConnectionActivity.getDeviceId());
                if(finished && ConnectionActivity.getViewId().equals("0")) {
                    Toast.makeText(RemoteActivity.this,"This device initiated successfully ", Toast.LENGTH_LONG).show();

                }


            }
            cursor.close();
        }
    }

    public void saveIncrease(View v){
        String cursor=MainActivity.getDatabaseHelper().getCommand(ConnectionActivity.getDeviceId(),"increase");

        if(cursor==null){
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "increase");
                intent.putExtra("type","insert");
                startActivity(intent);

            }
            else {
                Toast.makeText(RemoteActivity.this,"Error occurred. try again",Toast.LENGTH_LONG).show();
            }

        }
        else {
            //do next task
            if (ConnectionActivity.getViewId().equals("0")) {
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "increase");
                intent.putExtra("type", "update");
                startActivity(intent);

            } else {


                byte[] bytes = cursor.getBytes(Charset.defaultCharset());
                ConnectionActivity.mBluetoothConnection.write(bytes);
            }

        }


    }

    public void saveDecrease(View v){
        String cursor=MainActivity.getDatabaseHelper().getCommand(ConnectionActivity.getDeviceId(),"decrease");

        if(cursor==null){
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "decrease");
                intent.putExtra("type","insert");
                startActivity(intent);

            }
            else {
                Toast.makeText(RemoteActivity.this,"Error occurred. try again",Toast.LENGTH_LONG).show();
            }

        }
        else {
            //do next task
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "decrease");
                intent.putExtra("type","update");
                startActivity(intent);

            }
            else {
                // String signal=cursor.getString(3);

                byte[] bytes=cursor.getBytes(Charset.defaultCharset());
                ConnectionActivity.mBluetoothConnection.write(bytes);
            }

        }

    }
    public void saveForward(View v){
        String cursor=MainActivity.getDatabaseHelper().getCommand(ConnectionActivity.getDeviceId(),"forward");

        if(cursor==null){
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "forward");
                intent.putExtra("type","insert");
                startActivity(intent);

            }
            else {
                Toast.makeText(RemoteActivity.this,"Error occurred. try again",Toast.LENGTH_LONG).show();
            }

        }
        else {
            //do next task
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "forward");
                intent.putExtra("type","update");
                startActivity(intent);

            }
            else {
                // String signal=cursor.getString(3);

                byte[] bytes=cursor.getBytes(Charset.defaultCharset());
                ConnectionActivity.mBluetoothConnection.write(bytes);
            }

        }

    }
    public void saveBackward(View v){
        String cursor=MainActivity.getDatabaseHelper().getCommand(ConnectionActivity.getDeviceId(),"backward");

        if(cursor==null){
            Log.d(TAG,ConnectionActivity.getViewId());
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "backward");
                intent.putExtra("type","insert");
                startActivity(intent);

            }
            else {
                Toast.makeText(RemoteActivity.this,"Error occurred. try again",Toast.LENGTH_LONG).show();
            }

        }
        else {
            //do next task
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "backward");
                intent.putExtra("type","update");
                startActivity(intent);

            }
            else {

                byte[] bytes=cursor.getBytes(Charset.defaultCharset());
                ConnectionActivity.mBluetoothConnection.write(bytes);
            }

        }

    }
    public void saveOnOff(View v){
        String cursor=MainActivity.getDatabaseHelper().getCommand(ConnectionActivity.getDeviceId(),"on_off");

        if(cursor==null){
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "on_off");
                intent.putExtra("type","insert");
                startActivity(intent);

            }
            else {
                Toast.makeText(RemoteActivity.this,"Error occurred. try again",Toast.LENGTH_LONG).show();
            }

        }
        else {
            //do next task
            if(ConnectionActivity.getViewId().equals("0")){
                Intent intent = new Intent(this, SaveCommandActivity.class);
                intent.putExtra("message", "on_off");
                intent.putExtra("type","update");
                startActivity(intent);

            }
            else {
                // String signal=cursor.getString(3);

                byte[] bytes=cursor.getBytes(Charset.defaultCharset());
                ConnectionActivity.mBluetoothConnection.write(bytes);
            }

        }

    }



}
