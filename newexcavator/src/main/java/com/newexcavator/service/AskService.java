/**
 * 
 */
package com.newexcavator.service;

import java.util.List;


import com.newexcavator.domain.Ask;
import com.newexcavator.util.PageSupport;



/**
 * @author Administrator
 *
 */
public interface AskService {
	public void delAskByid(Integer id);
	public void saveAsk(Ask a);
	public List<Ask> queryAskListByMid(Integer mid,PageSupport p);
}
