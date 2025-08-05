package th.co.ais.ipfm.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import th.co.ais.email.service.EMailService;
import th.co.ais.ipfm.dao.CallProcedureDao;
import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPAccessListDao;
import th.co.ais.ipfm.dao.IIPAccessListTNPDao;
import th.co.ais.ipfm.dao.IIPF5Dao;
import th.co.ais.ipfm.dao.IIPFirewallDAO;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IIPIntfGatewayDao;
import th.co.ais.ipfm.dao.IIPPhysicalInterfaceDao;
import th.co.ais.ipfm.dao.IIPRoutingDao;
import th.co.ais.ipfm.dao.IIPStatusDAO;
import th.co.ais.ipfm.dao.IIPUserDao;
import th.co.ais.ipfm.dao.IPEmailNotifyDao;
import th.co.ais.ipfm.dao.IPRoleMemberDao;
import th.co.ais.ipfm.dao.IPSubUrAttachmentDao;
import th.co.ais.ipfm.dao.IPTeamDao;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.dao.IPUrAttachmentDao;
import th.co.ais.ipfm.dao.IPUrNwConfigDao;
import th.co.ais.ipfm.dao.IpNetworkConfigDao;
import th.co.ais.ipfm.domain.IPRole;
import th.co.ais.ipfm.domain1.EmailDetail;
import th.co.ais.ipfm.domain1.IpEmailNotify;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpNetworkConfig;
import th.co.ais.ipfm.domain1.IpRoleMember;
import th.co.ais.ipfm.domain1.IpSubUrAttachment;
import th.co.ais.ipfm.domain1.IpSubUrAttachmentId;
import th.co.ais.ipfm.domain1.IpUrAccessListCdn;
import th.co.ais.ipfm.domain1.IpUrAccessListCdnId;
import th.co.ais.ipfm.domain1.IpUrAccessListTnp;
import th.co.ais.ipfm.domain1.IpUrAccessListTnpId;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrF5;
import th.co.ais.ipfm.domain1.IpUrF5Id;
import th.co.ais.ipfm.domain1.IpUrFirewall;
import th.co.ais.ipfm.domain1.IpUrFirewallId;
import th.co.ais.ipfm.domain1.IpUrInterfaceGateway;
import th.co.ais.ipfm.domain1.IpUrInterfaceGatewayId;
import th.co.ais.ipfm.domain1.IpUrNwConfig;
import th.co.ais.ipfm.domain1.IpUrPhysicalInterface;
import th.co.ais.ipfm.domain1.IpUrPhysicalInterfaceId;
import th.co.ais.ipfm.domain1.IpUrRouting;
import th.co.ais.ipfm.domain1.IpUrRoutingId;
import th.co.ais.ipfm.domain1.IpUrStatus;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.domain1.IpWfConfig;
import th.co.ais.ipfm.domain1.IpvJobAssign;
import th.co.ais.ipfm.domain1.PMAssetDTO;
import th.co.ais.ipfm.domain1.PmAssign;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IPUrNwConfigService;
import th.co.ais.ipfm.service.IpUrActionService;
import th.co.ais.ipfm.service.IpUrAttachmentService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;


public class IPUrNwConfigServiceImpl implements IPUrNwConfigService {
	private IPUrNwConfigDao ipUrNwConfigDao;
	private IPUrActionDao ipUrActionDao;
	private IIPStatusDAO ipStatusDao;
	private IIPInfoDAO ipInfoDao;
	private IPTeamDao ipTeamDao;
	private IPUrActionHistoryDao ipUrActionHistoryDao;
	private IIPUserDao ipUserDao;
	private IPRoleMemberDao ipRoleMemberDao;
	private IPEmailNotifyDao ipEmailNotify;
	private IpNetworkConfigDao ipNetworkConfigDao;
	private CommonDao commonDao;

	private IIPFirewallDAO firewallDao;
	private IIPAccessListDao accessListDao;
	private IIPAccessListTNPDao accessListTNPDao;
	private IIPPhysicalInterfaceDao physicalInterfaceDao;
	private IIPIntfGatewayDao intfGatewayDao;
	private IIPRoutingDao routingDao;
	private IIPF5Dao f5Dao;
	private IPUrAttachmentDao ipUrAttachmentDao;
	private CallProcedureDao callProcedureDao;
	
	private EMailService emailService;
	private IpUrActionService ipUrActionService;
	private IpUrAttachmentService ipUrAttachmentService;
	private IPSubUrAttachmentDao ipSubUrAttachmentDao;
	
	

	public CallProcedureDao getCallProcedureDao() {
		return callProcedureDao;
	}

	public void setCallProcedureDao(CallProcedureDao callProcedureDao) {
		this.callProcedureDao = callProcedureDao;
	}

	public IpUrActionService getIpUrActionService() {
		return ipUrActionService;
	}

	public void setIpUrActionService(IpUrActionService ipUrActionService) {
		this.ipUrActionService = ipUrActionService;
	}

	public EMailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EMailService emailService) {
		this.emailService = emailService;
	}

	public IpUrAttachmentService getIpUrAttachmentService() {
		return ipUrAttachmentService;
	}

	public void setIpUrAttachmentService(IpUrAttachmentService ipUrAttachmentService) {
		this.ipUrAttachmentService = ipUrAttachmentService;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setIpNetworkConfigDao(IpNetworkConfigDao ipNetworkConfigDao) {
		this.ipNetworkConfigDao = ipNetworkConfigDao;
	}

	public void setIpEmailNotify(IPEmailNotifyDao ipEmailNotify) {
		this.ipEmailNotify = ipEmailNotify;
	}

	public void setFirewallDao(IIPFirewallDAO firewallDao) {
		this.firewallDao = firewallDao;
	}

	public void setAccessListDao(IIPAccessListDao accessListDao) {
		this.accessListDao = accessListDao;
	}

	public void setAccessListTNPDao(IIPAccessListTNPDao accessListTNPDao) {
		this.accessListTNPDao = accessListTNPDao;
	}

	public void setPhysicalInterfaceDao(
			IIPPhysicalInterfaceDao physicalInterfaceDao) {
		this.physicalInterfaceDao = physicalInterfaceDao;
	}

	public void setIntfGatewayDao(IIPIntfGatewayDao intfGatewayDao) {
		this.intfGatewayDao = intfGatewayDao;
	}

	public void setRoutingDao(IIPRoutingDao routingDao) {
		this.routingDao = routingDao;
	}

	public void setF5Dao(IIPF5Dao f5Dao) {
		this.f5Dao = f5Dao;
	}

	public void setIpUserDao(IIPUserDao ipUserDao) {
		this.ipUserDao = ipUserDao;
	}

	public void setIpUrActionHistoryDao(
			IPUrActionHistoryDao ipUrActionHistoryDao) {
		this.ipUrActionHistoryDao = ipUrActionHistoryDao;
	}

	public IPTeamDao getIpTeamDao() {
		return ipTeamDao;
	}

	public void setIpTeamDao(IPTeamDao ipTeamDao) {
		this.ipTeamDao = ipTeamDao;
	}

	public void setIpInfoDao(IIPInfoDAO ipInfoDao) {
		this.ipInfoDao = ipInfoDao;
	}

	public void setIpStatusDao(IIPStatusDAO ipStatusDao) {
		this.ipStatusDao = ipStatusDao;
	}

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) {
		this.ipUrActionDao = ipUrActionDao;
	}

	public void setIpUrNwConfigDao(IPUrNwConfigDao ipUrNwConfigDao) {
		this.ipUrNwConfigDao = ipUrNwConfigDao;
	}

	public IPUrAttachmentDao getIpUrAttachmentDao() {
		return ipUrAttachmentDao;
	}

	public void setIpUrAttachmentDao(IPUrAttachmentDao ipUrAttachmentDao) {
		this.ipUrAttachmentDao = ipUrAttachmentDao;
	}

	
	public void setIpRoleMemberDao(IPRoleMemberDao ipRoleMemberDao) {
		this.ipRoleMemberDao = ipRoleMemberDao;
	}

	@Override
	public String getTempUrNO() {
		// TODO Auto-generated method stub
		return ipUrNwConfigDao.getTempURNo();
	}

	@Override
	public String getUrNO() {
		return ipUrNwConfigDao.getURNo();
	}

	@Override
	public IpUrNwConfig findByUrNo(String urNo) {
		IpUrNwConfig data = ipUrNwConfigDao.findByUrNo(urNo);
		if(data != null){
			if(data.getListFirewall() != null && data.getListFirewall().size() > 0){
				for(IpUrFirewall fw : data.getListFirewall()){
					if(fw != null && fw.getServiceTcpPort() != null){
						fw.setServiceTcpPortStr(IPFMUtils.validateServicePort(fw.getServiceTcpPort()));
					}else{
						fw.setServiceTcpPortStr("");
					}
					
					if(fw != null && fw.getServiceUdpPort() != null){
						fw.setServiceUdpPortStr(IPFMUtils.validateServicePort(fw.getServiceUdpPort()));
					}else{
						fw.setServiceUdpPortStr("");
					}
					
					if(fw != null && fw.getServiceOtherPort() != null){
						fw.setServiceOtherPortStr(IPFMUtils.validateServicePort(fw.getServiceOtherPort()));
					}else{
						fw.setServiceOtherPortStr("");
					}
				}
			}
		}
		
		
		return data;
	}
	
	

	public void setIpSubUrAttachmentDao(IPSubUrAttachmentDao ipSubUrAttachmentDao) {
		this.ipSubUrAttachmentDao = ipSubUrAttachmentDao;
	}

	@Override
	public void deleteByUrNo(String urNo) throws IPFMBusinessException {
		try{
		   IpUrNwConfig ur = this.findByUrNo(urNo);
		if(ur!=null) ipUrNwConfigDao.delete(ur);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}

	@Override
	public IpUrAction findActionByUrNo(String urNo, String subUrNo) {
		return ipUrActionDao.findUrAction(urNo, subUrNo);
	}

	@Override
	public void deleteAction(IpUrAction action) {
		ipUrActionDao.delete(action);

	}

	@Override
	public void saveAction(IpUrAction action) {
		ipUrActionDao.saveOrUpdate(action);
	}

	@Override
	public IpUrNwConfig saveUr(IpUrNwConfig draft) throws IPFMBusinessException {
		IpUrNwConfig  ipUrNwConfig =null;
		try{
			draft.setRowId(commonDao.getROW_ID());
			ipUrNwConfigDao.insert(draft);
			ipUrNwConfig = ipUrNwConfigDao.findByUrNo(draft.getUrNo());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrNwConfig;
	}

	@Override
	public IpUrNwConfig updateUr(IpUrNwConfig draft) throws IPFMBusinessException {
		IpUrNwConfig  ipUrNwConfig  = null;
		try{
 		    ipUrNwConfigDao.update(draft);
		    ipUrNwConfig  =  ipUrNwConfigDao.findByUrNo(draft.getUrNo());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrNwConfig;
	}

	@Override
	public Date getSLADate(Date reqDate, String urType, String pmTeam) {
		return ipUrActionDao.getSLADate(reqDate, urType, pmTeam);
	}

	@Override
	public Date getSLADate(Date reqDate, String urType, String pmTeam,
			String isImpact) {
		return ipUrActionDao.getSLADate(reqDate, urType, pmTeam, isImpact);
	}

	@Override
	public Date getOLADate(Date reqDate, String urType, String nodeId) {
		return ipUrActionDao.getOLADate(reqDate, urType, nodeId);
	}

	@Override
	public List<Map> ListManager(IpUser user) {
		return ipUrNwConfigDao.listManager(user);
	}

	@Override
	public IpvJobAssign getJobAssign(String urNo, String subUrNo) {
		return ipUrNwConfigDao.getJobAssign(urNo, subUrNo);
	}

	@Override
	public IpUrStatus getSubUrStatus(String urType, String subUrId) {
		return ipStatusDao.getSubStatus(urType, subUrId);
	}
	
	@Override
	public IpUrStatus getUrStatus(String urType, String subUrId) {
		return ipStatusDao.getStatus(urType, subUrId);
	}

	@Override
	public String getOwnerIp(String ip) {
		return ipInfoDao.getOwnerIP(ip);
	}

	@Override
	public String getPmRileId(String teamId) {
		return ipTeamDao.getPmRoleId(teamId);
	}

	@Override
	public List<IpUrAction> listActionByStatus(String urNo, String status) {
		return ipUrActionDao.listActionByStatus(urNo, status);
	}
	
	@Override
	public List<IpUrAction> listActionByOwner(String urNo, String owner) {
		return ipUrActionDao.listActionByOwner(urNo, owner);
	}
	
	@Override
	public void managerApproveUr(String urNo,IpUser user,String remark) throws IPFMBusinessException {
		Date now = new Date();
		List<IpUrAction> actionList = this.listActionByStatus(urNo,IPFMConstant.STATUS_WAIT_MRG);
		try {
		IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_MGR_APPROVED);
		IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_OWNER);
		
		for(IpUrAction action : actionList){
			// Move Action => into Action History 
			IpUrActionHistory previous = copyHistory(action);
//			previous.setUserRemark(remark);
			previous.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			previous.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			previous.setLastUpd(now);
			BigDecimal nextSeqAction = ipUrActionHistoryDao.getNextActionSeq(previous.getUrNo(), previous.getUrType(), previous.getSubUrNo()) ;
			previous.setActionSeq(nextSeqAction);
			ipUrActionHistoryDao.insert(previous);
			// Insert New Record to History
			//int  actionSeq = Integer.parseInt(action.getActionSeq().toString())+ 1;
			IpUrActionHistory next = copyHistory(action);
			//next.setActionSeq(new BigDecimal(actionSeq));
			next.setReqestUserId(action.getReqestUserId());
			next.setReqestUser(action.getReqestUser());
			next.setActionName(ipUrStatus.getSubUrStatusName());
			next.setUrStatus(IPFMConstant.STATUS_MGR_APPROVED);
			next.setActionRemark("");
			next.setUserRemark(remark);
			next.setActionUserId(user.getUserId());
			next.setActionUser(user.getUserName()+"("+user.getTel()+")");
			if(action.getOlaDate()!=null){
				next.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			}
			if(action.getSlaDate()!=null){
				next.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			}
			next.setCreatedBy(user.getUserId());
			next.setCreated(now);
			next.setLastUpdBy(user.getUserId());
			next.setLastUpd(now);
			BigDecimal nextSeqAction2 = ipUrActionHistoryDao.getNextActionSeq(next.getUrNo(), next.getUrType(), next.getSubUrNo()) ;
			next.setActionSeq(nextSeqAction2);
			//next.setRowId(null);
			ipUrActionHistoryDao.insert(next);
			// Insert New Record to Action
			int  actionSeq2 = Integer.parseInt(action.getActionSeq().toString())+ 2;
			String ip = getIpAddress(action);
			if(ip == null || ip.equals("")) {
				continue;
			}
			Map<String,String> map = ipUrNwConfigDao.getIpOwner(ip);
			if(map==null){
				throw new IPFMBusinessException("ER0210",IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0210"),ip));
			}
			if (map.get("id")==null || map.get("id").trim().length()==0) {
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0106"),ip));
			}
			action.setActionUserId(map.get("id"));
			action.setActionUser(map.get("name"));
			
			action.setActionSeq(new BigDecimal(actionSeq2));
			action.setActionName(ipUrStatus2.getSubUrStatusName());
			action.setUrStatus(IPFMConstant.STATUS_WAIT_OWNER);
			action.setActionRemark(null);
			action.setUserRemark(null);
			action.setOlaDate(ipUrActionDao.getOLADate(now, "NC","N5"));
			action.setCallFunctionId("F031");
			action.setCallMode(IPFMConstant.ACTION_OWNER_APPR);
			action.setCreatedBy(user.getUserId());
			action.setCreated(now);
			action.setLastUpdBy(user.getUserId());
			action.setLastUpd(now);
			ipUrActionDao.update(action);
			//Update Sub UR Table
			this.updateTab(action.getUrNo(), user.getUserId(),IPFMConstant.STATUS_WAIT_OWNER,action.getSubUrNo());
			//this.updateTab(action.getUrNo(), user.getUserId(),IPFMConstant.STATUS_MGR_APPROVED,action.getSubUrNo());
			
		}
			//Update NC Table
		    IpUrAction  ipUrAction  = actionList.get(0);
			IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
			config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_OWNER);
			config.setUrStatusName(ipUrStatus2.getUrStatusName());
			config.setApproveManager(user.getUserName());
			config.setMrgRemark(remark);
			config.setLastUpdBy(user.getUserId());
			config.setLastUpd(null);
			if(ipUrAction.getSlaDate()!=null){
				config.setOverSla(isDateOverCurrent(ipUrAction.getSlaDate(), now));
			} else {
				config.setOverSla("N");
			}
			config.setMrgRemark(remark);
			ipUrNwConfigDao.update(config);
	
		} catch(IPFMBusinessException ex){
			ex.printStackTrace();
			throw ex;
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
	}

	private IpUrActionHistory copyHistory(IpUrAction action) {
		IpUrActionHistory history = new IpUrActionHistory();
		history.setUrNo(action.getUrNo());
		history.setUrType(action.getUrType());
		history.setSubUrNo(action.getSubUrNo());
		history.setSubUrType(action.getSubUrType());
		history.setActionSeq(action.getActionSeq());
		history.setReqestUserId(action.getReqestUserId());							
		history.setReqestUser(action.getReqestUser());							
		history.setReqestDate(action.getReqestDate());							
		history.setActionUserId(action.getActionUserId());							
		history.setActionUser(action.getActionUser());					
		history.setActionName(action.getActionName());							
		history.setUrStatus(action.getUrStatus());							
		history.setActionRemark(action.getActionRemark());							
		history.setUserRemark(action.getUserRemark());							
		history.setOlaDate(action.getOlaDate());							
		history.setSlaDate(action.getSlaDate());							
		history.setOverOla(null);
		history.setOverSla(null);
		history.setCreatedBy(action.getCreatedBy());
		history.setCreated(action.getCreated());						
		history.setLastUpdBy(action.getLastUpdBy());
		history.setLastUpd(action.getLastUpd());
		history.setSubject(action.getSubject());
		return history;
	}
	
	private IpUrAction copyAction(IpUrAction action) {
		IpUrAction actionNew = new IpUrAction();
		actionNew.setUrNo(action.getUrNo());
		actionNew.setUrType(action.getUrType());
		actionNew.setSubUrNo(action.getSubUrNo());
		actionNew.setSubUrType(action.getSubUrType());
		actionNew.setActionSeq(action.getActionSeq());
		actionNew.setReqestUserId(action.getReqestUserId());
		actionNew.setReqestUser(action.getReqestUser());
		actionNew.setReqestDate(action.getReqestDate());
		actionNew.setActionUserId(action.getActionUserId());
		actionNew.setActionUser(action.getActionUser());
		actionNew.setActionName(action.getActionName());
		actionNew.setUrStatus(action.getUrStatus());
		actionNew.setActionRemark(action.getActionRemark());
		actionNew.setUserRemark(action.getUserRemark());
		actionNew.setOlaDate(action.getOlaDate());
		actionNew.setSlaDate(action.getSlaDate());
		actionNew.setCallFunctionId(action.getCallFunctionId());
		actionNew.setCallMode(action.getCallMode());
		actionNew.setCreatedBy(action.getCreatedBy());
		actionNew.setCreated(action.getCreated());
		actionNew.setLastUpdBy(action.getLastUpdBy());
		actionNew.setLastUpd(action.getLastUpd());
		actionNew.setSubject(action.getSubject());
		return actionNew;
	}	
	
	private String getIpAddress(IpUrAction action){
		String ip="";
		if(action.getSubUrType().equals("FW")) { 
			ip=(action.getFirewall() != null) ? action.getFirewall().getDestIp1() : "";
		}else if(action.getSubUrType().equals("AL")) { 
			ip=(action.getAccessListCdn() != null ) ? action.getAccessListCdn().getDestIp1() : "";
		}
		else if(action.getSubUrType().equals("AT")) { 
			ip=(action.getAccessListTnp() !=null) ? action.getAccessListTnp().getDestIp() : "";
		}
		else if(action.getSubUrType().equals("PI")) {
			ip=(action.getPhyInterface() != null) ? action.getPhyInterface().getIpNode() : "";
		}
		else if(action.getSubUrType().equals("IG")) {
			ip=(action.getGateway() != null) ? action.getGateway().getIpAddress() : "";
		}
		else if(action.getSubUrType().equals("RT")) {
			ip=(action.getRouting() != null) ? action.getRouting().getIpAddress() : "";
		}
		else if(action.getSubUrType().equals("F5")) {
			ip=(action.getF5() != null) ? action.getF5().getVipAddress() : "";
		}
		return ip;
	}

	@Override
	public void managerRejectUr(String urNo, IpUser user, String remark) throws IPFMBusinessException{
		Date now = new Date();
		List<IpUrAction> actionList = this.listActionByStatus(urNo,
				IPFMConstant.STATUS_WAIT_MRG);
		try {
			IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_MRG);
			IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_MRG);
		for (IpUrAction action : actionList) {
			// Move Action => into Action History 
			IpUrActionHistory previous = copyHistory(action);
			previous.setUserRemark("");
			previous.setOverOla(null);
			previous.setOverSla(null);
			ipUrActionHistoryDao.copyHistory(previous);
			// Insert New Record to History
			int  actionSeq = Integer.parseInt(action.getActionSeq().toString())+ 1;
			IpUrActionHistory next = copyHistory(action);
			next.setActionSeq(new BigDecimal(actionSeq));
			next.setReqestUserId(action.getReqestUserId());
			next.setReqestUser(action.getReqestUser());
			next.setActionName(ipUrStatus.getSubUrStatusName());
			next.setUrStatus(IPFMConstant.STATUS_REJECT_MRG);
			next.setActionUserId(user.getUserId());
			next.setActionUser(user.getUserName()+" ("+user.getTel()+")");
			next.setActionRemark("");
			next.setUserRemark(remark);
			if(action.getOlaDate()!=null){
				next.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			}
			if(action.getSlaDate()!=null){
				next.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			}
			next.setCreatedBy(user.getUserId());
			next.setCreated(now);
			next.setLastUpdBy(user.getUserId());
			next.setLastUpd(now);
			
			ipUrActionHistoryDao.insert(next);
			// Insert New Record to Action
			int  actionSeq2 = Integer.parseInt(action.getActionSeq().toString())+ 2;
			action.setOlaDate(null);
			action.setSlaDate(null);
			
			action.setActionUser(action.getReqestUser());
			action.setActionUserId(action.getReqestUserId());
			action.setActionSeq(new BigDecimal(actionSeq2));
			action.setActionName(ipUrStatus2.getSubUrStatusName());
			action.setUrStatus(IPFMConstant.STATUS_REJECT_MRG);
			action.setActionRemark(null);
			action.setUserRemark(remark);
			action.setCallFunctionId("F008");
			action.setCallMode(IPFMConstant.MODE_EDIT);
			action.setCreatedBy(user.getUserId());
			action.setCreated(now);
			action.setLastUpdBy(user.getUserId());
			action.setLastUpd(now);
			ipUrActionDao.update(action);
			
			this.updateTab(action.getUrNo(), user.getUserId(), IPFMConstant.STATUS_REJECT_MRG , action.getSubUrNo());

		}
		    IpUrAction  ipUrAction  = actionList.get(0);
			IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
			
			IpUrStatus ipUrStatus3  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_MRG);
			
			config.setUrStatusTxt(IPFMConstant.STATUS_REJECT_MRG);
			config.setUrStatusName(ipUrStatus3.getUrStatusName());
			config.setApproveManager(user.getUserName());
			config.setMrgRemark(remark);
			config.setLastUpdBy(user.getUserId());
			config.setLastUpd(now);
			if(ipUrAction.getSlaDate()!=null){
				config.setOverSla(isDateOverCurrent(ipUrAction.getSlaDate(), now));
			} else {
				config.setOverSla("N");
			}
			config.setMrgRemark(remark);
			ipUrNwConfigDao.update(config);
	
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException(ex);
		}
		
	}
	
	

	@Override
	public void deleteActionByRowId(String rowID) {
		ipUrActionDao.deleteByRowID(rowID);
		
	}

	@Override
	public void deleteByNA(String urNo) {
		ipUrActionDao.deleteNA(urNo);
		
	}

	@Override
	public void ownerApproveUr(String urNo, IpUser user, Map subUR) throws IPFMBusinessException{
		Date now = new Date();
		Date slaDate = null;
		//1.2
		List<String> subList = new ArrayList<String>();
		Map<String, String> subStatus = new HashMap<String, String>();
		List<IpUrAction> actionList = this.listActionByOwner(urNo,user.getUserId());
//		String remark = "";
//		String approve = "";
//		String reject = "";
//		String piType = "";
//		String trunkFlag = "";
//		String trunkNo = "";
//		HashMap<String, String> tmpMap = new HashMap<String, String>();
//		int index = 0;
//		boolean isApprove = false;
//		boolean isReject = false;
		try {
			for(IpUrAction action : actionList){
				Object subUrItem = subUR.get(action.getSubUrNo());
				//---------- For Trunk ------------
//				isApprove = false;
//				isReject = false;
//				if(subUrItem instanceof IpUrPhysicalInterface){
//					piType = getProperty(subUrItem,"piType");
//					trunkFlag = getProperty(subUrItem,"trunkFlag");
//					trunkNo = getProperty(subUrItem,"trunkNo");
//				}
//				if(StringUtils.equals("T", trunkFlag)){
//					remark = getProperty(subUrItem,"sysOwnerRemark");
//					approve = getProperty(subUrItem,"isApprove");
//					reject = getProperty(subUrItem,"isReject");
//					tmpMap.put(trunkNo+"_remark",remark);
//					tmpMap.put(trunkNo+"_approve",approve);
//					tmpMap.put(trunkNo+"_reject",reject);
//				}else if(StringUtils.equals("N", trunkFlag)){
//					remark = tmpMap.get(trunkNo+"_remark");
//					approve = tmpMap.get(trunkNo+"_approve");
//					reject = tmpMap.get(trunkNo+"_reject");
//				}
				//----------------------------------
				//1.3 Move Action => into Action History
				IpUrActionHistory history = copyHistory(action);
				if(action.getOlaDate()!=null){
					history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
				}
				if(action.getSlaDate()!=null){
					history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
				}
				ipUrActionHistoryDao.insert(history);
				// Insert New Record to History
				IpUrActionHistory newHistory = new IpUrActionHistory();
				newHistory = (IpUrActionHistory)history.clone();
				newHistory.setVersion(null);
				newHistory.setRowId(null);
				newHistory.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
				newHistory.setActionUserId(user.getUserId());
				newHistory.setActionUser(user.getUserName()+" ("+user.getTel()+")");
				if(action.getOlaDate()!=null){
					newHistory.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
				} else {
					newHistory.setOverOla("N");				
				}
				
				if(action.getSlaDate()!=null){
					newHistory.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
				} else {
					newHistory.setOverSla("N");				
				}
				//20101126 - ����������� User Remarks ��������ʴ�� History
				
				//Check For Trunk
//				if(StringUtils.equals(piType, "T")){
//					newHistory.setUserRemark(remark);
//				}else{
//					newHistory.setUserRemark(getProperty(subUrItem,"sysOwnerRemark"));
//				}
				newHistory.setUserRemark(getProperty(subUrItem,"sysOwnerRemark"));
				newHistory.setCreatedBy(user.getUserId());
				newHistory.setCreated(now);
				newHistory.setLastUpdBy(user.getUserId());
				newHistory.setLastUpd(now);
				if(getProperty(subUrItem,"isApprove").equals("Y")){
					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_APPROVE_OWNER);
					newHistory.setActionName(ipUrStatus.getSubUrStatusName());
					newHistory.setUrStatus(IPFMConstant.STATUS_APPROVE_OWNER);
				} else if(getProperty(subUrItem,"isApprove").equals("Y")){
					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_OWNER);
					newHistory.setActionName(ipUrStatus.getSubUrStatusName());
					newHistory.setUrStatus(IPFMConstant.STATUS_REJECT_OWNER);
				}
				//Check For Trunk
//				if(StringUtils.equals(piType, "T")){
//					if(StringUtils.equals("Y", approve)){
//						isApprove = true;
//					} else if(StringUtils.equals("Y", reject)){
//						isReject = true;
//					}
//				}else{
//					if(getProperty(subUrItem,"isApprove").equals("Y")){
//						isApprove = true;
//					}else if(getProperty(subUrItem,"isReject").equals("Y")){
//						isReject = true;
//					}
//				}
				
//				if(isApprove){
//					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_APPROVE_OWNER);
//					newHistory.setActionName(ipUrStatus.getSubUrStatusName());
//					newHistory.setUrStatus(IPFMConstant.STATUS_APPROVE_OWNER);
//				} else if(isReject){
//					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_OWNER);
//					newHistory.setActionName(ipUrStatus.getSubUrStatusName());
//					newHistory.setUrStatus(IPFMConstant.STATUS_REJECT_OWNER);
//				}
				
				
				ipUrActionHistoryDao.insert(newHistory);
				
				//1.4 Insert New Record to Action
				
	//			action.setActionUserId(action.getReqestUserId());
	//			action.setActionUser(action.getReqestUser());
				
				action.setActionUserId(user.getUserId());
				action.setActionUser(user.getUserName() + (IPFMUtils.ifBlank(user.getTel(), "").length()>0?(" (" +user.getTel()+ ") "): "" ));
				
				action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(),action.getSubUrNo()));
				//20101122 : ���� Case Reject ��� Approve ���ҧ����
				if(getProperty(subUrItem,"isApprove").equals("Y")){
					IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_APPROVE_OWNER);
					action.setActionName(ipUrStatus2.getSubUrStatusName());
					action.setUrStatus(IPFMConstant.STATUS_APPROVE_OWNER);
					subList.add(action.getSubUrNo());
				} else if(getProperty(subUrItem,"isReject").equals("Y")){
					IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_OWNER);
					action.setActionName(ipUrStatus2.getSubUrStatusName());
					action.setUrStatus(IPFMConstant.STATUS_REJECT_OWNER);
					subList.add(action.getSubUrNo());
				}
				
//				if(isApprove){
//					IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_APPROVE_OWNER);
//					action.setActionName(ipUrStatus2.getSubUrStatusName());
//					action.setUrStatus(IPFMConstant.STATUS_APPROVE_OWNER);
//				} else if(isReject){
//					IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_OWNER);
//					action.setActionName(ipUrStatus2.getSubUrStatusName());
//					action.setUrStatus(IPFMConstant.STATUS_REJECT_OWNER);
//				}
				
				action.setUserRemark(getProperty(subUrItem,"sysOwnerRemark"));
				action.setUserRemark("");
				action.setCreatedBy(user.getUserId());
				action.setCreated(now);
				action.setLastUpdBy(user.getUserId());
				action.setLastUpd(now);
				ipUrActionDao.update(action);
				
				if(action.getSubUrType().equals("FW")){
					IpUrFirewall firewall = action.getFirewall();
					firewall.setSysOwnerRemark(getProperty(subUrItem, "sysOwnerRemark"));
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						
						firewall.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
	//					firewall.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_APPROVE_OWNER);
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						firewall.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_REJECT_OWNER);
					}
					firewall.setLastUpdBy(user.getUserId());
					firewall.setLastUpd(now);
					firewallDao.update(firewall);
				} else if(action.getSubUrType().equals("AL")){
					IpUrAccessListCdn cdn = action.getAccessListCdn();
					cdn.setSysOwnerRemark(getProperty(subUrItem, "sysOwnerRemark"));
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						
						cdn.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_APPROVE_OWNER);
	//					cdn.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
						
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						cdn.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_REJECT_OWNER);
					}
					cdn.setLastUpdBy(user.getUserId());
					cdn.setLastUpd(now);
					accessListDao.update(cdn);
				} else if(action.getSubUrType().equals("AT")){
					IpUrAccessListTnp tnp = action.getAccessListTnp();
					tnp.setSysOwnerRemark(getProperty(subUrItem, "sysOwnerRemark"));
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						
						tnp.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_APPROVE_OWNER);
	//					tnp.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
						
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						tnp.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_REJECT_OWNER);
					}
					tnp.setLastUpdBy(user.getUserId());
					tnp.setLastUpd(now);
					accessListTNPDao.update(tnp);
				} else if(action.getSubUrType().equals("PI")){
					IpUrPhysicalInterface phyInterface = action.getPhyInterface();
//					if(StringUtils.equals(piType, "T")){
//						phyInterface.setSysOwnerRemark(remark);
//					}else{
						phyInterface.setSysOwnerRemark(getProperty(subUrItem, "sysOwnerRemark"));
//					}
					
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						
						phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_APPROVE_OWNER);
	//					phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
						
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_REJECT_OWNER);
					}
					
