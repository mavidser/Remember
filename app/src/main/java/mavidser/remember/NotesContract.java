package mavidser.remember;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sid on 14/1/15.
 */

public final class NotesContract {

    private static final int NORMAL=1;
    private static final int ARCHIVED=2;
    private static final int PINNED=3;
    private static final int ALL=4;
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public NotesContract() {}

    /* Inner class that defines the table contents */
    public static abstract class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_PINNED = "pinned";
        public static final String COLUMN_NAME_ARCHIVED = "archived";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
                    NoteEntry._ID + " INTEGER PRIMARY KEY," +
                    NoteEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_PINNED + TEXT_TYPE + COMMA_SEP +
                    NoteEntry.COLUMN_NAME_ARCHIVED + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME;


    public void createNote(View view, NoteInfo note) {
        NotesDbHelper mDbHelper = new NotesDbHelper(view.getContext());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_CONTENT, note.content);
        values.put(NoteEntry.COLUMN_NAME_TIME, note.date);
        values.put(NoteEntry.COLUMN_NAME_PINNED, note.pinned);
        values.put(NoteEntry.COLUMN_NAME_ARCHIVED, note.archived);

        long newRowId;
        newRowId = db.insert(
                NotesContract.NoteEntry.TABLE_NAME,
                null,
                values);
        System.out.println(newRowId);
    }

    public List<NoteInfo> readNotesList(View view,int TYPE) {

        NotesDbHelper mDbHelper = new NotesDbHelper(view.getContext());

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_NAME_CONTENT,
                NoteEntry.COLUMN_NAME_PINNED,
                NoteEntry.COLUMN_NAME_TIME,
                NoteEntry.COLUMN_NAME_ARCHIVED
        };

        String sortOrder = NoteEntry._ID + " DESC";
        String selection = NoteEntry.COLUMN_NAME_ARCHIVED + " ='0'";

        Cursor cursor = db.query(
                NoteEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<NoteInfo> result = new ArrayList<NoteInfo>();
        if (cursor != null)
            try {
                cursor.moveToFirst();
                while ( !cursor.isAfterLast() ) {
                    NoteInfo ci = new NoteInfo();
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry._ID));
                    ci.content = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_CONTENT));
                    ci.pinned = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_PINNED));
                    ci.date = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_TIME));
                    ci.archived = cursor.getString(cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_ARCHIVED));
                    result.add(ci);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }

        return result;
    }



    public class NotesDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 6;
        public static final String DATABASE_NAME = "Notes.db";

        public NotesDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}