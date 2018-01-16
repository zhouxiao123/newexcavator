/**
 * 
 */
package com.newexcavator.service;

import java.util.List;

import com.newexcavator.domain.Message;
import com.newexcavator.domain.Reply;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 * 
 */
public interface MessageService {
	public void saveMessage(Message message);

	public void saveReply(Reply reply);

	public List<Reply> queryReplyByMessageId(Integer message_id);
	
	public List<Message> queryMessage(PageSupport ps);
}