//					if(isApprove){
//						
//						phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
//	//					phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
//						
//					} else if(isReject){
//						phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
//					}
					phyInterface.setLastUpdBy(user.getUserId());
					phyInterface.setLastUpd(now);
					physicalInterfaceDao.update(phyInterface);
				} else if(action.getSubUrType().equals("IG")){
					IpUrInterfaceGateway gateway = action.getGateway();
					gateway.setSysOwnerRemark(getProperty(subUrItem, "sysOwnerRemark"));
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						
						gateway.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_APPROVE_OWNER);
	//					gateway.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
						
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						gateway.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_REJECT_OWNER);
					}
					gateway.setLastUpdBy(user.getUserId());
					gateway.setLastUpd(now);
					intfGatewayDao.update(gateway);
				} else if(action.getSubUrType().equals("RT")){
					IpUrRouting routing = action.getRouting();
					routing.setSysOwnerRemark(getProperty(subUrItem, "sysOwnerRemark"));
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						
						routing.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_APPROVE_OWNER);
	//					routing.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
						
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						routing.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_REJECT_OWNER);
					}
					routing.setLastUpdBy(user.getUserId());
					routing.setLastUpd(now);
					routingDao.update(routing);
				} else if(action.getSubUrType().equals("F5")){
					IpUrF5 f5 = action.getF5();
					f5.setSysOwnerRemark(getProperty(subUrItem, "sysOwnerRemark"));
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						
						f5.setSubUrStatusTxt(IPFMConstant.STATUS_APPROVE_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_APPROVE_OWNER);
	//					f5.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
						
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						f5.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_OWNER);
						subStatus.put(action.getSubUrNo(), IPFMConstant.STATUS_REJECT_OWNER);
					}
					f5.setLastUpdBy(user.getUserId());
					f5.setLastUpd(now);
					f5Dao.update(f5);	
				}
				
				
				slaDate=action.getSlaDate();
//				index++;
			}
			//1.5
			if(ipUrActionDao.isOwnerApproveAll(urNo)){		
				//1.6  
				if(!ipUrActionDao.isOwnerRejectAll(urNo)){
					IpUser reqUser = new IpUser();
					if (actionList!=null && actionList.size()>0) {
						IpUrAction action = actionList.get(0);					
						reqUser.setUserId(action.getReqestUserId());
						reqUser.setUserName(action.getReqestUser());
					}
					
					//1.6.2
					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_OWNER);
					
					ipUrActionDao.updateActionReject(urNo, IPFMConstant.STATUS_REJECT_OWNER, "F008", IPFMConstant.MODE_EDIT, user, reqUser);
					
					IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
					config.setUrStatusTxt(IPFMConstant.STATUS_REJECT_UR);
					config.setUrStatusName(ipUrStatus.getSubUrStatusName());
					config.setOverSla(isDateOverCurrent(slaDate, now));
					config.setLastUpdBy(user.getUserId());
					config.setLastUpd(now);
					ipUrNwConfigDao.update(config);
					
				} else {
					//1.6.3
					//ipUrActionDao.updateAction(urNo, IPFMConstant.STATUS_REJECT_UR, "F008", IPFMConstant.MODE_EDIT, user);
					// 20101118 -> actionList => get �� Where Owner �����Ũ֧�ѧ�Դ ����
					List<IpUrAction> actionList2 = ipUrActionDao.getURActionList(urNo);
					for(IpUrAction action : actionList2){
						String pmRoleId = "";
						String actionUserId = "";
						String actionUser = "";
						Date olaDate = null;
						if(action.getSubUrType().equals("FW") || action.getSubUrType().equals("AL") || action.getSubUrType().equals("F5")){
							pmRoleId="R07";
							List<IpRoleMember> pmUser = ipUserDao.getUserForOwnerApprove(pmRoleId);
							
							for(IpRoleMember member : pmUser){
								actionUserId+=(";"+member.getUserId()); 
								actionUser+=(";"+member.getIpUser().getUserName()+" (" +member.getIpUser().getTel()+")");
							}
							if(!actionUserId.equals("")){
								actionUserId=actionUserId.substring(1);
							}
							if(!actionUser.equals("")){
								actionUser=actionUser.substring(1);
							}
							olaDate=ipUrActionDao.getOLADate(new Date(),"NC", "N7");
						} else if(action.getSubUrType().equals("AT")){
							pmRoleId="R06";
							List<IpRoleMember> pmUser = ipUserDao.getUserForOwnerApprove(pmRoleId);
							for(IpRoleMember member : pmUser){
								actionUserId+=(";"+member.getUserId()); 
								actionUser+=(";"+member.getIpUser().getUserName()+" (" +member.getIpUser().getTel()+")");
							}
							if(!actionUserId.equals("")){
								actionUserId=actionUserId.substring(1);
							}
							if(!actionUser.equals("")){
								actionUser=actionUser.substring(1);
							}
							olaDate=ipUrActionDao.getOLADate(new Date(),"NC", "N6");
						} else if((action.getSubUrType().equals("PI")) || action.getSubUrType().equals("IG") || action.getSubUrType().equals("RT")){	
							String ipAddress = "";
							if(action.getSubUrType().equals("PI")){
								ipAddress = action.getPhyInterface().getIpNode();
							} else if(action.getSubUrType().equals("IG")){
								ipAddress = action.getGateway().getIpAddress();
							} else if(action.getSubUrType().equals("RT")){
								ipAddress = action.getRouting().getIpAddress();
							}
							IpInfo info = ipInfoDao.getIpInfo(ipAddress);
							pmRoleId=info.getT2Team().getPmRoleId();
							List<IpRoleMember> pmUser = ipUserDao.getUserForOwnerApprove(pmRoleId);
							for(IpRoleMember member : pmUser){
								actionUserId+=(";"+member.getUserId()); 
								actionUser+=(";"+member.getIpUser().getUserName()+" (" +member.getIpUser().getTel()+")");
							}
							if(!actionUserId.equals("")){
								actionUserId=actionUserId.substring(1);
							}
							if(!actionUser.equals("")){
								actionUser=actionUser.substring(1);
							}
							olaDate=ipUrActionDao.getOLADate(new Date(),"NC", (pmRoleId.equals("R6"))?"N6":"N7");
						} 
						IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
						action.setUrStatus(IPFMConstant.STATUS_WAIT_PM);
						action.setActionName(ipUrStatus2.getSubUrStatusName());
						action.setActionUserId(actionUserId);
						action.setActionUser(actionUser);
						action.setOlaDate(olaDate);
						action.setCallFunctionId("F034");
						action.setCallMode(IPFMConstant.MODE_CALL_PM);
						action.setLastUpdBy(user.getUserId());
						action.setLastUpd(now);
						ipUrActionDao.update(action);
						
						this.updateTab(action.getUrNo(), user.getUserId(), IPFMConstant.STATUS_WAIT_PM, action.getSubUrNo());
						
					}
					IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
					IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
					config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_CONFIG);
					config.setUrStatusName(ipUrStatus2.getUrStatusName());
					
					if(slaDate!=null){
						config.setOverSla(isDateOverCurrent(slaDate, now));
					} else {
						config.setOverSla("N");
					}
					
					config.setLastUpdBy(user.getUserId());
					config.setLastUpd(now);
					ipUrNwConfigDao.update(config);
				}
			}
			
			List<IpSubUrAttachment> subFileList = ipSubUrAttachmentDao.getIpUrAttachment(urNo, "temp", IPFMConstant.STATUS_WAIT_OWNER, user.getUserId());			
			if(subFileList != null && subFileList.size() > 0){
				for(IpSubUrAttachment data : subFileList){
					for(String sub : subList){
						if(sub != null && !sub.equals("")){
							IpSubUrAttachment subData = new IpSubUrAttachment();
							IpSubUrAttachmentId id = new IpSubUrAttachmentId();
							id.setUrNo(data.getId().getUrNo());
							id.setSubUrNo(sub);
							id.setFileName(data.getId().getFileName());
							id.setCategory(data.getId().getCategory());
							
							BigDecimal seqNo = ipSubUrAttachmentDao.getSeqNo(urNo, data.getId().getCategory());
							id.setSeq(seqNo);
							subData.setId(id);
							
							subData.setVersion(new Long(0));
							subData.setDescription(data.getDescription());
							subData.setUrStatus(IPFMConstant.STATUS_WAIT_OWNER);
							subData.setFileLocation(data.getFileLocation());
							subData.setCreated(now);
							subData.setCreatedBy(user.getUserId());
							subData.setLastUpd(now);
							subData.setLastUpdBy(user.getUserId());
							subData.setSubUrStatus(IPFMConstant.STATUS_WAIT_OWNER);
							subData.setAction(subStatus.get(sub));
							ipSubUrAttachmentDao.insert(subData);
							}
						}
						
					}
				}	
			ipSubUrAttachmentDao.deleteIpUrAttachmentSubmit(urNo, "temp", IPFMConstant.STATUS_WAIT_OWNER, user.getUserId());
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException();
		}
		
	}
	
	private String getProperty(Object obj, String name) throws Exception {
		String temp = "";
		try {
			Field FieldSysOwnerRemark = obj.getClass().getDeclaredField(name);
			FieldSysOwnerRemark.setAccessible(true);
			temp = (String) FieldSysOwnerRemark.get(obj);
			FieldSysOwnerRemark.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return temp;
	}

	private String getSubUrNo(Object obj, String name) {
		String temp = "";
		try{
			Field fieldId = obj.getClass().getDeclaredField("id");
			fieldId.setAccessible(true);
			Object idObj = fieldId.get(obj);
			Field fieldSubUrNO = idObj.getClass().getDeclaredField("subUrNo");
			fieldSubUrNO.setAccessible(true);
			temp = (String)fieldSubUrNO.get(idObj);
			fieldSubUrNO.setAccessible(false);
			fieldId.setAccessible(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	@Override
	public boolean isApproveAll(String urNo) {
		boolean done = false;
		if(ipUrActionDao.isOwnerApproveAll(urNo)){
			done =!(ipUrActionDao.isOwnerRejectAll(urNo));
		}
		return done;
	}
	
	@Override
	public boolean isOwnerApproveAll(String urNo) {
		return ipUrActionDao.isOwnerApproveAll(urNo);
	}
	
	@Override
	public boolean isOwnerRejectAll(String urNo) {
		return ipUrActionDao.isOwnerRejectAll(urNo);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void submitACTM(String urNo, IpUser user, Map subUR) throws IPFMBusinessException {
		Date now = new Date();
		Date slaDate = null;
		List<IpUrAction> actionList = this.listActionByACTM(urNo, user);
		String remarkTrunk = "";
		String piType = "";
		String trunkFlag = "";
		String trunkNo = "";
		HashMap<String, String> tmpMap = new HashMap<String, String>();
		int index = 0;
		try {
		for(IpUrAction action : actionList){
			Object subUrItem = subUR.get(action.getSubUrNo());
			String remark = getProperty(subUrItem,"actmRemark");
			if(subUrItem instanceof IpUrPhysicalInterface){
				piType = getProperty(subUrItem,"piType");
				trunkFlag = getProperty(subUrItem,"trunkFlag");
				trunkNo = getProperty(subUrItem,"trunkNo");
			}
			if(StringUtils.equals("T", piType) && StringUtils.equals("T", trunkFlag)){
				remarkTrunk = remark;
				tmpMap.put(trunkNo+"_actmRemark", remark);
			}else if(StringUtils.equals("T", piType) && StringUtils.equals("N", trunkFlag)){
				remarkTrunk = tmpMap.get(trunkNo+"_actmRemark");
			}
			
			IpUrActionHistory historyOld = null;
			try {
				historyOld = copyHistory((IpUrAction)action.clone());
				historyOld.setUserRemark("");
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				throw e;
			}
			historyOld.setRowId(commonDao.getROW_ID());
			if(action.getOlaDate()!=null){
			historyOld.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			}
			ipUrActionHistoryDao.insert(historyOld);
			
			IpUrActionHistory history = copyHistory(action);
			history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
			history.setActionUserId(user.getUserId());
			history.setActionUser(user.getUserName()+" ("+user.getTel()+")");
			if(StringUtils.equals("T", piType)){
				history.setUserRemark(remarkTrunk);
			}else{
				history.setUserRemark(remark);
			}
			if(action.getOlaDate()!=null){
			history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			}
			if(action.getOlaDate()!=null){
			history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			}
			history.setCreatedBy(user.getUserId());
			history.setCreated(now);
			history.setLastUpdBy(user.getUserId());
			history.setLastUpd(now);
			history.setRowId(commonDao.getROW_ID());
			IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_COMPLETE_ACTM);
			history.setActionName(ipUrStatus.getSubUrStatusName());
			history.setUrStatus(IPFMConstant.STATUS_COMPLETE_ACTM);
			ipUrActionHistoryDao.insert(history);
			
			if(action.getSubUrType().equals("FW")){
				IpUrFirewall firewall = action.getFirewall();
				firewall.setActmRemark(remark);
				firewall.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_TEAM);
				firewall.setLastUpdBy(user.getUserId());
				firewall.setLastUpd(now);
				firewallDao.update(firewall);
			} else if(action.getSubUrType().equals("AL")){
				IpUrAccessListCdn cdn = action.getAccessListCdn();
				cdn.setActmRemark(remark);
				cdn.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_TEAM);
				cdn.setLastUpdBy(user.getUserId());
				cdn.setLastUpd(now);
				accessListDao.update(cdn);
			} else if(action.getSubUrType().equals("AT")){
				IpUrAccessListTnp tnp = action.getAccessListTnp();
				tnp.setActmRemark(remark);
				tnp.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_TEAM);
				tnp.setLastUpdBy(user.getUserId());
				tnp.setLastUpd(now);
				accessListTNPDao.update(tnp);
			} else if(action.getSubUrType().equals("PI")){
				IpUrPhysicalInterface phyInterface = action.getPhyInterface();
				if(StringUtils.equals("T", piType)){
					phyInterface.setActmRemark(remarkTrunk);
				}else{
					phyInterface.setActmRemark(remark);
				}
				
				phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_TEAM);
				phyInterface.setLastUpdBy(user.getUserId());
				phyInterface.setLastUpd(now);
				physicalInterfaceDao.update(phyInterface);
			} else if(action.getSubUrType().equals("IG")){
				IpUrInterfaceGateway gateway = action.getGateway();
				gateway.setActmRemark(remark);
				gateway.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_TEAM);
				gateway.setLastUpdBy(user.getUserId());
				gateway.setLastUpd(now);
				intfGatewayDao.update(gateway);
			} else if(action.getSubUrType().equals("RT")){
				IpUrRouting routing = action.getRouting();
				routing.setActmRemark(remark);
				routing.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_TEAM);
				routing.setLastUpdBy(user.getUserId());
				routing.setLastUpd(now);
				routingDao.update(routing);
			} else if(action.getSubUrType().equals("F5")){
				IpUrF5 f5 = action.getF5();
				f5.setActmRemark(remark);
				f5.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_TEAM);
				f5.setLastUpdBy(user.getUserId());
				f5.setLastUpd(now);
				f5Dao.update(f5);	
			}
			//ipUrActionDao.delete(action);
			
			slaDate=action.getSlaDate();
			index++;
		}
		
		ipUrActionDao.deleteACTM(urNo, user);

		Set<String> userEmailList = new HashSet<String>();
		List<IpUrAction> teamList = this.listActionByACTMTeam(urNo,user);
		Map subUrNoMap = new HashMap();
		for(IpUrAction team : teamList){
			Object subUrItem = subUR.get(team.getSubUrNo());
			String remark = getProperty(subUrItem,"actmRemark");
			String actionUserId = team.getActionUserId();
			Date olaDate = null;
			IpUser ipUser = null;
			String strAction = "";
			boolean n9sendMail = ipUrActionService.isSendEmail("NC", "N9");
			boolean n10sendMail = ipUrActionService.isSendEmail("NC", "N10");
			boolean n11sendMail = ipUrActionService.isSendEmail("NC", "N11");
			boolean n12sendMail = ipUrActionService.isSendEmail("NC", "N12");
			boolean n13sendMail = ipUrActionService.isSendEmail("NC", "N13");
			boolean n14sendMail = ipUrActionService.isSendEmail("NC", "N14");
			boolean n15sendMail = ipUrActionService.isSendEmail("NC", "N15");
			boolean n16sendMail = ipUrActionService.isSendEmail("NC", "N16");
			boolean n17sendMail = ipUrActionService.isSendEmail("NC", "N17");
			int count = 0;
			for(String actionUser : actionUserId.split(";")){
				String subUrNoEmail = (String)subUrNoMap.get(actionUser);
				if (subUrNoEmail!=null && !subUrNoEmail.equalsIgnoreCase("")) {
					count++;
					if (count<6) {
						subUrNoEmail = subUrNoEmail+";"+team.getSubUrNo();
						subUrNoMap.put(actionUser, subUrNoEmail);
					}else if (count==6) {
						subUrNoMap.put(actionUser, subUrNoEmail+"... Sub Ur มีมากกว่า 5 รายการ");
					}
				}else{
					subUrNoMap.put(actionUser, team.getSubUrNo());
					count++;
				}
				Date olaDateN = null;
				if(ipUrActionDao.isInRole(actionUser, "R09")){
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N9");
					if (n9sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R13")){
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N17");
					if (n17sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R10") && team.getSubUrType().equals("FW")){
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N10");
					if (n10sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R11") && team.getSubUrType().equals("FW")){	
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N11");
					if (n11sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R12") && team.getSubUrType().equals("FW")){	
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N12");
					if (n12sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R14") && team.getSubUrType().equals("PI")){
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N13");
					if (n13sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R15") && team.getSubUrType().equals("IG")){
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N15");
					if (n15sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R16") && team.getSubUrType().equals("RT")){
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N14");
					if (n14sendMail) userEmailList.add(actionUser);
				} else if(ipUrActionDao.isInRole(actionUser, "R17") && team.getSubUrType().equals("F5")){	
					olaDateN=ipUrActionDao.getOLADate(new Date(),"NC", "N16");
					if (n16sendMail) userEmailList.add(actionUser);
				} else {
					olaDateN=olaDate;
				}
				olaDate=(olaDate!=null)?((olaDate.after(olaDateN))?olaDate:olaDateN):(olaDateN);
				
				ipUser = ipUserDao.getIPUser(actionUser);
				if (strAction.equals("")) strAction = ipUser.getUserName()+ipUser.getPhone();
				else strAction = strAction +","+ ipUser.getUserName()+ipUser.getPhone();
			}
			
			IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_TEAM);
			team.setUrStatus(IPFMConstant.STATUS_WAIT_TEAM);
			team.setActionName(ipUrStatus.getSubUrStatusName());
			team.setActionUserId(actionUserId);
			team.setActionUser(strAction);
			team.setUserRemark(remark);
//			team.setUserRemark("");
			team.setOlaDate(olaDate);
			team.setLastUpdBy(user.getUserId());
			team.setLastUpd(now);
			ipUrActionDao.update(team);
		}
		
		IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
		config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_CONFIG);
		config.setOverSla(isDateOverCurrent(slaDate, now));
		config.setLastUpdBy(user.getUserId());
		config.setLastUpd(now);
		ipUrNwConfigDao.update(config);
		
		for (String userEmail : userEmailList) {
			IpUser ipUser = ipUserDao.getIPUser(userEmail);
			EmailDetail email = new EmailDetail();
			email.setEmail(ipUser.getEmail());
			email.setUrNo(urNo);
			email.setSubUrNo((String)subUrNoMap.get(ipUser.getUserId()));
			email.setUserId(ipUser.getUserId());
			email.setUserName(ipUser.getUserName());
			email.setSubject(config.getReqSubject());
			email.setUrType("Request Network Config");
			email.setUrStatusDesc("Wait For Team Process");
			email.setUrStatus("WAIT_TEAM");
			email.setSubjectDesc("Wait For Team Process");
			email.setBodyDesc("Wait For Team Process");
			email.setRequestBy(config.getReqUserName());
			email.setRequestDate(config.getReqDate());
			String urNoEmail = email.getUrNo();				
//			String key = "IPFM" + urNoEmail + ":" + email.getUserId();
			String key = emailService.getKeyCodeEmail();
			String[] to = {""};
			if(ipUser != null) {
				to[0] = ipUser.getEmail();
			}
			String[] cc = null;
			String from = "ipfm@ais.co.th";				
			
			try {
				Map model = new HashMap();
				model.put("userName", email.getUserName());
				model.put("urNo", email.getUrNo());
				model.put("subUrNo", email.getSubUrNo());
				model.put("urType", email.getUrType());
				model.put("urStatus", email.getUrStatusDesc());
				model.put("subjectDesc", email.getSubjectDesc());
				model.put("bodyDesc", email.getBodyDesc());
				model.put("subject", email.getSubject());
				model.put("requestBy", email.getRequestBy());
				SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy",new Locale("TH", "th"));
		        model.put("requestDate", smf.format(email.getRequestDate()));
				String encodeUrl = new String(Base64.encodeBase64(key.getBytes()));
				model.put("url", user.getUrl()+ "/home/view.jsf?key=" + encodeUrl);

				emailService.sendMail(to, cc, from, EMailService.TEMPLATE_IPFM_NC_SUBUR, model);

				IpEmailNotify notify = new IpEmailNotify();
				notify.setUserId(email.getUserId());
				notify.setUrNo(email.getUrNo());
				notify.setUrStatus(email.getUrStatus());
				notify.setCreatedBy(user.getUserId());
				notify.setLastUpdBy(user.getUserId());
				notify.setEmailLinkStatus("N");
				notify.setEmailCode(key);
				ipUrActionService.saveEmailNotify(notify);
			}
			catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException();
		}
		
	}


	@Override
	public List<IpUrAction> listActionByACTM(String urNo, IpUser user) {
		return ipUrActionDao.listActionForActm(urNo, user);
	}

	@Override
	public List<IpUrAction> listActionByACTMTeam(String urNo, IpUser user) {
		return ipUrActionDao.listActionTeam(urNo, user);
	}

	@Override
	public boolean isSendMailACTM(String user) {
		return ipUrActionDao.isSendMailACTM(user);
	}
	
	
	public void teamProcessBK(String urNo, IpUser user, Map subUR) throws IPFMBusinessException{
		Date now = new Date();
		
		List<IpUrAction> actionList = this.listActionTeamProcess(urNo, user);
		try {
			for(IpUrAction action : actionList){
				String subUrStatus = "";
				Object subUrItem = subUR.get(action.getSubUrNo());
				if(subUrItem != null){
					String remark = getProperty(subUrItem,"sysOwnerRemark");
					
					IpUrActionHistory history0 = copyHistory(action);
					history0.setLastUpd(now);
					history0.setLastUpdBy(user.getUserId());
					history0.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
					history0.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
					ipUrActionHistoryDao.insert(history0);
					
					IpUrActionHistory history = copyHistory(action);
					history.setUserRemark(remark);
					history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
					history.setActionUserId(user.getUserId());
					history.setActionUser(user.getUserName());
					history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
					history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
					history.setCreatedBy(user.getUserId());
					history.setCreated(now);
					history.setLastUpdBy(user.getUserId());
					history.setLastUpd(now);
					
					if(getProperty(subUrItem,"isApprove").equals("Y")){
						IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_COMPLETE_TEAM);
						history.setActionName(ipUrStatus.getSubUrStatusName());
						history.setUrStatus(IPFMConstant.STATUS_COMPLETE_TEAM);
						subUrStatus = IPFMConstant.STATUS_COMPLETE_TEAM;
						action.setActionName(ipUrStatus.getSubUrStatusName());
						action.setUrStatus(IPFMConstant.STATUS_COMPLETE_TEAM);
					} else if(getProperty(subUrItem,"isReject").equals("Y")){
						IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_TEAM);
						history.setActionName(ipUrStatus.getSubUrStatusName());
						history.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM);
						subUrStatus = IPFMConstant.STATUS_REJECT_TEAM;
						action.setActionName(ipUrStatus.getSubUrStatusName());
						action.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM);
					}
					ipUrActionHistoryDao.insert(history);
					
					IpUrActionHistory pmHistory = ipUrActionHistoryDao.getPMAssign(urNo, action.getUrType(), action.getSubUrNo(), action.getSubUrType()); 
					action.setUserRemark(remark);
					action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(),action.getSubUrNo()));
					action.setCallFunctionId("F039");
					action.setCallMode(IPFMConstant.MODE_USER);
					action.setActionUser(pmHistory.getActionUser());
					action.setActionUserId(pmHistory.getActionUserId());
					action.setLastUpdBy(user.getUserId());
					action.setLastUpd(now);
					ipUrActionDao.update(action);
					
					if(action.getSubUrType().equals("FW")){
						IpUrFirewall firewall = action.getFirewall();
						firewall.setSubUrStatusTxt(subUrStatus);
						firewall.setLastUpdBy(user.getUserId());
						firewall.setLastUpd(now);
						firewallDao.update(firewall);
					} else if(action.getSubUrType().equals("AL")){
						IpUrAccessListCdn cdn = action.getAccessListCdn();
						cdn.setSubUrStatusTxt(subUrStatus);
						cdn.setLastUpdBy(user.getUserId());
						cdn.setLastUpd(now);
						accessListDao.update(cdn);
					} else if(action.getSubUrType().equals("AT")){
						IpUrAccessListTnp tnp = action.getAccessListTnp();
						tnp.setSubUrStatusTxt(subUrStatus);
						tnp.setLastUpdBy(user.getUserId());
						tnp.setLastUpd(now);
						accessListTNPDao.update(tnp);
					} else if(action.getSubUrType().equals("PI")){
						IpUrPhysicalInterface phyInterface = action.getPhyInterface();
						phyInterface.setSubUrStatusTxt(subUrStatus);
						phyInterface.setLastUpdBy(user.getUserId());
						phyInterface.setLastUpd(now);
						
						String networkRackName = getProperty(subUrItem, "networkRackName");
						String networkDevice = getProperty(subUrItem, "networkDevice");
						String networkPort = getProperty(subUrItem, "networkPort");
						String networkLocationDesc = getProperty(subUrItem, "networkLocationDesc");
						String networkFloor = getProperty(subUrItem, "networkFloor");
		
						phyInterface.setNetworkRackName(networkRackName);
						phyInterface.setNetworkDevice(networkDevice);
						phyInterface.setNetworkPort(networkPort);
						phyInterface.setNetworkLocationDesc(networkLocationDesc);
						phyInterface.setNetworkFloor(networkFloor);
						
						physicalInterfaceDao.update(phyInterface);
					} else if(action.getSubUrType().equals("IG")){
						IpUrInterfaceGateway gateway = action.getGateway();
						gateway.setSubUrStatusTxt(subUrStatus);
						gateway.setLastUpdBy(user.getUserId());
						gateway.setLastUpd(now);
						intfGatewayDao.update(gateway);
					} else if(action.getSubUrType().equals("RT")){
						IpUrRouting routing = action.getRouting();
						routing.setSubUrStatusTxt(subUrStatus);
						routing.setLastUpdBy(user.getUserId());
						routing.setLastUpd(now);
						routingDao.update(routing);
					} else if(action.getSubUrType().equals("F5")){
						IpUrF5 f5 = action.getF5();
						f5.setSubUrStatusTxt(subUrStatus);
						f5.setLastUpdBy(user.getUserId());
						f5.setLastUpd(now);
						f5Dao.update(f5);	
					}
				}				
			}
			
			//List ipActionByWaitTeam = ipUrActionDao.findActionStatus(action, IPFMConstant.STATUS_WAIT_TEAM);
			String [] status = {IPFMConstant.STATUS_WAIT_ACTM,IPFMConstant.STATUS_ACTM_TEAM,IPFMConstant.STATUS_WAIT_TEAM, IPFMConstant.STATUS_WAIT_PM};

			List<IpUrAction> ipActionByWaitTeam = ipUrActionDao.findActionByURAndStatus(urNo, status);
			if(ipActionByWaitTeam.size() == 0){
				List<IpUrAction> rejectList = ipUrActionDao.listActionByStatus(urNo, IPFMConstant.STATUS_REJECT_TEAM);
				boolean isRejectFound = false;
				if(rejectList.size() > 0) {
					IpUrStatus rejectStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_TEAM);
					for(IpUrAction rejectAction : rejectList) {
						rejectAction.setActionName(rejectStatus.getSubUrStatusName());
						rejectAction.setUrStatus(IPFMConstant.STATUS_WAIT_PM);
						isRejectFound = true;
						rejectAction.setLastUpd(now);
						rejectAction.setLastUpdBy(user.getUserId());
						rejectAction.setCallFunctionId("F034");
						rejectAction.setCallMode("PM");
						ipUrActionDao.update(rejectAction);
					}
					ipEmailNotify.deleteByUser(urNo, user.getUserId(), IPFMConstant.STATUS_WAIT_TEAM);
				} 
				List<IpUrAction> completeList = ipUrActionDao.listActionByStatus(urNo, IPFMConstant.STATUS_COMPLETE_TEAM);
				IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
				IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_USER);
				if(completeList.size() > 0) {
					for(IpUrAction completeAction : completeList) {
						if(isRejectFound) {
							completeAction.setUrStatus(IPFMConstant.STATUS_WAIT_PM);
							completeAction.setActionName(ipUrStatus.getSubUrStatusName());
							completeAction.setCallFunctionId("F034");
							completeAction.setCallMode("PM");
						} else {
							completeAction.setActionUserId(completeAction.getReqestUserId());
							completeAction.setActionUser(completeAction.getReqestUser());
							completeAction.setUrStatus(IPFMConstant.STATUS_WAIT_USER);
							completeAction.setActionName(ipUrStatus2.getSubUrStatusName());
							completeAction.setCallFunctionId("F039");
							completeAction.setCallMode("USER");							
						}
						completeAction.setLastUpd(now);
						completeAction.setLastUpdBy(user.getUserId());
						
						ipUrActionDao.update(completeAction);
					}
				}
			
				
			}
			
//		
//		List<IpUrAction> waitList = ipUrActionDao.listActionByStatus(urNo, IPFMConstant.STATUS_WAIT_TEAM);
//		List<IpUrAction> rejectList = ipUrActionDao.listActionByStatus(urNo, IPFMConstant.STATUS_REJECT_TEAM);
//		
//		if((waitList.size()>0) || (rejectList.size()>0)){
//			ipEmailNotify.deleteByUser(urNo, user.getUserId(), IPFMConstant.STATUS_WAIT_TEAM);
//		}
//		
//		if((waitList.size()==0) && (rejectList.size()==0)){ 
//			Date slaDate = null;
//			List<IpUrAction> listComplete = ipUrActionDao.getURActionList(urNo);
//			IpUrAction tempAction = new IpUrAction();
//			for(IpUrAction ac : listComplete){
//				IpUrActionHistory history = copyHistory(ac);
//				ipUrActionHistoryDao.insert(history);
//				tempAction = ac;
//			}
//			slaDate = tempAction.getSlaDate();
//			tempAction.setActionName("User Verify");
//			tempAction.setUrStatus(IPFMConstant.STATUS_WAIT_USER);
//			tempAction.setOlaDate(null);
//			tempAction.setLastUpdBy(user.getUserId());
//			tempAction.setLastUpd(now);
//			ipUrActionDao.insert(tempAction);
//
//			IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
//			config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_USER);
//			config.setOverSla((slaDate.after(now) && slaDate!=null)?"N":"Y" );
//			config.setLastUpdBy(user.getUserId());
//			ipUrNwConfigDao.update(config);
//			
//			ipUrActionDao.deleteByUrNo(urNo);
//		}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException();
		}
		
	}
	
	private void preparePMAssign(IpUrAction action, Date now) throws Exception{
		IpUrActionHistory pmHistory = ipUrActionHistoryDao.getPMAssign(action.getUrNo(), action.getUrType(), action.getSubUrNo(), action.getSubUrType()); 
		if (pmHistory != null) {
			action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(), action.getSubUrNo()));
			String pmActionId = pmHistory.getActionUserId().replaceAll(";", "");
			List<IpRoleMember> roleMember = ipRoleMemberDao.findIpRoleMembersByUserId(pmActionId);
			String roleId = "";
			for (IpRoleMember member : roleMember) {
				if (member.getRoleId().equalsIgnoreCase("R06")) {
					if (!roleId.contains("R06")) roleId = roleId+"R06";
					else roleId="R06";
				}
				if (member.getRoleId().equalsIgnoreCase("R07")) {
					if (!roleId.contains("R07")) roleId = roleId+"R07";
					else roleId="R07";
				}
				if (roleId.contains("R07") && roleId.contains("R06")) break;
			}
			if (roleId.equalsIgnoreCase("R06")){
				action.setOlaDate(this.getOLADate(now, "NC", "N6"));
			}else if (roleId.equalsIgnoreCase("R07")){
				action.setOlaDate(this.getOLADate(now, "NC", "N7"));
			}else{
				Date tnpOlaDate = this.getOLADate(now, "NC", "N6");
				Date cdnOlaDate = this.getOLADate(now, "NC", "N7");
				if (tnpOlaDate.compareTo(cdnOlaDate)>0)
					action.setOlaDate(tnpOlaDate);
				else
					action.setOlaDate(cdnOlaDate);
			}
			
