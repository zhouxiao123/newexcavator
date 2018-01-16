/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Customer;
import com.newexcavator.domain.Point;
import com.newexcavator.domain.SysMenus;
import com.newexcavator.domain.SysUsers;

/**
 * @author randy
 * 
 */
public interface SysUsersDao {
	public SysUsers querySysUserByUsername(
			@Param(value = "username") String username,
			@Param(value = "password") String password);
	
	public Integer querySysUserByName(@Param(value = "name") String name, @Param(value = "id") Integer id);

	public Customer queryCustomerById(@Param(value = "id") Integer id);
	
	public Point queryPoint();
	
	public void updatePoint(Integer point);

	public List<SysMenus> queryUserMenusByUsrId(
			@Param(value = "user_id") Integer user_id);

	public void updatePassword(@Param(value = "password") String password,
			@Param(value = "id") Integer id);

	public void saveSysUser(SysUsers sysUsers);
	
	public void updateSysUsers(SysUsers sysUsers);
	
	public void updateLoginInfor(@Param(value = "id") Integer id, @Param(value = "last_login_ip") String last_login_ip);
	
	public void saveCustomer(Customer customer);
	
	public void updateCustomer(Customer customer);
	
	public SysUsers querySysUserByOpenid(@Param(value = "openid") String openid);
	
	public Integer queryCustomerByName(@Param(value = "name") String name, @Param(value = "id") Integer id);
	
	public Integer queryOne();
	
	public void removeUserPermission(Integer id);
	
	public SysUsers querySysUserByid(Integer id);
	
	public void updatePermission(Map<String, Object> param);
	
	public SysUsers queryTopicUserByOpenid(String openid);
	
	public List<String> queryTopicUserOpenid();
	
	public void updateSysUsersById(SysUsers sysUsers);
	
	public SysUsers querySysUserByPhone(@Param(value="cell_phone") String cell_phone);
	
	public Integer queryCountSysUserByUsername(@Param(value="username") String username);
	
	public void addPoint(@Param(value="id")Integer id,@Param(value="point")Integer point);
	
	public void subPoint(@Param(value="id")Integer id,@Param(value="point")Integer point);
	
	public void updateShareTime(@Param(value="id")Integer id);
	
}
