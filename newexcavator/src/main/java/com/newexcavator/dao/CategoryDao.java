/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Category;

/**
 * @author Administrator
 *
 */
public interface CategoryDao {
	public List<Category> queryCategoryByModuleId(@Param(value = "moduleId") Integer moduleId);
}
