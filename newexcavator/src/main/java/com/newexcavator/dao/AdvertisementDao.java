/**
 * 
 */
package com.newexcavator.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newexcavator.domain.Advertisement;



/**
 * @author randy
 * 
 */
public interface AdvertisementDao {
	public void delAdvertisementByid(@Param(value = "id") Integer id);
	public void saveAdvertisement(Advertisement advertisement);
	public List<Advertisement> queryAdvertisementList();
	public Advertisement queryAdvertisementById(Integer id);
	public Advertisement queryBottomAdvertisement(Integer flag);
	public void saveBottomAdvertisement(Advertisement advertisement);
	public void updateBottomAdvertisement(Advertisement advertisement);
}
