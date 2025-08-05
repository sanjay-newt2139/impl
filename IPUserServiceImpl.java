package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IIPUserDao;
import th.co.ais.ipfm.domain.TreeMenu;
import th.co.ais.ipfm.domain1.IpRoleMember;
import th.co.ais.ipfm.domain1.IpTeam;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IPUserService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;




public  class IPUserServiceImpl implements IPUserService{

	private IIPUserDao ipUserDao;

	public IIPUserDao getIpUserDao() {
		return ipUserDao;
	}

	public void setIpUserDao(IIPUserDao ipUserDao) {
		this.ipUserDao = ipUserDao;
	}
	
	public ArrayList<TreeMenu> getToDoListByUser(String userName) {
		// TODO Auto-generated method stub
		ArrayList<TreeMenu> returnResult = new ArrayList<TreeMenu>();
		ArrayList params = new ArrayList();
		params.add(userName); 
		List<Map> result = ipUserDao.callStore("{call LIST_TODO_LIST (?,?)}", params);
		//List<Map> result = ipfmCommondDao.callStore("{call LIST_TODO_LIST (?,?)}", params);
		
		for(Map map : result){ 
			TreeMenu menu = new TreeMenu();
			menu.setLevel1((String)map.get("LV1"));
			menu.setLevel2((String)map.get("LV2"));
			menu.setLevel3((String)map.get("LV3"));
			menu.setLevel4((String)map.get("LV4"));
			menu.setActionName((String)map.get("ACTION_NAME"));
			returnResult.add(menu);
		}
		return returnResult;
	}
	
	@Override
	public Integer getOnlineUser() throws Exception {
		return ipUserDao.getOnlineUser();	
	}
 
	@Override
	public List<IpUser> getManagerApproveList(String userId) throws Exception {
		List<IpUser> result = new ArrayList<IpUser>();
		try {
			result = ipUserDao.getManagerApproveList(userId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public IpUser findIpUserById(String userId) throws Exception {
		IpUser result = null;
		try {
			System.out.println("in Service Method .........");
			result = ipUserDao.getIPUser(userId);
			System.out.println("return result in service ===== " + result);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public List<IpUser> getManagerApproveList(List<String> userId)
			throws Exception {
		return ipUserDao.getManagerApproveList(userId);
	}
	
	@Override
	public List<IpUser> getIpUserListByTeam(String teamId) throws Exception {
		List<IpUser> result = null;
		try {
			result = ipUserDao.getTeamMemberList(teamId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	@Override
	public List<IpUser> getTeamAssignURList(String teamId,String roleId) throws IPFMBusinessException {
		List<IpUser> result = null;
		try {
			result = ipUserDao.getTeamAssignURList(teamId,roleId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	@Override
	public List<IpUser> getSystemOwnerList() throws IPFMBusinessException {
		List<IpUser> systemOwnerList = null;
		try {
			  systemOwnerList = ipUserDao.getSystemOwnerList();
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return systemOwnerList;
	}
	
	@Override
	public List<IpTeam> getSystemOwnerTeamList() throws IPFMBusinessException {
		List<IpTeam> systemOwnerList = null;
		try {
			  systemOwnerList = ipUserDao.getSystemOwnerTeamList();
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return systemOwnerList;
	}

	@Override
	public IpUser saveIpUser(IpUser ipUser) throws IPFMBusinessException {
		try {
			  ipUserDao.insert(ipUser);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUser;
	}

	@Override
	public List<IpUser> findUserByRoleId(String roleId) throws IPFMBusinessException {
		// TODO Auto-generated method stub
		try {
			return ipUserDao.getRoleIdList(roleId);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException(ex);
		}

	}
	
	@Override
	public List<IpRoleMember> findUserByIpRoleMember(String roleId) throws IPFMBusinessException {
		// TODO Auto-generated method stub
		try {
			return ipUserDao.getIpRoleMember(roleId);
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException(ex);
		}

	}
	@Override
	public List<IpRoleMember> getUserForOwnerApprove(String pmRoleId) throws IPFMBusinessException {
		List<IpRoleMember> result = null;
		try {
			result = ipUserDao.getUserForOwnerApprove(pmRoleId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
}
