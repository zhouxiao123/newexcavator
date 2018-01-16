/**
 * 
 */
package com.newexcavator.wechat.model.menu;


/**
 * @author Administrator
 * 
 */
public class ComplexButton extends Button {

	private Button[] sub_button;

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) throws MenuException {
		if (sub_button != null && sub_button.length > 5) {
			throw new MenuException("二级菜单超过5个。。。");
		} 
		this.sub_button = sub_button;
	}
	
}
