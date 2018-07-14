package edu.uci.ZotFinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.io.UnsupportedEncodingException;

public class DepartmentDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "UCI_department_directory";
    private SQLiteDatabase db;
    SQLiteStatement statement;

	public DepartmentDatabase(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * Create the employee table and populate it with sample data.
		 * In step 6, we will move these hardcoded statements to an XML document.
		 */
		String sql = "CREATE TABLE IF NOT EXISTS department (" +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"departmentName TEXT, " +
						"departmentLatitude TEXT, " +
						"departmentLongitude TEXT, " +
						"departmentPhoneNumber TEXT, " +
						"departmentAddress TEXT, " +
						"departmentWebsite TEXT)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS departments");
		onCreate(db);
	}

    public void clear() {
        db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS department");
        String sql = "CREATE TABLE IF NOT EXISTS department (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "departmentName TEXT, " +
                "departmentLatitude TEXT, " +
                "departmentLongitude TEXT, " +
                "departmentPhoneNumber TEXT, " +
                "departmentAddress TEXT, " +
                "departmentWebsite TEXT)";
        db.execSQL(sql);
        db.close();
        db = null;
    }

    public void open() {
        db = this.getWritableDatabase();
        db.beginTransaction();
        String sql = "INSERT INTO department VALUES (?,?,?,?,?,?,?);";
        statement = db.compileStatement(sql);
    }

    public void addToDatabase(String[] input) throws UnsupportedEncodingException {
        for (int i = 0; i<6; i++) {
            statement.bindString(i+2, input[i]);
        }
        statement.execute();
    }

    public void close() {
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        db = null;
    }
}
