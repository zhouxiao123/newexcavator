package com.newexcavator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newexcavator.dao.Wechat_tbDao;
import com.newexcavator.domain.Wechat_tb;
import com.newexcavator.service.Wechat_tbService;
import com.newexcavator.util.AbstractModuleSuport;

@Service(value="wechat_tbService")
public class Wechat_tbServiceImpl  extends AbstractModuleSuport implements Wechat_tbService {

	@Autowired
	private Wechat_tbDao wechat_tbDao;
	@Override
	public Wechat_tb queryWechatNo() {
		return wechat_tbDao.queryWechatNo();
	}

	@Override
	public void updateWechatNo(String wechatNo, Integer id) {
	   wechat_tbDao.updateWechatNo(wechatNo, id);		
	}
	@Override
	public void saveWechatNo(Wechat_tb wc) {
		wechat_tbDao.saveWechatNo(wc);
	}

}
