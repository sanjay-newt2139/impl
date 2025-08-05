package th.co.ais.ipfm.service.impl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import th.co.ais.ehr.domain.ApproverOM;
import th.co.ais.ehr.domain.EmployeeProfileOM;
import th.co.ais.ehr.domain.UserAccount;
import th.co.ais.ehr.service.AuthenDBService;
import th.co.ais.ehr.service.EmployeeProfileService;
import th.co.ais.ehr.service.EmployeeProfileService2;

import th.co.ais.ipfm.dao.IIPUserDao;
import th.co.ais.ipfm.dao.IPEmailNotifyDao;
import th.co.ais.ipfm.dao.IPFunctionDao;
import th.co.ais.ipfm.dao.IPRoleMemberDao;
import th.co.ais.ipfm.dao.IPTeamDao;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.domain1.IpEmailNotify;
import th.co.ais.ipfm.domain1.IpFunction;
import th.co.ais.ipfm.domain1.IpRoleMember;
import th.co.ais.ipfm.domain1.IpTeam;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.ILoginService;
import th.co.ais.ipfm.service.InboxService;
import th.co.ais.ipfm.service.MainMenuService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;
import th.co.ais.ipfm.util.PropertiesMessageUtil;
import th.co.ais.ipfm.vo.UserVO;
import th.co.ais.ldap.domain.LDAPResMsg;
import th.co.ais.ldap.service.LDAPService;
import th.co.ais.sso.service.SSOService;
import Permission.bean.ais.com.SSOResponse;

public class LoginServiceImpl implements ILoginService{
	private SSOService ssoService;
	private LDAPService ldapService;
	private EmployeeProfileService ehrService;
	private AuthenDBService authService;
	private EmployeeProfileService2 ehrService2;
	
	private MainMenuService mainMenuService;
	private InboxService inboxService;
	private IIPUserDao ipUserDao;
	private IPTeamDao ipTeamDao;
	private IPRoleMemberDao ipRoleMemberDao;
	private IPEmailNotifyDao ipEmailNotifyDao;
	private IPUrActionDao ipUrActionDao;
	private IPFunctionDao ipFunctionDao;
	
	public void setAuthService(AuthenDBService authService) {
		this.authService = authService;
	}
	
	public void setEhrService2(EmployeeProfileService2 ehrService2) {
		this.ehrService2 = ehrService2;
	}