//			IpUser ipUser = ipUserDao.getIPUser(pmHistory.getActionUserId());
//			String roleId = ipTeamDao.getPmRoleId(ipUser.getTeamId());
//			if (roleId.equalsIgnoreCase("R07")) {
//				action.setOlaDate(this.getOLADate(now, "NC", "N7"));
//			}else{
//				action.setOlaDate(this.getOLADate(now, "NC", "N6"));
//			}
			
			action.setActionUser(pmHistory.getActionUser());
			action.setActionUserId(pmHistory.getActionUserId());
		}
	}
	
	@Override
	public void teamProcess(String urNo, IpUser user, Map subUR) throws IPFMBusinessException{
		Date now = new Date();
		List<IpUrAction> actionTeamList = this.listActionTeamProcess(urNo, user);
		
		Map remarkMap = new HashMap();
		Map actionUserNameMap = new HashMap();
		Map actionDateMap = new HashMap();
		String approve = "";
		String reject = "";
		String remarkTrunk = "";
		int index = 0;
		String networkRackName = "";
		String networkDevice = "";
		String networkPort = "";
		String networkLocationDesc = "";
		String networkFloor = "";
		try {
			IpUrStatus rejectStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_TEAM);
			IpUrStatus completeStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_COMPLETE_TEAM);
			IpUser reqUser = null;
			String piType = "";
			String trunkNo = "";
			String trunkFlag = "";
			HashMap<String, String> tmpMap = new HashMap<String, String>();
			for (IpUrAction action : actionTeamList) {
				Object subUrItem = subUR.get(action.getSubUrNo());
				if(subUrItem != null){
					if(subUrItem instanceof IpUrPhysicalInterface){
						piType = getProperty(subUrItem,"piType");
						trunkNo = getProperty(subUrItem,"trunkNo");
						trunkFlag = getProperty(subUrItem,"trunkFlag");
					
						if(StringUtils.equals("T", trunkFlag)){
							approve = getProperty(subUrItem,"isApprove");
							reject = getProperty(subUrItem,"isReject");
							remarkTrunk = getProperty(subUrItem,"teamRemark");
							networkRackName = getProperty(subUrItem, "networkRackName");
							networkDevice = getProperty(subUrItem, "networkDevice");
							networkPort = getProperty(subUrItem, "networkPort");
							networkLocationDesc = getProperty(subUrItem, "networkLocationDesc");
							networkFloor = getProperty(subUrItem, "networkFloor");
							tmpMap.put(trunkNo+"_approve", approve);
							tmpMap.put(trunkNo+"_reject", reject);
							tmpMap.put(trunkNo+"_remarkTrunk", remarkTrunk);
							tmpMap.put(trunkNo+"_networkRackName", networkRackName);
							tmpMap.put(trunkNo+"_networkDevice", networkDevice);
							tmpMap.put(trunkNo+"_networkPort", networkPort);
							tmpMap.put(trunkNo+"_networkLocationDesc", networkLocationDesc);
							tmpMap.put(trunkNo+"_networkFloor", networkFloor);
						}else{
							approve = tmpMap.get(trunkNo+"_approve");
							reject = tmpMap.get(trunkNo+"_reject");
							remarkTrunk = tmpMap.get(trunkNo+"_remarkTrunk");
							networkRackName = tmpMap.get(trunkNo+"_networkRackName");
							networkDevice = tmpMap.get(trunkNo+"_networkDevice");
							networkPort = tmpMap.get(trunkNo+"_networkPort");
							networkLocationDesc = tmpMap.get(trunkNo+"_networkLocationDesc");
							networkFloor = tmpMap.get(trunkNo+"_networkFloor");
						}
					}
					if(!StringUtils.equals("T", piType)){
						approve = getProperty(subUrItem,"isApprove");
						reject = getProperty(subUrItem,"isReject");
						
					}
//					if ((getProperty(subUrItem,"isApprove") == null || getProperty(subUrItem,"isApprove").trim().length()==0) 
//							 && (getProperty(subUrItem,"isReject") == null || getProperty(subUrItem,"isReject").trim().length()==0)) continue;
					
					if ((approve == null || approve.trim().length()==0) 
							 && (reject == null || reject.trim().length()==0)) continue;
					String subUrStatus = "";
					int subUrCount = 0;
					List<IpUrAction> ipUrActions = ipUrActionDao.findUrActions(action.getUrNo(), action.getSubUrNo());
					if (reject.equals("Y")) {
						String remark = getProperty(subUrItem,"teamRemark");
						if(StringUtils.equals("T", piType)){
							remark = remarkTrunk;
						}
						IpUrActionHistory history0 = copyHistory(action);
						history0.setLastUpd(now);
						history0.setLastUpdBy(user.getUserId());
						history0.setUserRemark("");
						history0.setActionRemark(IPFMConstant.REPORT_COMPLETE);
						history0.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
						history0.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
						ipUrActionHistoryDao.insert(history0);
						
						IpUrActionHistory history = copyHistory(action);
						history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
						history.setActionUserId(user.getUserId());
						String tel1="";
						if (user.getTel()!=null) 
							tel1 = "("+user.getTel()+")";
						else if (user.getMobile() != null) 
							tel1 = "("+user.getMobile()+")";
						history.setActionUser(user.getUserName() + tel1);
						history.setActionRemark(IPFMConstant.REPORT_COMPLETE);
						history.setUserRemark(remark);
						history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
						history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
						history.setCreatedBy(user.getUserId());
						history.setCreated(now);
						history.setLastUpdBy(user.getUserId());
						history.setLastUpd(now);
						
						
						history.setActionName(rejectStatus.getSubUrStatusName());
						history.setUrStatus(rejectStatus.getSubUrStatusId());
						subUrStatus = rejectStatus.getSubUrStatusId();
						action.setActionName(rejectStatus.getSubUrStatusName());
						action.setUrStatus(subUrStatus);
						action.setUserRemark(remark);
						action.setCallMode(IPFMConstant.MODE_REJECT_TEAM);
						action.setCallFunctionId("F034");
						
						if (ipUrActions!=null && ipUrActions.size()>1) {
							history.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM_WAIT);
							action.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM_WAIT);
						}
						
						ipUrActionHistoryDao.insert(history);
						
						preparePMAssign(action, now);
						
						action.setLastUpdBy(user.getUserId());
						action.setLastUpd(now);
						ipUrActionDao.update(action);
//						ipUrActionDao.deleteBySubUrNo(action.getUrNo(),action.getSubUrNo(),IPFMConstant.STATUS_WAIT_TEAM);
						if (ipUrActions!=null && ipUrActions.size()>1) {
							String subRemark = "";
							String actionDate = "";
							String rejectUser = "";
							for (IpUrAction act : ipUrActions) {
								subRemark = (String)remarkMap.get(act.getSubUrNo());
								if (subRemark==null) subRemark = ""; 
								actionDate = (String)actionDateMap.get(act.getSubUrNo());
								if (actionDate==null) actionDate = ""; 
								rejectUser = (String)actionUserNameMap.get(act.getSubUrNo());
								if (rejectUser==null) rejectUser = ""; 
								
								if (action.getRowId().equals(act.getRowId())) {
									subUrCount++;
								}else if (act.getUrStatus().equals(IPFMConstant.STATUS_COMPLETE_TEAM_WAIT)
										|| act.getUrStatus().equals(IPFMConstant.STATUS_REJECT_TEAM_WAIT)) {
									
									if (subRemark==null || subRemark.trim().length()==0) subRemark = act.getUserRemark();
									else if (act.getUserRemark()!=null && act.getUserRemark().trim().length()>0) subRemark = subRemark + ", " + act.getUserRemark();
									if (actionDate==null || actionDate.trim().length()==0) actionDate = act.getLastUpdDes();
									else actionDate = actionDate + ", " + act.getLastUpdDes();
									if (rejectUser==null || rejectUser.trim().length()==0) rejectUser = act.getActionUser();
									else rejectUser = rejectUser + ", " + act.getUserRemark();
									
									if (!act.getUrStatus().equals(IPFMConstant.STATUS_COMPLETE_TEAM_WAIT)) {
										act.setSlaDate(action.getSlaDate());
										act.setOlaDate(action.getOlaDate());
										act.setActionUserId(action.getActionUserId());
										act.setActionUser(action.getActionUser());
										act.setCallMode(IPFMConstant.MODE_REJECT_TEAM);
										act.setCallFunctionId("F034");
										ipUrActionDao.update(act);
									}
									subUrCount++;
								}
								remarkMap.put(act.getSubUrNo(), subRemark);
								actionDateMap.put(act.getSubUrNo(), actionDate);
								actionUserNameMap.put(act.getSubUrNo(), rejectUser);
							}
							subRemark = (String)remarkMap.get(action.getSubUrNo());
							if (subRemark==null || subRemark.trim().length()==0) subRemark = remark;
							else if (remark!=null && remark.trim().length()>0) subRemark = subRemark + ", " + remark;
							
							actionDate = (String)actionDateMap.get(action.getSubUrNo());
							if (actionDate==null || actionDate.trim().length()==0) actionDate = action.getLastUpdDes();
							else actionDate = actionDate + ", " + action.getLastUpdDes();
							
							rejectUser = (String)actionUserNameMap.get(action.getSubUrNo());
							if (rejectUser==null || rejectUser.trim().length()==0) rejectUser = user.getUserName();
							else rejectUser = rejectUser + ", " + user.getUserName();
							
							remarkMap.put(action.getSubUrNo(), subRemark);
							actionDateMap.put(action.getSubUrNo(), actionDate);
							actionUserNameMap.put(action.getSubUrNo(), rejectUser);
							
							
							if (ipUrActions.size()==subUrCount) {
								for (IpUrAction act : ipUrActions) {
									if (action.getRowId().equals(act.getRowId())) {
										action.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM);
										action.setUserRemark((String)remarkMap.get(action.getSubUrNo()));
										
										ipUrActionDao.update(action);
									}else{
										ipUrActionDao.deleteByRowID(act.getRowId());
									}
								}
								if(StringUtils.equals("T", piType)){
									prepareSubUrForTrunk(action , user, now, subUrItem, subUrStatus, networkRackName, networkDevice, networkPort, networkLocationDesc, networkFloor);
								}else{
									prepareSubUr(action , user, now, subUrItem, subUrStatus);
								}
							}
						}else{
							if(StringUtils.equals("T", piType)){
								prepareSubUrForTrunk(action , user, now, subUrItem, subUrStatus, networkRackName, networkDevice, networkPort, networkLocationDesc, networkFloor);
							}else{
								prepareSubUr(action , user, now, subUrItem, subUrStatus);
							}
						}
					}else if (approve.equals("Y")) {
						if (reqUser == null) {
							reqUser = ipUserDao.getIPUser(action.getReqestUserId());
						}
						String remark = getProperty(subUrItem,"teamRemark");
						if(StringUtils.equals("T", piType)){
							remark = remarkTrunk;
						}
						IpUrActionHistory history0 = copyHistory(action);
						history0.setUserRemark("");
						history0.setLastUpd(now);
						history0.setLastUpdBy(user.getUserId());
						history0.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
						history0.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
						ipUrActionHistoryDao.insert(history0);
						
						IpUrActionHistory history = copyHistory(action);
						history.setUserRemark(remark);
						history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
						history.setActionUserId(user.getUserId());
						history.setActionUser(user.getUserName()+user.getPhone());
						history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
						history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
						history.setCreatedBy(user.getUserId());
						history.setCreated(now);
						history.setLastUpdBy(user.getUserId());
						history.setLastUpd(now);
						
						history.setActionName(completeStatus.getSubUrStatusName());
						history.setUrStatus(completeStatus.getSubUrStatusId());
						subUrStatus = completeStatus.getSubUrStatusId();
						action.setActionName(completeStatus.getSubUrStatusName());
						action.setUrStatus(subUrStatus);
						action.setCallMode(IPFMConstant.MODE_USER);
						action.setCallFunctionId("F039");
						
						if (ipUrActions!=null && ipUrActions.size()>1) {
							history.setUrStatus(IPFMConstant.STATUS_COMPLETE_TEAM_WAIT);
							action.setUrStatus(IPFMConstant.STATUS_COMPLETE_TEAM_WAIT);
						}
						
						ipUrActionHistoryDao.insert(history);
						
						action.setUserRemark(remark);
						action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(),action.getSubUrNo()));
						action.setActionUser(reqUser.getUserName()+reqUser.getPhone());
						action.setActionUserId(reqUser.getUserId());
						action.setLastUpd(now);
						action.setLastUpdBy(user.getUserId());
						ipUrActionDao.update(action);
						
						if (ipUrActions!=null && ipUrActions.size()>1) {
							boolean hasReject = false;
							int rejectCount = 0;
							int completeCount = 0;
							String subRemark = "";
							String actionDate = "";
							String rejectUser = "";
							for (IpUrAction act : ipUrActions) {
								
								subRemark = (String)remarkMap.get(act.getSubUrNo());
								if (subRemark==null) subRemark = ""; 
								actionDate = (String)actionDateMap.get(act.getSubUrNo());
								if (actionDate==null) actionDate = ""; 
								rejectUser = (String)actionUserNameMap.get(act.getSubUrNo());
								if (rejectUser==null) rejectUser = ""; 
								
								if (!act.getRowId().equals(action.getRowId())) {
									if (subRemark==null || subRemark.trim().length()==0) subRemark = act.getUserRemark();
									else if (act.getUserRemark()!=null && act.getUserRemark().trim().length()>0) subRemark = subRemark + ", " + act.getUserRemark();
									
									actionDate = (String)actionDateMap.get(action.getSubUrNo());
									if (actionDate==null || actionDate.trim().length()==0) actionDate = action.getLastUpdDes();
									else actionDate = actionDate + ", " + action.getLastUpdDes();
									
									rejectUser = (String)actionUserNameMap.get(action.getSubUrNo());
									if (rejectUser==null || rejectUser.trim().length()==0) rejectUser = user.getUserName();
									else rejectUser = rejectUser + ", " + user.getUserName();
								}
								if (act.getUrStatus().equals(IPFMConstant.STATUS_REJECT_TEAM_WAIT)) {
									hasReject = true;
									rejectCount++;
								}else if (act.getUrStatus().equals(IPFMConstant.STATUS_COMPLETE_TEAM_WAIT)) {
									completeCount++;
								}
								remarkMap.put(act.getSubUrNo(), subRemark);
								actionDateMap.put(act.getSubUrNo(), actionDate);
								actionUserNameMap.put(act.getSubUrNo(), rejectUser);
							}
							
							subRemark = (String)remarkMap.get(action.getSubUrNo());
							if (subRemark==null || subRemark.trim().length()==0) subRemark = remark;
							else if (remark!=null && remark.trim().length()>0) subRemark = subRemark + ", " + remark;
							remarkMap.put(action.getSubUrNo(), subRemark);
							actionDateMap.put(action.getSubUrNo(), actionDate);
							actionUserNameMap.put(action.getSubUrNo(), rejectUser);
							
							if (ipUrActions.size() != (rejectCount+completeCount)) {
								if (hasReject) {
//									preparePMAssign(action, now);
//									action.setCallMode(IPFMConstant.MODE_REJECT_TEAM);
//									action.setCallFunctionId("F034");
//									ipUrActionDao.update(action);
								}
							}else { 
								for (IpUrAction act : ipUrActions) {
									if (!act.getRowId().equals(action.getRowId())) {
										ipUrActionDao.deleteByRowID(act.getRowId());
									}else{
										if (hasReject) {
											subUrStatus = IPFMConstant.STATUS_REJECT_TEAM;
											preparePMAssign(action, now);
											action.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM);
											action.setUserRemark((String)remarkMap.get(act.getSubUrNo()));
											action.setActionName(rejectStatus.getSubUrStatusName());
											act.setCallMode(IPFMConstant.MODE_REJECT_TEAM);
											act.setCallFunctionId("F034");
											ipUrActionDao.update(action);
										}else{
											action.setUrStatus(IPFMConstant.STATUS_COMPLETE_TEAM);
											ipUrActionDao.update(action);
										}
									}
								}
								if(StringUtils.equals("T", piType)){
									prepareSubUrForTrunk(action , user, now, subUrItem, subUrStatus, networkRackName, networkDevice, networkPort, networkLocationDesc, networkFloor);
								}else{
									prepareSubUr(action , user, now, subUrItem, subUrStatus);
								}
								
							}
						}else{
							if(StringUtils.equals("T", piType)){
								prepareSubUrForTrunk(action , user, now, subUrItem, subUrStatus, networkRackName, networkDevice, networkPort, networkLocationDesc, networkFloor);
							}else{
								prepareSubUr(action , user, now, subUrItem, subUrStatus);
							}
						}
					}
					
					
					
					List<IpSubUrAttachment> subFileList = ipSubUrAttachmentDao.getIpUrAttachment(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());			
					if(subFileList != null && subFileList.size() > 0){
						for(IpSubUrAttachment data : subFileList){
									IpSubUrAttachment subData = new IpSubUrAttachment();
									IpSubUrAttachmentId id = new IpSubUrAttachmentId();
									id.setUrNo(data.getId().getUrNo());
									id.setSubUrNo(action.getSubUrNo());
									id.setFileName(data.getId().getFileName());
									id.setCategory(data.getId().getCategory());
									
									BigDecimal seqNo = ipSubUrAttachmentDao.getSeqNo(urNo, data.getId().getCategory());
									id.setSeq(seqNo);
									subData.setId(id);
									
									subData.setVersion(new Long(0));
									subData.setDescription(data.getDescription());
									subData.setUrStatus(IPFMConstant.STATUS_WAIT_CONFIG);
									subData.setFileLocation(data.getFileLocation());
									subData.setCreated(now);
									subData.setCreatedBy(user.getUserId());
									subData.setLastUpd(now);
									subData.setLastUpdBy(user.getUserId());
									subData.setSubUrStatus(IPFMConstant.STATUS_WAIT_TEAM);
									subData.setAction(action.getUrStatus());
									ipSubUrAttachmentDao.insert(subData);
							}
						}

				
				}
				index++;
			}
			
			ipSubUrAttachmentDao.deleteIpUrAttachmentSubmit(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());
			
			boolean completeFlag = true;
			List<IpUrAction> completeList = ipUrActionDao.listAction(urNo, null, null);
			for (IpUrAction completeAction : completeList) {
				if (!(completeAction.getUrStatus().equalsIgnoreCase(IPFMConstant.STATUS_COMPLETE_TEAM)
					|| completeAction.getUrStatus().equalsIgnoreCase(IPFMConstant.STATUS_COMPLETE_PM)	
				    )) {
					completeFlag = false; break;
				}
			}
			if (completeFlag) {
				String overSla = "N";
				IpUrStatus userVerifyStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_USER);
				for (IpUrAction completeAction : completeList) {
					completeAction.setActionUserId(reqUser.getUserId());
					completeAction.setActionUser(reqUser.getUserName()+reqUser.getPhone());
					completeAction.setUrStatus(IPFMConstant.STATUS_WAIT_USER);
					completeAction.setActionName(userVerifyStatus.getSubUrStatusName());
					completeAction.setCallFunctionId("F039");
					completeAction.setCallMode("USER");
					completeAction.setLastUpd(now);
					completeAction.setLastUpdBy(user.getUserId());
					if (overSla.equals("N")){
						overSla = isDateOverCurrent(completeAction.getSlaDate(), now);
					}
					ipUrActionDao.update(completeAction);					
				}
				IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
				IpUrStatus status = getUrStatus(config.getUrType(), IPFMConstant.STATUS_WAIT_USER);
				config.setUrStatusTxt(status.getUrStatusId());
				config.setUrStatusName(status.getUrStatusName());
				config.setOverSla(overSla);
				config.setLastUpdBy(user.getUserId());
				config.setLastUpd(now);
				ipUrNwConfigDao.update(config);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException();
		}
	}
	
	public void teamProcessBKRejectAllSub(String urNo, IpUser user, Map subUR) throws IPFMBusinessException{
		Date now = new Date();
		List<IpUrAction> actionTeamList = this.listActionTeamProcess(urNo, user);
		
		try {
			boolean hasReject = false;			
			for(IpUrAction action : actionTeamList){
				Object subUrItem = subUR.get(action.getSubUrNo());
				if(subUrItem != null){
					if(getProperty(subUrItem,"isReject") != null && getProperty(subUrItem,"isReject").equals("Y")){
						hasReject = true;
						break;
					}
				}
			}
			if (hasReject) {
				ipUrActionDao.deleteACTM(urNo, null);
				List<IpUrAction> actionList = ipUrActionDao.listAction(urNo, null, null);
				for(IpUrAction action : actionList){
					String subUrStatus = "";
					//Object subUrItem = subUR.get(action.getSubUrNo());
					IpUrActionHistory history0 = copyHistory(action);
					history0.setLastUpd(now);
					history0.setLastUpdBy(user.getUserId());
					history0.setActionRemark(IPFMConstant.REPORT_COMPLETE);
					history0.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
					history0.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
					ipUrActionHistoryDao.insert(history0);
					
					IpUrActionHistory history = copyHistory(action);
					history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
					history.setActionUserId(user.getUserId());
					history.setActionUser(user.getUserName());
					history.setActionRemark(IPFMConstant.REPORT_COMPLETE);
					history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
					history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
					history.setCreatedBy(user.getUserId());
					history.setCreated(now);
					history.setLastUpdBy(user.getUserId());
					history.setLastUpd(now);
					
					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_TEAM);
					history.setActionName(ipUrStatus.getSubUrStatusName());
					history.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM);
					subUrStatus = IPFMConstant.STATUS_REJECT_TEAM;
					action.setActionName(ipUrStatus.getSubUrStatusName());
					action.setUrStatus(IPFMConstant.STATUS_REJECT_TEAM);
					action.setCallMode(IPFMConstant.MODE_REJECT_TEAM);
					action.setCallFunctionId("F034");
					
					ipUrActionHistoryDao.insert(history);
					
					IpUrActionHistory pmHistory = ipUrActionHistoryDao.getPMAssign(urNo, action.getUrType(), action.getSubUrNo(), action.getSubUrType()); 
					if (pmHistory != null) {
						action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(), action.getSubUrNo()));
						action.setActionUser(pmHistory.getActionUser());
						action.setActionUserId(pmHistory.getActionUserId());
						action.setLastUpdBy(user.getUserId());
						action.setLastUpd(now);
						ipUrActionDao.update(action);
					}
					
					prepareSubUr(action , user, now, null, subUrStatus);
				}
			} else {
				for (IpUrAction action : actionTeamList) {
					String subUrStatus = "";
					Object subUrItem = subUR.get(action.getSubUrNo());
					if(subUrItem != null){
						String remark = getProperty(subUrItem,"sysOwnerRemark");
						
						IpUrActionHistory history0 = copyHistory(action);
						history0.setLastUpd(now);
						history0.setLastUpdBy(user.getUserId());
						history0.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
						history0.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
						ipUrActionHistoryDao.insert(history0);
						
						IpUrActionHistory history = copyHistory(action);
						history.setUserRemark(remark);
						history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
						history.setActionUserId(user.getUserId());
						history.setActionUser(user.getUserName());
						history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
						history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
						history.setCreatedBy(user.getUserId());
						history.setCreated(now);
						history.setLastUpdBy(user.getUserId());
						history.setLastUpd(now);
						
						if(getProperty(subUrItem,"isApprove").equals("Y")){
							IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_COMPLETE_TEAM);
							history.setActionName(ipUrStatus.getSubUrStatusName());
							history.setUrStatus(IPFMConstant.STATUS_COMPLETE_TEAM);
							subUrStatus = IPFMConstant.STATUS_COMPLETE_TEAM;
							action.setActionName(ipUrStatus.getSubUrStatusName());
							action.setUrStatus(IPFMConstant.STATUS_COMPLETE_TEAM);
							action.setCallMode(IPFMConstant.MODE_USER);
							action.setCallFunctionId("F039");
						}
						ipUrActionHistoryDao.insert(history);
						
						IpUrActionHistory pmHistory = ipUrActionHistoryDao.getPMAssign(urNo, action.getUrType(), action.getSubUrNo(), action.getSubUrType()); 
						action.setUserRemark(remark);
						action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(),action.getSubUrNo()));
						action.setActionUser(pmHistory.getActionUser());
						action.setActionUserId(pmHistory.getActionUserId());
						action.setLastUpdBy(user.getUserId());
						ipUrActionDao.update(action);
						
						prepareSubUr(action , user, now, subUrItem, subUrStatus);
					}
				}
			}
			boolean completeFlag = true;
			List<IpUrAction> completeList = ipUrActionDao.listAction(urNo, null, null);
			for (IpUrAction completeAction : completeList) {
				if (!completeAction.getUrStatus().equalsIgnoreCase(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					completeFlag = false; break;
				}
			}
			if (completeFlag) {
				String overSla = "N";
				IpUrStatus userVerifyStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_USER);
				for (IpUrAction completeAction : completeList) {
					completeAction.setActionUserId(completeAction.getReqestUserId());
					completeAction.setActionUser(completeAction.getReqestUser());
					completeAction.setUrStatus(IPFMConstant.STATUS_WAIT_USER);
					completeAction.setActionName(userVerifyStatus.getSubUrStatusName());
					completeAction.setCallFunctionId("F039");
					completeAction.setCallMode("USER");							
					completeAction.setLastUpd(now);
					completeAction.setLastUpdBy(user.getUserId());
					if (overSla.equals("N")){
						overSla = isDateOverCurrent(completeAction.getSlaDate(), now);
					}
					ipUrActionDao.update(completeAction);
				}
				IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
				config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_USER);
				config.setOverSla(overSla);
				config.setLastUpdBy(user.getUserId());
				config.setLastUpd(now);
				ipUrNwConfigDao.update(config);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException();
		}
	}
	public void prepareSubUr(IpUrAction action , IpUser user, Date now, Object subUrItem, String subUrStatus) throws Exception{
		if(action.getSubUrType().equals("FW")){
			IpUrFirewall firewall = action.getFirewall();
			firewall.setSubUrStatusTxt(subUrStatus);
			firewall.setLastUpdBy(user.getUserId());
			firewall.setLastUpd(now);
			firewallDao.update(firewall);
		} else if(action.getSubUrType().equals("AL")){
			IpUrAccessListCdn cdn = action.getAccessListCdn();
			cdn.setSubUrStatusTxt(subUrStatus);
			cdn.setLastUpdBy(user.getUserId());
			cdn.setLastUpd(now);
			accessListDao.update(cdn);
		} else if(action.getSubUrType().equals("AT")){
			IpUrAccessListTnp tnp = action.getAccessListTnp();
			tnp.setSubUrStatusTxt(subUrStatus);
			tnp.setLastUpdBy(user.getUserId());
			tnp.setLastUpd(now);
			accessListTNPDao.update(tnp);
		} else if(action.getSubUrType().equals("PI")){
			IpUrPhysicalInterface phyInterface = action.getPhyInterface();
			phyInterface.setSubUrStatusTxt(subUrStatus);
			phyInterface.setLastUpdBy(user.getUserId());
			phyInterface.setLastUpd(now);
			if (subUrItem != null) {
				String networkRackName = getProperty(subUrItem, "networkRackName");
				String networkDevice = getProperty(subUrItem, "networkDevice");
				String networkPort = getProperty(subUrItem, "networkPort");
				String networkLocationDesc = getProperty(subUrItem, "networkLocationDesc");
				String networkFloor = getProperty(subUrItem, "networkFloor");
		
				phyInterface.setNetworkRackName(networkRackName);
				phyInterface.setNetworkDevice(networkDevice);
				phyInterface.setNetworkPort(networkPort);
				phyInterface.setNetworkLocationDesc(networkLocationDesc);
				phyInterface.setNetworkFloor(networkFloor);
			}
			physicalInterfaceDao.update(phyInterface);
		} else if(action.getSubUrType().equals("IG")){
			IpUrInterfaceGateway gateway = action.getGateway();
			gateway.setSubUrStatusTxt(subUrStatus);
			gateway.setLastUpdBy(user.getUserId());
			gateway.setLastUpd(now);
			intfGatewayDao.update(gateway);
		} else if(action.getSubUrType().equals("RT")){
			IpUrRouting routing = action.getRouting();
			routing.setSubUrStatusTxt(subUrStatus);
			routing.setLastUpdBy(user.getUserId());
			routing.setLastUpd(now);
			routingDao.update(routing);
		} else if(action.getSubUrType().equals("F5")){
			IpUrF5 f5 = action.getF5();
			f5.setSubUrStatusTxt(subUrStatus);
			f5.setLastUpdBy(user.getUserId());
			f5.setLastUpd(now);
			f5Dao.update(f5);	
		}
	}
	
	public void prepareSubUrForTrunk(IpUrAction action , IpUser user, Date now, Object subUrItem, String subUrStatus, String networkRackName, String networkDevice, String networkPort, String networkLocationDesc, String networkFloor) throws Exception{
		if(action.getSubUrType().equals("FW")){
			IpUrFirewall firewall = action.getFirewall();
			firewall.setSubUrStatusTxt(subUrStatus);
			firewall.setLastUpdBy(user.getUserId());
			firewall.setLastUpd(now);
			firewallDao.update(firewall);
		} else if(action.getSubUrType().equals("AL")){
			IpUrAccessListCdn cdn = action.getAccessListCdn();
			cdn.setSubUrStatusTxt(subUrStatus);
			cdn.setLastUpdBy(user.getUserId());
			cdn.setLastUpd(now);
			accessListDao.update(cdn);
		} else if(action.getSubUrType().equals("AT")){
			IpUrAccessListTnp tnp = action.getAccessListTnp();
			tnp.setSubUrStatusTxt(subUrStatus);
			tnp.setLastUpdBy(user.getUserId());
			tnp.setLastUpd(now);
			accessListTNPDao.update(tnp);
		} else if(action.getSubUrType().equals("PI")){
			IpUrPhysicalInterface phyInterface = action.getPhyInterface();
			phyInterface.setSubUrStatusTxt(subUrStatus);
			phyInterface.setLastUpdBy(user.getUserId());
			phyInterface.setLastUpd(now);
			if (subUrItem != null) {
				phyInterface.setNetworkRackName(networkRackName);
				phyInterface.setNetworkDevice(networkDevice);
				phyInterface.setNetworkPort(networkPort);
				phyInterface.setNetworkLocationDesc(networkLocationDesc);
				phyInterface.setNetworkFloor(networkFloor);
			}
			physicalInterfaceDao.update(phyInterface);
		} else if(action.getSubUrType().equals("IG")){
			IpUrInterfaceGateway gateway = action.getGateway();
			gateway.setSubUrStatusTxt(subUrStatus);
			gateway.setLastUpdBy(user.getUserId());
			gateway.setLastUpd(now);
			intfGatewayDao.update(gateway);
		} else if(action.getSubUrType().equals("RT")){
			IpUrRouting routing = action.getRouting();
			routing.setSubUrStatusTxt(subUrStatus);
			routing.setLastUpdBy(user.getUserId());
			routing.setLastUpd(now);
			routingDao.update(routing);
		} else if(action.getSubUrType().equals("F5")){
			IpUrF5 f5 = action.getF5();
			f5.setSubUrStatusTxt(subUrStatus);
			f5.setLastUpdBy(user.getUserId());
			f5.setLastUpd(now);
			f5Dao.update(f5);	
		}
	}
	
	@Override
	public List<IpUrAction> listActionTeamProcess(String urNo, IpUser user) {
		return ipUrActionDao.listWaitTeamProcess(urNo, user);
	}

	@Override
	public void userVerify(String urNo, IpUser user) throws IPFMBusinessException {
		Date now = new Date();
		List<IpUrAction> actionList = ipUrActionDao.getURActionList(urNo);
		String subUrStatus="";
		String subUrStatusName="";
		try {
		IpUrStatus ipUrStatus = null;
		
		IpUrStatus ipUrStatusVerify  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_USER);
		
		for (IpUrAction action : actionList) {
			if(action.getUrStatus().equals(IPFMConstant.STATUS_COMPLETE_TEAM) || action.getUrStatus().equals(IPFMConstant.STATUS_COMPLETE_PM) || action.getUrStatus().equals(IPFMConstant.STATUS_WAIT_USER)){
				ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_USER_COMPLTE);
				subUrStatus =IPFMConstant.STATUS_USER_COMPLTE;
				subUrStatusName=ipUrStatus.getUrStatusName();
			}else{
				ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_USER_REJECT);
				subUrStatus =IPFMConstant.STATUS_USER_REJECT;
				subUrStatusName=ipUrStatus.getUrStatusName();
			}
			
			
			IpUrActionHistory history = copyHistory(action);
			history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
			history.setActionName(ipUrStatusVerify.getUrStatusName());
			history.setUrStatus(ipUrStatusVerify.getUrStatusId());
			history.setUserRemark("");
			history.setActionRemark(IPFMConstant.REPORT_COMPLETE);
			ipUrActionHistoryDao.insert(history);
		
			history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
			history.setActionUserId(user.getUserId());
			history.setActionUser(user.getUserName()+user.getPhone());
			history.setActionName(subUrStatusName);
			history.setUrStatus(subUrStatus);
			history.setActionRemark(null);
			history.setUserRemark(null);
			history.setOlaDate(null);
			history.setOverOla("N");
			if (action.getSlaDate() != null) {
				history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			} else {
				history.setOverSla("N");
			}
			history.setCreatedBy(user.getUserId());
			history.setCreated(now);
			history.setLastUpdBy(user.getUserId());
			history.setLastUpd(now);
			ipUrActionHistoryDao.insert(history);
			try{
				if(action.getSubUrType().equals("FW")){
					IpUrFirewallId id = new IpUrFirewallId(action.getUrNo(), action.getSubUrNo());
					firewallDao.updateSubUrStatus(id,subUrStatus,user);
				} else if(action.getSubUrType().equals("AL")){
					IpUrAccessListCdnId id = new IpUrAccessListCdnId(action.getUrNo(), action.getSubUrNo());
					accessListDao.updateSubUrStatus(id,subUrStatus,user);
				} else if(action.getSubUrType().equals("AT")){
					IpUrAccessListTnpId id = new IpUrAccessListTnpId(action.getUrNo(), action.getSubUrNo());
					accessListTNPDao.updateSubUrStatus(id,subUrStatus,user);
				} else if(action.getSubUrType().equals("PI")){
					IpUrPhysicalInterfaceId id = new IpUrPhysicalInterfaceId(action.getUrNo(), action.getSubUrNo());
					physicalInterfaceDao.updateSubUrStatus(id,subUrStatus,user);
				} else if(action.getSubUrType().equals("IG")){
					IpUrInterfaceGatewayId id = new IpUrInterfaceGatewayId(action.getUrNo(), action.getSubUrNo());
					intfGatewayDao.updateSubUrStatus(id,subUrStatus,user);
				} else if(action.getSubUrType().equals("RT")){
					IpUrRoutingId id = new IpUrRoutingId(action.getUrNo(), action.getSubUrNo());
					routingDao.updateSubUrStatus(id,subUrStatus,user);
				} else if(action.getSubUrType().equals("F5")){
					IpUrF5Id id = new IpUrF5Id(action.getUrNo(), action.getSubUrNo());
					f5Dao.updateSubUrStatus(id,subUrStatus,user);	
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new IPFMBusinessException(e);
			}
			
		}
		
		ipUrActionDao.deleteByUrNo(urNo);
		IpUrStatus urStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_USER_COMPLTE);
		IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
		config.setUrStatusTxt(urStatus.getUrStatusId());
		config.setUrStatusName(urStatus.getUrStatusName());
		config.setLastUpdBy(user.getUserId());
		config.setLastUpd(now);
		ipUrNwConfigDao.update(config);

		if(config.getFwSts().equals("Y")){
			for(IpUrFirewall subUr : config.getListFirewall()){
				if (subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_PM) 
						|| subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					addFirewall(subUr,config,user);
				}
			}
		}
		if(config.getAlSts().equals("Y")){
			for(IpUrAccessListCdn subUr : config.getListAccessListCdn()){
				if (subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_PM) 
						|| subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					addAccessListCDN(subUr,config,user);
				}
			}
		}
		if(config.getAtSts().equals("Y")){
			for(IpUrAccessListTnp subUr : config.getListAccessListTnp()){
				if (subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_PM) 
						|| subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					addAccessListTNP(subUr,config,user);
				}
			}
		}
		if(config.getPiSts().equals("Y")){
			for(IpUrPhysicalInterface subUr : config.getListPhyInterface()){
				if (subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_PM) 
						|| subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					addPhysicalInterface(subUr,config,user);
				}
			}
		}
		if(config.getIgSts().equals("Y")){
			for(IpUrInterfaceGateway subUr : config.getListIntfGateway()){
				if (subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_PM) 
						|| subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					addInterfaceGateway(subUr,config,user);
				}
			}
		}
		if(config.getRtSts().equals("Y")){
			for(IpUrRouting subUr : config.getListRouting()){
				if (subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_PM) 
						|| subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					addRouting(subUr,config,user);
				}
			}
		}
		if(config.getF5Sts().equals("Y")){
			Set<String> ipAddressTemp = new HashSet<String>();
			for(IpUrF5 subUr : config.getListF5()) {
				ipAddressTemp.add(subUr.getVipAddress()+"-"+subUr.getVipPort());
			}
			
			for(String ipAddress : ipAddressTemp) {
				String[] temp = ipAddress.split("-");
				String ipAddressStr = temp[0];
				String ipPortStr = temp[1];
				ipNetworkConfigDao.deleteNetWorkConfigByIpAddress(ipAddressStr, ipPortStr);
			}
			
			for(IpUrF5 subUr : config.getListF5()){
				if (subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_PM) 
						|| subUr.getSubUrStatusTxt().equals(IPFMConstant.STATUS_COMPLETE_TEAM)) {
					addF5(subUr,config,user);
				}
			}
		}
		
		ipEmailNotify.deleteNotibyByUrNo(urNo);
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
		
	}

	@Override
	public List<IpUrAction> listUserVarify(String urNo, IpUser user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addFirewall(IpUrFirewall fw ,IpUrNwConfig config,IpUser user) {
		
		//20101125 Update Create/Update Date => Time stamp
		IpNetworkConfig nwConfig = new IpNetworkConfig();
		nwConfig.setIpAddress(fw.getSourceIp());
		// source
		nwConfig.setSourceIp1(fw.getSourceIp1());
		nwConfig.setSourceIp2(fw.getSourceIp2());
		nwConfig.setSourceHostName(fw.getSourceHostName());
		nwConfig.setSourceEmail(fw.getSourceEmail());
		nwConfig.setSourceVip(fw.getSourceVip());
		nwConfig.setSourceNatIp(fw.getSourceNatIp());
		// destination
		nwConfig.setDestIp1(fw.getDestIp1());
		nwConfig.setDestIp2(fw.getDestIp2());
		nwConfig.setDestHostName(fw.getDestHostName());
		nwConfig.setDestEmail(fw.getDestEmail());
		nwConfig.setDestVip(fw.getDestVip());
		nwConfig.setDestNatIp(fw.getDestNatIp());
		// service
		nwConfig.setServiceStatus(fw.getServiceStatus());
		nwConfig.setServiceId(fw.getServiceId());
		nwConfig.setServiceName(fw.getServiceName());
		nwConfig.setServiceDesc(fw.getServiceDesc());
		nwConfig.setServiceTcpPort(fw.getServiceTcpPort());
		nwConfig.setServiceTcpStatus(fw.getServiceTcpStatus());
		nwConfig.setServiceUdpPort(fw.getServiceUdpPort());
		nwConfig.setServiceUdpStatus(fw.getServiceUdpStatus());
		nwConfig.setServiceOtherPort(fw.getServiceOtherPort());
		nwConfig.setServiceOtherStatus(fw.getServiceOtherStatus());
		// user request
		nwConfig.setUrNo(fw.getId().getUrNo());
		nwConfig.setSubUrNo(fw.getId().getSubUrNo());
		nwConfig.setReqUserId(config.getReqUserId());
		nwConfig.setReqUserName(config.getReqUserName());
		nwConfig.setReqFor(config.getReqFor());
		
		nwConfig.setCreatedBy(user.getUserId());
		nwConfig.setCreated(new Timestamp(new Date().getTime()));
		nwConfig.setLastUpdBy(user.getUserId());
		nwConfig.setLastUpd(new Timestamp(new Date().getTime()));
		
		ipNetworkConfigDao.insert(nwConfig);
		
	}
	
	public void addAccessListCDN(IpUrAccessListCdn al ,IpUrNwConfig config,IpUser user) {
		IpNetworkConfig nwConfig = new IpNetworkConfig();
		nwConfig.setIpAddress(al.getSourceIp());
		// source
		nwConfig.setSourceIp1(al.getSourceIp1());
		nwConfig.setSourceIp2(al.getSourceIp2());
		nwConfig.setSourceHostName(al.getSourceHostName());
		nwConfig.setSourceEmail(al.getSourceEmail());
		// destination
		nwConfig.setDestIp1(al.getDestIp1());
		nwConfig.setDestIp2(al.getDestIp2());
		nwConfig.setDestHostName(al.getDestHostName());
		nwConfig.setDestEmail(al.getDestEmail());
		// user request
		nwConfig.setUrNo(al.getId().getUrNo());
		nwConfig.setSubUrNo(al.getId().getSubUrNo());
		nwConfig.setReqUserId(config.getReqUserId());
		nwConfig.setReqUserName(config.getReqUserName());
		nwConfig.setReqFor(config.getReqFor());
		
		nwConfig.setCreatedBy(user.getUserId());
		nwConfig.setCreated(new Timestamp(new Date().getTime()));
		nwConfig.setLastUpdBy(user.getUserId());
		nwConfig.setLastUpd(new Timestamp(new Date().getTime()));
		
		ipNetworkConfigDao.insert(nwConfig);
	}
	
	public void addAccessListTNP(IpUrAccessListTnp at ,IpUrNwConfig config,IpUser user) {
		IpNetworkConfig nwConfig = new IpNetworkConfig();
		nwConfig.setIpAddress(at.getSourceIp());
		// source
		nwConfig.setSourceHostName(at.getSourceHostName());
		nwConfig.setSourceLocationName(at.getSourceLocationName());
		nwConfig.setSourceNetworkIp(at.getSourceNetworkIp());
		nwConfig.setSourceInterNode(at.getSourceInterNode());
		nwConfig.setSourceInterNw(at.getSourceInterNw());
		// destination
		nwConfig.setDestHostName(at.getDestHostName());
		nwConfig.setDestLocationName(at.getDestLocationName());
		nwConfig.setDestNetworkIp(at.getDestNetworkIp());
		nwConfig.setDestInterNode(at.getDestInterNode());
		nwConfig.setDestInterNw(at.getDestInterNw());
		// description
		nwConfig.setDescription(at.getDescription());
		nwConfig.setReqImpact(at.getReqImpact());
		
		// user request
		nwConfig.setUrNo(at.getId().getUrNo());
		nwConfig.setSubUrNo(at.getId().getSubUrNo());
		nwConfig.setReqUserId(config.getReqUserId());
		nwConfig.setReqUserName(config.getReqUserName());
		nwConfig.setReqFor(config.getReqFor());
		nwConfig.setCreatedBy(user.getUserId());
		nwConfig.setCreated(new Timestamp(new Date().getTime()));
		nwConfig.setLastUpdBy(user.getUserId());
		nwConfig.setLastUpd(new Timestamp(new Date().getTime()));
		
		ipNetworkConfigDao.insert(nwConfig);
	}
	
	public void addPhysicalInterface(IpUrPhysicalInterface pi ,IpUrNwConfig config,IpUser user) {
		IpNetworkConfig nwConfig = new IpNetworkConfig();
		nwConfig.setIpAddress(pi.getIpNode());
		nwConfig.setSourceHostName(pi.getHostName());
		nwConfig.setMacAddress(pi.getMacAddress());
		nwConfig.setSourceLocationName(pi.getLocationName());
		nwConfig.setFloor(pi.getFloor());
		nwConfig.setReqVlanId(pi.getVlanId());
		nwConfig.setInterfaceNodeDesc(pi.getInterfaceNodeDesc());
		nwConfig.setInterfaceDesc(pi.getInterfaceDesc());
		nwConfig.setPortStatusDesc(pi.getPortStatusDesc());
		nwConfig.setSpeedPortDesc(pi.getSpeedPortDesc());
		nwConfig.setEnclosure(pi.getEnclosure());
		nwConfig.setReqRemarks(pi.getReqRemarks());
		nwConfig.setNetworkDevice(pi.getNetworkDevice());
		nwConfig.setNetworkLocationDesc(pi.getNetworkLocationDesc());
		nwConfig.setNetworkFloor(pi.getNetworkFloor());
		nwConfig.setNetworkPort(pi.getNetworkPort());
		nwConfig.setNetworkRackName(pi.getNetworkRackName());
		// user request
		nwConfig.setUrNo(pi.getId().getUrNo());
		nwConfig.setSubUrNo(pi.getId().getSubUrNo());
		nwConfig.setReqUserId(config.getReqUserId());
		nwConfig.setReqUserName(config.getReqUserName());
		nwConfig.setReqFor(config.getReqFor());
		nwConfig.setCreatedBy(user.getUserId());
		nwConfig.setCreated(new Timestamp(new Date().getTime()));
		nwConfig.setLastUpdBy(user.getUserId());
		nwConfig.setLastUpd(new Timestamp(new Date().getTime()));
		
		ipNetworkConfigDao.insert(nwConfig);
	}
	
	public void addInterfaceGateway(IpUrInterfaceGateway ig ,IpUrNwConfig config,IpUser user) {
		IpNetworkConfig nwConfig = new IpNetworkConfig();
		nwConfig.setIpAddress(ig.getIpAddress());
		nwConfig.setMask(ig.getMask());
		nwConfig.setNetworkTypeDesc(ig.getNetworkTypeDesc());
		nwConfig.setSourceLocationName(ig.getLocationName());
		nwConfig.setReqVlanId(ig.getReqVlanId());
		nwConfig.setDescription(ig.getReqDesc());

		// user request
		nwConfig.setUrNo(ig.getId().getUrNo());
		nwConfig.setSubUrNo(ig.getId().getSubUrNo());
		nwConfig.setReqUserId(config.getReqUserId());
		nwConfig.setReqUserName(config.getReqUserName());
		nwConfig.setReqFor(config.getReqFor());
		nwConfig.setCreatedBy(user.getUserId());
		nwConfig.setCreated(new Timestamp(new Date().getTime()));
		nwConfig.setLastUpdBy(user.getUserId());
		nwConfig.setLastUpd(new Timestamp(new Date().getTime()));
		
		ipNetworkConfigDao.insert(nwConfig);
	}
	
	public void addRouting(IpUrRouting rt ,IpUrNwConfig config,IpUser user) {
		IpNetworkConfig nwConfig = new IpNetworkConfig();
		nwConfig.setIpAddress(rt.getIpAddress());
		nwConfig.setMask(rt.getMask());
		nwConfig.setRoutingInterface(rt.getInterface());
		nwConfig.setRoutingNextHop(rt.getNextHop());
		nwConfig.setRoutingReqDesc(rt.getReqDesc());
		// user request
		nwConfig.setUrNo(rt.getId().getUrNo());
		nwConfig.setSubUrNo(rt.getId().getSubUrNo());
		nwConfig.setReqUserId(config.getReqUserId());
		nwConfig.setReqUserName(config.getReqUserName());
		nwConfig.setReqFor(config.getReqFor());
		nwConfig.setCreatedBy(user.getUserId());
		nwConfig.setCreated(new Timestamp(new Date().getTime()));
		nwConfig.setLastUpdBy(user.getUserId());
		nwConfig.setLastUpd(new Timestamp(new Date().getTime()));
		
		ipNetworkConfigDao.insert(nwConfig);
	}
	
	public void addF5(IpUrF5 f5 ,IpUrNwConfig config,IpUser user) {
		IpNetworkConfig nwConfig = new IpNetworkConfig();
		nwConfig.setIpAddress(f5.getVipAddress());
		nwConfig.setVipPort(f5.getVipPort());
		nwConfig.setIsSnapIp(f5.getIsSnapIp());
		nwConfig.setIsPersistence(f5.getIsPersistence());
		nwConfig.setMethod(f5.getMethod());
		nwConfig.setPoolIpAddress(f5.getPoolIpAddress());
		nwConfig.setPoolIpPort(f5.getPoolIpPort());
		nwConfig.setPoolWeight(f5.getPoolWeight());
		// user request
		nwConfig.setUrNo(f5.getId().getUrNo());
		nwConfig.setSubUrNo(f5.getId().getSubUrNo());
		nwConfig.setReqUserId(config.getReqUserId());
		nwConfig.setReqUserName(config.getReqUserName());
		nwConfig.setReqFor(config.getReqFor());
		nwConfig.setCreatedBy(user.getUserId());
		nwConfig.setCreated(new Timestamp(new Date().getTime()));
		nwConfig.setLastUpdBy(user.getUserId());
		nwConfig.setLastUpd(new Timestamp(new Date().getTime()));
		
		ipNetworkConfigDao.insert(nwConfig);
	}

	@Override
	public boolean isRejectAble(String urNo, String subUrNo, String userId) throws IPFMBusinessException{
		try{
			return ipUrActionDao.isRejectAble(urNo, subUrNo, userId);
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException();
		}
	}	
	
	
	@Override
	public void rejectUnCompleteSubUr(String urNo,List<String> selectedSubUr,IpUser user, String actionRemark)throws IPFMBusinessException {
		Date now = new Date();
		Date slaDate = null;
		
		try {// Modified By Bob : For fix rejectUr Flow
			// as requirement 2.2  
//			List<IpUrAction> listAction = ipUrActionDao.listAction(urNo, user, IPFMConstant.STATUS_WAIT_PM);
//			if(listAction != null) {
//				for(IpUrAction ipUrAction : listAction) {
//					// as requirement 2.2.3
//					IpUrActionHistory history = copyHistory(ipUrAction);
//					history.setOverSla(history.getSlaDate().after(now)?"N":"Y");
//					history.setOverOla(history.getOlaDate().after(now)?"N":"Y");
//					history.setLastUpdBy(user.getUserId());
//					history.setLastUpd(new Date());
//					ipUrActionHistoryDao.insert(history);
//
//					// as requirement 2.2.1 - 2.2.2
//					ipUrAction.setActionUserId(user.getUserId());
//					ipUrAction.setActionUser(user.getUserName());
//					ipUrAction.setUrStatus(IPFMConstant.STATUS_WAIT_PM);
//					
//					ipUrActionDao.update(ipUrAction);
//				}
//			}

			// as requirement 2.3
			String[] notCompleteStatus = {IPFMConstant.STATUS_WAIT_PM, IPFMConstant.STATUS_WAIT_ACTM, IPFMConstant.STATUS_ACTM_TEAM, IPFMConstant.STATUS_WAIT_TEAM, IPFMConstant.STATUS_REJECT_TEAM};
			List<IpUrAction> notCompleteUrList = ipUrActionDao.findActionByURAndStatus(urNo, notCompleteStatus);
//			int notDoneSubUr = ipUrActionDao.getUnFinishUR(urNo);
//			if(notDoneSubUr > 0) { // as requirement 2.3.1
//				// as requirement 2.3.1.1
				//for(String subUr : selectedSubUr){
			if(notCompleteUrList != null && notCompleteUrList.size() > 0) {
			for(IpUrAction ipUrAction : notCompleteUrList) {
					// as requirement 2.3.1.1.1
					//IpUrAction ipUrAction = ipUrActionDao.findUrAction(urNo, subUr);
					
					// as requirement 2.3.1.1.3
					IpUrActionHistory history = copyHistory(ipUrAction);
					history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
					history.setOverSla(isDateOverCurrent(history.getSlaDate(), now));
					history.setOverOla(isDateOverCurrent(history.getOlaDate(), now));
					if (history.getUrStatus().equals(IPFMConstant.STATUS_REJECT_TEAM)) {
						IpUrStatus ipUrStatus1  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
						history.setActionName(ipUrStatus1.getSubUrStatusName());
						history.setUrStatus(ipUrStatus1.getSubUrStatusId());
					}
					history.setUserRemark("");
					history.setLastUpdBy(user.getUserId());
					history.setLastUpd(new Date());
					ipUrActionHistoryDao.insert(history);
					
					// as requirement 2.3.1.1.4
					//IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_PM_WAIT);
					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_PM);
					IpUrActionHistory history2 = copyHistory(ipUrAction);
					history2.setActionUserId((user.getUserId() != null && !"".equals(user.getUserId())? user.getUserId() + ";" : ""));
					history2.setActionUser((user.getUserName() != null && !"".equals(user.getUserName())? user.getUserName() +user.getPhone()+";" : ""));
					history2.setActionName(ipUrStatus.getSubUrStatusName());  // need get from service IP_UR_STaTus
					//history2.setUrStatus(IPFMConstant.STATUS_REJECT_PM_WAIT);
					history2.setUrStatus(IPFMConstant.STATUS_REJECT_PM);
					history2.setActionRemark(IPFMConstant.REPORT_COMPLETE);
					history2.setUserRemark(actionRemark);
					history2.setActionSeq(ipUrActionDao.getNextActionSeq(history2.getUrNo(), history2.getUrType(),history2.getSubUrNo()));
					history2.setOverOla(isDateOverCurrent(history2.getOlaDate(), now));
					history2.setOverSla(isDateOverCurrent(history2.getSlaDate(), now));
					history2.setCreatedBy(user.getUserId());
					history2.setCreated(now);
					history2.setLastUpdBy(user.getUserId());
					history2.setLastUpd(now);
					ipUrActionHistoryDao.insert(history2);

					// as requirement 2.3.1.1.5
					//IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_PM_WAIT);
					IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_PM);
					IpUser reqUser = ipUserDao.getIPUser(ipUrAction.getReqestUserId());
					ipUrAction.setActionUserId(reqUser.getUserId());
					ipUrAction.setActionUser(reqUser.getUserName()+reqUser.getPhone());
					ipUrAction.setActionName(ipUrStatus2.getSubUrStatusName());
					//ipUrAction.setUrStatus(IPFMConstant.STATUS_REJECT_PM_WAIT);
					ipUrAction.setUrStatus(IPFMConstant.STATUS_REJECT_PM);
					ipUrAction.setActionRemark(IPFMConstant.REPORT_COMPLETE);
					ipUrAction.setUserRemark(actionRemark);
					// Edit on 12/01/2011 
//					ipUrAction.setCallFunctionId("F030");
//					ipUrAction.setCallMode("VIEW");
					ipUrAction.setCallFunctionId("F039");
					ipUrAction.setCallMode("USER");		
//					// Edit on 12/01/2011					
					
					ipUrAction.setCreated(now);
					ipUrAction.setCreatedBy(user.getUserId());
					ipUrAction.setLastUpdBy(user.getUserId());
					ipUrAction.setLastUpd(now);
		
					ipUrActionDao.update(ipUrAction);		
					
					// as requirement 2.3.1.1.6
					if(ipUrAction.getSubUrType().equals("FW")){
						IpUrFirewall firewall = ipUrAction.getFirewall();
						firewall.setIsTnp("Y");
						firewall.setPmRemark(actionRemark);
						//firewall.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
						firewall.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
						firewall.setLastUpdBy(user.getUserId());
						firewall.setLastUpd(now);
						firewallDao.update(firewall);
					} else if(ipUrAction.getSubUrType().equals("AL")){
						IpUrAccessListCdn cdn = ipUrAction.getAccessListCdn();
						cdn.setIsTnp("Y");
						cdn.setPmRemark(actionRemark);
						//cdn.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
						cdn.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
						cdn.setLastUpdBy(user.getUserId());
						cdn.setLastUpd(now);
						accessListDao.update(cdn);
					} else if(ipUrAction.getSubUrType().equals("AT")){
						IpUrAccessListTnp tnp = ipUrAction.getAccessListTnp();
						tnp.setIsTnp("Y");
						tnp.setPmRemark(actionRemark);
						//tnp.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
						tnp.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
						tnp.setLastUpdBy(user.getUserId());
						tnp.setLastUpd(now);
						accessListTNPDao.update(tnp);
					} else if(ipUrAction.getSubUrType().equals("PI")){
						IpUrPhysicalInterface phyInterface = ipUrAction.getPhyInterface();
						phyInterface.setIsTnp("Y");
						phyInterface.setPmRemark(actionRemark);
						//phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
						phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
						phyInterface.setLastUpdBy(user.getUserId());
						phyInterface.setLastUpd(now);
						physicalInterfaceDao.update(phyInterface);
					} else if(ipUrAction.getSubUrType().equals("IG")){
						IpUrInterfaceGateway gateway = ipUrAction.getGateway();
						gateway.setIsTnp("Y");
						gateway.setPmRemark(actionRemark);
						//gateway.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
						gateway.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
						gateway.setLastUpdBy(user.getUserId());
						gateway.setLastUpd(now);
						intfGatewayDao.update(gateway);
					} else if(ipUrAction.getSubUrType().equals("RT")){
						IpUrRouting routing = ipUrAction.getRouting();
						routing.setIsTnp("Y");
						routing.setPmRemark(actionRemark);
						//routing.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
						routing.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
						routing.setLastUpdBy(user.getUserId());
						routing.setLastUpd(now);
						routingDao.update(routing);
					} else if(ipUrAction.getSubUrType().equals("F5")){
						IpUrF5 f5 = ipUrAction.getF5();
						f5.setIsTnp("Y");
						f5.setPmRemark(actionRemark);
						//f5.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
						f5.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
						f5.setLastUpdBy(user.getUserId());
						f5.setLastUpd(now);
						f5Dao.update(f5);	
					}	
					
					List<IpSubUrAttachment> subFileList = ipSubUrAttachmentDao.getIpUrAttachment(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());			
					if(subFileList != null && subFileList.size() > 0){
						for(IpSubUrAttachment data : subFileList){
									IpSubUrAttachment subData = new IpSubUrAttachment();
									IpSubUrAttachmentId id = new IpSubUrAttachmentId();
									id.setUrNo(data.getId().getUrNo());
									id.setSubUrNo(ipUrAction.getSubUrNo());
									id.setFileName(data.getId().getFileName());
									id.setCategory(data.getId().getCategory());
									
									BigDecimal seqNo = ipSubUrAttachmentDao.getSeqNo(urNo, data.getId().getCategory());
									id.setSeq(seqNo);
									subData.setId(id);
									
									subData.setVersion(new Long(0));
									subData.setDescription(data.getDescription());
									subData.setUrStatus(IPFMConstant.STATUS_WAIT_CONFIG);
									subData.setFileLocation(data.getFileLocation());
									subData.setCreated(now);
									subData.setCreatedBy(user.getUserId());
									subData.setLastUpd(now);
									subData.setLastUpdBy(user.getUserId());
									subData.setSubUrStatus(IPFMConstant.STATUS_WAIT_PM);
									subData.setAction(IPFMConstant.STATUS_REJECT_PM);
									ipSubUrAttachmentDao.insert(subData);
							}
						}
					
				}
				ipSubUrAttachmentDao.deleteIpUrAttachmentSubmit(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());
			}
				
				// as requirement 2.4
				IpUrNwConfig config = findByUrNo(urNo);
					String isImpact = config.getIsImpact(); 
					IpWfConfig wf = ipUrActionDao.getSLADateImpact(isImpact);
					slaDate = (wf!=null)?this.getSLADate(now, "NC", wf.getPmTeam(),isImpact):null;
					config.setTnpStatus("Y");
					
					//20110201 change ur status to user verify
					IpUrStatus status = getUrStatus(config.getUrType(), IPFMConstant.STATUS_REJECT_PM);
					config.setUrStatusTxt(status.getUrStatusId());
					config.setUrStatusName(status.getUrStatusName());
					
					config.setOverSla(isDateOverCurrent(slaDate, now));
					config.setLastUpdBy(user.getUserId());
					config.setLastUpd(now);
					ipUrNwConfigDao.update(config);
//			}
//			else {
//			if(notCompleteUrList != null && notCompleteUrList.size() > 0) {
//				// as requirement 2.3.2
//				// as requirement 2.3.2.1
//					// as requirement 2.3.2.1.1
//					List<IpUrAction> ipUrActions = ipUrActionDao.findUrActions(urNo, subUr);
//					
//					if(ipUrActions != null && ipUrActions.size() > 0) {
//						IpUrActionHistory history = copyHistory(ipUrActions.get(0));
//						history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
//						history.setOverSla(history.getSlaDate().after(now)?"N":"Y");
//						history.setOverOla(history.getOlaDate().after(now)?"N":"Y");
//						history.setLastUpdBy(user.getUserId());
//						history.setLastUpd(now);
//						ipUrActionHistoryDao.insert(history);					
//					}
//					// as requirement 2.3.2.1.3
//					for(IpUrAction ipUrAction: ipUrActions) { 
//						// as requirement 2.3.2.1.4
//						IpUrActionHistory history2 = copyHistory(ipUrAction);
//						history2.setActionSeq(ipUrActionDao.getNextActionSeq(history2.getUrNo(), history2.getUrType(),history2.getSubUrNo()));
//						history2.setOverOla(history2.getOlaDate().after(now)?"N":"Y");
//						history2.setOverSla(history2.getSlaDate().after(now)?"N":"Y");
//						history2.setLastUpdBy(user.getUserId());
//						history2.setLastUpd(now);
//						ipUrActionHistoryDao.insert(history2);
//					}
//
//					// as requirement 2.3.2.1.5
//					if(ipUrActions != null && ipUrActions.size() > 0) {
//						IpUrAction ipUrAction = ipUrActions.get(0);
//	 					IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_PM);
//	 					ipUrAction.setActionUserId((user.getUserId() != null && !"".equals(user.getUserId())? user.getUserId() + ";" : ""));
//	 					ipUrAction.setActionUser((user.getUserName() != null && !"".equals(user.getUserName())? user.getUserName() +" ( "+user.getTel()+");" : ""));
//						ipUrAction.setActionName(ipUrStatus2.getSubUrStatusName());
//						ipUrAction.setUrStatus(IPFMConstant.STATUS_REJECT_PM);
//						ipUrAction.setActionRemark(actionRemark);
//						ipUrAction.setUserRemark("");
//						ipUrAction.setCallFunctionId("F039");
//						ipUrAction.setCallMode("USER");
//						ipUrAction.setCreated(now);
//						ipUrAction.setCreatedBy(user.getUserId());
//						ipUrAction.setLastUpdBy(user.getUserId());
//						ipUrAction.setLastUpd(now);
//		
//						ipUrActionDao.update(ipUrAction);		
//					
//						// as requirement 2.3.2.1.6
//						if(ipUrAction.getSubUrType().equals("FW")){
//							IpUrFirewall firewall = ipUrAction.getFirewall();
//							firewall.setIsTnp("Y");
//							firewall.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
//							firewall.setLastUpdBy(user.getUserId());
//							firewall.setLastUpd(now);
//							firewallDao.update(firewall);
//						} else if(ipUrAction.getSubUrType().equals("AL")){
//							IpUrAccessListCdn cdn = ipUrAction.getAccessListCdn();
//							cdn.setIsTnp("Y");
//							cdn.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
//							cdn.setLastUpdBy(user.getUserId());
//							cdn.setLastUpd(now);
//							accessListDao.update(cdn);
//						} else if(ipUrAction.getSubUrType().equals("AT")){
//							IpUrAccessListTnp tnp = ipUrAction.getAccessListTnp();
//							tnp.setIsTnp("Y");
//							tnp.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
//							tnp.setLastUpdBy(user.getUserId());
//							tnp.setLastUpd(now);
//							accessListTNPDao.update(tnp);
//						} else if(ipUrAction.getSubUrType().equals("PI")){
//							IpUrPhysicalInterface phyInterface = ipUrAction.getPhyInterface();
//							phyInterface.setIsTnp("Y");
//							phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
//							phyInterface.setLastUpdBy(user.getUserId());
//							phyInterface.setLastUpd(now);
//							physicalInterfaceDao.update(phyInterface);
//						} else if(ipUrAction.getSubUrType().equals("IG")){
//							IpUrInterfaceGateway gateway = ipUrAction.getGateway();
//							gateway.setIsTnp("Y");
//							gateway.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
//							gateway.setLastUpdBy(user.getUserId());
//							gateway.setLastUpd(now);
//							intfGatewayDao.update(gateway);
//						} else if(ipUrAction.getSubUrType().equals("RT")){
//							IpUrRouting routing = ipUrAction.getRouting();
//							routing.setIsTnp("Y");
//							routing.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM);
//							routing.setLastUpdBy(user.getUserId());
//							routing.setLastUpd(now);
//							routingDao.update(routing);
//						} else if(ipUrAction.getSubUrType().equals("F5")){
//							IpUrF5 f5 = ipUrAction.getF5();
//							f5.setIsTnp("Y");
//							f5.setSubUrStatusTxt(IPFMConstant.STATUS_REJECT_PM_WAIT);
//							f5.setLastUpdBy(user.getUserId());
//							f5.setLastUpd(now);
//							f5Dao.update(f5);	
//						}			
//					}
//				}		
				
				// as requirement 2.4
//				IpUrNwConfig config = findByUrNo(urNo);
//					String isImpact = config.getIsImpact(); 
//					IpWfConfig wf = ipUrActionDao.getSLADateImpact(isImpact);
//					slaDate = (wf!=null)?this.getSLADate(now, "NC", wf.getPmTeam(),isImpact):null;
//					config.setTnpStatus("Y");
//					config.setOverSla(isDateOverCurrent(slaDate, now));
//					config.setLastUpdBy(user.getUserId());
//					config.setLastUpd(now);
//					
//					ipUrNwConfigDao.update(config);
			


		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
		
	}
	
	
	@Override
	public boolean isDelegateAble(String urNo, String subUrNo, String userId) {
		return ipUrActionDao.isDelegateAble(urNo, subUrNo, userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveDeleagte(String urNo,List<String> selectedSubUr,String roleId,IpUser user) throws IPFMBusinessException {
		Date now = new Date();
		Date slaDate = null;
		List<EmailDetail> emailList = new ArrayList<EmailDetail>();
		List<IpRoleMember> roleList = ipUrNwConfigDao.finByRoleId(roleId);
		String pmUserId = "";
		String pmUser = "";
		String emailSubject= "";
		for(IpRoleMember role : roleList){
			pmUserId+=(role.getIpUser().getUserId()+";");
			pmUser+=(role.getIpUser().getUserName()+role.getIpUser().getPhone()+";");
		}
		try {
		
		for(String subUr : selectedSubUr){
			IpUrAction action = ipUrActionDao.findUrAction(urNo, subUr, user.getUserId());
			emailSubject = action.getSubject();
			IpUrActionHistory history = copyHistory(action);
			IpUrStatus ipUrStatus1  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
			history.setActionName(ipUrStatus1.getSubUrStatusName());
			history.setUrStatus(ipUrStatus1.getSubUrStatusId());
			history.setActionRemark(IPFMConstant.REPORT_COMPLETE);
			history.setLastUpd(now);
			history.setLastUpdBy(user.getUserId());
			ipUrActionHistoryDao.insert(history);
			
			action.setActionUser(user.getUserName()+user.getPhone()+";");
			action.setActionUserId(user.getUserId()+";");
			action.setLastUpdBy(user.getUserId());
			action.setLastUpd(now);
//			ipUrActionDao.update(action);
			
			IpUrActionHistory history2 = copyHistory(action);
			
			history2.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			history2.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
//			ipUrActionHistoryDao.insert(history);
			
//			boolean isR06 = (user.getRoleIdList().indexOf("R06")>0);
//			boolean isR07 = (user.getRoleIdList().indexOf("R07")>0);

			boolean isR06 = (user.getRoleIdList().contains("R06"));
			boolean isR07 = (user.getRoleIdList().contains("R07"));
			
			if(isR06 && isR07){
				int N6 = ipUrActionDao.getOLADateFromNodeID("N6");
				int N7 = ipUrActionDao.getOLADateFromNodeID("N7");
				action.setOlaDate(this.getOLADate(now, "NC", (N6<N7)?"N6":"N7"));
			} else if(isR06){
				action.setOlaDate(this.getOLADate(now, "NC", "N7"));
			} else if(isR07){
				action.setOlaDate(this.getOLADate(now, "NC", "N6"));
			}
			
			//IpUrNwConfig config = findByUrNo(urNo);
			//String isImpact = config.getIsImpact(); 
			//IpWfConfig wf = ipUrActionDao.getSLADateImpact(isImpact);
			
			//20110107  use old value sla
//			slaDate = (wf!=null)?this.getSLADate(now, "NC", wf.getPmTeam(),isImpact):null;
//			action.setSlaDate(slaDate);		
			
			IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_PM_DELEGATE);
			history2.setActionName(ipUrStatus.getSubUrStatusName());
			history2.setUrStatus(IPFMConstant.STATUS_PM_DELEGATE);
			history2.setActionRemark("Delegate Sub UR to Other PM");
			history2.setUserRemark("");
			if(action.getActionSeq() != null) { 
				history2.setActionSeq(ipUrActionDao.getNextActionSeq(history2.getUrNo(), history2.getUrType(),history2.getSubUrNo()));
			} else {
				history2.setActionSeq(new BigDecimal(0));
			}
			history2.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			history2.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			history2.setCreatedBy(user.getUserId());
			history2.setCreated(now);
			history2.setLastUpdBy(user.getUserId());
			history2.setLastUpd(now);
			ipUrActionHistoryDao.insert(history2);
			
			IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);					
			action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(),action.getSubUrNo()));
			action.setActionUserId(pmUserId);
			action.setActionUser(pmUser);
			action.setActionName(ipUrStatus2.getSubUrStatusName());
			action.setUrStatus(IPFMConstant.STATUS_WAIT_PM);
			action.setActionRemark(IPFMConstant.REPORT_COMPLETE);
			action.setUserRemark("");
			action.setCallFunctionId("F034");
			action.setCallMode(IPFMConstant.MODE_CALL_PM);
			action.setCreatedBy(user.getUserId());
			action.setCreated(now);
			action.setLastUpdBy(user.getUserId());
			action.setLastUpd(now);
			ipUrActionDao.update(action);
			
			if(action.getSubUrType().equals("FW")){
				IpUrFirewall firewall = action.getFirewall();
				firewall.setIsTnp("Y");
				firewall.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				firewall.setLastUpdBy(user.getUserId());
				firewall.setLastUpd(now);
				firewallDao.update(firewall);
			} else if(action.getSubUrType().equals("AL")){
				IpUrAccessListCdn cdn = action.getAccessListCdn();
				cdn.setIsTnp("Y");
				cdn.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				cdn.setLastUpdBy(user.getUserId());
				cdn.setLastUpd(now);
				accessListDao.update(cdn);
			} else if(action.getSubUrType().equals("AT")){
				IpUrAccessListTnp tnp = action.getAccessListTnp();
				tnp.setIsTnp("Y");
				tnp.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				tnp.setLastUpdBy(user.getUserId());
				tnp.setLastUpd(now);
				accessListTNPDao.update(tnp);
			} else if(action.getSubUrType().equals("PI")){
				IpUrPhysicalInterface phyInterface = action.getPhyInterface();
				phyInterface.setIsTnp("Y");
				phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				phyInterface.setLastUpdBy(user.getUserId());
				phyInterface.setLastUpd(now);
					
				physicalInterfaceDao.update(phyInterface);
			} else if(action.getSubUrType().equals("IG")){
				IpUrInterfaceGateway gateway = action.getGateway();
				gateway.setIsTnp("Y");
				gateway.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				gateway.setLastUpdBy(user.getUserId());
				gateway.setLastUpd(now);
				intfGatewayDao.update(gateway);
			} else if(action.getSubUrType().equals("RT")){
				IpUrRouting routing = action.getRouting();
				routing.setIsTnp("Y");
				routing.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				routing.setLastUpdBy(user.getUserId());
				routing.setLastUpd(now);
				routingDao.update(routing);
			} else if(action.getSubUrType().equals("F5")){
				IpUrF5 f5 = action.getF5();
				f5.setIsTnp("Y");
				f5.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				f5.setLastUpdBy(user.getUserId());
				f5.setLastUpd(now);
				f5Dao.update(f5);	
			}
			
			
			List<IpSubUrAttachment> subFileList = ipSubUrAttachmentDao.getIpUrAttachment(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());			
			if(subFileList != null && subFileList.size() > 0){
				for(IpSubUrAttachment data : subFileList){
							IpSubUrAttachment subData = new IpSubUrAttachment();
							IpSubUrAttachmentId id = new IpSubUrAttachmentId();
							id.setUrNo(data.getId().getUrNo());
							id.setSubUrNo(subUr);
							id.setFileName(data.getId().getFileName());
							id.setCategory(data.getId().getCategory());
							
							BigDecimal seqNo = ipSubUrAttachmentDao.getSeqNo(urNo, data.getId().getCategory());
							id.setSeq(seqNo);
							subData.setId(id);
							
							subData.setVersion(new Long(0));
							subData.setDescription(data.getDescription());
							subData.setUrStatus(IPFMConstant.STATUS_WAIT_CONFIG);
							subData.setFileLocation(data.getFileLocation());
							subData.setCreated(now);
							subData.setCreatedBy(user.getUserId());
							subData.setLastUpd(now);
							subData.setLastUpdBy(user.getUserId());
							subData.setSubUrStatus(IPFMConstant.STATUS_WAIT_PM);
							subData.setAction(IPFMConstant.STATUS_PM_DELEGATE);
							ipSubUrAttachmentDao.insert(subData);
					}
				}
			
		}
		ipSubUrAttachmentDao.deleteIpUrAttachmentSubmit(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());
		
		IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
		config.setTnpStatus("Y");
		config.setOverSla(isDateOverCurrent(slaDate, now));
		config.setLastUpdBy(user.getUserId());
		config.setLastUpd(now);
		
		ipUrNwConfigDao.update(config);

		// Send Mail 		
		if(roleList != null && roleList.size() > 0) {
			for(IpRoleMember role : roleList) {
				EMailService emailService = this.getEmailService();
				IpUrActionService actionService = (IpUrActionService) this.getIpUrActionService();
				String nodeId = "";
				if (role.getRoleId().equalsIgnoreCase("R06"))  nodeId = "N6";
				if (role.getRoleId().equalsIgnoreCase("R07"))  nodeId = "N7";
				if (actionService.isSendEmail("NC", nodeId)) {
					
					actionService.deleteNotify(config.getUrNo(), "");
					
						EmailDetail email = new EmailDetail();
						email.setEmail(role.getIpUser().getEmail());
						email.setUrNo(urNo);
						email.setUserId(role.getIpUser().getUserId());
						email.setUserName(role.getIpUser().getUserName());
						email.setSubject(config.getReqSubject());
						email.setUrType("Request Network Config");
						email.setUrStatusDesc("Wait For PM Assign");
						email.setUrStatus("WAIT_PM");
						email.setSubjectDesc("Wait For PM Assign");
						email.setBodyDesc("Wait For PM Assign");
						email.setRequestBy(config.getReqUserName());
						email.setRequestDate(config.getReqDate());
						String urNoEmail = email.getUrNo();				
//						String key = "IPFM" + urNoEmail + ":" + email.getUserId();
						String key = emailService.getKeyCodeEmail();
						String[] to = {""};
						if(role.getIpUser() != null) {
							to[0] = email.getEmail();
						}
						String[] cc = null;
						String from = "ipfm@ais.co.th";				
						
						try {
							Map model = new HashMap();
							model.put("userName", email.getUserName());
							model.put("urNo", email.getUrNo());
							model.put("urType", email.getUrType());
							model.put("urStatus", "Wait For PM Assign");
							model.put("subjectDesc", "Wait For PM Assign (Delegrate by PM)");
							model.put("bodyDesc", "Wait For PM Assign (Delegrate by PM)");
							model.put("subject", emailSubject);
							model.put("requestBy", email.getRequestBy());
							SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy",new Locale("TH", "th"));
					        model.put("requestDate", smf.format(email.getRequestDate()));
							String encodeUrl = new String(Base64.encodeBase64(key.getBytes()));
							model.put("url", user.getUrl()+ "/home/view.jsf?key=" + encodeUrl);
	
							emailService.sendMail(to, cc, from, EMailService.TEMPLATE_IPFM_NC_UR, model);
	
							IpEmailNotify notify = new IpEmailNotify();
							notify.setUserId(email.getUserId());
							notify.setUrNo(email.getUrNo());
							notify.setUrStatus(email.getUrStatus());
							notify.setCreatedBy(user.getUserId());
							notify.setLastUpdBy(user.getUserId());
							notify.setEmailLinkStatus("N");
							notify.setEmailCode(key);
							actionService.saveEmailNotify(notify);
						}
						catch (Exception e) {
							e.printStackTrace();
						}					
					}
				}					
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException();
		}

	}

	@Override
	public List<IpRoleMember> listPerson(String teamId) {
		return ipUrNwConfigDao.listPersonAssign(teamId);
	}
	
	public Date getDateACTM(Date slaDate){
		Integer olaValue = ipUrActionDao.getValueOLADate("NC", "N8");
		if (olaValue==null) olaValue = 0; 		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(slaDate);
		calendar.add(Calendar.DATE, olaValue.intValue());
    
		int nonwork = ipUrActionDao.nonWorkDay(slaDate,calendar.getTime());		
		int holiday = ipUrActionDao.getHoliday(slaDate,calendar.getTime()); 
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(calendar.getTime());
		calendar1.add(Calendar.DATE, nonwork + holiday);
		while (true) {
			if (calendar.getTime().compareTo(calendar1.getTime()) > -1) break;
			calendar.add(Calendar.DATE, 1);
			int nonwork1 = ipUrActionDao.nonWorkDay(calendar.getTime(),calendar1.getTime());
			int holiday1 = ipUrActionDao.getHoliday(calendar.getTime(),calendar1.getTime());
			
			if (nonwork1==0 && holiday1==0) break;
			
			calendar.setTime(calendar1.getTime());
			calendar1.add(Calendar.DATE, nonwork1 + holiday1);
		}
		
		return calendar1.getTime();
	}

	@SuppressWarnings("unchecked")
	public void savePMAssign(String urNo, List<String> subUrNoList,
			List<PMAssetDTO> pmAssetDTOList, IpUser user, PmAssign pm)
			throws IPFMBusinessException {
		Date now = new Date();

		String teamName = "";
		String roleForProcess = "";
		String pmRemark = pm.getMgrRemark();
		String emailSubject= "";
		if(user != null) {
			if("R06".contains(user.getRoleIdList())) {
				teamName = "TNP";
			} else {
				teamName = "CDN";
			}
		}
		try {
			Set<String> userActmList = new HashSet<String>();
			Set<String> userTeamList = new HashSet<String>();
			List<String> actmSubUrList = new ArrayList<String>();
			List<String> teamSubUrList = new ArrayList<String>();
			Map teamRole = new HashMap();
			Map actmSubUr = new HashMap();
			Map teamSubUr = new HashMap();
			for (String subUrNo : subUrNoList) {
				IpUrAction action = ipUrActionDao.findUrAction(urNo, subUrNo, user.getUserId());
				if(action != null) {
				// Object subUrObj = getSubUrObj(action);
				emailSubject = action.getSubject();
				String isImpact = getIsImpactValue(action); // getProperty(subUrObj,
										// "isImpact");
				String urStatus = action.getUrStatus();
				//20101117:Change get Max Seq
				//long seq = action.getActionSeq().longValue();

				IpUrActionHistory history = copyHistory(action);
				history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
				if (history.getUrStatus().equals(IPFMConstant.STATUS_REJECT_TEAM)) {
					IpUrStatus ipUrStatus1  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
					history.setActionName(ipUrStatus1.getSubUrStatusName());
					history.setUrStatus(ipUrStatus1.getSubUrStatusId());
				}
				history.setActionRemark(IPFMConstant.REPORT_COMPLETE);
				history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
				history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
				ipUrActionHistoryDao.insert(history);

				IpUrActionHistory history2 = copyHistory(action);
				//20101117:Change get Max Seq
				//seq += 1;
				IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_PM_ASSIGN);
				history2.setActionSeq(ipUrActionDao.getNextActionSeq(history2.getUrNo(), history2.getUrType(),history2.getSubUrNo()));
				String userIdCl = "";
				String userNameCl = "";
				if (user.getUserId() != null && !user.getUserId().equals("")) {
					userIdCl = user.getUserId() + ";";
				}
				if (user.getUserName() != null && !user.getUserName().equals("")) {
					userNameCl = user.getUserName() + user.getPhone() + ";";
				}
				history2.setActionUserId(userIdCl);
				history2.setActionUser(userNameCl);
				history2.setActionName(ipUrStatus.getSubUrStatusName());
				history2.setUrStatus(IPFMConstant.STATUS_PM_ASSIGN);
				history2.setActionRemark(IPFMConstant.REPORT_COMPLETE);
				history2.setUserRemark(pm.getMgrRemark());
				history2.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
				history2.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
				history2.setCreated(new Date());
				history2.setCreatedBy(user.getUserId());
				history2.setLastUpd(new Date());
				history2.setLastUpdBy(user.getUserId());
				ipUrActionHistoryDao.insert(history2);

				// String userId = "";
				// String userName = "";
				String actmId = "";
				String actmName = "";

				/*
				 * if(pm.getAssignForTeam().equals("T")){ List<IpUser> userList
				 * = ipUserDao.getRoleIdList("R09"); for(IpUser us : userList){
				 * userId+=(us.getUserId()+";");
				 * userName+=(us.getUserName()+";"); } } else
				 * if(pm.getAssignForTeam().equals("P")){ for(String selected :
				 * pm.getSelectedPerson()){ IpUser us = null; try{ us =
				 * ipUserDao.getIPUser(selected); } catch (Exception ex){ //
				 * exception } finally{ userId+=(us.getUserId()+";");
				 * userName+=(us.getUserName()+";"); } } }
				 */
				
					boolean isInsertDone = false;
				
					if (isImpact != null && isImpact.equals("Y")) {
						if (action.getUrStatus().equals(IPFMConstant.STATUS_WAIT_PM) 
							|| action.getUrStatus().equals(IPFMConstant.STATUS_REJECT_TEAM)) {
							List<IpUser> userList = ipUserDao.getRoleIdList("R08");
							actmSubUrList.add(action.getSubUrNo());
							
							for (IpUser us : userList) {
								actmId += (us.getUserId() + ";");
								actmName += (us.getUserName()+ us.getPhone() +";");
								userActmList.add(us.getUserId());
								actmSubUr.put(us.getUserId(), actmSubUrList);
							}
							
	//						Calendar calendar = Calendar.getInstance();
	//						calendar.setTime(action.getSlaDate());
	//						Integer olaValue = ipUrActionDao.getValueOLADate(action.getUrType(), "N8");
	//						if (olaValue==null) olaValue = 0; 
	//						calendar.add(Calendar.DATE, olaValue.intValue());
	//						Date slaDateACTM = calendar.getTime();
							
							
							Date slaDateACTM = action.getSlaDate();
							List<IpUrActionHistory> historyList = ipUrActionHistoryDao.findHistoryAction(action.getUrNo(),action.getSubUrNo(),action.getUrType(),IPFMConstant.STATUS_WAIT_ACTM);
							if (historyList==null || historyList.size()==0) {
								slaDateACTM = getDateACTM(action.getSlaDate());
							}
							
							//20110107 use old value sla
	//						Date slaDateACTM = getSLADate(new Date(), action.getUrType(), teamName, "N");
							
							Date olaDateACTM = getOLADate("R08", action);
							
							IpUrAction action2 = copyAction(action);
							//20101117:Change get Max Seq
							//seq += 1;
							//20101117 : Get Status From Table IP_UR_STATUS
							IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_ACTM);
							
							action2.setActionSeq(ipUrActionDao.getNextActionSeq(action2.getUrNo(), action2.getUrType(),action2.getSubUrNo()));
							action2.setActionUserId(actmId);
							action2.setActionUser(actmName);
							action2.setActionName(ipUrStatus2.getSubUrStatusName());
							action2.setUrStatus(IPFMConstant.STATUS_WAIT_ACTM);
							action2.setUserRemark(pm.getMgrRemark());
							action2.setCallFunctionId("F035");
							action2.setCallMode(IPFMConstant.ACTION_ACTM);
							action2.setOlaDate(olaDateACTM);
							action2.setSlaDate(slaDateACTM);
							
							action2.setCreated(new Date());
							action2.setCreatedBy(user.getUserId());
							action2.setLastUpd(new Date());
							action2.setLastUpdBy(user.getUserId());
							ipUrActionDao.insert(action2);
	
							if (pmAssetDTOList != null) {
								for (PMAssetDTO pmAssetDTO : pmAssetDTOList) {
									List<String> userIdList = pmAssetDTO.getSelectUserIdList();
									String userId = "";
									String userName = "";
									for (String userS : userIdList) {
										String[] tmp = userS.split(":split:");
										userId += tmp[0] + ";";
										userName += tmp[1] + ";";
									}
									IpUrAction action3 = copyAction(action);
									List<IpUser> ipUserList = ipUserDao.getRoleIdList(pmAssetDTO.getRoleId());
									//String teamName = "";
	//								if (ipUserList != null && ipUserList.size() > 0) {
	//									teamName = ipUserList.get(0).getTeamName();
	//								}
									
	//								Calendar calendar1 = Calendar.getInstance();
	//								calendar1.setTime(action2.getSlaDate());
									//20110109 use old value sla
	//								Integer olaValue1 = ipUrActionDao.getValueOLADate(action.getUrType(), getOLANode(pmAssetDTO.getRoleId()));
	//								if (olaValue1==null) olaValue1 = 0; 
	//								calendar1.add(Calendar.DATE, olaValue1.intValue());
									Date slaDateY = action2.getSlaDate();
									
									//20110107 use old value sla
	//								Date slaDateY = getSLADate(new Date(), action.getUrType(), teamName, "Y");
									
									Date olaDateY = getOLADate(pmAssetDTO.getRoleId(), action);
									
									//20101117 : Get Status From Table IP_UR_STATUS
									IpUrStatus ipUrStatus3  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_ACTM_TEAM);
									
									//20101117:Change get Max Seq
									//seq += 1;
									action3.setActionSeq(ipUrActionDao.getNextActionSeq(action3.getUrNo(), action3.getUrType(),action3.getSubUrNo()));
									action3.setActionUserId(userId);
									action3.setActionUser(userName);
									action3.setActionName(ipUrStatus3.getSubUrStatusName());
									action3.setUrStatus(IPFMConstant.STATUS_ACTM_TEAM);
									action3.setCallFunctionId("F036");
									action3.setCallMode(IPFMConstant.MODE_TEAM);
									action3.setUserRemark(pm.getMgrRemark());
									action3.setOlaDate(olaDateY);
									action3.setSlaDate(slaDateY);
									
									action3.setCreated(new Date());
									action3.setCreatedBy(user.getUserId());
									action3.setLastUpd(new Date());
									action3.setLastUpdBy(user.getUserId());
									ipUrActionDao.insert(action3);
								}
							}
							isInsertDone = true;
						}
					} else {
						if (pmAssetDTOList != null) {
							teamSubUrList.add(action.getSubUrNo());
							for (PMAssetDTO pmAssetDTO : pmAssetDTOList) {
								List<String> userIdList = pmAssetDTO.getSelectUserIdList();
								String userId = "";
								String userName = "";
								for (String userS : userIdList) {
									String[] tmp = userS.split(":split:");
									userId += tmp[0] + ";";
									userName += tmp[1] + ";";
									userTeamList.add(tmp[0]);
									teamSubUr.put(tmp[0], teamSubUrList);
								}
								teamRole.put(pmAssetDTO.getRoleId(),userTeamList);
								
								IpUrAction action2 = copyAction(action);
	//							List<IpUser> ipUserList = ipUserDao.getRoleIdList(pmAssetDTO.getRoleId());
	//							//String teamName = "";
	//							if (ipUserList != null && ipUserList.size() > 0) {
	//								teamName = ipUserList.get(0).getTeamName();
	//							}
								
								
								Calendar calendar1 = Calendar.getInstance();
								calendar1.setTime(action.getSlaDate());
								//20110109 use old value sla
	//							Integer olaValue1 = ipUrActionDao.getValueOLADate(action.getUrType(), getOLANode(pmAssetDTO.getRoleId()));
	//							if (olaValue1==null) olaValue1 = 0; 
	//							calendar1.add(Calendar.DATE, olaValue1.intValue());
								Date slaDateN = calendar1.getTime();
								
								//20110107 use old value sla
	//							Date slaDateN = getSLADate(new Date(), action.getUrType(), teamName, "N");
								
								Date olaDateN = getOLADate(pmAssetDTO.getRoleId(),action);
								
								//20101117 : Get Status From Table IP_UR_STATUS
								IpUrStatus ipUrStatus4  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_TEAM);
								
								//20101117:Change get Max Seq
								//seq += 1;
								action2.setActionSeq(ipUrActionDao.getNextActionSeq(action2.getUrNo(), action2.getUrType(),action2.getSubUrNo()));
								action2.setActionUserId(userId);
								action2.setActionUser(userName);
								action2.setActionName(ipUrStatus4.getSubUrStatusName());
								action2.setUrStatus(IPFMConstant.STATUS_WAIT_TEAM);
								action2.setUserRemark(pm.getMgrRemark());
								action2.setCallFunctionId("F036");
								action2.setCallMode(IPFMConstant.MODE_TEAM);
								action2.setOlaDate(olaDateN);
								action2.setSlaDate(slaDateN);
								action2.setCreated(new Date());
								action2.setCreatedBy(user.getUserId());
								action2.setLastUpd(new Date());
								action2.setLastUpdBy(user.getUserId());
								ipUrActionDao.insert(action2);
								
								isInsertDone = true;
							}
						}
					}
					String sts = (urStatus.equals(IPFMConstant.STATUS_WAIT_PM) && 
							isImpact.equals("Y")) ? IPFMConstant.STATUS_WAIT_ACTM : IPFMConstant.STATUS_WAIT_TEAM;
					if (action.getSubUrType().equals("FW")) {
						IpUrFirewall firewall = action.getFirewall();
						firewall.setIsAssign("Y");
						firewall.setPmRemark(pmRemark);
						firewall.setSubUrStatusTxt(sts);
						firewall.setLastUpdBy(user.getUserId());
						firewall.setLastUpd(now);
						firewallDao.update(firewall);
					} else if (action.getSubUrType().equals("AL")) {
						IpUrAccessListCdn cdn = action.getAccessListCdn();
						//cdn.setIsTnp("Y");
						cdn.setIsAssign("Y");
						cdn.setPmRemark(pmRemark);
						cdn.setSubUrStatusTxt(sts);
						cdn.setLastUpdBy(user.getUserId());
						cdn.setLastUpd(now);
						accessListDao.update(cdn);
					} else if (action.getSubUrType().equals("AT")) {
						IpUrAccessListTnp tnp = action.getAccessListTnp();
						// tnp.setIsTnp("Y");
						tnp.setIsAssign("Y");
						tnp.setPmRemark(pmRemark);
						tnp.setSubUrStatusTxt(sts);
						tnp.setLastUpdBy(user.getUserId());
						tnp.setLastUpd(now);
						accessListTNPDao.update(tnp);
					} else if (action.getSubUrType().equals("PI")) {
						IpUrPhysicalInterface phyInterface = action.getPhyInterface();
						// phyInterface.setIsTnp("Y");
						phyInterface.setIsAssign("Y");
						phyInterface.setPmRemark(pmRemark);
						phyInterface.setSubUrStatusTxt(sts);
						phyInterface.setLastUpdBy(user.getUserId());
						phyInterface.setLastUpd(now);
	
						physicalInterfaceDao.update(phyInterface);
					} else if (action.getSubUrType().equals("IG")) {
						IpUrInterfaceGateway gateway = action.getGateway();
						// gateway.setIsTnp("Y");
						gateway.setIsAssign("Y");
						gateway.setPmRemark(pmRemark);
						gateway.setSubUrStatusTxt(sts);
						gateway.setLastUpdBy(user.getUserId());
						gateway.setLastUpd(now);
						intfGatewayDao.update(gateway);
					} else if (action.getSubUrType().equals("RT")) {
						IpUrRouting routing = action.getRouting();
						// routing.setIsTnp("Y");
						routing.setIsAssign("Y");
						routing.setPmRemark(pmRemark);
						routing.setSubUrStatusTxt(sts);
						routing.setLastUpdBy(user.getUserId());
						routing.setLastUpd(now);
						routingDao.update(routing);
					} else if (action.getSubUrType().equals("F5")) {
						IpUrF5 f5 = action.getF5();
						// f5.setIsTnp("Y");
						f5.setIsAssign("Y");
						f5.setPmRemark(pmRemark);
						f5.setSubUrStatusTxt(sts);
						f5.setLastUpdBy(user.getUserId());
						f5.setLastUpd(now);
						f5Dao.update(f5);
					}
				
					if(isInsertDone) {
						ipUrActionDao.deleteByRowID(action.getRowId());
					}
				}
			}
			//20101117 : Get Status From Table IP_UR_STATUS
			IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
			
			IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
			config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_CONFIG);
			config.setUrStatusName(ipUrStatus.getUrStatusName());
			config.setLastUpdBy(user.getUserId());
			config.setLastUpd(now);

			ipUrNwConfigDao.update(config);
			for (String userId : userActmList) {
				int count = 0;
				String subUrEmail = "";
				for (String subUr : (List<String>)actmSubUr.get(userId)) {
					count++;
					if (subUrEmail.equalsIgnoreCase("")){
						subUrEmail = subUr;
					}else if (count>5) { 
							subUrEmail = subUrEmail+"... Sub Ur มีมากกว่า 5 รายการ";
							break;
					}else{
						subUrEmail = subUrEmail + "," + subUr;
					}
					
				}
				IpUser ipUser = ipUserDao.getIPUser(userId);
				EmailDetail email = new EmailDetail();
				email.setEmail(ipUser.getEmail());
				email.setUserId(ipUser.getUserId());
				email.setUserName(ipUser.getUserName());
				email.setRequestBy(config.getReqUserName());
				email.setRequestDate(config.getReqDate());
				email.setSubject(config.getReqSubject());
				email.setUrNo(config.getUrNo());
				email.setSubUrNo(subUrEmail);
				email.setUrStatusDesc("Wait For Impact Analyst");						
				email.setUrStatus("WAIT_ACTM");
				email.setSubjectDesc("Wait For Impact Analyst");
				email.setBodyDesc("Wait For Impact Analyst");
				email.setUrType("Request Network Config");
				email.setNodeId("N8");
				if (ipUrActionService.isSendEmail("NC", email.getNodeId())){
//					String key = "IPFM" + email.getUrNo() + ": "+email.getUserId();
					String key = emailService.getKeyCodeEmail();
					String[] to = { email.getEmail() };
					String[] cc = null;
					String from = "ipfm@ais.co.th";
					try {
						Map model = new HashMap();
						model.put("userName", email.getUserName());
						model.put("urNo", email.getUrNo());
						model.put("subUrNo", email.getSubUrNo());
						model.put("urType", email.getUrType());
						model.put("urStatus", email.getUrStatusDesc());
						model.put("subjectDesc", email.getSubjectDesc());
						model.put("bodyDesc", email.getBodyDesc());
						model.put("subject", email.getSubject());
						model.put("requestBy", email.getRequestBy());
						SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy",new Locale("TH", "th"));
				        model.put("requestDate", smf.format(email.getRequestDate()));
						String encodeUrl = new String(Base64.encodeBase64(key.getBytes()));
						model.put("url", pm.getHomeUrl()+ "/home/view.jsf?key=" + encodeUrl);
						emailService.sendMail(to, cc, from,EMailService.TEMPLATE_IPFM_NC_SUBUR, model);

						IpEmailNotify notify = new IpEmailNotify();
						notify.setUserId(email.getUserId());
						notify.setUrNo(email.getUrNo());
						notify.setUrStatus(email.getUrStatus());
						notify.setCreatedBy(user.getUserId());
						notify.setLastUpdBy(user.getUserId());
						notify.setEmailLinkStatus("N");
						notify.setEmailCode(key);
						ipUrActionService.saveEmailNotify(notify);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			Set<String> user09List = (HashSet<String>)teamRole.get("R09");
			Set<String> user10List = (HashSet<String>)teamRole.get("R10");
			Set<String> user11List = (HashSet<String>)teamRole.get("R11");
			Set<String> user12List = (HashSet<String>)teamRole.get("R12");
			Set<String> user13List = (HashSet<String>)teamRole.get("R13");
			Set<String> user14List = (HashSet<String>)teamRole.get("R14");
			Set<String> user15List = (HashSet<String>)teamRole.get("R15");
			Set<String> user16List = (HashSet<String>)teamRole.get("R16");
			Set<String> user17List = (HashSet<String>)teamRole.get("R17");
			
			for (String userId : userTeamList) {
				String nodeId = "";
				if (user09List!=null && user09List.contains(userId)) nodeId = "N9";
				if (user10List!=null && user10List.contains(userId)) nodeId = "N10";
				if (user11List!=null && user11List.contains(userId)) nodeId = "N11";
				if (user12List!=null && user12List.contains(userId)) nodeId = "N12";
				if (user13List!=null && user13List.contains(userId)) nodeId = "N13";
				if (user14List!=null && user14List.contains(userId)) nodeId = "N14";
				if (user15List!=null && user15List.contains(userId)) nodeId = "N15";
				if (user16List!=null && user16List.contains(userId)) nodeId = "N16";
				if (user17List!=null && user17List.contains(userId)) nodeId = "N17";
				
				if (ipUrActionService.isSendEmail("NC", nodeId)){
					int count = 0;
					String subUrEmail = "";
					List<String> list = (List<String>)teamSubUr.get(userId);
					if (list == null) continue;
					for (String subUr : list) {
						count++;
						if (subUrEmail.equalsIgnoreCase("")){
							subUrEmail = subUr;
						}else if (count>5) { 
							subUrEmail = subUrEmail+"... Sub Ur มีมากกว่า 5 รายการ";
							break;
						}else{
							subUrEmail = subUrEmail + "," + subUr;
						}
						
					}
					IpUser ipUser = ipUserDao.getIPUser(userId);
					EmailDetail email = new EmailDetail();
					email.setEmail(ipUser.getEmail());
					email.setUserId(ipUser.getUserId());
					email.setUserName(ipUser.getUserName());
					email.setRequestBy(config.getReqUserName());
					email.setRequestDate(config.getReqDate());
					email.setSubject(config.getReqSubject());
					email.setUrNo(config.getUrNo());
					email.setSubUrNo(subUrEmail);
					email.setUrStatusDesc("Wait For Team Process");						
					email.setUrStatus("WAIT_TEAM");
					email.setSubjectDesc("Wait For Team Process");
					email.setBodyDesc("Wait For Team Process");
					email.setUrType("Request Network Config");
					email.setNodeId("N8");
					if (ipUrActionService.isSendEmail("NC", email.getNodeId())){
//						String key = "IPFM" + email.getUrNo() + ": "+email.getUserId();
						String key = emailService.getKeyCodeEmail();
						String[] to = { email.getEmail() };
						String[] cc = null;
						String from = "ipfm@ais.co.th";
						try {
							Map model = new HashMap();
							model.put("userName", email.getUserName());
							model.put("urNo", email.getUrNo());
							model.put("subUrNo", email.getSubUrNo());
							model.put("urType", email.getUrType());
							model.put("urStatus", email.getUrStatusDesc());
							model.put("subjectDesc", email.getSubjectDesc());
							model.put("bodyDesc", email.getBodyDesc());
							model.put("subject", email.getSubject());
							model.put("requestBy", email.getRequestBy());
							SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy",new Locale("TH", "th"));
					        model.put("requestDate", smf.format(email.getRequestDate()));
							String encodeUrl = new String(Base64.encodeBase64(key.getBytes()));
							model.put("url", pm.getHomeUrl()+ "/home/view.jsf?key=" + encodeUrl);
							emailService.sendMail(to, cc, from,EMailService.TEMPLATE_IPFM_NC_SUBUR, model);
	
							IpEmailNotify notify = new IpEmailNotify();
							notify.setUserId(email.getUserId());
							notify.setUrNo(email.getUrNo());
							notify.setUrStatus(email.getUrStatus());
							notify.setCreatedBy(user.getUserId());
							notify.setLastUpdBy(user.getUserId());
							notify.setEmailLinkStatus("N");
							notify.setEmailCode(key);
							ipUrActionService.saveEmailNotify(notify);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void savePMAssign(String urNo,String subUrType,List<Object> listUr, List<String> subUrNoList,
			List<PMAssetDTO> pmAssetDTOList, IpUser user, PmAssign pm)
			throws IPFMBusinessException {
		Date now = new Date();
		String teamName = "";
		String roleForProcess = "";
		String pmRemark = pm.getMgrRemark();
		String emailSubject= "";
		if(user != null) {
			if("R06".contains(user.getRoleIdList())) {
				teamName = "TNP";
			} else {
				teamName = "CDN";
			}
		}
		boolean impactTrunk = false;
		int index = 0;
		try {
			String piType = "";
			String trunkFlag = "";
			String trunkNo = "";
			HashMap tmpMap = new HashMap();
			for (Object obj : listUr) {
				Field fieldId = obj.getClass().getDeclaredField("impactSelected");
				fieldId.setAccessible(true);
				boolean impact = (Boolean) fieldId.get(obj);
				
				if(obj instanceof IpUrPhysicalInterface){
					Field piTypeField = obj.getClass().getDeclaredField("piType");
					piTypeField.setAccessible(true);
					piType = (String) piTypeField.get(obj);
					
					Field trunkFlagField = obj.getClass().getDeclaredField("trunkFlag");
					trunkFlagField.setAccessible(true);
					trunkFlag = (String) trunkFlagField.get(obj);
					
					Field trunkNoField = obj.getClass().getDeclaredField("trunkNo");
					trunkNoField.setAccessible(true);
					trunkNo = (String) trunkNoField.get(obj);
				}
				
				if(StringUtils.equals("T", piType) && StringUtils.equals("T", trunkFlag)){
					impactTrunk = impact;
					tmpMap.put(trunkNo+"_impactTrunk", impactTrunk);
				}else if(StringUtils.equals("T", piType) && StringUtils.equals("N", trunkFlag)){
					impactTrunk = (Boolean)tmpMap.get(trunkNo+"_impactTrunk");
				}
				
				
				if (subUrType.equals("FW")) {
					IpUrFirewall sub = (IpUrFirewall)obj;
					sub.setIsImpact((impact)?"Y" : "N");
					firewallDao.update(sub);
				} else if (subUrType.equals("AL")) {
					IpUrAccessListCdn sub = (IpUrAccessListCdn)obj;
					sub.setIsImpact((impact)?"Y" : "N");
					accessListDao.update(sub);
				} else if (subUrType.equals("AT")) {
					IpUrAccessListTnp sub = (IpUrAccessListTnp)obj;
					sub.setIsImpact((impact)?"Y" : "N");
					accessListTNPDao.update(sub);
				} else if (subUrType.equals("PI")) {
					IpUrPhysicalInterface sub = (IpUrPhysicalInterface)obj;
					if(StringUtils.equals("T", piType)){
						sub.setIsImpact((impactTrunk)?"Y" : "N");
					}else{
						sub.setIsImpact((impact)?"Y" : "N");
					}
					physicalInterfaceDao.update(sub);
				} else if (subUrType.equals("IG")) {
					IpUrInterfaceGateway sub = (IpUrInterfaceGateway)obj;
					sub.setIsImpact((impact)?"Y" : "N");
					intfGatewayDao.update(sub);
				} else if (subUrType.equals("RT")) {
					IpUrRouting sub = (IpUrRouting)obj;
					sub.setIsImpact((impact)?"Y" : "N");
					routingDao.update(sub);
				} else if (subUrType.equals("F5")) {
					IpUrF5 sub = (IpUrF5)obj;
					sub.setIsImpact((impact)?"Y" : "N");
					f5Dao.update(sub);
				}
				index++;
			}
			
			
			Set<String> userActmList = new HashSet<String>();
			Set<String> userTeamList = new HashSet<String>();
			List<String> actmSubUrList = new ArrayList<String>();
			List<String> teamSubUrList = new ArrayList<String>();
			Map teamRole = new HashMap();
			Map actmSubUr = new HashMap();
			Map teamSubUr = new HashMap();
			for (String subUrNo : subUrNoList) {
				IpUrAction action = ipUrActionDao.findUrAction(urNo, subUrNo, user.getUserId());
				if(action != null) {
				// Object subUrObj = getSubUrObj(action);
				emailSubject = action.getSubject();
				String isImpact = getIsImpactValue(action); // getProperty(subUrObj,
										// "isImpact");
				String urStatus = action.getUrStatus();
				//20101117:Change get Max Seq
				//long seq = action.getActionSeq().longValue();

				IpUrActionHistory history = copyHistory(action);
				history.setActionSeq(ipUrActionDao.getNextActionSeq(history.getUrNo(), history.getUrType(),history.getSubUrNo()));
				if (history.getUrStatus().equals(IPFMConstant.STATUS_REJECT_TEAM)) {
					IpUrStatus ipUrStatus1  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
					history.setActionName(ipUrStatus1.getSubUrStatusName());
					history.setUrStatus(ipUrStatus1.getSubUrStatusId());
				}
				history.setActionRemark(IPFMConstant.REPORT_COMPLETE);
				history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
				history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
				ipUrActionHistoryDao.insert(history);

				IpUrActionHistory history2 = copyHistory(action);
				//20101117:Change get Max Seq
				//seq += 1;
				IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_PM_ASSIGN);
				history2.setActionSeq(ipUrActionDao.getNextActionSeq(history2.getUrNo(), history2.getUrType(),history2.getSubUrNo()));
				String userIdCl = "";
				String userNameCl = "";
				if (user.getUserId() != null && !user.getUserId().equals("")) {
					userIdCl = user.getUserId() + ";";
				}
				if (user.getUserName() != null && !user.getUserName().equals("")) {
					userNameCl = user.getUserName() + user.getPhone() + ";";
				}
				history2.setActionUserId(userIdCl);
				history2.setActionUser(userNameCl);
				history2.setActionName(ipUrStatus.getSubUrStatusName());
				history2.setUrStatus(IPFMConstant.STATUS_PM_ASSIGN);
				history2.setActionRemark(IPFMConstant.REPORT_COMPLETE);
				history2.setUserRemark(pm.getMgrRemark());
				history2.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
				history2.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
				history2.setCreated(new Date());
				history2.setCreatedBy(user.getUserId());
				history2.setLastUpd(new Date());
				history2.setLastUpdBy(user.getUserId());
				ipUrActionHistoryDao.insert(history2);

				// String userId = "";
				// String userName = "";
				String actmId = "";
				String actmName = "";

				/*
				 * if(pm.getAssignForTeam().equals("T")){ List<IpUser> userList
				 * = ipUserDao.getRoleIdList("R09"); for(IpUser us : userList){
				 * userId+=(us.getUserId()+";");
				 * userName+=(us.getUserName()+";"); } } else
				 * if(pm.getAssignForTeam().equals("P")){ for(String selected :
				 * pm.getSelectedPerson()){ IpUser us = null; try{ us =
				 * ipUserDao.getIPUser(selected); } catch (Exception ex){ //
				 * exception } finally{ userId+=(us.getUserId()+";");
				 * userName+=(us.getUserName()+";"); } } }
				 */
				
					boolean isInsertDone = false;
				
					if (isImpact != null && isImpact.equals("Y")) {
						if (action.getUrStatus().equals(IPFMConstant.STATUS_WAIT_PM) 
							|| action.getUrStatus().equals(IPFMConstant.STATUS_REJECT_TEAM)) {
							List<IpUser> userList = ipUserDao.getRoleIdList("R08");
							actmSubUrList.add(action.getSubUrNo());
							
							for (IpUser us : userList) {
								actmId += (us.getUserId() + ";");
								actmName += (us.getUserName()+ us.getPhone() +";");
								userActmList.add(us.getUserId());
								actmSubUr.put(us.getUserId(), actmSubUrList);
							}
							
	//						Calendar calendar = Calendar.getInstance();
	//						calendar.setTime(action.getSlaDate());
	//						Integer olaValue = ipUrActionDao.getValueOLADate(action.getUrType(), "N8");
	//						if (olaValue==null) olaValue = 0; 
	//						calendar.add(Calendar.DATE, olaValue.intValue());
	//						Date slaDateACTM = calendar.getTime();
							
							
							Date slaDateACTM = action.getSlaDate();
							List<IpUrActionHistory> historyList = ipUrActionHistoryDao.findHistoryAction(action.getUrNo(),action.getSubUrNo(),action.getUrType(),IPFMConstant.STATUS_WAIT_ACTM);
							if (historyList==null || historyList.size()==0) {
								slaDateACTM = getDateACTM(action.getSlaDate());
							}
							
							//20110107 use old value sla
	//						Date slaDateACTM = getSLADate(new Date(), action.getUrType(), teamName, "N");
							
							Date olaDateACTM = getOLADate("R08", action);
							
							IpUrAction action2 = copyAction(action);
							//20101117:Change get Max Seq
							//seq += 1;
							//20101117 : Get Status From Table IP_UR_STATUS
							IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_ACTM);
							
							action2.setActionSeq(ipUrActionDao.getNextActionSeq(action2.getUrNo(), action2.getUrType(),action2.getSubUrNo()));
							action2.setActionUserId(actmId);
							action2.setActionUser(actmName);
							action2.setActionName(ipUrStatus2.getSubUrStatusName());
							action2.setUrStatus(IPFMConstant.STATUS_WAIT_ACTM);
							action2.setUserRemark(pm.getMgrRemark());
							action2.setCallFunctionId("F035");
							action2.setCallMode(IPFMConstant.ACTION_ACTM);
							action2.setOlaDate(olaDateACTM);
							action2.setSlaDate(slaDateACTM);
							
							action2.setCreated(new Date());
							action2.setCreatedBy(user.getUserId());
							action2.setLastUpd(new Date());
							action2.setLastUpdBy(user.getUserId());
							ipUrActionDao.insert(action2);
	
							if (pmAssetDTOList != null) {
								for (PMAssetDTO pmAssetDTO : pmAssetDTOList) {
									List<String> userIdList = pmAssetDTO.getSelectUserIdList();
									String userId = "";
									String userName = "";
									for (String userS : userIdList) {
										String[] tmp = userS.split(":split:");
										userId += tmp[0] + ";";
										userName += tmp[1] + ";";
									}
									IpUrAction action3 = copyAction(action);
									List<IpUser> ipUserList = ipUserDao.getRoleIdList(pmAssetDTO.getRoleId());
									//String teamName = "";
	//								if (ipUserList != null && ipUserList.size() > 0) {
	//									teamName = ipUserList.get(0).getTeamName();
	//								}
									
	//								Calendar calendar1 = Calendar.getInstance();
	//								calendar1.setTime(action2.getSlaDate());
									//20110109 use old value sla
	//								Integer olaValue1 = ipUrActionDao.getValueOLADate(action.getUrType(), getOLANode(pmAssetDTO.getRoleId()));
	//								if (olaValue1==null) olaValue1 = 0; 
	//								calendar1.add(Calendar.DATE, olaValue1.intValue());
									Date slaDateY = action2.getSlaDate();
									
									//20110107 use old value sla
	//								Date slaDateY = getSLADate(new Date(), action.getUrType(), teamName, "Y");
									
									Date olaDateY = getOLADate(pmAssetDTO.getRoleId(), action);
									
									//20101117 : Get Status From Table IP_UR_STATUS
									IpUrStatus ipUrStatus3  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_ACTM_TEAM);
									
									//20101117:Change get Max Seq
									//seq += 1;
									action3.setActionSeq(ipUrActionDao.getNextActionSeq(action3.getUrNo(), action3.getUrType(),action3.getSubUrNo()));
									action3.setActionUserId(userId);
									action3.setActionUser(userName);
									action3.setActionName(ipUrStatus3.getSubUrStatusName());
									action3.setUrStatus(IPFMConstant.STATUS_ACTM_TEAM);
									action3.setCallFunctionId("F036");
									action3.setCallMode(IPFMConstant.MODE_TEAM);
									action3.setUserRemark(pm.getMgrRemark());
									action3.setOlaDate(olaDateY);
									action3.setSlaDate(slaDateY);
									
									action3.setCreated(new Date());
									action3.setCreatedBy(user.getUserId());
									action3.setLastUpd(new Date());
									action3.setLastUpdBy(user.getUserId());
									ipUrActionDao.insert(action3);
								}
							}
							isInsertDone = true;
						}
					} else {
						if (pmAssetDTOList != null) {
							teamSubUrList.add(action.getSubUrNo());
							for (PMAssetDTO pmAssetDTO : pmAssetDTOList) {
								List<String> userIdList = pmAssetDTO.getSelectUserIdList();
								String userId = "";
								String userName = "";
								for (String userS : userIdList) {
									String[] tmp = userS.split(":split:");
									userId += tmp[0] + ";";
									userName += tmp[1] + ";";
									userTeamList.add(tmp[0]);
									teamSubUr.put(tmp[0], teamSubUrList);
								}
								teamRole.put(pmAssetDTO.getRoleId(),userTeamList);
								
								IpUrAction action2 = copyAction(action);
	//							List<IpUser> ipUserList = ipUserDao.getRoleIdList(pmAssetDTO.getRoleId());
	//							//String teamName = "";
	//							if (ipUserList != null && ipUserList.size() > 0) {
	//								teamName = ipUserList.get(0).getTeamName();
	//							}
								
								
								Calendar calendar1 = Calendar.getInstance();
								calendar1.setTime(action.getSlaDate());
								//20110109 use old value sla
	//							Integer olaValue1 = ipUrActionDao.getValueOLADate(action.getUrType(), getOLANode(pmAssetDTO.getRoleId()));
	//							if (olaValue1==null) olaValue1 = 0; 
	//							calendar1.add(Calendar.DATE, olaValue1.intValue());
								Date slaDateN = calendar1.getTime();
								
								//20110107 use old value sla
	//							Date slaDateN = getSLADate(new Date(), action.getUrType(), teamName, "N");
								
								Date olaDateN = getOLADate(pmAssetDTO.getRoleId(),action);
								
								//20101117 : Get Status From Table IP_UR_STATUS
								IpUrStatus ipUrStatus4  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_TEAM);
								
								//20101117:Change get Max Seq
								//seq += 1;
								action2.setActionSeq(ipUrActionDao.getNextActionSeq(action2.getUrNo(), action2.getUrType(),action2.getSubUrNo()));
								action2.setActionUserId(userId);
								action2.setActionUser(userName);
								action2.setActionName(ipUrStatus4.getSubUrStatusName());
								action2.setUrStatus(IPFMConstant.STATUS_WAIT_TEAM);
								action2.setUserRemark(pm.getMgrRemark());
								action2.setCallFunctionId("F036");
								action2.setCallMode(IPFMConstant.MODE_TEAM);
								action2.setOlaDate(olaDateN);
								action2.setSlaDate(slaDateN);
								action2.setCreated(new Date());
								action2.setCreatedBy(user.getUserId());
								action2.setLastUpd(new Date());
								action2.setLastUpdBy(user.getUserId());
								ipUrActionDao.insert(action2);
								
								isInsertDone = true;
							}
						}
					}
					String sts = (urStatus.equals(IPFMConstant.STATUS_WAIT_PM) && 
							isImpact.equals("Y")) ? IPFMConstant.STATUS_WAIT_ACTM : IPFMConstant.STATUS_WAIT_TEAM;
					
					if (action.getSubUrType().equals("FW")) {
						IpUrFirewall firewall = action.getFirewall();
						firewall.setIsAssign("Y");
						firewall.setPmRemark(pmRemark);
						firewall.setSubUrStatusTxt(sts);
						firewall.setLastUpdBy(user.getUserId());
						firewall.setLastUpd(now);
						firewallDao.update(firewall);
					} else if (action.getSubUrType().equals("AL")) {
						IpUrAccessListCdn cdn = action.getAccessListCdn();
						//cdn.setIsTnp("Y");
						cdn.setIsAssign("Y");
						cdn.setPmRemark(pmRemark);
						cdn.setSubUrStatusTxt(sts);
						cdn.setLastUpdBy(user.getUserId());
						cdn.setLastUpd(now);
						accessListDao.update(cdn);
					} else if (action.getSubUrType().equals("AT")) {
						IpUrAccessListTnp tnp = action.getAccessListTnp();
						// tnp.setIsTnp("Y");
						tnp.setIsAssign("Y");
						tnp.setPmRemark(pmRemark);
						tnp.setSubUrStatusTxt(sts);
						tnp.setLastUpdBy(user.getUserId());
						tnp.setLastUpd(now);
						accessListTNPDao.update(tnp);
					} else if (action.getSubUrType().equals("PI")) {
						IpUrPhysicalInterface phyInterface = action.getPhyInterface();
						// phyInterface.setIsTnp("Y");
						phyInterface.setIsAssign("Y");
						phyInterface.setPmRemark(pmRemark);
						phyInterface.setSubUrStatusTxt(sts);
						phyInterface.setLastUpdBy(user.getUserId());
						phyInterface.setLastUpd(now);
	
						physicalInterfaceDao.update(phyInterface);
					} else if (action.getSubUrType().equals("IG")) {
						IpUrInterfaceGateway gateway = action.getGateway();
						// gateway.setIsTnp("Y");
						gateway.setIsAssign("Y");
						gateway.setPmRemark(pmRemark);
						gateway.setSubUrStatusTxt(sts);
						gateway.setLastUpdBy(user.getUserId());
						gateway.setLastUpd(now);
						intfGatewayDao.update(gateway);
					} else if (action.getSubUrType().equals("RT")) {
						IpUrRouting routing = action.getRouting();
						// routing.setIsTnp("Y");
						routing.setIsAssign("Y");
						routing.setPmRemark(pmRemark);
						routing.setSubUrStatusTxt(sts);
						routing.setLastUpdBy(user.getUserId());
						routing.setLastUpd(now);
						routingDao.update(routing);
					} else if (action.getSubUrType().equals("F5")) {
						IpUrF5 f5 = action.getF5();
						// f5.setIsTnp("Y");
						f5.setIsAssign("Y");
						f5.setPmRemark(pmRemark);
						f5.setSubUrStatusTxt(sts);
						f5.setLastUpdBy(user.getUserId());
						f5.setLastUpd(now);
						f5Dao.update(f5);
					}
				
					if(isInsertDone) {
						ipUrActionDao.deleteByRowID(action.getRowId());
					}
					
					List<IpSubUrAttachment> subFileList = ipSubUrAttachmentDao.getIpUrAttachment(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());			
					if(subFileList != null && subFileList.size() > 0){
						for(IpSubUrAttachment data : subFileList){
								if(subUrNo != null && !subUrNo.equals("")){
									IpSubUrAttachment subData = new IpSubUrAttachment();
									IpSubUrAttachmentId id = new IpSubUrAttachmentId();
									id.setUrNo(data.getId().getUrNo());
									id.setSubUrNo(subUrNo);
									id.setFileName(data.getId().getFileName());
									id.setCategory(data.getId().getCategory());
									
									BigDecimal seqNo = ipSubUrAttachmentDao.getSeqNo(urNo, data.getId().getCategory());
									id.setSeq(seqNo);
									subData.setId(id);
									
									subData.setVersion(new Long(0));
									subData.setDescription(data.getDescription());
									subData.setUrStatus(IPFMConstant.STATUS_WAIT_CONFIG);
									subData.setFileLocation(data.getFileLocation());
									subData.setCreated(now);
									subData.setCreatedBy(user.getUserId());
									subData.setLastUpd(now);
									subData.setLastUpdBy(user.getUserId());
									subData.setSubUrStatus(IPFMConstant.STATUS_WAIT_PM);
									subData.setAction(IPFMConstant.STATUS_PM_ASSIGN);
									ipSubUrAttachmentDao.insert(subData);
									}
							}
						}
				}
			}
			
			ipSubUrAttachmentDao.deleteIpUrAttachmentSubmit(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());
			
			//20101117 : Get Status From Table IP_UR_STATUS
			IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
			
			IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
			config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_CONFIG);
			config.setUrStatusName(ipUrStatus.getUrStatusName());
			config.setLastUpdBy(user.getUserId());
			config.setLastUpd(now);

			ipUrNwConfigDao.update(config);
			for (String userId : userActmList) {
				int count = 0;
				String subUrEmail = "";
				for (String subUr : (List<String>)actmSubUr.get(userId)) {
					count++;
					if (subUrEmail.equalsIgnoreCase("")){
						subUrEmail = subUr;
					}else if (count>5) { 
							subUrEmail = subUrEmail+"... Sub Ur มีมากกว่า 5 รายการ";
							break;
					}else{
						subUrEmail = subUrEmail + "," + subUr;
					}
					
				}
				IpUser ipUser = ipUserDao.getIPUser(userId);
				EmailDetail email = new EmailDetail();
				email.setEmail(ipUser.getEmail());
				email.setUserId(ipUser.getUserId());
				email.setUserName(ipUser.getUserName());
				email.setRequestBy(config.getReqUserName());
				email.setRequestDate(config.getReqDate());
				email.setSubject(config.getReqSubject());
				email.setUrNo(config.getUrNo());
				email.setSubUrNo(subUrEmail);
				email.setUrStatusDesc("Wait For Impact Analyst");						
				email.setUrStatus("WAIT_ACTM");
				email.setSubjectDesc("Wait For Impact Analyst");
				email.setBodyDesc("Wait For Impact Analyst");
				email.setUrType("Request Network Config");
				email.setNodeId("N8");
				if (ipUrActionService.isSendEmail("NC", email.getNodeId())){
//					String key = "IPFM" + email.getUrNo() + ": "+email.getUserId();
					String key = emailService.getKeyCodeEmail();
					String[] to = { email.getEmail() };
					String[] cc = null;
					String from = "ipfm@ais.co.th";
					try {
						Map model = new HashMap();
						model.put("userName", email.getUserName());
						model.put("urNo", email.getUrNo());
						model.put("subUrNo", email.getSubUrNo());
						model.put("urType", email.getUrType());
						model.put("urStatus", email.getUrStatusDesc());
						model.put("subjectDesc", email.getSubjectDesc());
						model.put("bodyDesc", email.getBodyDesc());
						model.put("subject", email.getSubject());
						model.put("requestBy", email.getRequestBy());
						SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy",new Locale("TH", "th"));
				        model.put("requestDate", smf.format(email.getRequestDate()));
						String encodeUrl = new String(Base64.encodeBase64(key.getBytes()));
						model.put("url", pm.getHomeUrl()+ "/home/view.jsf?key=" + encodeUrl);
						emailService.sendMail(to, cc, from,EMailService.TEMPLATE_IPFM_NC_SUBUR, model);

						IpEmailNotify notify = new IpEmailNotify();
						notify.setUserId(email.getUserId());
						notify.setUrNo(email.getUrNo());
						notify.setUrStatus(email.getUrStatus());
						notify.setCreatedBy(user.getUserId());
						notify.setLastUpdBy(user.getUserId());
						notify.setEmailLinkStatus("N");
						notify.setEmailCode(key);
						ipUrActionService.saveEmailNotify(notify);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			Set<String> user09List = (HashSet<String>)teamRole.get("R09");
			Set<String> user10List = (HashSet<String>)teamRole.get("R10");
			Set<String> user11List = (HashSet<String>)teamRole.get("R11");
			Set<String> user12List = (HashSet<String>)teamRole.get("R12");
			Set<String> user13List = (HashSet<String>)teamRole.get("R13");
			Set<String> user14List = (HashSet<String>)teamRole.get("R14");
			Set<String> user15List = (HashSet<String>)teamRole.get("R15");
			Set<String> user16List = (HashSet<String>)teamRole.get("R16");
			Set<String> user17List = (HashSet<String>)teamRole.get("R17");
			
			for (String userId : userTeamList) {
				String nodeId = "";
				if (user09List!=null && user09List.contains(userId)) nodeId = "N9";
				if (user10List!=null && user10List.contains(userId)) nodeId = "N10";
				if (user11List!=null && user11List.contains(userId)) nodeId = "N11";
				if (user12List!=null && user12List.contains(userId)) nodeId = "N12";
				if (user13List!=null && user13List.contains(userId)) nodeId = "N13";
				if (user14List!=null && user14List.contains(userId)) nodeId = "N14";
				if (user15List!=null && user15List.contains(userId)) nodeId = "N15";
				if (user16List!=null && user16List.contains(userId)) nodeId = "N16";
				if (user17List!=null && user17List.contains(userId)) nodeId = "N17";
				
				if (ipUrActionService.isSendEmail("NC", nodeId)){
					int count = 0;
					String subUrEmail = "";
					List<String> list = (List<String>)teamSubUr.get(userId);
					if (list == null) continue;
					for (String subUr : list) {
						count++;
						if (subUrEmail.equalsIgnoreCase("")){
							subUrEmail = subUr;
						}else if (count>5) { 
							subUrEmail = subUrEmail+"... Sub Ur มีมากกว่า 5 รายการ";
							break;
						}else{
							subUrEmail = subUrEmail + "," + subUr;
						}
						
					}
					IpUser ipUser = ipUserDao.getIPUser(userId);
					EmailDetail email = new EmailDetail();
					email.setEmail(ipUser.getEmail());
					email.setUserId(ipUser.getUserId());
					email.setUserName(ipUser.getUserName());
					email.setRequestBy(config.getReqUserName());
					email.setRequestDate(config.getReqDate());
					email.setSubject(config.getReqSubject());
					email.setUrNo(config.getUrNo());
					email.setSubUrNo(subUrEmail);
					email.setUrStatusDesc("Wait For Team Process");						
					email.setUrStatus("WAIT_TEAM");
					email.setSubjectDesc("Wait For Team Process");
					email.setBodyDesc("Wait For Team Process");
					email.setUrType("Request Network Config");
					email.setNodeId("N8");
					if (ipUrActionService.isSendEmail("NC", email.getNodeId())){
//						String key = "IPFM" + email.getUrNo() + ": "+email.getUserId();
						String key = emailService.getKeyCodeEmail();
						String[] to = { email.getEmail() };
						String[] cc = null;
						String from = "ipfm@ais.co.th";
						try {
							Map model = new HashMap();
							model.put("userName", email.getUserName());
							model.put("urNo", email.getUrNo());
							model.put("subUrNo", email.getSubUrNo());
							model.put("urType", email.getUrType());
							model.put("urStatus", email.getUrStatusDesc());
							model.put("subjectDesc", email.getSubjectDesc());
							model.put("bodyDesc", email.getBodyDesc());
							model.put("subject", email.getSubject());
							model.put("requestBy", email.getRequestBy());
							SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy",new Locale("TH", "th"));
					        model.put("requestDate", smf.format(email.getRequestDate()));
							String encodeUrl = new String(Base64.encodeBase64(key.getBytes()));
							model.put("url", pm.getHomeUrl()+ "/home/view.jsf?key=" + encodeUrl);
							emailService.sendMail(to, cc, from,EMailService.TEMPLATE_IPFM_NC_SUBUR, model);
	
							IpEmailNotify notify = new IpEmailNotify();
							notify.setUserId(email.getUserId());
							notify.setUrNo(email.getUrNo());
							notify.setUrStatus(email.getUrStatus());
							notify.setCreatedBy(user.getUserId());
							notify.setLastUpdBy(user.getUserId());
							notify.setEmailLinkStatus("N");
							notify.setEmailCode(key);
							ipUrActionService.saveEmailNotify(notify);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
	}
	
	private String getOLANode(String roleId) {
		String nodeId = null;
		if (roleId.equals("R09")) {
			nodeId = "N9";
		} else if (roleId.equals("R10")) {
			nodeId = "N10";
		} else if (roleId.equals("R11")) {
			nodeId = "N11";
		} else if (roleId.equals("R12")) {
			nodeId = "N12";
		} else if (roleId.equals("R13")) {
			nodeId = "N17";
		} else if (roleId.equals("R14")) {
			nodeId = "N13";
		} else if (roleId.equals("R15")) {
			nodeId = "N15";
		} else if (roleId.equals("R16")) {
			nodeId = "N14";
		} else if (roleId.equals("R17")) {
			nodeId = "N16";
		} else if(roleId.equals("R08")) {
			nodeId = "N8";
		}
		return nodeId;
	}	

	private Date getOLADate(String roleId, IpUrAction action) {
		Date olaDateN = null;
		if (roleId.equals("R09")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N9");
		} else if (roleId.equals("R10")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N10");
		} else if (roleId.equals("R11")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N11");
		} else if (roleId.equals("R12")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N12");
		} else if (roleId.equals("R13")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N17");
		} else if (roleId.equals("R14")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N13");
		} else if (roleId.equals("R15")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N15");
		} else if (roleId.equals("R16")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N14");
		} else if (roleId.equals("R17")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
					"N16");
		} else if(roleId.equals("R08")) {
			olaDateN = ipUrActionDao.getOLADate(new Date(), action.getUrType(),
			"N8");
		}

		return olaDateN;
	}	
	

	private String getIsImpactValue(IpUrAction action) throws Exception {
		String result = null;
		try {
			if (action != null && action.getSubUrType().equals("FW")) {
				result = (action.getFirewall() != null ? action.getFirewall()
						.getIsImpact() : "");
			} else if (action != null && action.getSubUrType().equals("AL")) {
				result = (action.getAccessListCdn() != null ? action
						.getAccessListCdn().getIsImpact() : "");
			} else if (action != null && action.getSubUrType().equals("AT")) {
				result = (action.getAccessListTnp() != null ? action
						.getAccessListTnp().getIsImpact() : "");
			} else if (action != null && action.getSubUrType().equals("PI")) {
				result = (action.getPhyInterface() != null ? action
						.getPhyInterface().getIsImpact() : "");
			} else if (action != null && action.getSubUrType().equals("IG")) {
				result = (action.getGateway() != null ? action.getGateway()
						.getIsImpact() : "");
			} else if (action != null && action.getSubUrType().equals("RT")) {
				result = (action.getRouting() != null ? action.getRouting()
						.getIsImpact() : "");
			} else if (action != null && action.getSubUrType().equals("F5")) {
				result = (action.getF5() != null ? action.getF5().getIsImpact()
						: "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return (result == null) ? "" : result;
	}	
	
	private Object getSubUrObj(IpUrAction action){
		Object result = null;
		if(action.getUrType().equals("FW")){
			result=action.getFirewall();
		}
		return result;
	}

	@Override
	public List<IpvJobAssign> getJobAssignDetail(String urNo, String subUrNo) {
		return ipUrNwConfigDao.getJobAssignDetail(urNo, subUrNo);
	}

	@Override
	public boolean isJobComplete(String urNo, String subUrNo) {
		return ipUrNwConfigDao.isJobComplete(urNo, subUrNo);
	}

	@Override
	public void forceRejectStatus(String urNo, String subUrNo, IpUser user) throws IPFMBusinessException {
		Date now = new Date();
		int unfinish = ipUrActionDao.getUnFinishUR(urNo);
		String actionNamePrev = "";
		String urStatusPrev = "";
		String actionNameNext = "";
		String urStatusNext = "";
		String functionId = "";
		String callMode = "";
		try{
			if(unfinish>0){
				//20101117 : Get Status From Table IP_UR_STATUS
				IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_PM_WAIT);
				
				actionNamePrev = ipUrStatus.getSubUrStatusName();
				urStatusPrev = IPFMConstant.STATUS_REJECT_PM_WAIT;
				actionNameNext = ipUrStatus.getUrStatusName();
				urStatusNext = IPFMConstant.STATUS_REJECT_PM_WAIT;
				functionId = "F030";
				callMode = IPFMConstant.MODE_VIEW;
				
			} else {
				//20101117 : Get Status From Table IP_UR_STATUS
				IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_REJECT_PM);
				actionNameNext = ipUrStatus.getUrStatusName();
				urStatusNext = IPFMConstant.STATUS_REJECT_PM;
				functionId = "F039";
				callMode = IPFMConstant.MODE_USER;
			}
			
			IpUrAction action = ipUrActionDao.findUrAction(urNo, subUrNo);
			IpUrActionHistory history = copyHistory(action);
			history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			history.setLastUpdBy(user.getUserId());
			history.setLastUpd(now);
			ipUrActionHistoryDao.insert(history);
			
			
			if(unfinish>0){
				history.setActionUserId(action.getReqestUserId()+";");
				history.setActionUser(action.getReqestUser() + ";");
				history.setActionName(actionNamePrev);
				history.setUrStatus(urStatusPrev);
				history.setUserRemark("");
				history.setCreatedBy(user.getUserId());
				history.setCreated(now);
				ipUrActionHistoryDao.insert(history);
			}
			IpUser reqUser = ipUserDao.getIPUser(action.getReqestUserId());
			action.setActionUser(reqUser.getUserName()+reqUser.getPhone());
			action.setActionUserId(reqUser.getUserId());			
			action.setActionName(actionNameNext);
			action.setUrStatus(urStatusNext);
			action.setCallFunctionId(functionId);
			action.setCallMode(callMode);
			action.setCreatedBy(user.getUserId());
			action.setCreated(now);
			action.setLastUpdBy(user.getUserId());
			action.setLastUpd(now);
			ipUrActionDao.update(action);
			
			if(action.getSubUrType().equals("FW")){
				IpUrFirewall firewall = action.getFirewall();
				firewall.setSubUrStatusTxt(urStatusNext);
				firewall.setLastUpdBy(user.getUserId());
				firewall.setLastUpd(now);
				firewallDao.update(firewall);
			} else if(action.getSubUrType().equals("AL")){
				IpUrAccessListCdn cdn = action.getAccessListCdn();
				cdn.setSubUrStatusTxt(urStatusNext);
				cdn.setLastUpdBy(user.getUserId());
				cdn.setLastUpd(now);
				accessListDao.update(cdn);
			} else if(action.getSubUrType().equals("AT")){
				IpUrAccessListTnp tnp = action.getAccessListTnp();
				tnp.setSubUrStatusTxt(urStatusNext);
				tnp.setLastUpdBy(user.getUserId());
				tnp.setLastUpd(now);
				accessListTNPDao.update(tnp);
			} else if(action.getSubUrType().equals("PI")){
				IpUrPhysicalInterface phyInterface = action.getPhyInterface();
				phyInterface.setSubUrStatusTxt(urStatusNext);
				phyInterface.setLastUpdBy(user.getUserId());
				phyInterface.setLastUpd(now);
					
				physicalInterfaceDao.update(phyInterface);
			} else if(action.getSubUrType().equals("IG")){
				IpUrInterfaceGateway gateway = action.getGateway();
				gateway.setSubUrStatusTxt(urStatusNext);
				gateway.setLastUpdBy(user.getUserId());
				gateway.setLastUpd(now);
				intfGatewayDao.update(gateway);
			} else if(action.getSubUrType().equals("RT")){
				IpUrRouting routing = action.getRouting();
				routing.setSubUrStatusTxt(urStatusNext);
				routing.setLastUpdBy(user.getUserId());
				routing.setLastUpd(now);
				routingDao.update(routing);
			} else if(action.getSubUrType().equals("F5")){
				IpUrF5 f5 = action.getF5();
				f5.setSubUrStatusTxt(urStatusNext);
				f5.setLastUpdBy(user.getUserId());
				f5.setLastUpd(now);
				f5Dao.update(f5);	
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException(ex);
		}
		
	}

	@Override
	public void forceCompleteStatus(String urNo, String subUrNo, IpUser user, String remark) throws IPFMBusinessException {
		
		Date now = new Date();
		try{
				IpUrAction action = ipUrActionDao.findUrAction(urNo, subUrNo);
				IpUser reqUser = ipUserDao.getIPUser(action.getReqestUserId());
				//List<IpUrAction> actions = ipUrActionDao.getURActionList(urNo);
				//for(IpUrAction action : actions) {
					IpUrActionHistory history = copyHistory(action);
					history.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
					history.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
					IpUrStatus ipUrStatus1  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
					history.setActionName(ipUrStatus1.getSubUrStatusName());
					history.setUrStatus(ipUrStatus1.getSubUrStatusId());
					history.setUserRemark("");
					history.setLastUpdBy(user.getUserId());
					history.setLastUpd(now);
					ipUrActionHistoryDao.insert(history);
					
					IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_COMPLETE_PM);
					history.setActionUserId(user.getUserId()+";");
					history.setActionUser(user.getUserName()+user.getPhone()+";");
					history.setActionName(ipUrStatus.getSubUrStatusName());
					history.setUrStatus(IPFMConstant.STATUS_COMPLETE_PM);
					history.setUserRemark(remark);
					history.setCreatedBy(user.getUserId());
					history.setCreated(now);
					ipUrActionHistoryDao.insert(history);
					
					action.setActionUser(reqUser.getUserName()+reqUser.getPhone());
					action.setActionUserId(action.getReqestUserId());
					action.setActionName(ipUrStatus.getSubUrStatusName());
					action.setUrStatus(IPFMConstant.STATUS_COMPLETE_PM);
					action.setUserRemark(remark);
					action.setCallFunctionId("F030");
					action.setCallMode(IPFMConstant.MODE_VIEW);
					action.setCreatedBy(user.getUserId());
					action.setCreated(now);
					action.setLastUpdBy(user.getUserId());
					action.setLastUpd(now);
					ipUrActionDao.update(action);
					
					if(action.getSubUrType().equals("FW")){
						IpUrFirewall firewall = action.getFirewall();
						firewall.setSubUrStatusTxt(IPFMConstant.STATUS_COMPLETE_PM);
						firewall.setPmRemark(remark);
						firewall.setLastUpdBy(user.getUserId());
						firewall.setLastUpd(now);
						firewallDao.update(firewall);
					} else if(action.getSubUrType().equals("AL")){
						IpUrAccessListCdn cdn = action.getAccessListCdn();
						cdn.setSubUrStatusTxt(IPFMConstant.STATUS_COMPLETE_PM);
						cdn.setPmRemark(remark);
						cdn.setLastUpdBy(user.getUserId());
						cdn.setLastUpd(now);
						accessListDao.update(cdn);
					} else if(action.getSubUrType().equals("AT")){
						IpUrAccessListTnp tnp = action.getAccessListTnp();
						tnp.setSubUrStatusTxt(IPFMConstant.STATUS_COMPLETE_PM);
						tnp.setPmRemark(remark);
						tnp.setLastUpdBy(user.getUserId());
						tnp.setLastUpd(now);
						accessListTNPDao.update(tnp);
					} else if(action.getSubUrType().equals("PI")){
						IpUrPhysicalInterface phyInterface = action.getPhyInterface();
						phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_COMPLETE_PM);
						phyInterface.setPmRemark(remark);
						phyInterface.setLastUpdBy(user.getUserId());
						phyInterface.setLastUpd(now);
							
						physicalInterfaceDao.update(phyInterface);
					} else if(action.getSubUrType().equals("IG")){
						IpUrInterfaceGateway gateway = action.getGateway();
						gateway.setSubUrStatusTxt(IPFMConstant.STATUS_COMPLETE_PM);
						gateway.setPmRemark(remark);
						gateway.setLastUpdBy(user.getUserId());
						gateway.setLastUpd(now);
						intfGatewayDao.update(gateway);
					} else if(action.getSubUrType().equals("RT")){
						IpUrRouting routing = action.getRouting();
						routing.setSubUrStatusTxt(IPFMConstant.STATUS_COMPLETE_PM);
						routing.setPmRemark(remark);
						routing.setLastUpdBy(user.getUserId());
						routing.setLastUpd(now);
						routingDao.update(routing);
					} else if(action.getSubUrType().equals("F5")){
						IpUrF5 f5 = action.getF5();
						f5.setSubUrStatusTxt(IPFMConstant.STATUS_COMPLETE_PM);
						f5.setPmRemark(remark);
						f5.setLastUpdBy(user.getUserId());
						f5.setLastUpd(now);
						f5Dao.update(f5);	
					}
				//}
				
				boolean completeFlag = true;
				List<IpUrAction> completeList = ipUrActionDao.listAction(urNo, null, null);
				for (IpUrAction completeAction : completeList) {
					if (!completeAction.getUrStatus().equalsIgnoreCase(IPFMConstant.STATUS_COMPLETE_TEAM)  && !completeAction.getUrStatus().equalsIgnoreCase(IPFMConstant.STATUS_COMPLETE_PM)) {
						completeFlag = false; break;
					} 
				}
				if (completeFlag) {
					String overSla = "N";
					IpUrStatus userVerifyStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_USER);
					for (IpUrAction completeAction : completeList) {
						completeAction.setActionUserId(reqUser.getUserId());
						completeAction.setActionUser(reqUser.getUserName()+reqUser.getPhone());
						completeAction.setUrStatus(IPFMConstant.STATUS_WAIT_USER);
						completeAction.setActionName(userVerifyStatus.getSubUrStatusName());
						completeAction.setCallFunctionId("F039");
						completeAction.setCallMode("USER");							
						completeAction.setLastUpd(now);
						completeAction.setLastUpdBy(user.getUserId());
						if (overSla.equals("Y")){
							overSla = isDateOverCurrent(completeAction.getSlaDate(), now);
						}
						ipUrActionDao.update(completeAction);					
					}
					IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
					config.setUrStatusTxt(IPFMConstant.STATUS_WAIT_USER);
					config.setUrStatusName(userVerifyStatus.getUrStatusName());
					config.setOverSla(overSla);
					config.setLastUpdBy(user.getUserId());
					config.setLastUpd(now);
					ipUrNwConfigDao.update(config);
				}
						
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException(ex);
		}
		
	}

	@Override
	public String genRowId() {
		return commonDao.getROW_ID();
	}

	@Override
	public Map deletePrevious(String urNo) {
		return ipUrActionDao.deletePreviousUrAction(urNo);
	}

	@Override
	public String isTnp(String ip) {
		String result = "N";
		IpInfo info = ipInfoDao.findIpInfo(ip);
		if(info!=null && info.getT2Team()!=null && info.getT2Team().getPmRoleId()!=null){
			result=(info.getT2Team().getPmRoleId().equals("R06"))?"Y":"N";
		}
		return result;
	}

	@Override
	public void saveDeleagte(String urNo, String roleId, IpUser user) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void deleteUrNo(String urNo) throws IPFMBusinessException {
		try{ 
			ipUrAttachmentDao.deleteIpUrAttachment(urNo);
			firewallDao.deleteByUrNo(urNo);
			accessListDao.deleteByUrNo(urNo);
			accessListTNPDao.deleteByUrNo(urNo);
			physicalInterfaceDao.deleteByUrNo(urNo);
			intfGatewayDao.deleteByUrNo(urNo);
			routingDao.deleteByUrNo(urNo);

			deleteByUrNo(urNo);

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}


	@Override
	public void insertHistoryNwConfig(String urNo,IpUser ipUser) throws IPFMBusinessException {
		IpUrActionHistory  ipUrActionHistory = null;
		try{ 
			List<IpUrAction>  ipUrActionList = ipUrActionDao.getURActionList(urNo);
			if(ipUrActionList.size()!=0){
			   for(IpUrAction obj :ipUrActionList ){
				ipUrActionHistory=    new IpUrActionHistory();
				ipUrActionHistory.setUrNo(obj.getUrNo());
				ipUrActionHistory.setUrType("NC");
				ipUrActionHistory.setSubUrNo(obj.getSubUrNo());
				ipUrActionHistory.setSubUrType(obj.getSubUrType()); /// xxxxxx
				
				//20101117:Change get Max Seq
				//int actionSeq  = Integer.parseInt(obj.getActionSeq().toString())+1;
				//ipUrActionHistory.setActionSeq(new BigDecimal(actionSeq));
				ipUrActionHistory.setActionSeq(ipUrActionDao.getNextActionSeq(obj.getUrNo(), obj.getUrType(),obj.getSubUrNo()));
				ipUrActionHistory.setReqestUserId(ipUser.getUserId());
				ipUrActionHistory.setReqestUser(ipUser.getUserName());
				ipUrActionHistory.setReqestDate(obj.getReqestDate());
				ipUrActionHistory.setActionUserId(obj.getActionUserId());
				ipUrActionHistory.setActionUser(obj.getActionUser());
				ipUrActionHistory.setActionName("Delete UR");
				ipUrActionHistory.setUrStatus("DEL");
				
			    ipUrActionHistoryDao.insert(ipUrActionHistory);
			   }
			    ipUrActionDao.deleteByUrNo(urNo);
			}
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	
	
	@Override
	public void deleteTab(boolean fw,boolean al,boolean at,boolean pi,boolean ig,boolean rt,boolean f5,String urNo,String userId) throws IPFMBusinessException {
		try{ 

	         if(fw){
	        	 List<IpUrFirewall>  ipUrFirewallList  = firewallDao.findByUrNo(urNo);
	        	 for(IpUrFirewall ipUrFirewall : ipUrFirewallList){
			    	 ipUrFirewall.setSubUrStatusTxt("DEL");
			    	 ipUrFirewall.setCreatedBy(userId);
		        	 firewallDao.update(ipUrFirewall);
	        	 }
	         }else{
	        	     firewallDao.deleteByUrNo(urNo); 
	         }

	         if(al){
	        	 List<IpUrAccessListCdn>  ipUrAccessListCdnList  = accessListDao.findByURNo(urNo);
	        	 for(IpUrAccessListCdn ipUrAccessListCdn : ipUrAccessListCdnList){
	        		 ipUrAccessListCdn.setSubUrStatusTxt("DEL");
	        		 ipUrAccessListCdn.setCreatedBy(userId);
	        	     accessListDao.update(ipUrAccessListCdn); 
	        	 }
	         }else{
	        	     accessListDao.deleteByUrNo(urNo);
	         }
	         
			 if(at){	 
	        	 List<IpUrAccessListTnp>  ipUrAccessListTnpList  = accessListTNPDao.findByURNo(urNo);
	        	 for(IpUrAccessListTnp ipUrAccessListCdn : ipUrAccessListTnpList){
	        		 ipUrAccessListCdn.setSubUrStatusTxt("DEL");
	        		 ipUrAccessListCdn.setCreatedBy(userId);
				     accessListTNPDao.update(ipUrAccessListCdn);
	        	 }
			 }else{
				     accessListTNPDao.deleteByUrNo(urNo);
			 }
			 

			 if(pi){
				 List<IpUrPhysicalInterface>  ipUrPhysicalInterfaceList  = physicalInterfaceDao.findByURNo(urNo);
	        	 for(IpUrPhysicalInterface phyInterface : ipUrPhysicalInterfaceList){
	        		 phyInterface.setSubUrStatusTxt("DEL");
	        		 phyInterface.setCreatedBy(userId);
					 physicalInterfaceDao.update(phyInterface); 
	        	 }
			 }else{
				     physicalInterfaceDao.deleteByUrNo(urNo);
			 }
			 
			 if(ig){
				 List<IpUrInterfaceGateway>  ipUrInterfaceGatewayList  = intfGatewayDao.findByURNo(urNo);
	        	 for(IpUrInterfaceGateway ipUrInterfaceGateway : ipUrInterfaceGatewayList){
					 ipUrInterfaceGateway.setSubUrStatusTxt("DEL");
					 ipUrInterfaceGateway.setCreatedBy(userId);
				     intfGatewayDao.update(ipUrInterfaceGateway); 
				 }
			 }else{
				    intfGatewayDao.deleteByUrNo(urNo);
			 }

             if(rt){
            	 List<IpUrRouting>  ipUrRoutingList  = routingDao.findByURNo(urNo);
            	 for(IpUrRouting ipUrRouting :ipUrRoutingList){
	    			 ipUrRouting.setSubUrStatusTxt("DEL");
	    			 ipUrRouting.setCreatedBy(userId);
	            	 routingDao.update(ipUrRouting); 
            	 }
             }else{
            	 routingDao.deleteByUrNo(urNo);
             }
             
			 if(f5){
				 List<IpUrF5>  ipUrF5List  = f5Dao.findByURNo(urNo);
				 for(IpUrF5 ipUrF5 :ipUrF5List){
		             ipUrF5.setSubUrStatusTxt("DEL");
		             ipUrF5.setCreatedBy(userId);
				     f5Dao.update(ipUrF5);
				 }
			 }else{
				 f5Dao.deleteByUrNo(urNo);
			 }
			 
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	
	private String isDateOverCurrent(Date date, Date currentDate) {
		String rs = "";
		if(date != null) {
			SimpleDateFormat engFormat = new SimpleDateFormat("yyyyMMdd", new Locale("en"));
			String dateStr = engFormat.format(date);
			String currStr = engFormat.format(currentDate);
			
			if(Integer.parseInt(currStr) > Integer.parseInt(dateStr)) {
				rs = "Y";
			} else {
				rs = "N";
			}
		} else {
			rs = "";
		}
		return rs;
	}
	
	
	public void checkIPBeforeApprove(String newUrNo)throws IPFMBusinessException {
		try {
			//a move
			List<IpUrAction> ipUrActionList = ipUrActionDao.getURAction(newUrNo);
			for(IpUrAction  ipUrAction :ipUrActionList){
				String ip = getIpAddress(ipUrAction);
				if(ip == null || ip.equals("")) {
					continue;
				}
				Map<String,String> map = ipUrNwConfigDao.getIpOwner(ip);
				if(map==null){
					throw new IPFMBusinessException("ER0210",IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0210"),ip));
				}
			}
			
		} catch (IPFMBusinessException ipe){	    
			if(!(ipe.getMessageCode()!=null && ipe.getMessageCode().equalsIgnoreCase("ER0210"))){
				ipe.printStackTrace();
			}		
			throw ipe;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	
	@Override
	public void sendForApprove(String urNo, String newUrNo, IpUser user,IpUrNwConfig ipUrNwConfig,String managerId,String managername,HashMap<String, Boolean> checked) throws IPFMBusinessException {
		try{ 
			Date now = new Date();
			//String urNumber = (urNo.startsWith("T")) ? this.getUrNO(): urNo;
			
			String urNumber = newUrNo;
			
		// Start Attachment part
			IpUrAttachmentService attachmentService = (IpUrAttachmentService) this.getIpUrAttachmentService();
			//if(urNo.startsWith("T")){
			   //String newUrno = urNumber;
			   attachmentService.updateAttachFileName(urNo, newUrNo);
			//}
		// End 	Attachment Part
			
		// Start NwConfig Part	
			
			IpUrStatus ipUrStatus1  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_MRG);
			
			IpUrNwConfig nwConfig = this.findByUrNo(urNo);

			if (nwConfig == null) {
				ipUrNwConfig.setReqDate(new Date());
				ipUrNwConfig.setUrNo(urNumber);
				ipUrNwConfig.setUrType("NC");
				ipUrNwConfig.setUrStatusTxt(IPFMConstant.STATUS_WAIT_MRG);
				ipUrNwConfig.setUrStatusName(ipUrStatus1.getUrStatusName());				
				nwConfig = this.saveUr(ipUrNwConfig);
			}else{			
				nwConfig.setAlSts(ipUrNwConfig.getAlSts());
				nwConfig.setAtSts(ipUrNwConfig.getAtSts());
				nwConfig.setF5Sts(ipUrNwConfig.getF5Sts());
				nwConfig.setFwSts(ipUrNwConfig.getFwSts());
				nwConfig.setIgSts(ipUrNwConfig.getIgSts());
				nwConfig.setPiSts(ipUrNwConfig.getPiSts());
				nwConfig.setRtSts(ipUrNwConfig.getRtSts());
				nwConfig.setReqSubject(ipUrNwConfig.getReqSubject());
				nwConfig.setReqObjective(ipUrNwConfig.getReqObjective());
				nwConfig.setReqRequirement(ipUrNwConfig.getReqRequirement());
				nwConfig.setIsPeriod(ipUrNwConfig.getIsPeriod());
				nwConfig.setPeriodStart(ipUrNwConfig.getPeriodStart());
				nwConfig.setPeriodEnd(ipUrNwConfig.getPeriodEnd());
				nwConfig.setReqFor(ipUrNwConfig.getReqFor());
				nwConfig.setIsAttach(ipUrNwConfig.getIsAttach());
				
				nwConfig.setReqUserId(user.getUserId());
				nwConfig.setCreatedBy(user.getUserId());
				nwConfig.setReqDate(new Date());
				nwConfig.setUrNo(urNumber);
				ipUrNwConfigDao.update(nwConfig);
			}
			
			if (urNo != urNumber){
				this.deleteNetWorkType(checked, urNo ,urNumber, user.getUserId());
			}
			
			//5.3
			List<IpUrAction> ipUrActionList = ipUrActionDao.getURAction(urNumber);
			//IpUrActionHistory  ipUrActionHistory =new IpUrActionHistory();
			for(IpUrAction  ipUrAction :ipUrActionList){
				IpUrActionHistory  ipUrActionHistory =new IpUrActionHistory();
				ipUrActionHistory.setUrNo(ipUrAction.getUrNo());
				ipUrActionHistory.setUrType(ipUrAction.getUrType());
				ipUrActionHistory.setSubUrNo(ipUrAction.getSubUrNo());
				ipUrActionHistory.setSubUrType(ipUrAction.getSubUrType());
				ipUrActionHistory.setSubject(ipUrAction.getSubject());
				ipUrActionHistory.setActionSeq(ipUrAction.getActionSeq());
				ipUrActionHistory.setReqestUserId(ipUrAction.getReqestUserId());
				ipUrActionHistory.setReqestUser(ipUrAction.getReqestUser());
				ipUrActionHistory.setReqestDate(ipUrAction.getReqestDate());
				ipUrActionHistory.setActionUserId(ipUrAction.getActionUserId());
				ipUrActionHistory.setActionUser(ipUrAction.getActionUser());
				ipUrActionHistory.setActionName(ipUrAction.getActionName());
				ipUrActionHistory.setUrStatus(ipUrAction.getUrStatus());
				ipUrActionHistory.setActionRemark(ipUrAction.getActionRemark());
				ipUrActionHistory.setUserRemark("");
				ipUrActionHistory.setOlaDate(ipUrAction.getOlaDate());
				ipUrActionHistory.setSlaDate(ipUrAction.getSlaDate());
				if(ipUrAction.getOlaDate()!=null){
					ipUrActionHistory.setOverOla(isDateOverCurrent(ipUrAction.getOlaDate(), now));
				}
				if(ipUrAction.getSlaDate()!=null){
					ipUrActionHistory.setOverSla(isDateOverCurrent(ipUrAction.getSlaDate(), now));
				}
				ipUrActionHistory.setCreatedBy(ipUrAction.getCreatedBy());
				ipUrActionHistory.setLastUpdBy(ipUrAction.getLastUpdBy());
				ipUrActionHistory.setCreated(ipUrAction.getCreated());
				ipUrActionHistory.setLastUpd(new Date());
				ipUrActionHistoryDao.insert(ipUrActionHistory);
			}
			//Insert New Record to History
		    //20110110
//		    String tnp = callProcedureDao.checkIsTnp(urNo);
		     
			IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_SENT_MRG );
			for(IpUrAction  ipUrAction :ipUrActionList){
				
//				String tnp  = ipUrAction.getSubUrType().equalsIgnoreCase("AT") ? "Y" : "N";
				
				//20101117:Change get Max Seq
				//int  actionSeq = Integer.parseInt(ipUrAction.getActionSeq().toString())+ 1;
				IpUrActionHistory ipUrActionHistory =new IpUrActionHistory();
				ipUrActionHistory.setUrNo(ipUrAction.getUrNo());
				ipUrActionHistory.setUrType(ipUrAction.getUrType());
				ipUrActionHistory.setSubUrNo(ipUrAction.getSubUrNo());
				ipUrActionHistory.setSubUrType(ipUrAction.getSubUrType());
				ipUrActionHistory.setSubject(ipUrAction.getSubject());
				//ipUrActionHistory.setActionSeq(new BigDecimal(actionSeq));
				ipUrActionHistory.setActionSeq(ipUrActionDao.getNextActionSeq(ipUrAction.getUrNo(), ipUrAction.getUrType(),ipUrAction.getSubUrNo()));
				ipUrActionHistory.setReqestUserId(ipUrAction.getReqestUserId());
				ipUrActionHistory.setReqestUser(ipUrAction.getReqestUser());
				ipUrActionHistory.setReqestDate(ipUrAction.getReqestDate());
				ipUrActionHistory.setActionUserId(user.getUserId());
				ipUrActionHistory.setActionUser(user.getUserName()+" ("+user.getTel()+")");
				ipUrActionHistory.setActionName(ipUrStatus2.getSubUrStatusName());
				ipUrActionHistory.setUrStatus(IPFMConstant.STATUS_SENT_MRG);
				ipUrActionHistory.setActionRemark(ipUrAction.getActionRemark());
				ipUrActionHistory.setUserRemark(ipUrAction.getUserRemark());
				//20101118 -> Update OLA 
				ipUrActionHistory.setOlaDate(this.getOLADate(now, "NC","N4"));
				//20101118 -> Update SLA 
				String team = "";
//				if("Y".equals(tnp))team ="TNP"; else team="CDN";
//				ipUrActionHistory.setSlaDate(this.getSLADate(now, "NC",team,"N"));
				//ipUrActionHistory.setSlaDate(ipUrAction.getSlaDate());
				
				// find PM Team for assign job
				if(ipUrActionHistory.getSubUrType().equals("FW") || ipUrActionHistory.getSubUrType().equals("AL") || ipUrActionHistory.getSubUrType().equals("F5")){
					team="CDN";
				} else if(ipUrActionHistory.getSubUrType().equals("AT")){
					team="TNP";
				} else if((ipUrActionHistory.getSubUrType().equals("PI")) || ipUrActionHistory.getSubUrType().equals("IG") || ipUrActionHistory.getSubUrType().equals("RT")){	
					String ipAddress = "";
					if(ipUrActionHistory.getSubUrType().equals("PI")){
						ipAddress = ipUrAction.getPhyInterface().getIpNode();
					} else if(ipUrActionHistory.getSubUrType().equals("IG")){
						ipAddress = ipUrAction.getGateway().getIpAddress();
					} else if(ipUrActionHistory.getSubUrType().equals("RT")){
						ipAddress = ipUrAction.getRouting().getIpAddress();
					}
					IpInfo info = ipInfoDao.getIpInfo(ipAddress);
					if (info.getT2Team().getPmRoleId().equalsIgnoreCase("R06")) team="TNP";
					else team="CDN";
				} 
				
				ipUrActionHistory.setSlaDate(this.getSLADate(now, "NC",team,"N"));
				
				if(ipUrAction.getOlaDate()!=null){
				  ipUrActionHistory.setOverOla(isDateOverCurrent(ipUrAction.getOlaDate(), now));
				} else { ipUrActionHistory.setOverOla("N");}
				if(ipUrAction.getSlaDate()!=null){
				  ipUrActionHistory.setOverSla(isDateOverCurrent(ipUrAction.getSlaDate(), now));
				} else { ipUrActionHistory.setOverSla("N");}
				ipUrActionHistory.setCreatedBy(user.getUserId());
				ipUrActionHistory.setLastUpdBy(user.getUserId());
				ipUrActionHistoryDao.insert(ipUrActionHistory);
			}
			
		    //  ipUrActionDao.deleteByUrNo(urNo);
			  
			    IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_MRG);
			    
			for(IpUrAction  action :ipUrActionList){ 
				action.setUrType("NC");
				//20101117:Change get Max Seq
				//int  actionSeq = Integer.parseInt(action.getActionSeq().toString())+ 2;
				//action.setActionSeq(new BigDecimal(actionSeq));
				
				action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(),action.getSubUrNo()));
				action.setReqestUserId(user.getUserId());
				action.setReqestUser(user.getUserName());
				action.setReqestDate(new Date());
				action.setActionUserId(managerId);
				action.setActionUser(managername);
				action.setUrStatus(IPFMConstant.STATUS_WAIT_MRG);
				action.setActionName(ipUrStatus.getSubUrStatusName());
				action.setActionRemark(null);
				action.setUserRemark(null);
				action.setOlaDate(this.getOLADate(now, "NC","N4"));
				String team = "";

				// find PM Team for assign job
				if(action.getSubUrType().equals("FW") || action.getSubUrType().equals("AL") || action.getSubUrType().equals("F5")){
					team="CDN";
				} else if(action.getSubUrType().equals("AT")){
					team="TNP";
				} else if((action.getSubUrType().equals("PI")) || action.getSubUrType().equals("IG") || action.getSubUrType().equals("RT")){	
					String ipAddress = "";
					if(action.getSubUrType().equals("PI")){
						ipAddress = action.getPhyInterface().getIpNode();
					} else if(action.getSubUrType().equals("IG")){
						ipAddress = action.getGateway().getIpAddress();
					} else if(action.getSubUrType().equals("RT")){
						ipAddress = action.getRouting().getIpAddress();
					}
					IpInfo info = ipInfoDao.getIpInfo(ipAddress);
					if (info.getT2Team().getPmRoleId().equalsIgnoreCase("R06")) team="TNP";
					else team="CDN";
				} 
				

//				if("Y".equals(tnp))team ="TNP"; else team="CDN";
				
				action.setSlaDate(this.getSLADate(now, "NC",team,"N"));
				action.setCallFunctionId("F025");
				action.setCallMode("ManagerApprove");
				action.setCreatedBy(user.getUserId());
				action.setLastUpdBy(user.getUserId());
			    ipUrActionDao.update(action);
			    
			    this.updateTab(action.getUrNo(), user.getUserId(), IPFMConstant.STATUS_WAIT_MRG, action.getSubUrNo());
			    
			}
			    //IpUrNwConfig objIpUrNwConfig  = ipUrNwConfigDao.findByUrNo(urNumber);
//20110112
//			    nwConfig.setTnpStatus(tnp);
			    nwConfig.setTnpStatus("N");
			    nwConfig.setUrStatusTxt(IPFMConstant.STATUS_WAIT_MRG);
			    nwConfig.setUrStatusName(ipUrStatus.getUrStatusName());
			    ipUrNwConfigDao.update(nwConfig);
			    
			    ipUrActionDao.deleteNA(urNo);
			    ipUrActionHistoryDao.deleteNA(urNo);
		} catch (IPFMBusinessException ipe){	    
			if(!(ipe.getMessageCode()!=null && ipe.getMessageCode().equalsIgnoreCase("ER0210"))){
				ipe.printStackTrace();
			}		
			throw ipe;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	
	
	public void deleteNetWorkType(HashMap<String, Boolean> checked,String urNo,String newUrNo, String userId) throws IPFMBusinessException {
		try{ 

	         if(checked.get("fw")){
	 			List<IpUrFirewall>  ipUrFirewallList  = firewallDao.findByUrNo(urNo);	
	        	 for(IpUrFirewall ipUrFirewall : ipUrFirewallList){	        		 
	        		 IpUrFirewall fw = (IpUrFirewall) ipUrFirewall.clone();
	        		 fw.setId(new IpUrFirewallId(newUrNo, ipUrFirewall.getId().getSubUrNo()));
			    	 fw.setSubUrStatusTxt("WAIT_MRG");
			    	 fw.setCreatedBy(userId);
			    	 fw.setRowId(null);
		        	 firewallDao.insert(fw);
		        	 firewallDao.delete(ipUrFirewall);
	        	 }
	         }else{
	        	     firewallDao.deleteByUrNo(urNo); 
	         }

	         if(checked.get("al")){
	        	 List<IpUrAccessListCdn>  ipUrAccessListCdnList  = accessListDao.findByURNo(urNo);
	        	 for(IpUrAccessListCdn ipUrAccessListCdn : ipUrAccessListCdnList){
	        		 IpUrAccessListCdn al = (IpUrAccessListCdn) ipUrAccessListCdn.clone();
	        		 al.setId(new IpUrAccessListCdnId(newUrNo, ipUrAccessListCdn.getId().getSubUrNo()));
	        		 al.setSubUrStatusTxt("WAIT_MRG");
	        		 al.setCreatedBy(userId);
	        		 al.setRowId(null);
		        	 accessListDao.insert(al);
		        	 accessListDao.delete(ipUrAccessListCdn);	        		 
	        	 }
	         }else{
	        	     accessListDao.deleteByUrNo(urNo);
	         }
	         
			 if(checked.get("at")){	 
	        	 List<IpUrAccessListTnp>  ipUrAccessListTnpList  = accessListTNPDao.findByURNo(urNo);
	        	 for(IpUrAccessListTnp ipUrAccessListTnp : ipUrAccessListTnpList){
	        		 IpUrAccessListTnp at = (IpUrAccessListTnp) ipUrAccessListTnp.clone();
	        		 at.setId(new IpUrAccessListTnpId(newUrNo, ipUrAccessListTnp.getId().getSubUrNo()));
	        		 at.setSubUrStatusTxt("WAIT_MRG");
	        		 at.setCreatedBy(userId);
	        		 at.setRowId(null);
	        		 accessListTNPDao.insert(at);
	        		 accessListTNPDao.delete(ipUrAccessListTnp);	       	        		 
	        	 }
			 }else{
				     accessListTNPDao.deleteByUrNo(urNo);
			 }

			 if(checked.get("pi")){
				 List<IpUrPhysicalInterface>  ipUrPhysicalInterfaceList  = physicalInterfaceDao.findByURNo(urNo);
	        	 for(IpUrPhysicalInterface phyInterface : ipUrPhysicalInterfaceList){
	        		 IpUrPhysicalInterface pi = (IpUrPhysicalInterface) phyInterface.clone();
	        		 pi.setId(new IpUrPhysicalInterfaceId(newUrNo, phyInterface.getId().getSubUrNo()));
	        		 pi.setSubUrStatusTxt("WAIT_MRG");
	        		 pi.setCreatedBy(userId);
	        		 pi.setRowId(null);
	        		 physicalInterfaceDao.insert(pi);
	        		 physicalInterfaceDao.delete(phyInterface);	  	        		 
	        		 
	        	 }
			 }else{
				     physicalInterfaceDao.deleteByUrNo(urNo);
			 }
			 
			 if(checked.get("ig")){
				 List<IpUrInterfaceGateway>  ipUrInterfaceGatewayList  = intfGatewayDao.findByURNo(urNo);
	        	 for(IpUrInterfaceGateway ipUrInterfaceGateway : ipUrInterfaceGatewayList){
	        		 IpUrInterfaceGateway ig = (IpUrInterfaceGateway) ipUrInterfaceGateway.clone();
	        		 ig.setId(new IpUrInterfaceGatewayId(newUrNo, ipUrInterfaceGateway.getId().getSubUrNo()));
	        		 ig.setSubUrStatusTxt("WAIT_MRG");
	        		 ig.setCreatedBy(userId);
	        		 ig.setRowId(null);
	        		 intfGatewayDao.insert(ig);
	        		 intfGatewayDao.delete(ipUrInterfaceGateway);	  
	        		 
				 }
			 }else{
				    intfGatewayDao.deleteByUrNo(urNo);
			 }

             if(checked.get("rt")){
            	 List<IpUrRouting>  ipUrRoutingList  = routingDao.findByURNo(urNo);
            	 for(IpUrRouting ipUrRouting :ipUrRoutingList){
            		 IpUrRouting rt = (IpUrRouting) ipUrRouting.clone();
            		 rt.setId(new IpUrRoutingId(newUrNo, ipUrRouting.getId().getSubUrNo()));
            		 rt.setSubUrStatusTxt("WAIT_MRG");
            		 rt.setCreatedBy(userId);
            		 rt.setRowId(null);
	        		 routingDao.insert(rt);
	        		 routingDao.delete(ipUrRouting);	 
            	 }
             }else{
            	 routingDao.deleteByUrNo(urNo);
             }
             
			 if(checked.get("f5")){
				 List<IpUrF5>  ipUrF5List  = f5Dao.findByURNo(urNo);
				 for(IpUrF5 ipUrF5 :ipUrF5List){
					 IpUrF5 f5 = (IpUrF5) ipUrF5.clone();
					 f5.setId(new IpUrF5Id(newUrNo, ipUrF5.getId().getSubUrNo()));
					 f5.setSubUrStatusTxt("WAIT_MRG");
					 f5.setCreatedBy(userId);
					 f5.setRowId(null);
					 f5Dao.insert(f5);
					 f5Dao.delete(ipUrF5);	 
				 }
			 }else{
				 f5Dao.deleteByUrNo(urNo);
			 }
			 
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	
	
	
	public void updateTab(String urNo,String userId,String urStatus,String  subUrno ) throws IPFMBusinessException {
		try{ 

	        	 List<IpUrFirewall>  ipUrFirewallList  = firewallDao.findByUrNoSubUr(urNo,subUrno);
	        	 for(IpUrFirewall ipUrFirewall : ipUrFirewallList){
			    	 ipUrFirewall.setSubUrStatusTxt(urStatus);
			    	 ipUrFirewall.setCreatedBy(userId);
		        	 firewallDao.update(ipUrFirewall);
	        	 }
	        	 
	        	 List<IpUrAccessListCdn>  ipUrAccessListCdnList  = accessListDao.findByURNo(urNo);
	        	 for(IpUrAccessListCdn ipUrAccessListCdn : ipUrAccessListCdnList){
	        		 ipUrAccessListCdn.setSubUrStatusTxt(urStatus);
	        		 ipUrAccessListCdn.setCreatedBy(userId);
	        	     accessListDao.update(ipUrAccessListCdn); 
	        	 }
	          
	        	 List<IpUrAccessListTnp>  ipUrAccessListTnpList  = accessListTNPDao.findByURNo(urNo);
	        	 for(IpUrAccessListTnp ipUrAccessListCdn : ipUrAccessListTnpList){
	        		 ipUrAccessListCdn.setSubUrStatusTxt(urStatus);
	        		 ipUrAccessListCdn.setCreatedBy(userId);
				     accessListTNPDao.update(ipUrAccessListCdn);
	        	 }

				 List<IpUrPhysicalInterface>  ipUrPhysicalInterfaceList  = physicalInterfaceDao.findByURNo(urNo);
	        	 for(IpUrPhysicalInterface phyInterface : ipUrPhysicalInterfaceList){
	        		 phyInterface.setSubUrStatusTxt(urStatus);
	        		 phyInterface.setCreatedBy(userId);
					 physicalInterfaceDao.update(phyInterface); 
	        	 }
	        	 
				 List<IpUrInterfaceGateway>  ipUrInterfaceGatewayList  = intfGatewayDao.findByURNo(urNo);
	        	 for(IpUrInterfaceGateway ipUrInterfaceGateway : ipUrInterfaceGatewayList){
					 ipUrInterfaceGateway.setSubUrStatusTxt(urStatus);
					 ipUrInterfaceGateway.setCreatedBy(userId);
				     intfGatewayDao.update(ipUrInterfaceGateway); 
				 }

            	 List<IpUrRouting>  ipUrRoutingList  = routingDao.findByURNo(urNo);
            	 for(IpUrRouting ipUrRouting :ipUrRoutingList){
	    			 ipUrRouting.setSubUrStatusTxt(urStatus);
	    			 ipUrRouting.setCreatedBy(userId);
	            	 routingDao.update(ipUrRouting); 
            	 }

				 List<IpUrF5>  ipUrF5List  = f5Dao.findByURNo(urNo);
				 for(IpUrF5 ipUrF5 :ipUrF5List){
		             ipUrF5.setSubUrStatusTxt(urStatus);
		             ipUrF5.setCreatedBy(userId);
				     f5Dao.update(ipUrF5);
				 }

			 
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	
	@Override
	public IpUrAction findUrActionNotify(String urNo,String status,String userId) throws IPFMBusinessException{
		IpUrAction ipUrAction =null;
		try{ 
			ipUrAction = ipUrActionDao.findUrActionNotify(urNo, status, userId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
		
		return  ipUrAction;
	}
	
	
	@Override
	public String  findActmRemark(String urNo,String subUrNo) throws IPFMBusinessException{
		String actmRemark ="";
		if(subUrNo.startsWith("FW")){
			IpUrFirewall ipUrFirewall = firewallDao.findIpUrFirewall(urNo, subUrNo);
			actmRemark = ipUrFirewall.getActmRemark();
		}
		if(subUrNo.startsWith("AL")){
			IpUrAccessListCdn ipUrAccessListCdn = accessListDao.findIpUrAccessList(urNo, subUrNo);
			actmRemark = ipUrAccessListCdn.getActmRemark();
		}
		if(subUrNo.startsWith("AT")){
			IpUrAccessListTnp  ipUrAccessListTnp = accessListTNPDao.findIpUrAccessListTnp(urNo, subUrNo);
			actmRemark = ipUrAccessListTnp.getActmRemark();
		}
		if(subUrNo.startsWith("PI")){  
			IpUrPhysicalInterface ipUrPhysicalInterface = physicalInterfaceDao.findIpUrPhysicalInterface(urNo, subUrNo);
			actmRemark = ipUrPhysicalInterface.getActmRemark();
		}
		if(subUrNo.startsWith("IG")){
			IpUrInterfaceGateway  ipUrInterfaceGateway = intfGatewayDao.fineIpUrInterfaceGateway(urNo, subUrNo);
			actmRemark = ipUrInterfaceGateway.getActmRemark();
		}
		if(subUrNo.startsWith("RT")){
			IpUrRouting ipUrRouting = routingDao.fineIpUrRouting(urNo, subUrNo);
			actmRemark = ipUrRouting.getActmRemark();
		}
		if(subUrNo.startsWith("F5")){
			IpUrF5 ipUrF5 = f5Dao.fineIpUrF5(urNo, subUrNo);
			actmRemark = ipUrF5.getActmRemark();
		}
	
		return  actmRemark;
	}

	@Override
	public void cancelImpact(String urNo) throws IPFMBusinessException {
		try {
			firewallDao.cancelImpact(urNo);
			accessListDao.cancelImpact(urNo);
			accessListTNPDao.cancelImpact(urNo);
			physicalInterfaceDao.cancelImpact(urNo);
			intfGatewayDao.cancelImpact(urNo);
			routingDao.cancelImpact(urNo);
			f5Dao.cancelImpact(urNo);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0010")));
		}
		
	}

	@Override
	public void cancelImpact(List<IpUrFirewall> listFirewall,
			List<IpUrAccessListCdn> listAccessList,
			List<IpUrAccessListTnp> listAccessTNP,
			List<IpUrPhysicalInterface> listPhysicalInterface,
			List<IpUrInterfaceGateway> listGateway,
			List<IpUrRouting> listRouting, List<IpUrF5> listF5)
			throws IPFMBusinessException {

		try {
			if (listFirewall!=null && listFirewall.size()>0 ) {
				for (IpUrFirewall subUr : listFirewall) {
					if (subUr.getIsImpact()!=null && subUr.getIsImpact().equalsIgnoreCase("Y")) {
						subUr.setIsImpact("N");
						firewallDao.update(subUr);
					}
				}
			}
			if (listAccessList!=null && listAccessList.size()>0 ) {
				for (IpUrAccessListCdn subUr : listAccessList) {
					if (subUr.getIsImpact()!=null && subUr.getIsImpact().equalsIgnoreCase("Y")) {
						subUr.setIsImpact("N");
						accessListDao.update(subUr);
					}
				}
			}
			if (listAccessTNP!=null && listAccessTNP.size()>0 ) {
				for (IpUrAccessListTnp subUr : listAccessTNP) {
					if (subUr.getIsImpact()!=null && subUr.getIsImpact().equalsIgnoreCase("Y")) {
						subUr.setIsImpact("N");
						accessListTNPDao.update(subUr);
					}
				}
			}
			if (listPhysicalInterface !=null && listPhysicalInterface.size()>0 ) {
				for (IpUrPhysicalInterface subUr : listPhysicalInterface) {
					if (subUr.getIsImpact()!=null && subUr.getIsImpact().equalsIgnoreCase("Y")) {
						subUr.setIsImpact("N");
						physicalInterfaceDao.update(subUr);
					}
				}
			}
			if (listGateway!=null && listGateway.size()>0 ) {
				for (IpUrInterfaceGateway subUr : listGateway) {
					if (subUr.getIsImpact()!=null && subUr.getIsImpact().equalsIgnoreCase("Y")) {
						subUr.setIsImpact("N");
						intfGatewayDao.update(subUr);
					}
				}
			}
			if (listRouting!=null && listRouting.size()>0 ) {
				for (IpUrRouting subUr : listRouting) {
					if (subUr.getIsImpact()!=null && subUr.getIsImpact().equalsIgnoreCase("Y")) {
						subUr.setIsImpact("N");
						routingDao.update(subUr);
					}
				}
			}
			if (listF5 !=null && listF5.size()>0 ) {
				for (IpUrF5 subUr : listF5) {
					if (subUr.getIsImpact()!=null && subUr.getIsImpact().equalsIgnoreCase("Y")) {
						subUr.setIsImpact("N");
						f5Dao.update(subUr);
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0010")));
		}
	}
	public List<IpUrActionHistory> findHistoryAction(String urNo, String urType) throws IPFMBusinessException{
		List<IpUrActionHistory> result = ipUrActionHistoryDao.findHistoryAction(urNo,urType);
		return result;
	}
	
	public List<IpvJobAssign> findListTeamReject(String urNo, String subUrNo){
		List<IpUrActionHistory> urActionHistoryList = ipUrActionHistoryDao.findListTeamReject(urNo, subUrNo);
		List<IpvJobAssign> result = new ArrayList<IpvJobAssign>();
		IpvJobAssign ipvJobAssign = null;
		for (IpUrActionHistory history : urActionHistoryList) {
			ipvJobAssign = new IpvJobAssign();
			ipvJobAssign.setUrNo(subUrNo);
			ipvJobAssign.setSubUrNo(subUrNo);
			ipvJobAssign.setUrStatus(history.getUrStatus());
			ipvJobAssign.setActionUser(history.getActionUser());
			ipvJobAssign.setActionName(history.getActionName());
			ipvJobAssign.setCreateDate(history.getLastUpd());
			ipvJobAssign.setActionRemark(history.getUserRemark());
			result.add(ipvJobAssign);
		}
		return result;
	}

	@Override
	public List<IpvJobAssign> getJobAssignList(String urNo, String subUrNo) {
		List<IpUrAction> urActionList = ipUrActionDao.findUrActions(urNo, subUrNo);
		List<IpvJobAssign> result = new ArrayList<IpvJobAssign>();
		IpvJobAssign ipvJobAssign = null;
		IpUser user = null;
		for (IpUrAction urAction : urActionList) {
			if (urAction.getActionUserId()!=null && urAction.getActionUserId().trim().length()>0){
				if (urAction.getActionUserId().indexOf(";")>0){
					String[] userIdArr = urAction.getActionUserId().split(";");
//					String[] userNameArr = urAction.getActionUser().split(";");
					for (int i=0 ; i<userIdArr.length ; i++) {
						try {
							user = ipUserDao.getIPUser(userIdArr[i]);
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
						ipvJobAssign = new IpvJobAssign();
						ipvJobAssign.setUrNo(subUrNo);
						ipvJobAssign.setCreateDate(urAction.getCreated());
//						ipvJobAssign.setActionRemark(urAction.getActionRemark());
						ipvJobAssign.setActionRemark(urAction.getUserRemark());
						ipvJobAssign.setActionUser(user.getUserName() + user.getPhone());
						ipvJobAssign.setUrStatus(urAction.getUrStatus());
						ipvJobAssign.setActionName(urAction.getActionName());
						result.add(ipvJobAssign);
					}
				}else{
					ipvJobAssign = new IpvJobAssign();
					ipvJobAssign.setUrNo(subUrNo);
					ipvJobAssign.setCreateDate(urAction.getCreated());
//					ipvJobAssign.setActionRemark(urAction.getActionRemark());
					ipvJobAssign.setActionRemark(urAction.getUserRemark());
					ipvJobAssign.setActionUser(urAction.getActionUser());
					ipvJobAssign.setUrStatus(urAction.getUrStatus());
					ipvJobAssign.setActionName(urAction.getActionName());
					result.add(ipvJobAssign);
				}
			}
		}
		return result;
	}
	
	
	@Override
	public IpInfo getIpInfo(String ipAddress) throws IPFMBusinessException {
		IpInfo ipInfo = null;
		try {
			ipInfo = ipInfoDao.getIpInfo(ipAddress);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0010")));
		}
		return ipInfo;
	}
	
	@Override
	public void updateTrunkFlag(String urNo, String type) throws IPFMBusinessException {
		try{
			if(StringUtils.isNotEmpty(urNo) && StringUtils.isNotEmpty(type)){
				callProcedureDao.updateTrunkFlag(urNo, type);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<IpRoleMember> getRoleMember(String roleId) throws IPFMBusinessException {
		List<IpRoleMember> roleList  = new ArrayList<IpRoleMember>();
		try{
			if(StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId)){
				roleList = ipUrNwConfigDao.finByRoleId(roleId);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return roleList;
	}

	@Override
	public IPRole getRoleData(String roleId) throws IPFMBusinessException {

		IPRole result = new IPRole();
		try{
			if(StringUtils.isNotEmpty(roleId) && StringUtils.isNotEmpty(roleId)){
				result = ipUrNwConfigDao.getRoleData(roleId);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	
	}

	@Override
	public void saveDeleagteNew(String urNo, List<String> selectedSubUr,String pmUserId, String pmUser, IpUser user,String roleId,String remark) throws IPFMBusinessException {

		Date now = new Date();
		Date slaDate = null;
		List<EmailDetail> emailList = new ArrayList<EmailDetail>();

		String emailSubject= "";
		try {
		
		for(String subUr : selectedSubUr){
			IpUrAction action = ipUrActionDao.findUrAction(urNo, subUr, user.getUserId());
			emailSubject = action.getSubject();
			IpUrActionHistory history = copyHistory(action);
			IpUrStatus ipUrStatus1  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);
			history.setActionName(ipUrStatus1.getSubUrStatusName());
			history.setUrStatus(ipUrStatus1.getSubUrStatusId());
			history.setActionRemark(IPFMConstant.REPORT_COMPLETE);
			history.setLastUpd(now);
			history.setLastUpdBy(user.getUserId());
			ipUrActionHistoryDao.insert(history);
			
			action.setActionUser(user.getUserName()+user.getPhone()+";");
			action.setActionUserId(user.getUserId()+";");
			action.setLastUpdBy(user.getUserId());
			action.setLastUpd(now);
		
			IpUrActionHistory history2 = copyHistory(action);
			
			history2.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			history2.setOverOla(isDateOverCurrent(action.getOlaDate(), now));

			boolean isR06 = (user.getRoleIdList().contains("R06"));
			boolean isR07 = (user.getRoleIdList().contains("R07"));
			
			if(isR06 && isR07){
				int N6 = ipUrActionDao.getOLADateFromNodeID("N6");
				int N7 = ipUrActionDao.getOLADateFromNodeID("N7");
				action.setOlaDate(this.getOLADate(now, "NC", (N6<N7)?"N6":"N7"));
			} else if(isR06){
				action.setOlaDate(this.getOLADate(now, "NC", "N7"));
			} else if(isR07){
				action.setOlaDate(this.getOLADate(now, "NC", "N6"));
			}
			

			IpUrStatus ipUrStatus  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_PM_DELEGATE);
			history2.setActionName(ipUrStatus.getSubUrStatusName());
			history2.setUrStatus(IPFMConstant.STATUS_PM_DELEGATE);
			history2.setActionRemark("Delegate Sub UR to Other PM");
			history2.setUserRemark(remark);
			if(action.getActionSeq() != null) { 
				history2.setActionSeq(ipUrActionDao.getNextActionSeq(history2.getUrNo(), history2.getUrType(),history2.getSubUrNo()));
			} else {
				history2.setActionSeq(new BigDecimal(0));
			}
			history2.setOverOla(isDateOverCurrent(action.getOlaDate(), now));
			history2.setOverSla(isDateOverCurrent(action.getSlaDate(), now));
			history2.setCreatedBy(user.getUserId());
			history2.setCreated(now);
			history2.setLastUpdBy(user.getUserId());
			history2.setLastUpd(now);
			ipUrActionHistoryDao.insert(history2);
			
			IpUrStatus ipUrStatus2  =  this.getSubUrStatus("NC", IPFMConstant.STATUS_WAIT_PM);					
			action.setActionSeq(ipUrActionDao.getNextActionSeq(action.getUrNo(), action.getUrType(),action.getSubUrNo()));
			action.setActionUserId(pmUserId);
			action.setActionUser(pmUser);
			action.setActionName(ipUrStatus2.getSubUrStatusName());
			action.setUrStatus(IPFMConstant.STATUS_WAIT_PM);
			action.setActionRemark(IPFMConstant.REPORT_COMPLETE);
			action.setUserRemark("");
			action.setCallFunctionId("F034");
			action.setCallMode(IPFMConstant.MODE_CALL_PM);
			action.setCreatedBy(user.getUserId());
			action.setCreated(now);
			action.setLastUpdBy(user.getUserId());
			action.setLastUpd(now);
			ipUrActionDao.update(action);
			
			if(action.getSubUrType().equals("FW")){
				IpUrFirewall firewall = action.getFirewall();
				firewall.setIsTnp("Y");
				firewall.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				firewall.setLastUpdBy(user.getUserId());
				firewall.setLastUpd(now);
				firewallDao.update(firewall);
			} else if(action.getSubUrType().equals("AL")){
				IpUrAccessListCdn cdn = action.getAccessListCdn();
				cdn.setIsTnp("Y");
				cdn.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				cdn.setLastUpdBy(user.getUserId());
				cdn.setLastUpd(now);
				accessListDao.update(cdn);
			} else if(action.getSubUrType().equals("AT")){
				IpUrAccessListTnp tnp = action.getAccessListTnp();
				tnp.setIsTnp("Y");
				tnp.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				tnp.setLastUpdBy(user.getUserId());
				tnp.setLastUpd(now);
				accessListTNPDao.update(tnp);
			} else if(action.getSubUrType().equals("PI")){
				IpUrPhysicalInterface phyInterface = action.getPhyInterface();
				phyInterface.setIsTnp("Y");
				phyInterface.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				phyInterface.setLastUpdBy(user.getUserId());
				phyInterface.setLastUpd(now);
					
				physicalInterfaceDao.update(phyInterface);
			} else if(action.getSubUrType().equals("IG")){
				IpUrInterfaceGateway gateway = action.getGateway();
				gateway.setIsTnp("Y");
				gateway.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				gateway.setLastUpdBy(user.getUserId());
				gateway.setLastUpd(now);
				intfGatewayDao.update(gateway);
			} else if(action.getSubUrType().equals("RT")){
				IpUrRouting routing = action.getRouting();
				routing.setIsTnp("Y");
				routing.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				routing.setLastUpdBy(user.getUserId());
				routing.setLastUpd(now);
				routingDao.update(routing);
			} else if(action.getSubUrType().equals("F5")){
				IpUrF5 f5 = action.getF5();
				f5.setIsTnp("Y");
				f5.setSubUrStatusTxt(IPFMConstant.STATUS_WAIT_PM);
				f5.setLastUpdBy(user.getUserId());
				f5.setLastUpd(now);
				f5Dao.update(f5);	
			}
			
			
			List<IpSubUrAttachment> subFileList = ipSubUrAttachmentDao.getIpUrAttachment(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());			
			if(subFileList != null && subFileList.size() > 0){
				for(IpSubUrAttachment data : subFileList){
							IpSubUrAttachment subData = new IpSubUrAttachment();
							IpSubUrAttachmentId id = new IpSubUrAttachmentId();
							id.setUrNo(data.getId().getUrNo());
							id.setSubUrNo(subUr);
							id.setFileName(data.getId().getFileName());
							id.setCategory(data.getId().getCategory());
							
							BigDecimal seqNo = ipSubUrAttachmentDao.getSeqNo(urNo, data.getId().getCategory());
							id.setSeq(seqNo);
							subData.setId(id);
							
							subData.setVersion(new Long(0));
							subData.setDescription(data.getDescription());
							subData.setUrStatus(IPFMConstant.STATUS_WAIT_CONFIG);
							subData.setFileLocation(data.getFileLocation());
							subData.setCreated(now);
							subData.setCreatedBy(user.getUserId());
							subData.setLastUpd(now);
							subData.setLastUpdBy(user.getUserId());
							subData.setSubUrStatus(IPFMConstant.STATUS_WAIT_PM);
							subData.setAction(IPFMConstant.STATUS_PM_DELEGATE);
							ipSubUrAttachmentDao.insert(subData);
					}
				}
			
		}
		ipSubUrAttachmentDao.deleteIpUrAttachmentSubmit(urNo, "temp", IPFMConstant.STATUS_WAIT_CONFIG, user.getUserId());
		
		IpUrNwConfig config = ipUrNwConfigDao.findByUrNo(urNo);
		config.setTnpStatus("Y");
		config.setOverSla(isDateOverCurrent(slaDate, now));
		config.setLastUpdBy(user.getUserId());
		config.setLastUpd(now);
		
		ipUrNwConfigDao.update(config);

		// Send Mail 	
		String[] pmUserIdList = pmUserId.split(";");
		
		
		if(pmUserIdList != null && pmUserIdList.length > 0) {
			for(String userId : pmUserIdList) {
				userId = userId.trim().replaceAll(";", "");
				IpRoleMember role = ipUrNwConfigDao.getRoleMemberData(roleId, userId);
				if(role != null && role.getRoleId() != null){
					System.out.println("####################### Send Mail to : "+role.getIpUser().getEmail());					
					EMailService emailService = this.getEmailService();
					IpUrActionService actionService = (IpUrActionService) this.getIpUrActionService();
					String nodeId = "";
					if (role.getRoleId().equalsIgnoreCase("R06"))  nodeId = "N6";
					if (role.getRoleId().equalsIgnoreCase("R07"))  nodeId = "N7";
					if (actionService.isSendEmail("NC", nodeId)) {
						
						actionService.deleteNotify(config.getUrNo(), "");
						
							EmailDetail email = new EmailDetail();
							email.setEmail(role.getIpUser().getEmail());
							email.setUrNo(urNo);
							email.setUserId(role.getIpUser().getUserId());
							email.setUserName(role.getIpUser().getUserName());
							email.setSubject(config.getReqSubject());
							email.setUrType("Request Network Config");
							email.setUrStatusDesc("Wait For PM Assign");
							email.setUrStatus("WAIT_PM");
							email.setSubjectDesc("Wait For PM Assign");
							email.setBodyDesc("Wait For PM Assign");
							email.setRequestBy(config.getReqUserName());
							email.setRequestDate(config.getReqDate());
							String urNoEmail = email.getUrNo();				
							String key = emailService.getKeyCodeEmail();
							String[] to = {""};
							if(role.getIpUser() != null) {
								to[0] = email.getEmail();
							}
							String[] cc = null;
							String from = "ipfm@ais.co.th";				
							
							try {
								Map model = new HashMap();
								model.put("userName", email.getUserName());
								model.put("urNo", email.getUrNo());
								model.put("urType", email.getUrType());
								model.put("urStatus", "Wait For PM Assign");
								model.put("subjectDesc", "Wait For PM Assign (Delegrate by PM)");
								model.put("bodyDesc", "Wait For PM Assign (Delegrate by PM)");
								model.put("subject", emailSubject);
								model.put("requestBy", email.getRequestBy());
								SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy",new Locale("TH", "th"));
						        model.put("requestDate", smf.format(email.getRequestDate()));
								String encodeUrl = new String(Base64.encodeBase64(key.getBytes()));
								model.put("url", user.getUrl()+ "/home/view.jsf?key=" + encodeUrl);
		
								try{
									emailService.sendMail(to, cc, from, EMailService.TEMPLATE_IPFM_NC_UR, model);
								}catch(Exception me){
									me.printStackTrace();
								}
		
								IpEmailNotify notify = new IpEmailNotify();
								notify.setUserId(email.getUserId());
								notify.setUrNo(email.getUrNo());
								notify.setUrStatus(email.getUrStatus());
								notify.setCreatedBy(user.getUserId());
								notify.setLastUpdBy(user.getUserId());
								notify.setEmailLinkStatus("N");
								notify.setEmailCode(key);
								actionService.saveEmailNotify(notify);
							}
							catch (Exception e) {
								e.printStackTrace();
							}					
						}
					}
				}					
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new IPFMBusinessException();
		}

	
	}

}
