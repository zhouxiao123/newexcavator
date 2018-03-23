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
import com.newexcavator.dao.AskDao;
import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Ask;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.service.AskService;
import com.newexcavator.util.AbstractModuleSuport;
import com.newexcavator.util.DataUtil;
import com.newexcavator.util.PageSupport;



/**
 * @author Administrator
 *
 */
@Service
public class AskServiceImpl extends AbstractModuleSuport implements AskService {

	@Autowired
	private AskDao askDao;

	@Override
	public void delAskByid(Integer id) {
		// TODO Auto-generated method stub
		askDao.delAskByid(id);
	}

	@Override
	public void saveAsk(Ask a) {
		// TODO Auto-generated method stub
		askDao.saveAsk(a);
	}

	@Override
	public List<Ask> queryAskListByMid(Integer mid, PageSupport p) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("mid", mid);
		return this.getListPageSupportByManualOperation("com.newexcavator.dao.AskDao.queryAskListByMid", "com.newexcavator.dao.AskDao.queryAskListByMid_count", param, p);

	}


	
	
	

}
