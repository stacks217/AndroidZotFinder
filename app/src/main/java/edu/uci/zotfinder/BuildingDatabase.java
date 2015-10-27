package edu.uci.zotfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuildingDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "UCI_building_directory";
	
	public BuildingDatabase(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * Create the employee table and populate it with sample data.
		 * In step 6, we will move these hardcoded statements to an XML document.
		 */
		String sql = "CREATE TABLE IF NOT EXISTS building (" +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
						"buildingName TEXT, " +
						"buildingLongitude TEXT, " +
						"buildingLatitude TEXT, " +
						"buildingType TEXT, " +
						"buildingAddress TEXT, " + 
						"buildingNumber TEXT, " + 
						"buildingAbbreviation TEXT)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS buildings");
		onCreate(db);
	}
	
	public void addToDatabase(String[] input)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("buildingName", input[0]);
		values.put("buildingLongitude", input[1]);
		values.put("buildingLatitude", input[2]);
		values.put("buildingType", input[3]);
		values.put("buildingAddress", input[4]);
		values.put("buildingNumber", input[5]);
		values.put("buildingAbbreviation", input[6]);
		db.insert("building", null, values);
		db.close();
	}
	
}
