package com.newexcavator.dao;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Wechat_tb;

public interface Wechat_tbDao {
  public Wechat_tb queryWechatNo();
  public void saveWechatNo(Wechat_tb wc);
  public  void updateWechatNo(@Param(value = "wechatNo") String wechatNo, @Param(value = "id") Integer id);
}
