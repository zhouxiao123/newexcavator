/**
 * 
 */
package com.newexcavator.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.newexcavator.dao.AdvertisementDao;
import com.newexcavator.domain.Advertisement;
import com.newexcavator.service.AdvertisementService;
import com.newexcavator.util.DataUtil;



/**
 * @author Administrator
 *
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {

	@Autowired
	private AdvertisementDao advertisementDao;
	
	/* (non-Javadoc)
	 * @see com.video.service.AdvertisementService#delAdvertisementByid(java.lang.Integer)
	 */
	@Override
	public void delAdvertisementByid(Integer id) {
		Advertisement ad = advertisementDao.queryAdvertisementById(id);
		DataUtil.deleteByUpload2Img(ad.getImg_path());
		advertisementDao.delAdvertisementByid(id);
	}

	/* (non-Javadoc)
	 * @see com.video.service.AdvertisementService#saveAdvertisement(com.video.model.Advertisement)
	 */
	@Override
	public void saveAdvertisement(Advertisement advertisement) {
		
		advertisementDao.saveAdvertisement(advertisement);
	}

	/* (non-Javadoc)
	 * @see com.video.service.AdvertisementService#queryAdvertisementList(java.lang.Integer)
	 */
	@Override
	public List<Advertisement> queryAdvertisementList() {
		return advertisementDao.queryAdvertisementList();
	}

	@Override
	public Advertisement queryBottomAdvertisement(Integer flag) {
		return advertisementDao.queryBottomAdvertisement(flag);
	}

	@Override
	public void saveBottomAdvertisement(Advertisement advertisement) {
		if(advertisement.getId()!=null && advertisement.getId().intValue() > 0){
			if(!StringUtils.isBlank(advertisement.getImg_path())){
				Advertisement a = advertisementDao.queryBottomAdvertisement(advertisement.getFlag());
				DataUtil.deleteByUpload2Img(a.getImg_path());
			}
			advertisementDao.updateBottomAdvertisement(advertisement);
		} else{
			advertisementDao.saveBottomAdvertisement(advertisement);
		}
	}

	@Override
	public void updateBottomAdvertisement(Advertisement advertisement) {
		advertisementDao.updateBottomAdvertisement(advertisement);
	}
	

}
