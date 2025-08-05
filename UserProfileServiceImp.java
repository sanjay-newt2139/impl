package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ehr.domain.ApproverOM;
import th.co.ais.ehr.domain.EmployeeProfileOM;
import th.co.ais.ehr.domain.UserAccount;
import th.co.ais.ehr.service.AuthenDBService;
import th.co.ais.ehr.service.EmployeeProfileService;
import th.co.ais.ehr.service.EmployeeProfileService2;
import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPUserDao;
import th.co.ais.ipfm.dao.IPDelegateUserDao;
import th.co.ais.ipfm.dao.IPRoleDao;
import th.co.ais.ipfm.dao.IPRoleMemberDao;
import th.co.ais.ipfm.domain1.IpDelegateUser;
import th.co.ais.ipfm.domain1.IpRole;
import th.co.ais.ipfm.domain1.IpRoleMember;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.UserProfileService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;


public class UserProfileServiceImp implements UserProfileService {
	
	private IIPUserDao ipUserDao;
	private IPRoleDao ipRoleDao;
    private IPRoleMemberDao ipRoleMemberDao;
    private IPDelegateUserDao ipDelegateUserDao;
    private EmployeeProfileService ehrService;
    private CommonDao commonDao;
    private AuthenDBService authService;
    
	public AuthenDBService getAuthService() {
		return authService;
	}

	public EmployeeProfileService2 getEhrService2() {
		return ehrService2;
	}

	private EmployeeProfileService2 ehrService2;
	
	public void setAuthService(AuthenDBService authService) {
		this.authService = authService;
	}
	
	public void setEhrService2(EmployeeProfileService2 ehrService2) {
		this.ehrService2 = ehrService2;
	}
	
	public IIPUserDao getIpUserDao() {
		return ipUserDao;
	}

	public void setIpUserDao(IIPUserDao ipUserDao) {
		this.ipUserDao = ipUserDao;
	}
	
	public IPRoleDao getIpRoleDao() {
		return ipRoleDao;
	}

	public void setIpRoleDao(IPRoleDao ipRoleDao) {
		this.ipRoleDao = ipRoleDao;
	}
	
	public IPRoleMemberDao getIpRoleMemberDao() {
		return ipRoleMemberDao;
	}

	public void setIpRoleMemberDao(IPRoleMemberDao ipRoleMemberDao) {
		this.ipRoleMemberDao = ipRoleMemberDao;
	}

	public IPDelegateUserDao getIpDelegateUserDao() {
		return ipDelegateUserDao;
	}

	public void setIpDelegateUserDao(IPDelegateUserDao ipDelegateUserDao) {
		this.ipDelegateUserDao = ipDelegateUserDao;
	}
	
	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
	public EmployeeProfileService getEhrService() {
		return ehrService;
	}

	public void setEhrService(EmployeeProfileService ehrService) {
		this.ehrService = ehrService;
	}

