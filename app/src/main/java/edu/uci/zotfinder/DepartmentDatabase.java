package edu.uci.zotfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DepartmentDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "UCI_department_directory"; 
	
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
	
	public void addToDatabase(String[] input)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("departmentName", input[0]);
		values.put("departmentLatitude", input[1]);
		values.put("departmentLongitude", input[2]);
		values.put("departmentPhoneNumber", input[3]);
		values.put("departmentAddress", input[4]);
		values.put("departmentWebsite", input[5]);
		db.insert("department", null, values);
		db.close();
	}
	
}
