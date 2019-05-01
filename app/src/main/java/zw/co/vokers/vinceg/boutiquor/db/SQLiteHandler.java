package zw.co.vokers.vinceg.boutiquor.db;

/**
 * Created by Vince G on 23/2/2019
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import zw.co.vokers.vinceg.boutiquor.models.User;


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "boutiquorLite";
    // Login table name
    private static final String TABLE_USER = "users";
    // order table name
    private static final String TABLE_ORDER = "orders";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UUID = "unique_id";
    private static final String KEY_NAME = "fullname";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SALT = "salt";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CREATED_AT = "created";

    // Login Table Columns names
    private static final String O_ID = "id";
    private static final String O_ORDER_NUMBER = "ordernumber";
    private static final String O_CLIENT = "client";
    private static final String O_AMOUNT = "amount";
    private static final String O_STATUS = "status";
    private static final String O_AGENT = "agent";
    private static final String O_ITEMS = "items";
    private static final String O_ORDERED = "ordered";
    private static final String O_COMPLETED = "completed";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UUID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_ROLE + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_MOBILE + " TEXT UNIQUE,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_SALT + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "("
                + O_ID + " INTEGER PRIMARY KEY,"
                + O_ORDER_NUMBER + " TEXT UNIQUE,"
                + O_CLIENT + " TEXT,"
                + O_AMOUNT + " TEXT,"
                + O_STATUS + " TEXT,"
                + O_AGENT + " TEXT,"
                + O_ITEMS + " TEXT,"
                + O_ORDERED + " TEXT,"
                + O_COMPLETED + " TEXT"+ ")";


        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String uniqueid,String name,String role,String username,String mobile, String email,String password, String salt,String address, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UUID, uniqueid);
        values.put(KEY_NAME, name);
        values.put(KEY_ROLE, role);
        values.put(KEY_USERNAME, username);
        values.put(KEY_MOBILE, mobile);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_SALT, salt);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_CREATED_AT, created_at);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Storing order details in database
     * */
    public void addOrder(String id,String ordernum,String client, String amount, String status, String agent, String items,String ordered, String completed){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(O_ID, id);
        values.put(O_ORDER_NUMBER, ordernum);
        values.put(O_CLIENT, client);
        values.put(O_AMOUNT, amount);
        values.put(O_STATUS, status);
        values.put(O_AGENT, agent);
        values.put(O_ITEMS, items);
        values.put(O_ORDERED, ordered);
        values.put(O_COMPLETED, completed);

        // Inserting Row
        long idy = db.insert(TABLE_ORDER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New order inserted into sqlite: " + idy);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT fullname, mobile FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("fullname", cursor.getString(0));
            user.put("mobile", cursor.getString(1));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public User getUserDetailsByMobileAndPassword(String mobile, String password) {
        User user = null;
        String selectQuery = "SELECT * FROM "+ TABLE_USER +" WHERE "+ KEY_MOBILE +"='"+ mobile +"' AND "+ KEY_PASSWORD +"='"+ password +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.moveToFirst()) {
            do {
                user = new User(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10));
                // get the data into array, or class variable
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user

        return user;
    }

    public boolean isUserContentEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean empty = true;
        Cursor cur;
        try{
            cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USER, null);
        }catch(Exception e){
            e.printStackTrace();
            return empty;
        }
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt(0) == 0);
        }
        cur.close();

        return empty;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.delete(TABLE_ORDER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}