	@Override
	public List<IpUser> getUserList(String userId) throws IPFMBusinessException {
		List<IpUser> userIdList = null;
		try {
			userIdList = ipUserDao.getUserList(userId);

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return userIdList;
	}

	@Override
	public List<IpRole> getRoleList(String userId) throws IPFMBusinessException {
		List<IpRole> userIdList = null;
		try {
			userIdList = ipRoleDao.getRoleList(userId);

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return userIdList;
	}
	
	@Override
	public List<IpRole> getRoleList(String userId, String role06, String role07) throws IPFMBusinessException {
		List<IpRole> roleAllList = null;
		List<IpRole> result = new ArrayList<IpRole>();		
		try {
			roleAllList = ipRoleDao.getRoleList(userId, role06, role07);
			for (IpRole role : roleAllList) {
				if (role.getRoleId().equalsIgnoreCase("R02")) {
					continue;
				}
				result.add(role);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public List<IpUser> getDelegateToList(String userId) throws IPFMBusinessException {
		List<IpUser> userIdList = null;
		try {
			userIdList = ipUserDao.getDelegateToList(userId);

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return userIdList;
	}

	@Override
	public String getManagerName(String managerId) throws IPFMBusinessException {
		String managerName ="";
		try {
			managerName = ipUserDao.getManagerName(managerId);
			if (managerName==null || managerName.trim().length()==0) {
				managerName = managerId;
				
				// Get Authen User for Authen DB
				UserAccount authUser = authService.getAuthenUserConfig();
				
				// Get data from OM-eHR
				List<EmployeeProfileOM> managerList = ehrService2.getEmployeeProfileByEmail(authUser, managerId+"@%");
				if (managerList.size()>0) {
					EmployeeProfileOM manager = managerList.get(0);
					if (IPFMUtils.ifBlank(manager.getENGNAME(),"").length()>0 && IPFMUtils.ifBlank(manager.getENGSURNAME(), "").length()>0) {
						managerName = manager.getENGNAME()+" "+manager.getENGSURNAME();
					}
				}
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return managerName;
	}

	@Override
	public List<IpDelegateUser> getDelegateDataList(String userId) throws IPFMBusinessException {
		List<IpDelegateUser> delegateDataList = null;
		try {
			delegateDataList = ipDelegateUserDao.getDelegateDataList(userId);

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return delegateDataList;
	}

	@Override
	public List<IpRoleMember> getRoleDataList(String roleId) throws IPFMBusinessException {
		List<IpRoleMember> roleDataList = null;
		try {
			roleDataList = ipRoleMemberDao.getRoleDataList(roleId);

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return roleDataList;
	}

	@Override
	public IpUser getEhrByUserId(String userId) throws IPFMBusinessException {
		EmployeeProfileOM profile=null;
		ApproverOM approver=null;
		IpUser ipUser=new IpUser();
		
		try {
			UserAccount authUser = authService.getAuthenUserConfig();
			List<EmployeeProfileOM> employeeProfileList = ehrService2.getEmployeeProfileByEmail(authUser, userId+"@%");

			if (employeeProfileList!=null && employeeProfileList.size()>0) {
				profile = employeeProfileList.get(0);
				List<ApproverOM> aprlist = ehrService2.listAllApprover(authUser, profile.getPIN(), profile.getPOSITIONID());
				if (aprlist.size()>0) approver = aprlist.get(0);
				ipUser.setUserName(profile.getENGNAME()+" "+profile.getENGSURNAME());
				//ipUser.setSection(IPFMUtils.ifBlank(profile.getScname(),""));
				ipUser.setSection(IPFMUtils.ifBlank(profile.getORGNAME(),""));
				ipUser.setDept(IPFMUtils.ifBlank(profile.getDPNAME(),""));
				ipUser.setPosition(IPFMUtils.ifBlank(profile.getPOSITION(),""));
				String[] managerEmail = IPFMUtils.ifBlank(approver.getEMAIL(),"").split("@");
				String managerId = "";
				if(managerEmail != null && managerEmail.length > 1) {
					managerId = managerEmail[0];
				} else {
					managerId = profile.getEMAIL();
				}
				ipUser.setManagerId(managerId);
				ipUser.setEmail(profile.getEMAIL());
				if (IPFMUtils.ifBlank(approver.getENGNAME(),"").length()==0 && IPFMUtils.ifBlank(approver.getENGSURNAME(), "").length()==0) {
					ipUser.setManagerName(ipUser.getManagerId());
					List<EmployeeProfileOM> managerList = ehrService2.getEmployeeProfileByEmail(authUser, approver.getEMAIL());
					if (managerList.size()>0) {
						EmployeeProfileOM manager = managerList.get(0);
						if (IPFMUtils.ifBlank(manager.getENGNAME(),"").length()>0 && IPFMUtils.ifBlank(manager.getENGSURNAME(), "").length()>0) {
							ipUser.setManagerName(manager.getENGNAME()+" "+manager.getENGSURNAME());
						}
					}
				}else{ 
					ipUser.setManagerName(IPFMUtils.ifBlank(approver.getENGNAME(),"")+" "+IPFMUtils.ifBlank(approver.getENGSURNAME(),""));
				}
			}else{
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0102")));
			}
		} catch (IPFMBusinessException be) {
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipUser;
	}

	@Override
	public void saveUserProfile(IpUser ipUser) throws IPFMBusinessException {
			try {
				ipUserDao.saveUserProfile(ipUser);
			} catch (Exception e) {
				e.printStackTrace();
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
			}
	}

	@Override
	public List<IpDelegateUser>  addDelegate(IpDelegateUser ipDelegateUser, IpUser ipUser) throws IPFMBusinessException {
		List<IpDelegateUser> delegateDataList = null;
		try {
			String result = ipDelegateUserDao.getCheckExistingData(ipDelegateUser,ipUser);
			String rowId = commonDao.getROW_ID();
			if("Found".equals(result)){
			  ipDelegateUserDao.updateDelegate(ipDelegateUser,ipUser);
			}else{
			  ipDelegateUser.setRowId(rowId);
			  ipDelegateUserDao.insert(ipDelegateUser);
			}
			delegateDataList = ipDelegateUserDao.getDelegateDataList(ipUser.getUserId());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return delegateDataList;
	}
	
	@Override
	public List<IpDelegateUser>  deleteDelegate(IpDelegateUser ipDelegateUser, IpUser ipUser) throws IPFMBusinessException {
		List<IpDelegateUser> delegateDataList = null;
		try {
			  delegateDataList = ipDelegateUserDao.deleteDelegate(ipDelegateUser.getId().getDelegateUserId(),ipUser.getUserId());
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return delegateDataList;
	}

	@Override
	public List<IpRoleMember>  addRole(IpRoleMember ipRoleMember) throws IPFMBusinessException {
		List<IpRoleMember> delegateDataList = null;
		try {
			String result = ipRoleMemberDao.getCheckExistingDataRole(ipRoleMember);
			if("Found".equals(result)){
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0036")));
			}else{
//				IpRole  obj = ipRoleDao.getIpRole(ipRoleMember.getRoleId());
//				if(obj!=null){
//					if(obj.getUserIdList()!=null && obj.getUserNameList()!=null){
//						obj.setUserIdList(obj.getUserIdList()+ipRoleMember.getUserId()+";");
//						obj.setUserNameList(obj.getUserNameList()+ipRoleMember.getUserName()+";");
//					}else{
//						obj.setUserIdList(ipRoleMember.getUserId()+";");
//						obj.setUserNameList(ipRoleMember.getUserName()+";");
//					}
//					ipRoleDao.update(obj);
//				}
				
				IpRoleMember  objIpRoleMember  = new IpRoleMember();
				objIpRoleMember.setRoleId(ipRoleMember.getRoleId());
				objIpRoleMember.setUserId(ipRoleMember.getUserId());
				objIpRoleMember.setLastUpdBy(ipRoleMember.getLastUpdBy());
				objIpRoleMember.setCreatedBy(ipRoleMember.getCreatedBy());
				ipRoleMemberDao.insert(objIpRoleMember);
				IpUser ipUser;
				try {
					ipUser = ipUserDao.getIPUser(ipRoleMember.getUserId());
					 if(ipUser.getRoleIdList()!=null){
						 ipUser.setRoleIdList(ipUser.getRoleIdList()+ipRoleMember.getRoleId()+";");
					 }else{
						 ipUser.setRoleIdList(ipRoleMember.getRoleId()+";");
					 }
					ipUserDao.update(ipUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			delegateDataList = ipRoleMemberDao.getRoleDataList(ipRoleMember.getRoleId());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (IPFMBusinessException ipfme){
			throw ipfme;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return delegateDataList;
	}

	@Override
	public List<IpRoleMember> deleteRole(IpRoleMember ipRoleMember)throws IPFMBusinessException {
		List<IpRoleMember> roleDataList = null;
		try {
//			IpRole  obj = ipRoleDao.getIpRole(ipRoleMember.getRoleId());
//			String arrObjUserId[] = obj.getUserIdList().split(";");
//			String arrObjUserName[] = obj.getUserNameList().split(";"); 
//			String userIdList="";
//			String userNameList="";
//			for (int i = 0 ; i<arrObjUserId.length ; i++){
//				String str = arrObjUserId[i];
//				if(!str.equals(ipRoleMember.getUserId())){
//				  userIdList  = userIdList+str+";";
//				}
//			}
//			for (int i = 0 ; i<arrObjUserName.length ; i++){
//				String str = arrObjUserName[i];
//				if(!str.equals(ipRoleMember.getUserName())){
//					userNameList  = userNameList+str+";";
//				}
//			}
//			obj.setUserIdList(userIdList);
//			obj.setUserNameList(userNameList);
//			ipRoleDao.update(obj);
			
			IpUser ipUser;
			String roleIdList="";
			try {
				ipUser = ipUserDao.getIPUser(ipRoleMember.getUserId());
				if(ipUser!=null){
					String arrRoleId[] = ipUser.getRoleIdList().split(";");
					for (int i = 0 ; i<arrRoleId.length ; i++){
						String str = arrRoleId[i];
						if(!str.equals(ipRoleMember.getRoleId())){
						  roleIdList  = roleIdList+str+";";
						}
				   }
					ipUser.setRoleIdList(roleIdList);
				}
				ipUser.setRowId(ipUser.getRowId());
				ipUserDao.update(ipUser);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			roleDataList = ipRoleMemberDao.deleteRole(ipRoleMember.getRoleId(),ipRoleMember.getUserId());
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return roleDataList;
	}

	@Override
	public List<IpUser> getUserIdList() throws IPFMBusinessException {
		List<IpUser> userIdList = null;
		try {
			userIdList = ipUserDao.getUserIdList();
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return userIdList;
	}

	@Override
	public String checkRole(String userId) throws IPFMBusinessException {
		String checkRole="";
		try {
		     checkRole  =  ipRoleMemberDao.checkRole(userId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return checkRole;
	}
	
	

	@Override
	public List<IpRole> getRoleAllList() throws IPFMBusinessException {
//		List<IpRole> roleAllList = null;
		List<IpRole> result = new ArrayList<IpRole>();
		try {
			result = ipRoleDao.getRoleAllList();
//			for (IpRole role : roleAllList) {
//				if (role.getRoleId().equalsIgnoreCase("R02")) {
//					continue;
//				}
//				result.add(role);
//			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return result;
	}

	@Override
	public IpUser getIPUser(String userId) throws IPFMBusinessException {
		IpUser ipUser=null;
		try {
			ipUser  =  ipUserDao.getIPUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUser;
	}

	@Override
	public List<IpDelegateUser> getDelegateAdminList(String userId) throws IPFMBusinessException {
		List<IpDelegateUser> delegateAdminList = null;
		try {
			delegateAdminList = ipDelegateUserDao.getDelegateAdminList(userId);

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return delegateAdminList;

	}

	@Override
	public List<IpRoleMember> findIpRoleMemberByUserId(String userId) throws IPFMBusinessException {
		List<IpRoleMember> resultList = new ArrayList<IpRoleMember>();
		try {
			resultList = ipRoleMemberDao.findIpRoleMembersByUserId(userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	
}
