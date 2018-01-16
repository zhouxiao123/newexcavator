package com.newexcavator.service;

import java.util.List;
import java.util.Map;

import com.newexcavator.domain.Feedback;
import com.newexcavator.domain.Topic;
import com.newexcavator.domain.TopicAns;
import com.newexcavator.domain.TopicImg;
import com.newexcavator.domain.TopicName;
import com.newexcavator.util.PageSupport;

public interface TopicService {
	
	public List<TopicName> queryAllTopicName();
	
	public void deleteTopicName(Integer id);
	
	public void saveTopicName(String topicname);
	
	public List<Topic> queryTopic(Map<String, Object> param,PageSupport ps);
	
	public Topic queryTopicById(Integer id);
	
	public List<TopicAns> queryTopicAnsByTopicId(Integer id ,PageSupport ps);
	
	public void saveTopicAns(TopicAns topicans);
	
	public void updateTopicTime(Integer id);
	
	public void saveTopic(Topic topic, List<String> filePath);
	
	
	public void updateTopicFavour(Map<String, Object> param);
	
	public TopicAns queryTopicAnsByTopicAnsId(Integer id);
	
	public void updateTopicAnsFavour(Map<String, Object> param);
	
	public List<TopicImg> queryTopicImgByTopicId(String topic_id);
	
	public void saveFeedBack(Map<String, Object> param);
	
	public List<Feedback> selectFeedback(PageSupport ps);
}
