package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IIPUserDao;
import th.co.ais.ipfm.dao.IPTeamDao;
import th.co.ais.ipfm.dao.IPUserDao;
import th.co.ais.ipfm.domain1.IpTeam;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.TeamManageService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.vo.SearchResult;


public class TeamManageServiceImp implements TeamManageService {
	
	private IIPUserDao ipUserDao;
	private IPTeamDao ipTeamDao;
	private IPUserDao iipUserDao;
	

	public IIPUserDao getIpUserDao() {
		return ipUserDao;
	}

	public void setIpUserDao(IIPUserDao ipUserDao) {
		this.ipUserDao = ipUserDao;
	}

	public IPTeamDao getIpTeamDao() {
		return ipTeamDao;
	}

	public void setIpTeamDao(IPTeamDao ipTeamDao) {
		this.ipTeamDao = ipTeamDao;
	}

	public IPUserDao getIipUserDao() {
		return iipUserDao;
	}

	public void setIipUserDao(IPUserDao iipUserDao) {
		this.iipUserDao = iipUserDao;
	}

	@Override
	public SearchResult searchTeam(IpTeam ipTeam, int maxResult)throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
		try {
			searchResult.setTotalResult(ipTeamDao.countSearchTeam(ipTeam));
			searchResult.setResultList(ipTeamDao.searchTeam(ipTeam, maxResult));
			if(searchResult.getTotalResult()>maxResult){
				searchResult.setOverMaxResultLimit(true);
			}else{
				searchResult.setOverMaxResultLimit(false);
			}
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return searchResult;
	}

	@Override
	public String  getTeamId() throws IPFMBusinessException {
		String teamId = null;
		try {
			teamId = ipTeamDao.getTeamId();
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return teamId;
	}

	@Override
	public void deleteTeam(IpTeam ipTeam)throws IPFMBusinessException {
		try {
			if(ipTeam!=null){
				ipTeamDao.delete(ipTeam);
			}
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
	}
	@Override
	public List<IpTeam> searchTeam(IpTeam ipTeam)throws IPFMBusinessException {
		List<IpTeam> ipTeamList = null;
		try {	
			ipTeamList = ipTeamDao.searchTeam(ipTeam);
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipTeamList;
	}
	@Override
	public List<IpTeam> deleteTeamId(String teamId)throws IPFMBusinessException {
		List<IpTeam> ipTeamList = null;
		try {
			IpTeam ipTeam = ipTeamDao.getIpTeam(teamId);
			if(ipTeam!=null){
				deleteTeam(ipTeam);
				ipTeamList = searchTeam(ipTeam);
			}		
		} catch (IPFMBusinessException ex) {	  
			throw ex;
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipTeamList;
	}

	@Override
	public List<IpUser> getTeamMemberList(String teamId) throws IPFMBusinessException {
		List<IpUser> ipUserList = null;
		try {	
			ipUserList = ipUserDao.getTeamMemberList(teamId);
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipUserList;
	}

	@Override
	public List<IpUser> getSystemOwnerList() throws IPFMBusinessException {
		List<IpUser> systemOwnerList = null;
		try {
			  systemOwnerList = iipUserDao.getSystemOwnerList();
			  
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return systemOwnerList;
	}

	@Override
	public List<IpUser> addTeam(IpUser ipUser) throws IPFMBusinessException {
		 List<IpUser> ipUserList =null;
		try {
			 int counts = ipUserDao.getCheckDuplicate(ipUser.getUserId(), ipUser.getTeamId());
			 if(counts!=0){
				  throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0036")));
			 }else{
				  if(ipUser.getUserId()==null){
					   throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0008")));
				  }else{
					   ipUserDao.updateTeam(ipUser);
					   ipUserList  = ipUserDao.getTeamMemberList(ipUser.getTeamId());
				  }
				  
			 }
		} catch (IPFMBusinessException ex) {	  
			throw ex;
					
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipUserList;
	}
	
	@Override
	public List<IpUser>  getTeamMember(IpUser ipUser,String teamId) throws IPFMBusinessException {	
		List<IpUser> userList = null;
		try {
			userList = ipUserDao.getTeamMember(ipUser,teamId);
			  
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return userList;
	}
	
	@Override
	public void saveMember(IpTeam ipTeam) throws IPFMBusinessException {	
		try {
			IpTeam  obj = ipTeamDao.getIpTeam(ipTeam.getTeamId());
			obj.setTeamName(ipTeam.getTeamName());
			obj.setTeamDesc(ipTeam.getTeamDesc());
			obj.setLastUpd(ipTeam.getLastUpd());
			obj.setLastUpdBy(ipTeam.getLastUpdBy());
			ipTeamDao.update(obj);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
	}

	@Override
	public List<IpTeam> addTeamManage(IpTeam ipTeam) throws IPFMBusinessException {
		 List<IpTeam> ipTeamList =null;
			try {
				  String v_status = ipTeamDao.ipTeamByTeamName(ipTeam.getTeamName());
				  if("Found".equals(v_status)){
					  throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0036"),"Team Name"));
				  }else{
					  ipTeamDao.insert(ipTeam);
					  ipTeamList = ipTeamDao.searchTeam(ipTeam);
				  }

			} catch (IPFMBusinessException ex) {	  
				throw ex;
				  	
			} catch (DataAccessException e) {
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
			} 
	 return ipTeamList;
	}
	

}
