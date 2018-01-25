package com.bulletin.theinvincible.notemaking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;



public class DbHelper extends SQLiteOpenHelper{

    public static final String DB_NAME="HandNote";
    public static final int DB_VERSION=1;
    public static final String DB_TABLE="Task";
    public static final String DB_COLUMN="TaskName";

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query= String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL);",DB_TABLE,DB_COLUMN);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String query =String.format("DELETE TABLE IF EXISTS %s",DB_TABLE);
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }


    public void insertNewTask(String task)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues  =new ContentValues();
        contentValues.put(DB_COLUMN,task);
        sqLiteDatabase.insertWithOnConflict(DB_TABLE,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        sqLiteDatabase.close();
    }

    public void deleteTask(String task)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN + " = ?",new String[]{task});
        db.close();
    }

     public ArrayList<String> getTaskList()
     {
         ArrayList<String> tasklist =new ArrayList<>();
         SQLiteDatabase db= this.getReadableDatabase();

         Cursor cursor =db.query(DB_TABLE,new String[]{DB_COLUMN},null,null,null,null,null);

         while (cursor.moveToNext())

         {
             int index=cursor.getColumnIndex(DB_COLUMN);
             tasklist.add(cursor.getString(index));
         }

         cursor.close();
         db.close();
         return tasklist;
     }
}
