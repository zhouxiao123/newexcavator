/**
 * 
 */
package com.newexcavator.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Answer;
import com.newexcavator.domain.Ask;
import com.newexcavator.util.PageSupport;



/**
 * @author Administrator
 *
 */
public interface AnswerService {
	public void delAnswerByid(Integer id);
	public void saveAnswer(Answer a);
	public Answer queryAnswerById(Integer id);
	public List<Answer> queryAnswerListByAskid(Integer askid, PageSupport p);
}
