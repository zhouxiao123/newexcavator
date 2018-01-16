package com.newexcavator.timer;


import org.apache.log4j.Logger;

import com.newexcavator.wechat.AccessTokenTool;




public class AutoTimer {

	protected Logger log = Logger.getLogger(AutoTimer.class);
	public static int i = 0;


	
	public AutoTimer() {
	}

	public void run() {
		AccessTokenTool.getAccessToken(true);
	}
	
	public void check(){
		
	}
}
