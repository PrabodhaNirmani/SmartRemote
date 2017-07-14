package com.example.prabodhaharankahadeniya.smartremote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="smart_remote_database";
    private static final String TABLE_NAME_1="device_table";
    private static final String COL_1_1="id";
    private static final String COL_1_2="device_name";
    private static final String TAG="Database helper";

    private static final String TABLE_NAME_2="command_table";
    private static final String COL_2_0="dev_id";
    private static final String COL_2_1="type";
    private static final String COL_2_2="serial_code";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
      //  SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_1="CREATE TABLE "+TABLE_NAME_1+" ("+COL_1_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_1_2+" TEXT)";
        db.execSQL(sql_1);
        String sql_2="CREATE TABLE "+TABLE_NAME_2+" ( ID INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_2_0+" INTEGER, "+COL_2_1+" TEXT,"+COL_2_2+" TEXT)";
        db.execSQL(sql_2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS "+TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXITS "+TABLE_NAME_2);
        onCreate(db);
    }

    public boolean insertDevice(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL_1_2,name);
        long result=db.insert(TABLE_NAME_1,null,values);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor getDevice(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME_1+" WHERE "+COL_1_1+"="+id+"",null);
        return result;
    }

    public int getDeviceId(){

        String countQuery = "SELECT  * FROM " + TABLE_NAME_1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public void deleteDevice(String device){
        String[] res=device.split(" ");
        String id=res[0];
        SQLiteDatabase db=this.getWritableDatabase();
        int res1=db.delete(TABLE_NAME_1,"id=?",new String[]{id});
        int res2=db.delete(TABLE_NAME_2,"id=?",new String[]{id});
        Log.d("Tag", String.valueOf(res1)+" "+ String.valueOf(res2));

    }

    public Cursor getAllDevices(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME_1,null);
        return result;
    }

    public boolean updateDeviceName(String id, String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL_1_1,id);
        values.put(COL_1_2,name);
        long result=db.update(TABLE_NAME_1,values,"id = ?",new String[]{ id });
        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean insertCommand(String id, String type, String serial){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL_2_0,id);
        values.put(COL_2_1,type);
        values.put(COL_2_2,serial);
        long result=db.insert(TABLE_NAME_2,null,values);
        if(result==-1)
            return false;
        else
            return true;

    }

    public String getCommand(String id, String type){
        SQLiteDatabase db=this.getWritableDatabase();
        Log.d(TAG,id+" "+type);
        Cursor result=null;
        try {
//            Cursor c = db.rawQuery("SELECT * FROM tbl1 WHERE TRIM(name) = '"+name.trim()+"'", null)
            String query="SELECT * FROM "+TABLE_NAME_2+" WHERE "+COL_2_0+"="+id+"";
//            String query="SELECT * FROM "+TABLE_NAME_2.trim()+" WHERE "+COL_2_0.trim()+"= '"+id+"' AND "+COL_2_1.trim()+"= '"+type+"'";
            Log.d(TAG,query);

            result=db.rawQuery(query,null);
            for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
                Log.d(TAG,result.getString(2));
                if(result.getString(2).equals(type)){
                    Log.d(TAG,"he");
                    String signal=result.getString(3).toString();
                    Log.d(TAG,signal);
                    return signal;

                }
            }


        }catch (Exception e){
            Log.d(TAG,"Error occured during transaction");
            return null;
//            Toast.makeText(InstantiateRemoteActivity.class,"Error occured durin transaction try again",Toast.LENGTH_LONG).show();
        }
        return null;



    }

    public boolean updateCommand(String id, String type, String serial){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL_2_0,id);
        values.put(COL_2_1,type);
        values.put(COL_2_2,serial);
        long result=db.update(TABLE_NAME_2,values,"dev_id = ? and type = ?",new String[]{ id, type });
        if(result==-1)
            return false;
        else
            return true;

    }

    public boolean confirmRegistration(String id){
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor result=null;
        try {
            String query="SELECT * FROM "+TABLE_NAME_2+" WHERE "+COL_2_0+"="+id+"";
            Log.d(TAG,query);
            result=db.rawQuery(query,null);
//            for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
//
//                String line=result.getString(1)+" "+result.getString(3)+" "+result.getString(2);
//
//                Log.d(TAG,line);
//
//            }
        }catch (Exception e){
            Log.d(TAG,"Error occured during transaction");
//            Toast.makeText(InstantiateRemoteActivity.class,"Error occured durin transaction try again",Toast.LENGTH_LONG).show();
        }
        finally {
            Log.d(TAG, String.valueOf(result.getCount()));
            return result.getCount()== 5;
        }

    }
}
