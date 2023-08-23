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
@Deprecated
public class PNRMessagesManager {

	private final List<PNRStatusVo>	list;

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
			// TODO : Since the list is already sorted, find the position of difference and return the sublist, which
			// should be faster
			List<PNRStatusVo> pnrNumbers = new ArrayList<PNRStatusVo>();
			if (list != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
						calendar.get(Calendar.DATE) - 1);
				long now = calendar.getTimeInMillis();
				for (PNRStatusVo statusVo : list) {
					if (statusVo.journeyDateTimeStamp > now) {
						pnrNumbers.add(statusVo);
					}
				}
			}
			return pnrNumbers;
		}
	}
}
