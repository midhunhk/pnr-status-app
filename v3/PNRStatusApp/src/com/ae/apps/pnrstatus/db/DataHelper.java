package com.ae.apps.pnrstatus.db;

import com.ae.apps.pnrstatus.utils.AppConstants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper class for interacting with the system database
 * 
 * @author midhun_harikumar
 * 
 */
public class DataHelper {

	public static final String	KEY_ID				= "_id";
	public static final String	KEY_DATA1			= "data1";
	public static final String	KEY_DATA2			= "data2";

	private static final String	DATABASE_NAME		= "ApplicationData";
	private static final int	DATABASE_VERSION	= 1;
	private static final String	TABLE_NAME			= "DataTable";

	private static final String	DATABASE_CREATE		= "create table DataTable ("
															+ "_id integer primary key autoincrement, "
															+ "data1 text not null," + "data2 text);";

	private OpenHelper			mDbHelper;
	private SQLiteDatabase		mDb;
	private final Context		mCtx;

	/**
	 * Constructor
	 * 
	 * @param ctx
	 */
	public DataHelper(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Method to open the database
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DataHelper open() throws SQLException {
		mDbHelper = new OpenHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Method to close the database
	 */
	public void close() {
		mDbHelper.close();
	}

	// ---------------------------------
	// Database Operations
	// ---------------------------------

	/**
	 * Add a PNRRow to the database
	 */
	public long addPnrNumber(String pnrNumber, String data2) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATA1, pnrNumber);
		initialValues.put(KEY_DATA2, data2);

		return mDb.insert(TABLE_NAME, null, initialValues);
	}

	public long addPnrNumber(String pnrNumber) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATA1, pnrNumber);

		return mDb.insert(TABLE_NAME, null, initialValues);
	}

	/**
	 * Delete a PNRRow
	 * 
	 * @param rowId
	 * @return true if successful, false if failed
	 */
	public boolean deletePnrNumber(long rowId) {
		return mDb.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Fetch results
	 */
	public Cursor fetchAllPnrNumbers() {
		return mDb.query(TABLE_NAME, new String[] { KEY_ID, KEY_DATA1, KEY_DATA2 }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchPnrNumber(long rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_ID, KEY_DATA1, KEY_DATA2 }, KEY_ID + "="
				+ rowId, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	// ---------------------------------
	// Inner Class
	// ---------------------------------

	/**
	 * Helper class that extends the SQLiteOpenHelper class to do database operations
	 * 
	 * @author Midhun_Harikumar
	 * 
	 */
	private static class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(AppConstants.TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}

	}
}