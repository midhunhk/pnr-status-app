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

package com.ae.apps.pnrstatus.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.vo.MessageVo;

/**
 * Manages the content of the phone's inbox
 * 
 * @author midhun_harikumar
 * 
 */
public class SMSManager {

	private static String			TAG				= "SMSManager";
	private static String			SMS_INBOX_URI	= "content://sms/"; // inbox

	private final ContentResolver	contentResolver;
	private final List<MessageVo>	messagesList;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public SMSManager(Context context) {
		this.contentResolver = context.getContentResolver();
		messagesList = fetchAllMessages();
	}

	public List<MessageVo> fetchAllMessages() {
		List<MessageVo> list = new ArrayList<MessageVo>();
		// get the uri and cursor
		Uri mSmsinboxQueryUri = Uri.parse(SMS_INBOX_URI);
		Cursor cursor = contentResolver.query(mSmsinboxQueryUri, new String[] { "_id", "thread_id", "address",
				"person", "date", "body", "type" }, null, null, null);
		// Project the required columns
		String[] columns = new String[] { "address", "person", "date", "body", "type" };

		if (cursor.getCount() > 0) {
			MessageVo messageVo = null;
			while (cursor.moveToNext()) {
				messageVo = new MessageVo();
				messageVo.setAddress(cursor.getString(cursor.getColumnIndex(columns[0])));
				messageVo.setName(cursor.getString(cursor.getColumnIndex(columns[1])));
				messageVo.setDate(cursor.getString(cursor.getColumnIndex(columns[2])));
				messageVo.setMessage(cursor.getString(cursor.getColumnIndex(columns[3])));
				messageVo.setType(cursor.getString(cursor.getColumnIndex(columns[4])));
				list.add(messageVo);
			}
		}
		cursor.close();
		Logger.d(TAG, "Total SMS  : " + list.size());
		return list;
	}

	/**
	 * Fetch messages which match the address
	 * 
	 * @param address
	 * @return
	 */
	public List<MessageVo> fetchMessagesByAddress(String address) {
		List<MessageVo> list = new ArrayList<MessageVo>();
		if (messagesList != null && messagesList.size() > 0) {
			MessageVo messageVo;
			Iterator<MessageVo> iterator = messagesList.iterator();
			while (iterator.hasNext()) {
				messageVo = iterator.next();
				if (messageVo != null && messageVo.getAddress() != null) {
					if (messageVo.getAddress().contains(address)) {
						list.add(messageVo);
					}
				}
			}
		}
		return list;
	}

	/**
	 * Converts the list of MessageVos into a list of String for the value of the date parameter
	 * 
	 * @param collection
	 * @return
	 */
	public List<String> toDateStringList(List<MessageVo> collection) {
		List<String> list = null;
		if (collection != null && collection.size() > 0) {
			MessageVo messageVo;
			list = new ArrayList<String>();
			Iterator<MessageVo> iterator = collection.iterator();
			while (iterator.hasNext()) {
				messageVo = iterator.next();
				list.add(messageVo.getDate());
			}
		}
		return list;
	}

	/**
	 * Converts the list of MessageVos into a list of String for the value of Message Parameter
	 * 
	 * @param collection
	 * @return
	 */
	public List<String> toMessageStringList(List<MessageVo> collection) {
		List<String> list = null;
		if (collection != null && collection.size() > 0) {
			MessageVo messageVo;
			list = new ArrayList<String>();
			Iterator<MessageVo> iterator = collection.iterator();
			while (iterator.hasNext()) {
				messageVo = iterator.next();
				list.add(messageVo.getMessage());
			}
		}
		return list;
	}
}