	public void setIpFunctionDao(IPFunctionDao ipFunctionDao) {
		this.ipFunctionDao = ipFunctionDao;
	}

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) {
		this.ipUrActionDao = ipUrActionDao;
	}

	public void setIpEmailNotifyDao(IPEmailNotifyDao ipEmailNotifyDao) {
		this.ipEmailNotifyDao = ipEmailNotifyDao;
	}

	public void setIpTeamDao(IPTeamDao ipTeamDao) {
		this.ipTeamDao = ipTeamDao;
	}

	public void setIpRoleMemberDao(IPRoleMemberDao ipRoleMemberDao) {
		this.ipRoleMemberDao = ipRoleMemberDao;
	}
	
	public void setIpUserDao(IIPUserDao ipUserDao) {
		this.ipUserDao = ipUserDao;
	}
	
	public void setLdapService(LDAPService ldapService) {
		this.ldapService = ldapService;
	}
	
	public void setSsoService(SSOService ssoService) {
		this.ssoService = ssoService;
	}
	
	public void setEhrService(EmployeeProfileService ehrService) {
		this.ehrService = ehrService;
	}

	public void setMainMenuService(MainMenuService mainMenuService) {
		this.mainMenuService = mainMenuService;
	}
	public void setInboxService(InboxService inboxService) {
		this.inboxService = inboxService;
	}
	@Override
	public IpUser getIPUser(String userId) throws IPFMBusinessException {
		IpUser ipUser = null;
		try {
			ipUser = ipUserDao.getIPUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipUser;
	}
	@Override
	public String getSysdate() throws IPFMBusinessException {
		String sysDate=null;
		try {
			sysDate = ipUserDao.getSysdate();
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return sysDate;
	}
	@Override
	public IpUser addUser(IpUser ipUser) throws IPFMBusinessException {
		try {
			ipUserDao.insert(ipUser);
			//ipUser = ipUserDao.getIPUser(ipUser.getUserId());
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipUser;
	}
	
	@Override
	public IpUser updateUser(IpUser ipUser) throws IPFMBusinessException {
		try {
			ipUserDao.update(ipUser);
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipUser;
	}
	
	public Date addDay(String dateNow,Integer day) throws IPFMBusinessException{
		Calendar cd = Calendar.getInstance(new Locale("en","EN"));
		DecimalFormat df = new DecimalFormat("00");	
		DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",new Locale("en","EN"));
		
		Date dd =null;
		try{
			String date = dateNow.substring(0,2);
			String month = dateNow.substring(3,5);
			String year = dateNow.substring(6,10);
			String hour = dateNow.substring(11,13);
			String min = dateNow.substring(14,16);
			String sec = dateNow.substring(17,19);
			
			cd.set(Calendar.YEAR,Integer.parseInt(year));
			cd.set(Calendar.MONTH,Integer.parseInt(month)-1);
			cd.set(Calendar.DATE,Integer.parseInt(date)+day);
			cd.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour));
			cd.set(Calendar.MINUTE,Integer.parseInt(min));
			cd.set(Calendar.SECOND,Integer.parseInt(sec));
			
			 year = String.valueOf(cd.get(Calendar.YEAR));
			 month = df.format(cd.get(Calendar.MONTH)+1);
			 date = df.format(cd.get(Calendar.DATE));
			 hour = df.format(cd.get(Calendar.HOUR_OF_DAY));
			 min = df.format(cd.get(Calendar.MINUTE));
			 sec = df.format(cd.get(Calendar.SECOND));
			
			dd = dfm.parse(date+"/"+month+"/"+year+" "+hour+":"+min+":"+sec);
			
		}catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return dd;
	}
	
	public boolean checkInLdap(String userName,String password) throws IPFMBusinessException {
		boolean result = false;
		try {
			System.out.println("---------------- checkInLdap --------------");
			//Test
			//ssoService.setEndpoint("http://10.240.1.101:7201/EmployeeServiceWebServiceV2/EmployeeServiceWebServiceV2.jws");
			
			//Product
			//ssoService.setEndpoint("https://empws.ais.co.th/EmployeeServiceWebService/EmployeeServiceWebService.jws");
			
//			LDAPResMsg msg = ldapService.authenLDAP("puttapoa", "Ais.co.th", "CORP");
			LDAPResMsg msg = ldapService.authenLDAP(userName, password);
			if (!msg.getSTATUS().equalsIgnoreCase("0")) {
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(msg.getDETAIL()));
			}
			System.out.println("msg.getDETAIL() = " + msg.getDETAIL());
			System.out.println("msg.getSTATUS() = " + msg.getSTATUS());
			result = true;
		} catch (IPFMBusinessException be) {
			throw be;
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0026")));
		} 
		return result;
	}
	
	public IpUser getEhrByUserId(String userId) throws IPFMBusinessException{
		EmployeeProfileOM profile=null;
		ApproverOM approver=null;
		IpUser ipUser=new IpUser();
		try {
			String sysDate = ipUserDao.getSysdate();
			Date curDate = addDay(sysDate,0);
			
			// Get Authen User for Authen DB
			UserAccount authUser = authService.getAuthenUserConfig();
			
			// Get data from OM-eHR
			List<EmployeeProfileOM> employeeProfileList = ehrService2.getEmployeeProfileByEmail(authUser, userId+"@%");
			
			if(employeeProfileList == null || employeeProfileList.size() == 0 || employeeProfileList.get(0) == null) throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0035"),userId));
			profile = employeeProfileList.get(0);
			
			List<ApproverOM> aprlist = ehrService2.listAllApprover(authUser, profile.getPIN(), profile.getPOSITIONID());
			if (aprlist != null && aprlist.size()>0) approver = aprlist.get(0);
			ipUser.setUserName(IPFMUtils.ifBlank(IPFMUtils.ifBlank(profile.getENGNAME(),"")+" "+IPFMUtils.ifBlank(profile.getENGSURNAME(),""),""));
			//ipUser.setSection(profile.getScname()==null?"":profile.getScname());
			ipUser.setSection(profile.getORGNAME()==null?"":profile.getORGNAME());
			ipUser.setDept(profile.getDPNAME()==null?"":profile.getDPNAME());
			IpTeam ipTeam = null;
			String teamName = "";
			String teamId = "";
				
				
			profile.setSCCODE(profile.getORGCODE());
			profile.setSCNAME(profile.getORGNAME());

				
			if (profile.getSCNAME()!=null && profile.getSCNAME().trim().length()>0) {
				teamName = profile.getSCNAME();
			}else if (profile.getDPNAME()!=null && profile.getDPNAME().trim().length()>0) {
				teamName = profile.getDPNAME();
			}else{
				teamName = "NA";
			}
			ipTeam = ipTeamDao.getIpTeamByTeamName(teamName);				
			if (ipTeam != null) {
				teamId = ipTeam.getTeamId();
				if (!ipTeam.getActiveStatus().equalsIgnoreCase("Y")) {
						ipTeam.setActiveStatus("Y");
						ipTeamDao.update(ipTeam);
				}
			}else if (teamName!=null && teamName.trim().length()>0) {
					ipTeam = new IpTeam();
					teamId = ipTeamDao.getNextTeamId();
					ipTeam.setTeamId(teamId);
					ipTeam.setTeamName(teamName);
					ipTeam.setActiveStatus("Y");
					ipTeam.setCreatedBy(userId);
					ipTeam.setLastUpdBy(userId);
					ipTeamDao.insert(ipTeam);
				}
			ipUser.setTeamId(teamId);
			ipUser.setTeamName(teamName);
			ipUser.setEmail(profile.getEMAIL());
			ipUser.setPosition(profile.getPOSITION());
			ipUser.setMobile(profile.getMOBILENO());
			ipUser.setTel(profile.getTELNO());
			String[] mail = approver.getEMAIL().split("@");
			ipUser.setManagerId(mail[0]);	
			ipUser.setRoleIdList("R02;");				
			if (ipUser.getPosition()!=null && ipUser.getPosition().trim().length()>0){
				String position = PropertiesMessageUtil.getConfigMessage("CF0001");
				if (position != null && position.trim().length()>0) {
					String[] posit = position.split(";");
					for(int i=0 ; i<posit.length ; i++){
						if (ipUser.getPosition().toUpperCase().indexOf(posit[i])>(-1)) {
							ipUser.setRoleIdList("R02;R03;");
							break;
						}
					}
				}
			}
			
			ipUser.setUserId(userId);
			ipUser.setDelegateStatus("N");
			ipUser.setEffectiveDate(curDate);
			ipUser.setLastLogin(curDate);
			ipUser.setUserActiveStatus("X");
			ipUser.setCreatedBy(userId);
			ipUser.setLastUpdBy(userId);
			ipUser.setLastLogin(curDate);
		} catch (IPFMBusinessException be) {
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0027")));
		} 
		return ipUser;
	}
	
	public boolean checkInSSO(String userId,String password) throws IPFMBusinessException {
		boolean result = false;
		try {
			System.out.println("---------------- checkInSSO --------------");
			//Test
			//ssoService.setEndpoint("http://10.240.1.101:7201/EmployeeServiceWebServiceV2/EmployeeServiceWebServiceV2.jws");
			
			//Product
			//ssoService.setEndpoint("https://empws.ais.co.th/EmployeeServiceWebService/EmployeeServiceWebService.jws");
//			SSOResponse response = ssoService.getToken("IPFMTEST", "ipfmtest", "IPFM", "2", "TIPFMAUTHEN");
			SSOResponse response = ssoService.getToken(userId, password);
			if (!response.getMessage().isFlag()){
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(response.getMessage().getErrorMesg()));
			}
			result = response.getMessage().isFlag();
		} catch (IPFMBusinessException ex) {
			//if (ex.getMessage().equalsIgnoreCase("SSOService:getToken")) 
				return result;
			//throw ex;
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage(e.getMessage())));
		} 
		return result;
	}
	
	public UserVO login(String userId, String password) throws IPFMBusinessException {
		UserVO userSession = new UserVO();
		try {
//			test
//			List<Approver> aprlist = ehrService.listAllApprover("00000293", "50064777");
//			System.out.println(aprlist.size());
//			System.out.println(aprlist.get(0).getName());
			 
			String sysDate = ipUserDao.getSysdate();
			Date curDate = addDay(sysDate,0);

//			if (!checkInSSO(userId, password)){
			
			// Disable for Dev
			//checkInLdap(userId, password);
		
				System.out.println(" Login Method Service .............");
//			}

			IpUser ipUser = getIPUser(userId);
			if (ipUser==null){
				ipUser = getEhrByUserId(userId);
				addUser(ipUser);
				addIpRoleMemberByUser(ipUser);
				userSession.setNewUser("Y");
			}else if (ipUser.getUserActiveStatus().equalsIgnoreCase("X")) {
				ipUser.setLastLogin(curDate);
//				ipUser.setLastUpdBy(userId);
				ipUser.setLastUpd(null);
				updateUser(ipUser);
				userSession.setNewUser("Y");
			}else{
//				validateEffectiveUser(ipUser);				
				ipUser.setUserActiveStatus("Y");
				ipUser.setLastLogin(curDate);
//				ipUser.setLastUpdBy(userId);
				updateUser(ipUser);
				userSession.setNewUser("N");
			}
			if (userSession.getNewUser().equalsIgnoreCase("N")){
				IpEmailNotify ipEmailNotify = ipEmailNotifyDao.findLastEmailNotifyByUserId(userId);
				if (ipEmailNotify!=null){
					IpEmailNotify lastEmailNotify = (IpEmailNotify)ipEmailNotify.clone();
					userSession.setLastEmailNotify(lastEmailNotify);
					ipEmailNotifyDao.deleteByUser(userId, "Y");
					IpUrAction urAction = ipUrActionDao.findUrActionNotify(userSession.getLastEmailNotify().getUrNo(), userSession.getLastEmailNotify().getUrStatus() , ipUser.getUserId());
					if (urAction != null) {
						userSession.setLastUrActionNotify(urAction);
						IpFunction ipFunction = getIpFunction(userSession.getLastUrActionNotify().getCallFunctionId());
				        String urlPath = PropertiesMessageUtil.getProgramurl(ipFunction.getProgramId());
				        userSession.setNotifyProgramId(urlPath);
					}
				}
			}
			List<IpFunction> menuLevel1List = mainMenuService.findMenuLevel1ByUserId(ipUser.getUserId());
			Map listMenu = mainMenuService.listMenuByUserID(ipUser.getUserId());
			userSession.setRoleMonitor(getRoleMonitor(ipUser));
			userSession.setIpUser(ipUser);
			userSession.setLoginDateTime(sysDate);
			userSession.setMenuLevel1List(menuLevel1List);
			userSession.setObjUserMenu(listMenu);
			userSession.setNews(getNews());
		} catch (IPFMBusinessException be) {
			be.printStackTrace();
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} finally {
			
		}
		return userSession;
	}
	
	private boolean getRoleMonitor(IpUser ipUser) {
		List<IpRoleMember> memberList = ipRoleMemberDao.findIpRoleMembersByUserId(ipUser.getUserId());
		for (IpRoleMember member : memberList){
			if (member.getRoleId().equalsIgnoreCase("R06") || member.getRoleId().equalsIgnoreCase("R07") ||member.getRoleId().equalsIgnoreCase("R08")) {
				return true;
			}
		}
		return false;
	}

	private void validateEffectiveUser(IpUser ipUser) throws IPFMBusinessException {
		String sysDate = ipUserDao.getSysdate();
		Date curDate = addDay(sysDate,0);
		DateFormat dfm2 = new SimpleDateFormat("dd/MM/yyyy",new Locale("en","EN"));
		if ((ipUser.getEffectiveDate()!=null && ipUser.getExpireDate()!=null) ||
			(ipUser.getExpireDate()!=null && ipUser.getExpireDate().compareTo(curDate)<0) || 
			(ipUser.getEffectiveDate()!=null && ipUser.getEffectiveDate().compareTo(curDate)>0)) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0017")), dfm2.format(ipUser.getEffectiveDate()),dfm2.format(ipUser.getExpireDate()));
		}
	}

	public void addIpRoleMemberByUser(IpUser ipUser) throws IPFMBusinessException {
		IpRoleMember ipRoleMember;
		try {
			String roleIdList = ipUser.getRoleIdList();
			String[] roleId = roleIdList.split(";");
			for (int i=0 ; i<roleId.length ; i++) {
				ipRoleMember = new IpRoleMember();
				ipRoleMember.setRoleId(roleId[i]);
				ipRoleMember.setUserId(ipUser.getUserId());
				ipRoleMember.setCreatedBy(ipUser.getUserId());
				ipRoleMember.setLastUpdBy(ipUser.getUserId());
				ipRoleMemberDao.addIpRoleMembers(ipRoleMember);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0027")));
		}
	}

	@Override
	public String getNews() throws IPFMBusinessException {
		String news = "";
		try {
			news = inboxService.getNews();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}	
		return news;
	}

	@Override
	public IpFunction getIpFunction(String funcId) throws IPFMBusinessException{
		IpFunction ipFunction = new IpFunction();
		try{
			ipFunction = ipFunctionDao.findIPFunctionByFuncID(funcId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipFunction;
	}

	@Override
	public void updateSessionId(String userId, String sessionId)
			throws IPFMBusinessException {
		// TODO Auto-generated method stub
		try {
			IpUser ipUser = getIPUser(userId);
			ipUser.setSessionId(sessionId);
			ipUserDao.update(ipUser);
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
	}

	@Override
	public IpUser findUserBySessionId(String userId, String sessionId)
			throws IPFMBusinessException {
		// TODO Auto-generated method stub
		IpUser ipuser = null;
		try {
			ipuser = ipUserDao.findUserBySesionId(userId,sessionId);

		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
		return ipuser;
	}

	@Override
	public void checkNewUser(String userId) throws IPFMBusinessException {
		IpUser ipUser = getIPUser(userId);
		if (ipUser==null){
			ipUser = getEhrByUserId(userId);
			addUser(ipUser);
			addIpRoleMemberByUser(ipUser);		
		}
	}
}
