/**
 * 
 */
package com.newexcavator.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.newexcavator.dao.SysUsersDao;
import com.newexcavator.domain.SysMenus;
import com.newexcavator.domain.SysUsers;

/**
 * @author randy
 *
 */
public class DefaultUserDetailsService implements UserDetailsService {
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private SysUsersDao sysUsersDao;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUsers su = null;
		try {
			su = sysUsersDao.querySysUserByUsername(username, null);
			if (su != null) {
				List<SysMenus> menus = sysUsersDao.queryUserMenusByUsrId(su.getId());
				Map<Integer, SysMenus> map = new LinkedHashMap<Integer, SysMenus>();
				for (SysMenus sm : menus) {
					if (sm.getParent_id().intValue() == 0) {//一级菜单
						map.put(sm.getId(), sm);
						continue;
					}
					SysMenus psm = map.get(sm.getParent_id());
					psm.addSubMenus(sm);//设置二级菜单
				}
				
				List<SysMenus> newMenus = new ArrayList<SysMenus>();
				Iterator<SysMenus> is = map.values().iterator();
				while(is.hasNext()) {
					newMenus.add(is.next());
				}
				su.setMenus(newMenus);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return su;
	}
}
