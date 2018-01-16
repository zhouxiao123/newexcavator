/**
 * 
 */
package com.newexcavator.service;

import java.util.List;

import com.newexcavator.domain.Advertisement;



/**
 * @author Administrator
 *
 */
public interface AdvertisementService {
	public void delAdvertisementByid(Integer id);
	public void saveAdvertisement(Advertisement advertisement);
	public List<Advertisement> queryAdvertisementList();
	public Advertisement queryBottomAdvertisement(Integer flag);
	public void saveBottomAdvertisement(Advertisement advertisement);
	public void updateBottomAdvertisement(Advertisement advertisement);
}
