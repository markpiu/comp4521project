package comp4521.group_s;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;
import java.util.Date;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context ctx){
        context = ctx;
    }

    public DatabaseManager open() throws SQLDataException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert (String ENTRY_ID, Long date, String inputType, Float amount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ENTRY_ID, ENTRY_ID);
        contentValues.put(DatabaseHelper.DATE, date);
        contentValues.put(DatabaseHelper.INPUT_TYPE, inputType);
        contentValues.put(DatabaseHelper.AMOUNT, amount);
        database.insert(DatabaseHelper.TABLE_NAME,null, contentValues);
    }

    public Cursor query(String inputTYPE) {
        String[] columns = new String[] {DatabaseHelper.ENTRY_ID, DatabaseHelper.DATE, DatabaseHelper.INPUT_TYPE, DatabaseHelper.AMOUNT};
        String [] whereArgs = {inputTYPE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, "INPUT_TYPE = ?", whereArgs, null, null, "DATE");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update (long entry_id, String newInputType, Integer newAmount) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.INPUT_TYPE, newInputType);
        contentValues.put(DatabaseHelper.AMOUNT, newAmount);
        int result = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.ENTRY_ID + "=" + entry_id, null);
        return result;
    }

    public void delete (long sid) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ENTRY_ID + "=" + sid, null);
    }

    public long getSize() {
        long count = DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_NAME);
        return count;
    }
}
