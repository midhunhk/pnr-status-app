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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.MessageVo;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

/**
 * Manages the messages that are valid PNR Messages
 * 
 * @author midhun_harikumar
 * 
 */
public class PNRMessagesManager {

	private List<PNRStatusVo>	list;

	/**
	 * 
	 * @param messagesList
	 */
	public PNRMessagesManager(List<MessageVo> messagesList) {
		list = new ArrayList<PNRStatusVo>();
		initList(messagesList);
	}

	private void initList(List<MessageVo> messagesList) {
		if (messagesList != null) {

			// MessageVo messageVo = null;
			PNRStatusVo pnrStatusVo = null;
			for (MessageVo messageVo : messagesList) {
				pnrStatusVo = PNRUtils.parsePNRStatus(messageVo);
				if (pnrStatusVo != null) {
					list.add(pnrStatusVo);
				}
			}
		}
		Collections.sort(list);
	}

	/**
	 * Returns the list of PNRStatusVos
	 * 
	 * @return
	 */
	public List<PNRStatusVo> getMessagesList(boolean hidePastMessages) {
		if (hidePastMessages == false) {
			return list;
		} else {
			// Filter the list and return only future ticket dates
			// TODO : Since the list is already sorted, find the position of difference and return the sublist
			List<PNRStatusVo> tempList = new ArrayList<PNRStatusVo>();
			if (list != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE) - 1);
				long now = calendar.getTimeInMillis();
				for (PNRStatusVo statusVo : list) {
					if (statusVo.getJourneyDateTimeStamp() > now) {
						tempList.add(statusVo);
					}
				}
			}
			return tempList;
		}
	}
}
