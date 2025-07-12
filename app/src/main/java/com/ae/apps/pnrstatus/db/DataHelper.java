/*
 * MIT License
 *
 * Copyright (c) 2019 Midhun Harikumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ae.apps.pnrstatus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Logger;

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

	/* _id, pnrnumber, unused */
	private static final String	PNR_TABLE_CREATE		= "CREATE TABLE " + TABLE_PNR + " (" + KEY_ID
																+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATA1
																+ " TEXT NOT NULL," + KEY_DATA2 + " TEXT);";
	/* _id, reminderdate, note (optional) */
	private static final String	REMINDER_TABLE_CREATE	= "CREATE TABLE " + TABLE_REMINDERS + " (" + KEY_ID
																+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATA1
																+ " TEXT NOT NULL," + KEY_DATA2 + " TEXT);";

	private OpenHelper			mDbHelper;
	private SQLiteDatabase		mDatabase;
	private final Context		mContext;

	/**
	 * Constructor
	 * 
	 * @param ctx the context
	 */
	public DataHelper(Context ctx) {
		this.mContext = ctx;
	}

	/**
	 * Method to open the database
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DataHelper open() throws SQLException {
		mDbHelper = new OpenHelper(mContext);
		mDatabase = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Method to close the database
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * Add a PNRNumber with a note to the database
	 */
	public long addPnrNumber(String pnrNumber, String data2) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATA1, pnrNumber);
		initialValues.put(KEY_DATA2, data2);

		// Really insert the data
		return mDatabase.insert(TABLE_PNR, null, initialValues);
	}

	/**
	 * Add a PNRNumber to the database
	 * 
	 * @param pnrNumber pnr number
	 * @return
	 */
	public long addPnrNumber(String pnrNumber) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_DATA1, pnrNumber);

		return mDatabase.insert(TABLE_PNR, null, initialValues);
	}

	/**
	 * Delete a PNRRow
	 * 
	 * @param rowId rowid
	 * @return true if successful, false if failed
	 */
	public boolean deletePnrNumber(long rowId) {
		return mDatabase.delete(TABLE_PNR, KEY_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Fetch results
	 */
	public Cursor fetchAllPnrNumbers() {
		return mDatabase.query(TABLE_PNR, new String[] { KEY_ID, KEY_DATA1, KEY_DATA2 }, null, null, null, null, null);
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
		Cursor mCursor = mDatabase.query(true, TABLE_PNR, new String[] { KEY_ID, KEY_DATA1, KEY_DATA2 }, KEY_ID + "="
				+ rowId, null, null, null, null, null);

        mCursor.moveToFirst();
        return mCursor;

	}

	/**
	 * Helper class that extends the SQLiteOpenHelper class to do database operations
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
			Logger.w(AppConstants.TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PNR);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
			onCreate(db);
		}

	}
}