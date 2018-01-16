package com.newexcavator.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newexcavator.domain.Lottery;


public interface LotteryService {
	public void saveLottery(Lottery lottery);
	
	public Lottery queryLottery(Map<String,Object> param);
	
	public List<Lottery> queryLotteryList(Integer status);
	
	public void updateLottery(String phone);
	
	public void clearLotteryList();
}
