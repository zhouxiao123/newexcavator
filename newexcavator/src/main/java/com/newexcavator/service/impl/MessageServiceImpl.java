/**
 * 
 */
package com.newexcavator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newexcavator.dao.MessageDao;
import com.newexcavator.domain.Message;
import com.newexcavator.domain.Reply;
import com.newexcavator.service.MessageService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.PageSupport;

/**
 * @author Administrator
 *
 */
@Service(value = "messageService")
public class MessageServiceImpl extends AbstractModuleSuport implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public void saveMessage(Message message) {
		messageDao.saveMessage(message);
	}

	@Override
	public void saveReply(Reply reply) {
		messageDao.saveReply(reply);
		messageDao.updateMessage(reply.getMessage_id());
	}

	@Override
	public List<Reply> queryReplyByMessageId(Integer message_id) {
		return messageDao.queryReplyByMessageId(message_id);
	}

	@Override
	public List<Message> queryMessage(PageSupport ps) {
		return this.getListPageSupportByManualOperation("com.excavator.dao.MessageDao.queryMessage", "com.excavator.dao.MessageDao.queryMessage_count", null, ps);
	}
	
	
	
}
