package com.example.intel.hospital;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Intel on 21/06/2016.
 */
public class DBHelperD extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MESSAGES_DBP";
    private static  final int DATABASE_VERSION = 1;
    String Hdoctor = "";

    // TABLE NAME
    private static final String TABLE_MESSAGES = "Patients";

    // COLUMN NAME - Table Messages
    private static  final String KEY_ID = "id";
    private static final String KEY_NAME = "Name";
    private static  final String KEY_USER_NAME = "Gender";
    private static final String KEY_PASSWORD = "Age";
    private static final String KEY_MOBILE = "Mobile";
    private static final String KEY_USER_TYPE = "Doctor";


    private static final String CREATE_TABLE_MESSSAGES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MESSAGES + "("
            + KEY_ID + " INTEGER (12) PRIMARY KEY, "
            + KEY_NAME + " VARCHAR (30) , "
            + KEY_USER_NAME +  " VARCHAR (30) , "
            + KEY_PASSWORD + " VARCHAR (30) , "
            + KEY_MOBILE +  " VARCHAR (30) , "
            + KEY_USER_TYPE + " VARCHAR (30) "
            + ")";

    public DBHelperD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
    }


    public void insertMessage(MessageModel messageModel,String doctor){

        Hdoctor = doctor;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,messageModel.getTextId());
        values.put(KEY_NAME,messageModel.getName());
        values.put(KEY_USER_NAME,messageModel.getUserName());
        values.put(KEY_PASSWORD,messageModel.getPassword());
        values.put(KEY_MOBILE,messageModel.getMobile());
        values.put(KEY_USER_TYPE,messageModel.getUserType());

        db.insert(TABLE_MESSAGES, null, values);

        Log.d("DBHelper","Insert Messages Query Executed");
    }

    public ArrayList<MessageModel> readAllMessages(){

        ArrayList<MessageModel> temp = new ArrayList<>();
        String selectQuery;


        selectQuery = "SELECT * FROM " + TABLE_MESSAGES + " WHERE Doctor = '" + Hdoctor + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.moveToFirst()){

            do{

                MessageModel tempModel = new MessageModel();
                tempModel.setTextId(c.getInt((c.getColumnIndex(KEY_ID))));
                tempModel.setName(c.getString((c.getColumnIndex(KEY_NAME))));
                tempModel.setUserName(c.getString((c.getColumnIndex(KEY_USER_NAME))));
                tempModel.setPassword(c.getString((c.getColumnIndex(KEY_PASSWORD))));
                tempModel.setMobile(c.getString((c.getColumnIndex(KEY_MOBILE))));
                tempModel.setUserType(c.getString((c.getColumnIndex(KEY_USER_TYPE))));

                    temp.add(tempModel);

            }while(c.moveToNext());
        }

        Log.d("DBHelper","Fetching Messages Size : " + temp.size());
        return temp;
    }
}



