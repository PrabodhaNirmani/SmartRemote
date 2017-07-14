package com.example.prabodhaharankahadeniya.smartremote;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;


public class BluetoothConnectionService {

    private static final String TAG="BluetoothConnectionServ";

    private static final String appName="myApp";
    private static  final UUID MY_UUID_INSECURE=
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private final BluetoothAdapter mBluetoothAdapter;
    ConnectionActivity mContext;


    private  AcceptThread mInsecuredAcceptThread;

    private String incomingMsg;

    private ConnectThread mconnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(ConnectionActivity context){

        mContext=context;
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        start();

    }


    private class AcceptThread extends Thread {

        private final BluetoothServerSocket serviceSocket;
        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try{
                tmp =mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName,MY_UUID_INSECURE);
                Log.d(TAG,"AcceptThread : setting up server using"+MY_UUID_INSECURE);

            }catch (IOException e){

            }
            serviceSocket=tmp;
        }

        public void run(){
            Log.d(TAG,"run:AcceptThread running");
            BluetoothSocket socket=null;


            try {
                Log.d(TAG,"run:server socket started");
                socket=serviceSocket.accept();
                Log.d(TAG,"run:connection successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
//consider in 3rd video
            if(socket!=null){
                connected(socket,mmDevice);
            }
            Log.d(TAG,"end acceptedThread");



        }

        public void cancel(){
            Log.d(TAG,"cancel: canceling acceptThread");
            try{
                serviceSocket.close();
            }
            catch (IOException e){
                Log.e(TAG,"cancel:close of acceptthrea server socet failed"+e.getMessage());
            }

        }
    }

    private  class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG,"ConnectThread : started");
            mmDevice=device;
            deviceUUID=uuid;
        }

        public void run(){
            BluetoothSocket tmp=null;
            Log.d(TAG,"run mConnectThread");
            try{
                Log.d(TAG,"ConnectThread: trying to create InsecureRfcommSocket using UUID: "+MY_UUID_INSECURE);
                tmp=mmDevice.createRfcommSocketToServiceRecord(deviceUUID);

            }catch (IOException e){
                Log.d(TAG,"ConnectThread: cound not create InsecureRfcommSocket: "+e.getMessage());
            }

            mmSocket=tmp;

            mBluetoothAdapter.cancelDiscovery();

            try {
                Log.d(TAG,mmSocket.toString());
                mmSocket.connect();

                Log.d(TAG,"run :ConnectThread connected ");
            } catch (IOException e) {

                try {
                    mmSocket.close();
                    Log.d(TAG,"run :close socket ");
                } catch (IOException e1) {
                    Log.d(TAG,"mConnectThread: run: unable to close connection in socket "+e.getMessage());
                }
                //mContext.s
//                mContext.setMessage("could not connect to the device try again");
                Log.d(TAG,"run :mConnectThread:could not connect to UUID "+MY_UUID_INSECURE);

            }
            //in the 3rd video
            connected(mmSocket,mmDevice);


        }
        public void cancel(){
            Log.d(TAG,"cancel: cosing client socket");
            try{
                mmSocket.close();
            }
            catch (IOException e){
                Log.e(TAG,"cancel:close of mmSocket in ConnectThread failed"+e.getMessage());
            }

        }
    }





    public synchronized void start(){
        Log.d(TAG,"start");
        if(mconnectThread!=null){
            mconnectThread.cancel();
            mconnectThread=null;
            Log.d(TAG,"not null");


        }
        if(mInsecuredAcceptThread==null){
            mInsecuredAcceptThread=new AcceptThread();
            mInsecuredAcceptThread.start();
            Log.d(TAG,"null");
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG,"startClient : started");
        mProgressDialog= ProgressDialog.show(mContext,"connecting bluetooth"
                    ,"pleae wait....",true);
        mconnectThread=new ConnectThread(device,uuid);
        mconnectThread.start();
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG,"ConnectedThread : starting..");
            mmSocket=socket;
            InputStream tmpIn=null;
            OutputStream tmpOut=null;
            try {
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                Log.d(TAG,"null error");
                e.printStackTrace();
            }

            try {
                tmpIn=mmSocket.getInputStream();
                tmpOut=mmSocket.getOutputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInputStream=tmpIn;
            mmOutputStream=tmpOut;

        }

        public void run(){
            byte[] buffer=new byte[1024];
            int bytes;

            while (true){
                try {
                    bytes=mmInputStream.read(buffer);
                    incomingMsg=new String(buffer,0,bytes);
                    Log.d(TAG,"InputStream : "+incomingMsg);
                    mContext.runOnUiThread(new Runnable() {




                        @Override
                        public void run() {
                        //TestCommandActivity.setMessage(incomingMsg);
//                            mContext.setMessage(incomingMsg);

                        }
                    });

                } catch (IOException e) {
                    Log.d(TAG,"Error reading to inputStream : "+e.getMessage());
                    break;
                }

            }

        }

        public void write(byte[] bytes){
            String text=new String(bytes, Charset.defaultCharset());
            Log.d(TAG,"write : writing to outputStream : "+text);
            Log.d(TAG,"write : writing to outputStream : "+mmOutputStream);

            try {

                mmOutputStream.write(bytes);
            } catch (IOException e) {
                Log.d(TAG,"write : Error writing to outputStream : "+e.getMessage());
            }

        }

        public void cancel(){
            try {
                mmSocket.close();
            }catch (IOException e){
                Log.d(TAG," Error closing socket : "+e.getMessage());
            }
        }


    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice){
        Log.d(TAG,"connected: started");

        mConnectedThread=new ConnectedThread (mmSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out){
        ConnectThread r;

        Log.d(TAG,"write : write called");
        mConnectedThread.write(out);
    }



}
