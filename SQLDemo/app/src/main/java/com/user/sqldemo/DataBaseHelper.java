package com.user.sqldemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    // set up constants variables
    public static final String CUSTOMER_TABLE = "cutomer_table";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_AGE = "customer_Age";
    public static final String ACTIVE_CUSTOMER = "active_customer";
    public static final String ID = "ID";

    public DataBaseHelper(@Nullable Context context) { //Database helper
        super(context, "customer.db", null, 1);
    } //

    //create DataBase
    @Override
    public void onCreate(SQLiteDatabase db) { //create method
        //create a table in the database
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CUSTOMER_NAME + " TEXT, " + CUSTOMER_AGE + " INT, " + ACTIVE_CUSTOMER + " BOOL)";

        db.execSQL(createTableStatement); //execute query

    }
    //this is called if the database version number changes. It prevent previous users app from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //upgrade method

    }

    public boolean addOne(CustomerModel customerModel){ //add one record method

        SQLiteDatabase db = this.getReadableDatabase(); //open database
        ContentValues cv = new ContentValues(); //content value
        cv.put(CUSTOMER_NAME, customerModel.getName()); //get name
        cv.put(CUSTOMER_AGE, customerModel.getAge()); //get age
        cv.put(ACTIVE_CUSTOMER, customerModel.isActive()); //get active status

        long insert = db.insert(CUSTOMER_TABLE, null, cv); // insert data to the database

        if (insert == -1){ // check data insertion success or not
            return false;
        }
        else{
            return true;
        }


    }

    public boolean deleteOne(CustomerModel customerModel){ // delete record method
        // find customerModel in the database. If found delete it and return true
        // if not found return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + ID + " = " + customerModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){ //check deletion success
            return true;
        }
        else{
            return false;
        }
    }

    public List<CustomerModel> getEveryOne(){ // get all the from the database

        List<CustomerModel> returnList = new ArrayList<>();

        //get the data from the database
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE; // SQL query
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null); // execute query

        if (cursor.moveToFirst()){ //
            //loop through the cursor (result set) and create new customer object. put them into the return list
            do {
                int customerID = cursor.getInt(0);
                String customer_name = cursor.getString(1);
                int customer_age = cursor.getInt(2);
                boolean customer_active = cursor.getInt(3) == 1 ? true: false;

                CustomerModel newCustomer = new CustomerModel(customerID, customer_name, customer_age, customer_active);
                returnList.add(newCustomer);


            }while(cursor.moveToNext());
        }
        else {
            //failure do not add anything to the list
        }

        //close the both cursor and the db when done
        cursor.close();
        db.close();
        return returnList;
    }
}
