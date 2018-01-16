/**
 * 
 */
package com.newexcavator.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.newexcavator.dao.SysUsersDao;
import com.newexcavator.domain.SysUsers;

/**
 * @author Administrator
 *
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler,
		InitializingBean {
	@Autowired
	private SysUsersDao sysUsersDao;
	
	protected Log logger = LogFactory.getLog(getClass());
	
    private String defaultTargetUrl;  
    private boolean forwardToDestination = false;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy(); 
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		if(StringUtils.isEmpty(defaultTargetUrl))  
            throw new BeanInitializationException("You must configure defaultTargetUrl"); 
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	@Transactional(readOnly=false,propagation= Propagation.REQUIRED,rollbackFor={Exception.class})  
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		SysUsers ud = (SysUsers)authentication.getPrincipal();
		String ip = this.getIpAddress(request);
		sysUsersDao.updateLoginInfor(ud.getId(), ip);
		
		if(this.forwardToDestination){  
            logger.info("Login success,Forwarding to " + this.defaultTargetUrl);  
            request.getRequestDispatcher(this.defaultTargetUrl).forward(request, response);  
        }else{  
            logger.info("Login success,Redirecting to " + this.defaultTargetUrl);
            
            this.redirectStrategy.sendRedirect(request, response, this.defaultTargetUrl);  
        }  
	}

	public String getIpAddress(HttpServletRequest request){    
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("Proxy-Client-IP");    
        }    
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("WL-Proxy-Client-IP");    
        }    
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_CLIENT_IP");    
        }    
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");    
        }    
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getRemoteAddr();    
        }    
        return ip;    
    }  
	
	public void setDefaultTargetUrl(String defaultTargetUrl) {  
        this.defaultTargetUrl = defaultTargetUrl;  
    }  
  
    public void setForwardToDestination(boolean forwardToDestination) {  
        this.forwardToDestination = forwardToDestination;  
    }  
	
}
