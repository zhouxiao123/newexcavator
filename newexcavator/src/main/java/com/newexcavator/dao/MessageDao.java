/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Message;
import com.newexcavator.domain.Reply;

/**
 * @author randy
 * 
 */
public interface MessageDao {
	public void saveMessage(Message message);

	public void updateMessage(@Param(value = "id") Integer id);
	
	public void saveReply(Reply reply);
	
	public List<Reply> queryReplyByMessageId(@Param(value = "message_id") Integer message_id);
}
