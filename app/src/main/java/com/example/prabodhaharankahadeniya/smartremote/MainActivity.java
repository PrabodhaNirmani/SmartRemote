package com.example.prabodhaharankahadeniya.smartremote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";

    final Context context = this;

    private static DatabaseHelper myDB;


    private ArrayList<String> deviceList=new ArrayList<>();

    private static String viewId="0";



    private ImageButton btnAdd;
    private TextView mTextMessage;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_learn:
                    mTextMessage.setText(R.string.title_learn_mode);
                    btnAdd.setVisibility(View.VISIBLE);

                    generateListContent();
                    viewId="0";

                    return true;
                case R.id.navigation_operate:
                    mTextMessage.setText(R.string.title_operating_mode);
                    btnAdd.setVisibility(View.INVISIBLE);

                    viewId="1";
                    if(deviceList.size()==0){
                        Toast.makeText(MainActivity.this,"No devices registered yet. Register first",Toast.LENGTH_LONG).show();

                    }
                    generateFinalizedListItems();
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB=new DatabaseHelper(this);
        mTextMessage = (TextView) findViewById(R.id.message);
        btnAdd=(ImageButton)findViewById(R.id.btnAdd);
        ListView listView = (ListView) findViewById(R.id.device_list);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        generateListContent();


        Log.d(TAG,"before generate list content");



        listView.setAdapter(new MyListAdapter(getContext(),R.layout.activity_device_item,this.deviceList));





    }

//    @Override
//    public void onPause(){
//        super.onPause();
//        deviceList.clear();
//    }

    private void generateListContent(){
        deviceList.clear();
        Log.d(TAG,"inside generate list content");
        Cursor cursor=MainActivity.getDatabaseHelper().getAllDevices();
        if(cursor==null){
            Log.d(TAG,"null");
        }
        else {
            // Log.d(TAG,"else");
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                String line=cursor.getString(0)+" "+cursor.getString(1);

                Log.d(TAG,line);
                deviceList.add(line);
            }
            cursor.close();
        }

    }

    public void generateFinalizedListItems(){
        deviceList.clear();
        Log.d(TAG,"inside generate list content");
        Cursor cursor=MainActivity.getDatabaseHelper().getAllDevices();
        if(cursor==null){
            Log.d(TAG,"null");
        }
        else {
            Log.d(TAG,"else");
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String devId=cursor.getString(0);
                String devName=cursor.getString(1);
                String line=devId+" "+devName;
                boolean finished=MainActivity.getDatabaseHelper().confirmRegistration(devId);
                if(finished){
                    Log.d(TAG,line);
                    deviceList.add(line);
                }


            }
            cursor.close();
        }

    }


    public static DatabaseHelper getDatabaseHelper(){
        return myDB;
    }

    public MainActivity getContext(){
        return this;
    }

    public MainActivity getInstance(){
        return this;
    }



    public void addDevicePressed(View v){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                Log.d(TAG,userInput.getText().toString());
                                if(userInput.getText().toString().equals(""))
                                    Toast.makeText(MainActivity.this,"Please make sure to fill the device name field",Toast.LENGTH_LONG).show();
                                else{
                                    boolean inserted=MainActivity.getDatabaseHelper().insertDevice(userInput.getText().toString());
                                    if(inserted){
                                        Toast.makeText(MainActivity.this,"Device registered",Toast.LENGTH_LONG).show();
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);



                                    }else {
                                        Toast.makeText(MainActivity.this,"Error occurred. Try again",Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private class MyListAdapter extends ArrayAdapter<String> {

        private int layout;

        MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.layout=resource;
        }


        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            ViewHolder mainViewHolder=null;
            if(convertView==null){
                LayoutInflater inflater=LayoutInflater.from(getContext());
                convertView=inflater.inflate(layout,parent,false);
                final ViewHolder viewHolder=new ViewHolder();
//                viewHolder.imageView=(ImageView)convertView.findViewById()
                viewHolder.imageView=convertView.findViewById(R.id.ivDevice);
                viewHolder.deviceName=convertView.findViewById(R.id.tvDeviceName);
                viewHolder.deleteButton=convertView.findViewById(R.id.btnDelete);
                viewHolder.deviceName.setText(deviceList.get(position));
                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        MainActivity.getDatabaseHelper().deleteDevice(viewHolder.deviceName.getText().toString());
                        Intent intent=getIntent();
                        finish();

                        startActivity(intent);
                    }

                });

                viewHolder.deviceName.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getInstance(),ConnectionActivity.class);
//                        intent.putExtra("id", viewHolder.deviceName.getText().toString()+" "+viewId);
                        intent.putExtra("id",viewId);
                        intent.putExtra("device",viewHolder.deviceName.getText().toString());
                        startActivity(intent);
                    }
                });

                viewHolder.imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getInstance(),ConnectionActivity.class);
                        intent.putExtra("id",viewId);
                        intent.putExtra("device",viewHolder.deviceName.getText().toString());
                        startActivity(intent);


                    }
                });
                convertView.setTag(viewHolder);
            }
            else {
                mainViewHolder=(ViewHolder)convertView.getTag();
                mainViewHolder.deviceName.setText(getItem(position));
            }
            return convertView;
        }
    }

    private class ViewHolder{
        ImageView imageView;
        TextView deviceName;
        ImageButton deleteButton;
    }



}
