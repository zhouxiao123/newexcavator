/**
 * 
 */
package com.newexcavator.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Ask;
import com.newexcavator.util.PageSupport;



/**
 * @author Administrator
 *
 */
public interface AskService {
	public void delAskByid(Integer id);
	public void saveAsk(Ask a);
	public void updateAsk(Ask a);
	public List<Ask> queryAskListByMid(Integer mid,PageSupport p);
	public Ask queryAskById(Integer id);
}
