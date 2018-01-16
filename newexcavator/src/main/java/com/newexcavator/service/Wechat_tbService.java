package com.newexcavator.service;

import com.newexcavator.domain.Wechat_tb;

public interface Wechat_tbService {
	
	
  public Wechat_tb queryWechatNo();
  
  
  public void saveWechatNo(Wechat_tb wc);
  
  
  public  void updateWechatNo(String wechatNo, Integer id);
  
  
}
