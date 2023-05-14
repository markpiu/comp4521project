package comp4521.group_s;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "DATA.DB";
    static final int DATABASE_VERSION = 1;

    static final String TABLE_NAME = "USER";
    static final String ENTRY_ID = "ENTRY_ID";
    static final String DATE = "DATE";
    static final String INPUT_TYPE = "INPUT_TYPE";
    static final String AMOUNT = "AMOUNT";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME
            + " ( " + ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATE + " DATE NOT NULL, "
            + INPUT_TYPE + " STRING NOT NULL, "
            + AMOUNT + " INTEGER NOT NULL ); ";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
