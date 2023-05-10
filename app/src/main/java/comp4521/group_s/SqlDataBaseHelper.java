package comp4521.group_s;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class SqlDataBaseHelper extends SQLiteOpenHelper {

    private static SqlDataBaseHelper dbhelper;
    private static final String BDname = "DBUserData";
    private static final int DBversion = 1;

    public SqlDataBaseHelper(Context context) {
        super(context, BDname, null, DBversion);
    }

    public static SqlDataBaseHelper instanceOfDatabase(Context context)
    {
        if(dbhelper == null)
            dbhelper = new SqlDataBaseHelper(context);
        return dbhelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS profile (USER_ID varchar PRIMARY KEY, NAME varchar, EMAIL varchar, DOB date, GENDER varchar, WEIGHT decimal, HEIGHT decimal, ACTIVITY_LEVEL varchar, GOAL varchar);")
                .append("CREATE TABLE meal_plan (PLAN_ID integer PRIMARY KEY, USER_ID varchar, MEAL_TYPE varchar, FOOD_ID integer, SERVING_SIZE decimal);")
                .append("CREATE TABLE intake_record (INTAKE_ID integer PRIMARY KEY, USER_ID varchar, FOOD_ID integer, SERVING_SIZE decimal, DATE DATE);")
                .append("CREATE TABLE progress (PROGRESS_ID integer PRIMARY KEY, USER_ID varchar, WEIGHT decimal, BODY_MEASUREMENT_TYPE varchar, BODY_MEASUREMENT_VALUE decimal);");
        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        StringBuilder sql = new StringBuilder()
                        .append("DROP TABLE profile IF EXISTS profile;")
                        .append("DROP TABLE intake_record IF EXISTS intake_record;")
                        .append("DROP TABLE progress IF EXISTS progress;")
                        .append("DROP TABLE meal_plan IF EXISTS meal_plan;");
        sqLiteDatabase.execSQL(sql.toString());

    }

    public void UpdateUserprofile(String id, String name , String email, String DOB, String gender, String weight, String height, String activityLevel, String goal){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        StringBuilder sql = new StringBuilder()
        .append("INSERT OR REPLACE INTO profile (USER_ID, NAME, EMAIL, DOB, GENDER, WEIGHT, HEIGHT, ACTIVITY_LEVEL, GOAL) VALUES (")
        .append("'" + id + "',")
        .append("'" + name + "',")
        .append("'" + email + "',")
        .append("'" + DOB + "',")
        .append("'" + gender + "',")
        .append(weight + ",")
        .append(height + ",")
        .append("'" + activityLevel + "',")
        .append("'" + goal + "')");
        sqLiteDatabase.execSQL(sql.toString());
    }

    public String GetUserName(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String name = "";
        c = sqLiteDatabase.rawQuery("SELECT NAME FROM profile WHERE USER_ID = '" + id + "';", null);
        if (c.moveToFirst()) {
            name = c.getString(0);
        }
        c.close();
        return name;
    }
    public String GetUserEmail(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String email = "";
        c = sqLiteDatabase.rawQuery("SELECT EMAIL FROM profile WHERE USER_ID = '" + id + "';",null);
        if (c.moveToFirst()) {
            email = c.getString(0);
        }
        c.close();
        return email;
    }

    public String GetUserDOB(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String dob = "";
        c = sqLiteDatabase.rawQuery("SELECT DOB FROM profile WHERE USER_ID = '" + id + "';", null);
        if (c.moveToFirst()) {
            dob = c.getString(0);
        }
        c.close();
        return dob;
    }

    public String GetUserGender(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String gender = "";
        c = sqLiteDatabase.rawQuery("SELECT GENDER FROM profile WHERE USER_ID = '" + id + "';", null);
        if (c.moveToFirst()) {
            gender = c.getString(0);
        }
        c.close();
        return gender;
    }

    public String GetUserHeight(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String height = "";
        c = sqLiteDatabase.rawQuery("SELECT HEIGHT FROM profile WHERE USER_ID = '" + id + "';", null);
        if (c.moveToFirst()) {
            height = c.getString(0);
        }
        c.close();
        return height;
    }

    public String GetUserWeight(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String weight = "";
        c = sqLiteDatabase.rawQuery("SELECT WEIGHT FROM profile WHERE USER_ID = '" + id + "';", null);
        if (c.moveToFirst()) {
            weight = c.getString(0);
        }
        c.close();
        return weight;
    }

    public String GetUserActivityLevel(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String activityLevel = "";
        c = sqLiteDatabase.rawQuery("SELECT ACTIVITY_LEVEL FROM profile WHERE USER_ID = '" + id + "';", null);
        if (c.moveToFirst()) {
            activityLevel = c.getString(0);
        }
        c.close();
        return activityLevel;
    }

    public String GetUserFitnessGoal(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c = null;
        String fitnessGoal = "";
        c = sqLiteDatabase.rawQuery("SELECT GOAL FROM profile WHERE USER_ID = '" + id + "';", null);
        if (c.moveToFirst()) {
            fitnessGoal = c.getString(0);
        }
        c.close();
        return fitnessGoal;
    }

}
