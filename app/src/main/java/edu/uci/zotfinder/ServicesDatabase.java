package edu.uci.zotfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServicesDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "UCI_services_directory"; 
	
	public ServicesDatabase(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * Create the employee table and populate it with sample data.
		 * In step 6, we will move these hardcoded statements to an XML document.
		 */
		String sql = "CREATE TABLE IF NOT EXISTS service (" +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
						"serviceName TEXT, " +
						"serviceAddress TEXT, " +
						"serviceLatitude TEXT," +
						"serviceLongitude TEXT)";
		
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS services");
		onCreate(db);
	}
	
	public void addToDatabase(String[] input)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("serviceName", input[0]);
		values.put("serviceAddress", input[1]);
		values.put("serviceLatitude", input[2]);
		values.put("serviceLongitude", input[3]);
		db.insert("service", null, values);
		db.close();
	}
	
}
