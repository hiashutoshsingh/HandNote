package ashu.com.notes.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ashu.com.notes.Model.Notes;

import static ashu.com.notes.Model.Notes.TABLE_NAME;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NOTES";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Notes.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertNote(String note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Notes.COLUMN_NOTE, note);

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Notes getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{Notes.COLUMN_ID, Notes.COLUMN_NOTE},
                Notes.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Notes notes = new Notes(
                cursor.getInt(cursor.getColumnIndex(Notes.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Notes.COLUMN_NOTE)));

        cursor.close();

        return notes;
    }

    public List<Notes> getAllNotes() {
        List<Notes> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                Notes.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Notes note = new Notes();
                note.setId(cursor.getInt(cursor.getColumnIndex(Notes.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Notes.COLUMN_NOTE)));


                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateNote(Notes notes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Notes.COLUMN_NOTE, notes.getNote());
        return db.update(TABLE_NAME, values, Notes.COLUMN_ID + " = ?",
                new String[]{String.valueOf(notes.getId())});
    }

    public void deleteNote(Notes notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Notes.COLUMN_ID + " = ?",
                new String[]{String.valueOf(notes.getId())});
        db.close();
    }


}
