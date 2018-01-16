package com.newexcavator.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newexcavator.dao.LotteryDao;
import com.newexcavator.domain.Lottery;
import com.newexcavator.service.LotteryService;

@Service
public class LotteryServicImpl implements LotteryService {
	@Autowired
	private LotteryDao lotteryDao;
	
	@Override
	public void saveLottery(Lottery lottery) {
		// TODO Auto-generated method stub
		lotteryDao.saveLottery(lottery);
	}

	@Override
	public Lottery queryLottery(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return lotteryDao.queryLottery(param);
	}

	@Override
	public List<Lottery> queryLotteryList(Integer status) {
		// TODO Auto-generated method stub
		return lotteryDao.queryLotteryList(status);
	}

	@Override
	public void updateLottery(String phone) {
		// TODO Auto-generated method stub
		lotteryDao.updateLottery(phone);
	}

	@Override
	public void clearLotteryList() {
		lotteryDao.clearLotteryList();
	}

}
