package edu.uci.ZotFinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.io.UnsupportedEncodingException;

public class BuildingDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "UCI_building_directory";
    private SQLiteDatabase db;
    SQLiteStatement statement;

	public BuildingDatabase(Context context) {
		super(context, DATABASE_NAME, null, 4);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * Create the employee table and populate it with sample data.
		 * In step 6, we will move these hardcoded statements to an XML document.
		 */
        db.execSQL("DROP TABLE IF EXISTS building");
        String sql = "CREATE TABLE IF NOT EXISTS building (" +
						"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"buildingName TEXT, " +
						"buildingLongitude TEXT, " +
						"buildingLatitude TEXT, " +
						"buildingAddress TEXT, " +
                        "buildingNumber TEXT, " +
						"buildingAbbr TEXT)";
		db.execSQL(sql);
		
	}

    public void clear() {
        db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS building");
        String sql = "CREATE TABLE IF NOT EXISTS building (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "buildingName TEXT, " +
                "buildingLongitude TEXT, " +
                "buildingLatitude TEXT, " +
                "buildingAddress TEXT, " +
                "buildingNumber TEXT, " +
                "buildingAbbr TEXT)";
        db.execSQL(sql);
        db.close();
        db = null;
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS buildings");
		onCreate(db);
	}

    public void open() {
        db = this.getWritableDatabase();
        db.beginTransaction();
        String sql = "INSERT INTO building VALUES (?,?,?,?,?,?,?);";
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
