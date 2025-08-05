package th.co.ais.ipfm.service.impl;




import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IPLevel1Dao;
import th.co.ais.ipfm.dao.IPLevel2Dao;
import th.co.ais.ipfm.dao.IPTeamDao;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.dao.IPUrAttachmentDao;
import th.co.ais.ipfm.dao.IPUrIPDetailDao;
import th.co.ais.ipfm.dao.IPUrIPResultDao;
import th.co.ais.ipfm.dao.IPUserDao;
import th.co.ais.ipfm.dao.IpMaskDisplayDao;
import th.co.ais.ipfm.dao.PlanningPLDao;
import th.co.ais.ipfm.domain1.ExcelErrorMsg;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpLevel1;
import th.co.ais.ipfm.domain1.IpLevel2;
import th.co.ais.ipfm.domain1.IpMaskDisplay;
import th.co.ais.ipfm.domain1.IpMasterTable;
import th.co.ais.ipfm.domain1.IpTeam;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrAttachment;
import th.co.ais.ipfm.domain1.IpUrIpDetail;
import th.co.ais.ipfm.domain1.IpUrIpResult;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.ILoginService;
import th.co.ais.ipfm.service.IpUrAttachmentService;
import th.co.ais.ipfm.service.UserReqIPService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;


public class UserReqIPServiceImpl implements UserReqIPService {
	private IPUrIPDetailDao  ipUrIPDetailDao;
	private IPUrActionDao ipUrActionDao;  
	private IPUrAttachmentDao ipUrAttachmentDao;
	private CommonDao commonDao;
	private PlanningPLDao planningPLDao;
	private IPUserDao iipUserDao;
	private IPUrActionHistoryDao  ipUrActionHistoryDao;
	private IPUrIPResultDao ipUrIPResultDao;
	private IPLevel1Dao ipLevel1Dao;
	private IPLevel2Dao ipLevel2Dao;
	private IIPInfoDAO ipInfoDao;
	private IpMaskDisplayDao ipMaskDisplayDao;
	private IpUrAttachmentService ipUrAttachmentService;
	private ILoginService loginService;
	private IPTeamDao ipTeamDao;
	
	
	public void setIpTeamDao(IPTeamDao ipTeamDao) {
		this.ipTeamDao = ipTeamDao;
	}

	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}

	public void setIpUrAttachmentService(IpUrAttachmentService ipUrAttachmentService) {
		this.ipUrAttachmentService = ipUrAttachmentService;
	}

	public void setIpUrIPDetailDao(IPUrIPDetailDao ipUrIPDetailDao) {
		this.ipUrIPDetailDao = ipUrIPDetailDao;
	}

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) {
		this.ipUrActionDao = ipUrActionDao;
	}

	public void setIpUrAttachmentDao(IPUrAttachmentDao ipUrAttachmentDao) {
		this.ipUrAttachmentDao = ipUrAttachmentDao;
	}
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setIipUserDao(IPUserDao iipUserDao) {
		this.iipUserDao = iipUserDao;
	}
	public void setIpUrActionHistoryDao(IPUrActionHistoryDao ipUrActionHistoryDao) {
		this.ipUrActionHistoryDao = ipUrActionHistoryDao;
	}
	public void setPlanningPLDao(PlanningPLDao planningPLDao) {
		this.planningPLDao = planningPLDao;
	}

	public void setIpUrIPResultDao(IPUrIPResultDao ipUrIPResultDao) {
		this.ipUrIPResultDao = ipUrIPResultDao;
	}

	public void setIpMaskDisplayDao(IpMaskDisplayDao ipMaskDisplayDao) {
		this.ipMaskDisplayDao = ipMaskDisplayDao;
	}

	public void setIpLevel1Dao(IPLevel1Dao ipLevel1Dao) {
		this.ipLevel1Dao = ipLevel1Dao;
	}

	public void setIpLevel2Dao(IPLevel2Dao ipLevel2Dao) {
		this.ipLevel2Dao = ipLevel2Dao;
	}

	public void setIpInfoDao(IIPInfoDAO ipInfoDao) {
		this.ipInfoDao = ipInfoDao;
	}

	@Override
	public IpUrIpDetail createSendForApprove(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null){
				IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
					}
				}
				if(ipUrIpdetail.getUrNo().startsWith("T")){
					String urNo = getURNo();
					ipUrAttachmentService.updateAttachFileByCategory(ipUrIpdetail.getUrNo(), urNo, ipUrIpdetail.getLastUpdBy());
					ipUrIpdetail.setUrNo(urNo);					
				}	
				ipUrIpdetail.setReqDate(new Date());
				ipUrIPDetailDao.insert(ipUrIpdetail);
				String olaDate = getOlaDate(ipUrIpdetail.getReqDate(), ipUrIpdetail.getUrType(), "N2");
				String slaDate = getSlaDate(ipUrIpdetail.getReqDate(), ipUrIpdetail.getUrType(), "NA");
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),IPFMConstant.ACTION_SEND_FOR_MRG_APPR,olaDate,slaDate);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}

	@Override
	public IpUrIpDetail updateSendForApprove(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null){
				IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
					}
				}
				ipUrIpdetail.setReqDate(new Date());
				ipUrIPDetailDao.update(ipUrIpdetail);
				String olaDate = getOlaDate(ipUrIpdetail.getReqDate(), ipUrIpdetail.getUrType(), "N2");
				String slaDate = getSlaDate(ipUrIpdetail.getReqDate(), ipUrIpdetail.getUrType(), "NA");
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),IPFMConstant.ACTION_SEND_FOR_MRG_APPR,olaDate,slaDate);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}

	@Override
	public IpUrIpDetail deleteUR(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null){	
				if (ipUrIpdetail.getUrStatus().equalsIgnoreCase(IPFMConstant.STATUS_REJECT_MRG)) {
					ipUrIpdetail.setUrStatus(IPFMConstant.STATUS_DELETE);	
					ipUrIpdetail.setUrStatusName(getUrStatusDesc(ipUrIpdetail.getUrType(), ipUrIpdetail.getUrStatus()));
					ipUrIPDetailDao.update(ipUrIpdetail);
					planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),IPFMConstant.URTYPE_USER_REQ_IP,IPFMConstant.ACTION_DELETE,"","");
				}else{
					ipUrIPDetailDao.delete(ipUrIpdetail);
					ipUrAttachmentDao.deleteIpUrAttachment(ipUrIpdetail.getUrNo());
					planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),IPFMConstant.URTYPE_USER_REQ_IP,IPFMConstant.ACTION_DELETE,"","");
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}	
	
	
	@Override
	public List<IpUser> getSystemOwnerList() throws IPFMBusinessException {
		List<IpUser> systemOwnerList = null;
		try {
			  systemOwnerList = iipUserDao.getSystemOwnerList();
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
			  systemOwnerList = iipUserDao.getSystemOwnerTeamList();
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
	public String  getURNo() throws IPFMBusinessException {
		String urno = null;
		try {
			urno = ipUrIPDetailDao.getURNo();
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urno;
	}
	
	
	public String getTempURNo() throws IPFMBusinessException {
		String tempUrno = null;
		try {
			tempUrno = ipUrIPDetailDao.getTempURNo();
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return tempUrno;
	}
	
	@Override
	public IpUrIpDetail getIpUrIpDetail(String URNo) throws IPFMBusinessException {
		IpUrIpDetail ipUrIpDetail = null;
		try {
			if(URNo!=null){
				ipUrIpDetail = ipUrIPDetailDao.getIPUrIpDetail(URNo);			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpDetail;
	}
	
	@Override
	public List<IpMasterTable> getMaster_activeStatus_List(String refTable) throws IPFMBusinessException {
		List<IpMasterTable> netWorkZoneList = null;
		try {
			netWorkZoneList = commonDao.getMaster_activeStatus_List(refTable);

		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return netWorkZoneList;
	}
	
	@Override
	public List<IpUrActionHistory> getHistoryList(String urNo) throws IPFMBusinessException {
		List<IpUrActionHistory> historyList = null;
		try {
			historyList = ipUrActionHistoryDao.getHistoryList(urNo);

		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return historyList;
	}

	@Override
	public List<IpUrAction> getURActionList(String urNo) throws IPFMBusinessException {
		List<IpUrAction> urActionList = null;
		try {
			urActionList = ipUrActionDao.getURActionList(urNo);

		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urActionList;
	}
	@Override
	public IpUrIpDetail createDraft(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException{
		try {
			if(ipUrIpdetail != null){
				IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
					}
				}
				if(ipUrIpdetail.getUrNo().startsWith("T")){
					String urNo = getURNo();
					ipUrAttachmentService.updateAttachFileByCategory(ipUrIpdetail.getUrNo(), urNo ,ipUrIpdetail.getLastUpdBy());					
					ipUrIpdetail.setUrNo(urNo);					
				}
				ipUrIPDetailDao.insert(ipUrIpdetail);
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),IPFMConstant.ACTION_DRAFT,"","");
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpdetail;
	}

	@Override
	public IpUrIpDetail updateDraft(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException {
		try {			
			if(ipUrIpdetail != null){
				IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
					}
				}
				if(ipUrIpdetail.getUrNo().startsWith("T")){
					String urNo = getURNo();
					ipUrAttachmentService.updateAttachFileByCategory(ipUrIpdetail.getUrNo(), urNo,ipUrIpdetail.getLastUpdBy());
					ipUrIpdetail.setUrNo(urNo);					
				}
				ipUrIPDetailDao.update(ipUrIpdetail);
				IpUrAction urAction = ipUrActionDao.findUrAction(ipUrIpdetail.getUrNo(), "NA");
				urAction.setLastUpdBy(ipUrIpdetail.getLastUpdBy());
				urAction.setSubject(ipUrIpdetail.getSubject());
				urAction.setLastUpd(null);
				ipUrActionDao.update(urAction);
				//planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),IPFMConstant.ACTION_DRAFT,"","");
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpdetail;
	}

	@Override
	public String getUrStatusDesc(String urType, String urStatus) throws IPFMBusinessException {
		String urStatusDesc = "";
		try {
			urStatusDesc = planningPLDao.getUrStatusDesc(urType, urStatus);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urStatusDesc;
	}

	@Override
	public IpUrIpDetail approveUR(IpUrIpDetail ipUrIpdetail, String actionType) throws IPFMBusinessException{
		try {
			if(ipUrIpdetail != null){				
				ipUrIPDetailDao.update(ipUrIpdetail);
				String olaDate = getOlaDate(new Date(), ipUrIpdetail.getUrType(), "N3");
				String slaDate = getSlaDate(ipUrIpdetail.getReqDate(), ipUrIpdetail.getUrType(), "NA");
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),actionType,olaDate,slaDate);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpdetail;
	}

	@Override
	public IpUrIpDetail rejectUR(IpUrIpDetail ipUrIpdetail, String actionType) throws IPFMBusinessException{
		try {
			if(ipUrIpdetail != null){	
				ipUrIPDetailDao.update(ipUrIpdetail);
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),actionType,"","");
				//ipUrAttachmentDao.deleteIpUrAttachment(ipUrIpdetail.getUrNo());
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpdetail;
	}
	@Override
	public IpUrIpDetail rejectURAssignIP(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException{
		try {
			//ipUrIPDetailDao.delete(ipUrIpdetail);
			ipUrIPDetailDao.update(ipUrIpdetail);
			ipUrAttachmentDao.deleteIpUrAttachment(ipUrIpdetail.getUrNo());
			ipUrActionHistoryDao.deleteByUrNo(ipUrIpdetail.getUrNo());
			planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),IPFMConstant.URTYPE_USER_REQ_IP,IPFMConstant.ACTION_REJECT,"","");
			ipUrActionDao.deleteByUrNo(ipUrIpdetail.getUrNo());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpdetail;
	}
	public List<IpUrAttachment> getIpUrAttachmentList(String urNo, String category) throws IPFMBusinessException {
		List<IpUrAttachment> ipUrAttachmentList = null;
		try {
			ipUrAttachmentList = ipUrAttachmentDao.getIpUrAttachmentList(urNo,category);

		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrAttachmentList;
	}

	@Override
	public List<IpUrIpResult> getIpUrIpResultList(String urNo) throws IPFMBusinessException {
		List<IpUrIpResult> ipResultList = new ArrayList<IpUrIpResult>();
		try {
			ipResultList = ipUrIPResultDao.getIpUrIpResultList(urNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipResultList;
	}
	private void addErrorMsg(ExcelErrorMsg excelMsg, String errorMsg){		
		if (excelMsg.getErrorMsg()==null || excelMsg.getErrorMsg().trim().length()==0) {
			excelMsg.setErrorMsg(errorMsg);
		}else{
			excelMsg.setErrorMsg(excelMsg.getErrorMsg() + "<br>" + errorMsg);
		}
	}
	public IpUrIpDetail assignIP(IpUrIpDetail ipUrIpdetail, List<IpUrIpResult> ipUrIpResultList, String actionType, String userId) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null){	
//				ExcelErrorMsg excelMsg;
//				List<ExcelErrorMsg> errorlist = new ArrayList<ExcelErrorMsg>();
				ipUrIPDetailDao.update(ipUrIpdetail);
				int rowIndex = 1;
				for (IpUrIpResult result : ipUrIpResultList) {
					String resultDup = checkDuplicateIP(result.getBinary2Start(), result.getBinary2End(), "2");
					if (!IPFMConstant.RESULT_SUCCESS.equalsIgnoreCase(resultDup)) {
//						excelMsg = new ExcelErrorMsg();
//						excelMsg.setRowNo(String.valueOf(rowIndex));	
//						addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(resultDup));
//						errorlist.add(excelMsg);
						throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0060")));
					}
					result.getId().setSeq(ipUrIPResultDao.getSeqNo(result.getId().getUrNo()));
					result.setRowId(commonDao.getROW_ID());
					ipUrIPResultDao.insert(result);
//					if (result.getIpLevel2().getAvailableIp().intValue()>0) {
//						result.getIpLevel2().setAssignIp(result.getIpLevel2().getAssignIp().add(new BigDecimal(result.getTotalIp())));
//						result.getIpLevel2().
//						ipLevel2Dao.update(result.getIpLevel2());
//					}else{
					
						ipLevel2Dao.insert(result.getIpLevel2());
//					}
					for (IpInfo ipInfo : result.getIpInfoList()) {
						ipInfoDao.insert(ipInfo);
					}
					ipLevel1Dao.updateTotalT2Assign(result.getLevel1Id(),result.getTotalIp(),userId);
					rowIndex++;
				}
				
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),actionType,"","");
			}
		}catch (IPFMBusinessException ipfme){
			ipfme.printStackTrace();
			throw new IPFMBusinessException(ipfme.getMessage());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpdetail;
	}
	
	@Override
	public String reCheck_ip_range23(IpUrIpResult ipUrIpResult) throws IPFMBusinessException {
		String result;
		try {
			result = ipUrIPResultDao.reCheck_ip_range23(ipUrIpResult) ;
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	@Override
	public String  getLevel2_ID() throws IPFMBusinessException {
		String level2Id = null;
		try {
			level2Id = ipLevel2Dao.getLevel2_ID();
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return level2Id;
	}
	@Override
	public IpUrIpResult checkIPRange23(IpUrIpResult ipUrIpResult) throws IPFMBusinessException {
		IpUrIpResult result = new IpUrIpResult();
		try {					
			result = ipUrIPResultDao.checkIPRange23(ipUrIpResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	@Override
	public IpUrIpResult checkIPRange23_2(IpUrIpResult ipUrIpResult) throws IPFMBusinessException {
		IpUrIpResult result = new IpUrIpResult();
		try {
			result = ipUrIPResultDao.checkIPRange23_2(ipUrIpResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	@Override
	public IpUrIpResult checkIPRange23_3(IpUrIpResult ipUrIpResult) throws IPFMBusinessException {
		IpUrIpResult result = new IpUrIpResult();
		try {
			result = ipUrIPResultDao.checkIPRange23_3(ipUrIpResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	@Override
	public IpUrIpResult checkIPRange(IpUrIpResult ipUrIpResult) throws IPFMBusinessException {
		IpUrIpResult obj =null;
		try {
				//obj =  planningPLDao.checkIPRange(ipUrIpResult);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		
		return obj;
	}

	@Override
	public String getOlaDate(Date reqDate, String urType, String nodeId) throws IPFMBusinessException  {
		Date olaDate = ipUrActionDao.getOLADate(reqDate, urType, nodeId);
		String olaStr = IPFMDataUtility.toStringEngDateBySimpleFormat(olaDate, "yyyyMMdd");
		return olaStr;
	}
	
	@Override
	public String getSlaDate(Date reqDate, String urType, String pm) throws IPFMBusinessException  {
		Date slaDate = ipUrActionDao.getSLADate(reqDate, urType, pm);
		String slaStr = IPFMDataUtility.toStringEngDateBySimpleFormat(slaDate, "yyyyMMdd");
		return slaStr;
	}

	@Override
	public IpUrAction getIpUrAction(String urNo, String subUrNo) throws IPFMBusinessException {
		IpUrAction urAction;
		try {
			urAction = ipUrActionDao.findUrAction(urNo, subUrNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urAction;
	}

	@Override
	public String isOverSla(String urNo) throws IPFMBusinessException {
		String overSla="";
		try {
			overSla = ipUrActionDao.isOverSla(urNo);
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return overSla;
	}

	@Override
	public IpMaskDisplay findIpMaskDisplayByMask(String mask) throws IPFMBusinessException {
		IpMaskDisplay result = null;
		try {
			result = ipMaskDisplayDao.findIpMaskDisplayByMask(mask);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	@Override
	public IpInfo checkIPRange23SearchL1(IpInfo ipInfo) throws IPFMBusinessException {
		IpInfo result = null;
		try {
			result = ipInfoDao.checkIPRange23SearchL1(ipInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public IpInfo checkStatusDataIpInfo(String ipDigit1, String ipDigit2, String ipDigit3, String ipDigit4) throws IPFMBusinessException {
		IpInfo result = null;
		try {
			result = ipInfoDao.findIpInfo(ipDigit1,ipDigit2,ipDigit3,ipDigit4);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public BigDecimal checkIpInfoAvailable(IpInfo ipInfo) throws IPFMBusinessException {
		BigDecimal result = null;
		try {
			result = ipInfoDao.checkIpInfoTotalAvailable(ipInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	@Override
	public List<IpInfo> getIpInfoAssignIp(String binary2Start, String binary2End) throws IPFMBusinessException{
		List<IpInfo> result = null;
		try {
			result = ipInfoDao.findIpInfoAssignIp(binary2Start,binary2End);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	@Override
	public List<IpInfo> searchIpInfo(String ipAddress) throws IPFMBusinessException {
		List<IpInfo> resultList = new ArrayList<IpInfo>();
		try {
			resultList = ipInfoDao.searchIpInfo(ipAddress, "", "", "", null);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	
	@Override
	public String checkDuplicateIP(String binSt, String binEd, String option) throws IPFMBusinessException{
		String result = "";
		try {
			result = planningPLDao.checkDuplicateIP(binSt, binEd, option);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0070")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0070")));
		}
		return result;
	}

	@Override
	public IpUser checkNewUser(String userId) throws IPFMBusinessException {
		IpUser ipUser = null;
		try {
			ipUser = loginService.getIPUser(userId);
			if (ipUser==null){
				ipUser = loginService.getEhrByUserId(userId);
				loginService.addUser(ipUser);
				loginService.addIpRoleMemberByUser(ipUser);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUser;
	}

	@Override
	public IpTeam findIpTeamById(String teamId) throws IPFMBusinessException {
		IpTeam ipTeam = null;
		try {
			if (teamId!=null && !teamId.trim().equalsIgnoreCase("NA")) {
				ipTeam = ipTeamDao.getIpTeam(teamId);
			}else{
				ipTeam = ipTeamDao.getIpTeamByTeamName(teamId);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipTeam;
	}

	@Override
	public IpLevel2 findIpLevel2ByLevel2Id(String level2Id) throws IPFMBusinessException {
		IpLevel2 ipLevel2 = null;
		try {
			ipLevel2 = ipLevel2Dao.findIpLevel2ByLevel2Id(level2Id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipLevel2;
	}
	
	
	public List<IpLevel1> searchIpLevel1ByBinIpAndTeam(String binaryIp, String team2Id) throws IPFMBusinessException{
		List<IpLevel1> ipLevel1List = null;
		try{
			ipLevel1List = ipLevel1Dao.searchBinaryStartBetweenDate(binaryIp, team2Id);
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException();
		}
		
		return ipLevel1List;
		
	}
	
	
	@Override
	public List<IpLevel1> searchIpLevel1ByBinIpAndTeam(String ip1, String ip2, String ip3, String ip4, int mask, String team2Id) throws IPFMBusinessException{
		List<IpLevel1> ipLevel1List = null;
		try{
			String vDisplayMask = "32";
			if (mask>7 && mask<16) vDisplayMask = "16";
			else if (mask>15 && mask<24) vDisplayMask = "24";
			else if (mask>23 && mask<33) vDisplayMask = "32";
			
			String ip =  "";
			if(mask == 16) {
				ip = ip1;
			} else if(mask == 24) {
				ip = ip1+"."+ip2;
			} else if(mask == 32) {
				ip = ip1+"."+ip2+"."+ip3;
			}
			
			ipLevel1List = ipLevel1Dao.searchBySubIpMaskWithTeam(ip,mask,team2Id);
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException();
		}
		
		return ipLevel1List;
		
	}
	
	@Override
	public List<IpLevel1> searchByIpWithMark(String ip1, String ip2, String ip3, String ip4, String mark, String team2Id) throws IPFMBusinessException {
		List<IpLevel1> ipLevel1List = null;
		try{
			IpUrIpResult result = ipUrIPResultDao.getIpStartEnd3(ip1, ip2, ip3, ip4, mark);
			
			if(result != null) {
				ipLevel1List = ipLevel1Dao.searchStartIpBetweenBinaryStartEndWithTeam2(result.getBinary1Start(), result.getBinary1End(), team2Id);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException();
		}
		
		return ipLevel1List;
	}
	
}
