package com.newexcavator.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Topic;
import com.newexcavator.domain.TopicAns;
import com.newexcavator.domain.TopicImg;
import com.newexcavator.domain.TopicName;

public interface TopicDao {
	public List<TopicName> queryAllTopicName();
	
	public void deleteTopicName(Integer id);
	
	public void saveTopicName(String topicname);
	
	
	public Topic queryTopicById(Integer id);
	
	
	public void saveTopicAns(TopicAns topicans);
	
	public void updateTopicTime(Integer id);
	
	public void saveTopic(Topic topic);
	
	public void saveTopicImg(TopicImg topicImg);
	
	public void updateTopicFavour(Map<String, Object> param);
	
	public TopicAns queryTopicAnsByTopicAnsId(Integer id);
	
	public void updateTopicAnsFavour(Map<String, Object> param);
	
	public List<TopicImg> queryTopicImgByTopicId(@Param(value="id") String topic_id);
	
	public void saveFeedBack(Map<String, Object> param);
}
