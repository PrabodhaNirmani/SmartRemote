package com.example.prabodhaharankahadeniya.smartremote;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;

import static android.R.drawable.arrow_down_float;
import static android.R.drawable.arrow_up_float;
import static android.R.drawable.ic_lock_power_off;
import static android.R.drawable.ic_media_next;
import static android.R.drawable.ic_media_previous;

public class SaveCommandActivity extends AppCompatActivity {

    private static final String TAG="TestCommandActivity";
    private String commandType;

    ImageButton command;
    TextView tvCmd;
    static TextView tvSignal;
    Button save;
    Button update;
    TextView tv;

    private String saveMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_command);
        command=(ImageButton)findViewById(R.id.btn_command);
        tvCmd=(TextView)findViewById(R.id.tvCmd);
        tvSignal=(TextView)findViewById(R.id.tvMsg);
        update=(Button)findViewById(R.id.btnUpdate);
        save=(Button)findViewById(R.id.btnSave);
        tv=(TextView)findViewById(R.id.tvSavedMsg);


        Log.d(TAG,"testCom");
        Bundle bundle = getIntent().getExtras();
        commandType = bundle.getString("message");
        saveMode=bundle.getString("type");
        if(saveMode.equals("update")){
            update.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
        }
        else {
            save.setVisibility(View.VISIBLE);
            update.setVisibility(View.INVISIBLE);
        }
        checkCommand();

    }

    private void checkCommand(){


        if(commandType.equals("increase")){
            Log.d(TAG,2+commandType);
            command.setImageResource(arrow_up_float);
            command.setBackgroundColor(Color.BLACK);
            tvCmd.setText("Increase Volume");

        }
        else if(commandType.equals("decrease")){
            Log.d(TAG,3+commandType);
            command.setImageResource(arrow_down_float);
            command.setBackgroundColor(Color.BLACK);
            tvCmd.setText("Decrease Volume");

        }else if(commandType.equals("on_off")){
            Log.d(TAG,1+commandType);
            command.setImageResource(ic_lock_power_off);
            command.setBackgroundColor(Color.RED);
            tvCmd.setText("Turn Off/On Remote");

        }else if(commandType.equals("forward")){
            Log.d(TAG,5+commandType);
            command.setImageResource(ic_media_next);
            command.setBackgroundColor(Color.BLACK);
            tvCmd.setText("Forward Channels");

        }else {
            Log.d(TAG,4+commandType);
            command.setImageResource(ic_media_previous);
            command.setBackgroundColor(Color.BLACK);
            tvCmd.setText("Backward Channels");

        }



    }

    public void testCommand(View v){

        String signal=tvSignal.getText().toString();
        if(signal.equals("")){
            Toast.makeText(SaveCommandActivity.this,"Please check your bluetooth connection is working",Toast.LENGTH_LONG).show();

        }
        else {
            //send signal and check that
//            Cursor cursor=MainActivity.getDatabaseHelper().getCommand(ConnectionActivity.getDeviceId(),commandType);
//            String signal=cursor.getString(2);
            byte[] bytes=signal.getBytes(Charset.defaultCharset());
            ConnectionActivity.mBluetoothConnection.write(bytes);
        }

    }

    public void saveSignal(View v){
        String signal=tvSignal.getText().toString();
        if(signal.equals("")){
            Toast.makeText(SaveCommandActivity.this,"Please check your bluetooth connection is working",Toast.LENGTH_LONG).show();

        }
        else {
            MainActivity.getDatabaseHelper().insertCommand(ConnectionActivity.getDeviceId(),commandType,signal);
            save.setEnabled(false);
            String cursor=MainActivity.getDatabaseHelper().getCommand(ConnectionActivity.getDeviceId(),commandType);
            tv.setText(cursor);

        }

    }

    public void updateSignal(View v){
        String signal=tvSignal.getText().toString();
        if(signal.equals("")){
            Toast.makeText(SaveCommandActivity.this,"Please check your bluetooth connection is working",Toast.LENGTH_LONG).show();

        }
        else {
            MainActivity.getDatabaseHelper().updateCommand(ConnectionActivity.getDeviceId(),commandType,signal);
        }

    }

    public static void setMessage(String msg){
        Log.d(TAG,msg);
        tvSignal.setText(msg);
//        displayText.setText(msg);
    }



}
