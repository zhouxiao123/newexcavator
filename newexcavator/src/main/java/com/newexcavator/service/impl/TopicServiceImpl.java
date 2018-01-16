package com.newexcavator.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.newexcavator.dao.TopicDao;
import com.newexcavator.domain.Feedback;
import com.newexcavator.domain.Topic;
import com.newexcavator.domain.TopicAns;
import com.newexcavator.domain.TopicImg;
import com.newexcavator.domain.TopicName;
import com.newexcavator.service.TopicService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.PageSupport;


@Service
public class TopicServiceImpl extends AbstractModuleSuport implements TopicService{

	@Autowired
	private TopicDao topicDao;
	
	@Override
	public List<TopicName> queryAllTopicName() {
		// TODO Auto-generated method stub
		return topicDao.queryAllTopicName();
	}

	@Override
	public void deleteTopicName(Integer id) {
		// TODO Auto-generated method stub
		topicDao.deleteTopicName(id);
	}

	@Override
	public void saveTopicName(String topicname) {
		// TODO Auto-generated method stub
		topicDao.saveTopicName(topicname);
	}

	@Override
	public List<Topic> queryTopic(Map<String, Object> param,PageSupport ps) {
		// TODO Auto-generated method stub
		if(param!=null){
			return this.getListPageSupportByManualOperation("com.excavator.dao.TopicDao.queryTopic", "com.excavator.dao.TopicDao.queryTopic_count", param, ps);
		}else{
			return this.getListPageSupportByManualOperation("com.excavator.dao.TopicDao.queryTopic", "com.excavator.dao.TopicDao.queryTopic_count", null, ps);

		}
		//return topicDao.queryTopicByTopicId(id);
	}


	@Override
	public Topic queryTopicById(Integer id) {
		// TODO Auto-generated method stub
		return topicDao.queryTopicById(id);
	}

	@Override
	public List<TopicAns> queryTopicAnsByTopicId(Integer id,PageSupport ps) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		return this.getListPageSupportByManualOperation("com.excavator.dao.TopicDao.queryTopicAnsByTopicId", "com.excavator.dao.TopicDao.queryTopicAnsByTopicId_count", param, ps);
		//return topicDao.queryTopicAnsByTopicId(id);
	}

	@Override
	public void saveTopicAns(TopicAns topicans) {
		// TODO Auto-generated method stub
		topicDao.saveTopicAns(topicans);
	}

	@Override
	public void updateTopicTime(Integer id ) {
		// TODO Auto-generated method stub
		topicDao.updateTopicTime(id);
	}

	@Override
	public void saveTopic(Topic topic, List<String> filePath) {
		topicDao.saveTopic(topic);
		if (!CollectionUtils.isEmpty(filePath)) {
			for (String fp : filePath) {
				TopicImg ti = new TopicImg();
				ti.setImg_path(fp);
				ti.setTopic_id(topic.getId());
				
				topicDao.saveTopicImg(ti);
			}
		}
	}

	@Override
	public void updateTopicFavour(Map<String, Object> param) {
		// TODO Auto-generated method stub
		topicDao.updateTopicFavour(param);
	}

	@Override
	public TopicAns queryTopicAnsByTopicAnsId(Integer id) {
		// TODO Auto-generated method stub
		return topicDao.queryTopicAnsByTopicAnsId(id);
	}

	@Override
	public void updateTopicAnsFavour(Map<String, Object> param) {
		// TODO Auto-generated method stub
		topicDao.updateTopicAnsFavour(param);
	}

	@Override
	public List<TopicImg> queryTopicImgByTopicId(String topic_id) {
		return topicDao.queryTopicImgByTopicId(topic_id);
	}

	@Override
	public void saveFeedBack(Map<String, Object> param) {
		// TODO Auto-generated method stub
		topicDao.saveFeedBack(param);
	}

	@Override
	public List<Feedback> selectFeedback(PageSupport ps) {
		// TODO Auto-generated method stub
		return this.getListPageSupportByManualOperation("com.excavator.dao.TopicDao.selectFeedback", "com.excavator.dao.TopicDao.selectFeedback_count", null, ps);
	}

}
