package com.ae.apps.pnrstatus.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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

			MessageVo messageVo = null;
			PNRStatusVo pnrStatusVo = null;
			Iterator<MessageVo> iterator = messagesList.iterator();
			while (iterator.hasNext()) {
				messageVo = iterator.next();
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
	public List<PNRStatusVo> getMessagesList() {
		return list;
	}

}
