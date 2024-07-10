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

package com.ae.apps.pnrstatus.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
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
@Deprecated
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

	@SuppressLint("Range")
    public List<MessageVo> fetchAllMessages() {
		List<MessageVo> list = new ArrayList<MessageVo>();
		// get the uri and cursor
		Uri mSmsinboxQueryUri = Uri.parse(SMS_INBOX_URI);
		Cursor cursor = contentResolver.query(mSmsinboxQueryUri, new String[] { "_id", "thread_id", "address",
				"person", "date", "body", "type" }, null, null, null);
		// Project the required columns
		String[] columns = new String[] { "address", "person", "date", "body", "type" };

        assert cursor != null;
        if (cursor.getCount() > 0) {
			MessageVo messageVo = null;
			while (cursor.moveToNext()) {
				messageVo = new MessageVo();
				messageVo.address = cursor.getString(cursor.getColumnIndex(columns[0]));
				messageVo.name = cursor.getString(cursor.getColumnIndex(columns[1]));
				messageVo.date = cursor.getString(cursor.getColumnIndex(columns[2]));
				messageVo.message = cursor.getString(cursor.getColumnIndex(columns[3]));
				messageVo.type = cursor.getString(cursor.getColumnIndex(columns[4]));
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
		if (messagesList != null && !messagesList.isEmpty()) {
			MessageVo messageVo;
            for (MessageVo vo : messagesList) {
                messageVo = vo;
                if (messageVo != null && messageVo.address != null) {
                    if (messageVo.address.contains(address)) {
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
		if (collection != null && !collection.isEmpty()) {
			MessageVo messageVo;
			list = new ArrayList<String>();
            for (MessageVo vo : collection) {
                messageVo = vo;
                list.add(messageVo.date);
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
		if (collection != null && !collection.isEmpty()) {
			MessageVo messageVo;
			list = new ArrayList<String>();
            for (MessageVo vo : collection) {
                messageVo = vo;
                list.add(messageVo.message);
            }
		}
		return list;
	}
}