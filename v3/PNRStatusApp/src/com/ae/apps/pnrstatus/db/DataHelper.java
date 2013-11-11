/*
 * Copyright 2012 Midhun Harikumar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

	/* DataBase information */
	private static final int	DATABASE_VERSION		= 2;
	private static final String	DATABASE_NAME			= "ApplicationData";

	/* Table names */
	private static final String	TABLE_PNR				= "DataTable";
	private static final String	TABLE_REMINDERS			= "Reminders";
	
	/* Table Keys */
	public static final String	KEY_ID					= "_id";
	public static final String	KEY_DATA1				= "data1";
	public static final String	KEY_DATA2				= "data2";

	/* Table creation code */
	
	/* _id, pnrnumber, unused*/
	private static final String	PNR_TABLE_CREATE		= "CREATE TABLE " + TABLE_PNR + " (" + KEY_ID
																+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATA1
																+ " TEXT NOT NULL," + KEY_DATA2 + " TEXT);";
	/* _id, reminderdate, note (optional) */
	private static final String	REMINDER_TABLE_CREATE	= "CREATE TABLE " + TABLE_REMINDERS + " (" + KEY_ID
																+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATA1
																+ " TEXT NOT NULL," + KEY_DATA2 + " TEXT);";

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

		return mDb.insert(TABLE_PNR, null, initialValues);
	}

	public long addPnrNumber(String pnrNumber) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATA1, pnrNumber);

		return mDb.insert(TABLE_PNR, null, initialValues);
	}

	/**
	 * Delete a PNRRow
	 * 
	 * @param rowId
	 * @return true if successful, false if failed
	 */
	public boolean deletePnrNumber(long rowId) {
		return mDb.delete(TABLE_PNR, KEY_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Fetch results
	 */
	public Cursor fetchAllPnrNumbers() {
		return mDb.query(TABLE_PNR, new String[] { KEY_ID, KEY_DATA1, KEY_DATA2 }, null, null, null, null, null);
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
		Cursor mCursor = mDb.query(true, TABLE_PNR, new String[] { KEY_ID, KEY_DATA1, KEY_DATA2 },
				KEY_ID + "=" + rowId, null, null, null, null, null);

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
			db.execSQL(PNR_TABLE_CREATE);
			db.execSQL(REMINDER_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(AppConstants.TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PNR);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
			onCreate(db);
		}

	}
}