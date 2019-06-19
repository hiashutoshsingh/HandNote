package ashu.com.notes.Model;

import android.content.Context;

public class Notes {
    private Context context;


    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String TABLE_NAME = "notes";
    private int id;
    private String note;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT"
                    + ")";

    public Notes() {
    }

    public Notes(int id, String note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public void setId(int id) {
        this.id = id;
    }


}
