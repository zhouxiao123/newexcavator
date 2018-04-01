/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Advertisement;
import com.newexcavator.domain.Answer;
import com.newexcavator.domain.Ask;



/**
 * @author randy
 * 
 */
public interface AnswerDao {
	public void delAnswerByid(@Param(value = "id") Integer id);
	public void saveAnswer(Answer a);
	public Answer queryAnswerById(@Param(value = "id")Integer id);
	public List<Answer> queryAnswerListByAskid(@Param(value = "ask_id")Integer askid);

	
}
