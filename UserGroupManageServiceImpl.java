package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IIPGroupMemberDao;
import th.co.ais.ipfm.domain.IPGroupMemberUser;
import th.co.ais.ipfm.domain.IPGroupMembers;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.UserGroupManageService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.vo.SearchResult;

public class UserGroupManageServiceImpl implements UserGroupManageService{

	private IIPGroupMemberDao ipGroupMemberDao;

	

	public void setIpGroupMemberDao(IIPGroupMemberDao ipGroupMemberDao) {
		this.ipGroupMemberDao = ipGroupMemberDao;
	}


	@Override
	public List<IPGroupMemberUser> searchUserGroupManage(String groupId) throws IPFMBusinessException {
		List<IPGroupMemberUser> ipGroupMemberList = null;
		try {
			ipGroupMemberList = ipGroupMemberDao.searchUserGroupManage(groupId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipGroupMemberList;
	}


	@Override
	public SearchResult searchUserGroupManage(String groupId, int maxResult)
			throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
		try {
			searchResult.setTotalResult(ipGroupMemberDao.countSearchUserGroupManage(groupId));
			searchResult.setResultList(ipGroupMemberDao.searchUserGroupManage(groupId));
			if(searchResult.getTotalResult()>maxResult){
				searchResult.setOverMaxResultLimit(true);
			}else{
				searchResult.setOverMaxResultLimit(false);
			}
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return searchResult;
	}


	@Override
	public List<IPGroupMemberUser> deleteAndReSearchUserGroupManage(String rowId,String grpId) throws IPFMBusinessException {
		List<IPGroupMemberUser> ipGroupMemberList = null;
		IPGroupMemberUser groupMember = ipGroupMemberDao.getByPrimaryKey(rowId);
		if(groupMember!=null){
			deleteUserGroupManage(groupMember);
			ipGroupMemberList = searchUserGroupManage(grpId);
		}		
		return ipGroupMemberList;
	}

	@Override
	public void deleteUserGroupManage(IPGroupMemberUser ipGroupMember)
			throws IPFMBusinessException {
		try {
			if(ipGroupMember!=null){
				ipGroupMemberDao.delete(ipGroupMember);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
		
	}

	@Override
	public IPGroupMembers createUserGroupManage(IPGroupMembers ipGroupMember)throws IPFMBusinessException {
		try {
			if(ipGroupMember!=null){

				//ipUserGroupInsertDao.insert(ipGroupMember);				
			}
		} catch (DataAccessException e) {
			System.out.println(e);
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0008")));
		}
		return ipGroupMember;
	}


}
