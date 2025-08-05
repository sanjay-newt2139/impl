package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IIPUserGroupDao;
import th.co.ais.ipfm.domain.IPUserGroup;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IIPUserGroupService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;




public  class IPUserGroupServiceImpl implements IIPUserGroupService{

	private IIPUserGroupDao ipUserGroupDao;

	

	public void setIpUserGroupDao(IIPUserGroupDao ipUserGroupDao) {
		this.ipUserGroupDao = ipUserGroupDao;
	}


	@Override
	public List<IPUserGroup> getUserGroupList()throws IPFMBusinessException {

		List<IPUserGroup> userGroupList = null;
		try {
			userGroupList = ipUserGroupDao.getUserGroupList();
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return userGroupList;
	}

	
}
