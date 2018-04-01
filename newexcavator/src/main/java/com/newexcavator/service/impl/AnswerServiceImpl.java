/**
 * 
 */
package com.newexcavator.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.newexcavator.dao.AdvertisementDao;
import com.newexcavator.dao.AnswerDao;
import com.newexcavator.dao.AskDao;
import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Answer;
import com.newexcavator.domain.Ask;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.service.AnswerService;
import com.newexcavator.service.AskService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.PageSupport;



/**
 * @author Administrator
 *
 */
@Service
public class AnswerServiceImpl extends AbstractModuleSuport implements AnswerService {

	@Autowired
	private AnswerDao answerDao;

	@Override
	public void delAnswerByid(Integer id) {
		// TODO Auto-generated method stub
		answerDao.delAnswerByid(id);
	}

	@Override
	public void saveAnswer(Answer a) {
		// TODO Auto-generated method stub
		answerDao.saveAnswer(a);
	}

	@Override
	public List<Answer> queryAnswerListByAskid(Integer askid, PageSupport p) {
		// TODO Auto-generated method stub
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("ask_id", askid);
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.AnswerDao.queryAnswerListByAskid", "com.newexcavator.dao.AnswerDao.queryAnswerListByAskid_count", param, p);
	}

	@Override
	public Answer queryAnswerById(Integer id) {
		// TODO Auto-generated method stub
		return answerDao.queryAnswerById(id);
	}

	


	
	
	

}
