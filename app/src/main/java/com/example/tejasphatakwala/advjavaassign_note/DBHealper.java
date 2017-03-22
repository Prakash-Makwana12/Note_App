package com.example.tejasphatakwala.advjavaassign_note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.text.SimpleDateFormat;

public class DBHealper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "notedb";

    private static final String TABLE_Notes = "Notes";
    private static final String Note_id = "Id";
    private static final String Subject = "Subject";
    private static final String Title = "Title";
    private static final String Description = "Description";
    private static final String DateCreated = "DateCreated";


    private static final String TABLE_Category = "Catogery";
    private static final String Catogery = "CatogeryName";


    public DBHealper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //@Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "create table " + TABLE_Notes + "(" + Note_id + " TEXT PRIMARY KEY,"
                + Subject + " TEXT," + Title + " TEXT," + Description + " TEXT," +
                DateCreated + " TEXT)";
        db.execSQL(createTableQuery);

        String createTableQuery2 = "create table " + TABLE_Category + "(" + Catogery + " TEXT PRIMARY KEY)";
        db.execSQL(createTableQuery2);

    }

    //@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_Notes);
        onCreate(db);
        db.execSQL("drop table if exists " + TABLE_Category);
        onCreate(db);
    }

       public void insertData(String id, String sub, String title, String des , String curDate) {
           System.out.println("Inserting User Data");
           SQLiteDatabase db = this.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put(Note_id, id);
           values.put(Subject, sub);
           values.put(Title, title);
           values.put(Description, des);
           values.put(DateCreated, String.valueOf(curDate));
           db.insert(TABLE_Notes, null, values);
           db.close();
       }

    public void insertData(Note not) {
        System.out.println("Inserting User Data");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Note_id, not.NoteId);
        values.put(Subject, not.Subject);
        values.put(Title, not.Title);
        values.put(Description, not.Description);
        values.put(DateCreated, not.CreatedDate);
        db.insert(TABLE_Notes, null, values);
        db.close();
    }

    public Note[] getAllNotes(String order) {
        String selectQuery = "select * from " + TABLE_Notes + order;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery, null);
        Note array[] = new Note[cursor1.getCount()];

        if (cursor1.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            int i = 0;
            do {
                try {
                    System.out.println(cursor1.getString(0).toString());
                    Note not = new Note(cursor1.getString(0), cursor1.getString(1), cursor1.getString(2), cursor1.getString(3),
                            cursor1.getString(4));

                    array[i] = not;
                    i++;
                }catch (Exception e)
                {

                }
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        db.close();
        return array;
    }

    public Note[] getNotesByCondition(String condition, String order) {

        if (condition.isEmpty()) {
            return getAllNotes(order);
        } else {
            condition = " LOWER(" + Subject + ") LIKE '%" + condition.toLowerCase() + "%' "
                    + "OR LOWER(" + Title + ") LIKE '%" + condition.toLowerCase() + "%'"
            + "OR LOWER(" + Description + ") LIKE '%" + condition.toLowerCase() + "%'" ;

            String selectQuery = "select * from " + TABLE_Notes + " WHERE " + condition;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor1 = db.rawQuery(selectQuery, null);
            System.out.println("getting Data getAllCourseData retrived " + cursor1.getCount());
            Note array[] = new Note[cursor1.getCount()];

            if (cursor1.moveToFirst()) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                int i = 0;
                do {
                    try {
                        System.out.println(cursor1.getString(0).toString());
                        Note not = new Note(cursor1.getString(0), cursor1.getString(1), cursor1.getString(2), cursor1.getString(3),
                                cursor1.getString(4));

                        array[i] = not;
                        i++;
                    }catch (Exception e)
                    {

                    }
                } while (cursor1.moveToNext());
            }
            cursor1.close();
            db.close();
            db.close();
            System.out.println("exiting");
            return array;
        }
    }

    public void insertCategory(String c) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Catogery, c);

        db.insert(TABLE_Category, null, values);

        db.close();
    }

    public String[] getAllcategory() {
        String selectQuery = "select * from " + TABLE_Category;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery, null);
        String array[] = new String[cursor1.getCount()];

        if (cursor1.moveToFirst()) {
            int i = 0;
            do {
                System.out.println(cursor1.getString(0).toString());
                array[i] = cursor1.getString(0).toString();
                i++;
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        db.close();
        return array;
    }


    public void DeleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "DELETE FROM " + TABLE_Notes + " where " +
                Note_id + "='" + id + "'";
        db.execSQL(selectQuery);

        db.close();
    }

    public Note[] getNotesByID(String condition) {


        condition = Note_id + " = '" + condition + "'";

        String selectQuery = "select * from " + TABLE_Notes + " WHERE " + condition;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery, null);
        System.out.println("getting Data getAllCourseData retrived " + cursor1.getCount());
        Note array[] = new Note[cursor1.getCount()];

        if (cursor1.moveToFirst()) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            int i = 0;
            do {
                try {
                    System.out.println(cursor1.getString(0).toString());
                    Note not = new Note(cursor1.getString(0), cursor1.getString(1), cursor1.getString(2), cursor1.getString(3),
                            cursor1.getString(4));

                    array[i] = not;
                    i++;
                } catch (Exception e) {

                }
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        db.close();
        db.close();
        return array;
    }
}