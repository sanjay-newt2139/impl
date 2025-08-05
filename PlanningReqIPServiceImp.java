package th.co.ais.ipfm.service.impl;




import java.math.BigDecimal;
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
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpLevel1;
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
import th.co.ais.ipfm.service.PlanningReqIPService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;


public class PlanningReqIPServiceImp implements PlanningReqIPService {
	private IPUrIPDetailDao  ipUrIPDetailDao;
	private IPUrActionDao ipUrActionDao;  
	private IPUrAttachmentDao ipUrAttachmentDao;
	private CommonDao commonDao;
	private IPUserDao iipUserDao;
	private PlanningPLDao planningPLDao;
	private IPUrActionHistoryDao  ipUrActionHistoryDao;
	private IPUrIPResultDao ipUrIPResultDao;
	private IPLevel1Dao ipLevel1Dao;
	private IPLevel2Dao ipLevel2Dao;
	private IIPInfoDAO ipInfoDao;
	private IpMaskDisplayDao ipMaskDisplayDao;
	private ILoginService loginService;
	private IPTeamDao ipTeamDao;
	
	
	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}

	public void setIpTeamDao(IPTeamDao ipTeamDao) {
		this.ipTeamDao = ipTeamDao;
	}
	
	public void setIpInfoDao(IIPInfoDAO ipInfoDao) {
		this.ipInfoDao = ipInfoDao;
	}

	public void setIpLevel2Dao(IPLevel2Dao ipLevel2Dao) {
		this.ipLevel2Dao = ipLevel2Dao;
	}

	public void setIpMaskDisplayDao(IpMaskDisplayDao ipMaskDisplayDao) {
		this.ipMaskDisplayDao = ipMaskDisplayDao;
	}

	public IPUrIPDetailDao getIpUrIPDetailDao() {
		return ipUrIPDetailDao;
	}

	public void setIpUrIPDetailDao(IPUrIPDetailDao ipUrIPDetailDao) {
		this.ipUrIPDetailDao = ipUrIPDetailDao;
	}
	public IPUrActionDao getIpUrActionDao() {
		return ipUrActionDao;
	}

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) {
		this.ipUrActionDao = ipUrActionDao;
	}

	public IPUrAttachmentDao getIpUrAttachmentDao() {
		return ipUrAttachmentDao;
	}

	public void setIpUrAttachmentDao(IPUrAttachmentDao ipUrAttachmentDao) {
		this.ipUrAttachmentDao = ipUrAttachmentDao;
	}

	public CommonDao getCommonDao() {
		return commonDao;
	}
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
	public IPUserDao getIipUserDao() {
		return iipUserDao;
	}

	public void setIipUserDao(IPUserDao iipUserDao) {
		this.iipUserDao = iipUserDao;
	}

	public PlanningPLDao getPlanningPLDao() {
		return planningPLDao;
	}

	public void setPlanningPLDao(PlanningPLDao planningPLDao) {
		this.planningPLDao = planningPLDao;
	}

	public IPUrActionHistoryDao getIpUrActionHistoryDao() {
		return ipUrActionHistoryDao;
	}

	public void setIpUrActionHistoryDao(IPUrActionHistoryDao ipUrActionHistoryDao) {
		this.ipUrActionHistoryDao = ipUrActionHistoryDao;
	}
	public IPUrIPResultDao getIpUrIPResultDao() {
		return ipUrIPResultDao;
	}

	public void setIpUrIPResultDao(IPUrIPResultDao ipUrIPResultDao) {
		this.ipUrIPResultDao = ipUrIPResultDao;
	}
	
	public IPLevel1Dao getIpLevel1Dao() {
		return ipLevel1Dao;
	}

	public void setIpLevel1Dao(IPLevel1Dao ipLevel1Dao) {
		this.ipLevel1Dao = ipLevel1Dao;
	}
	@Override
	public IpUrIpDetail createDraft(IpUrIpDetail ipUrIpdetail ,IpUrAttachment ipUrAttachment) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null){
				IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
					}
				}
				ipUrIPDetailDao.insert(ipUrIpdetail);
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),IPFMConstant.ACTION_DRAFT,"","");
				// ipUrAttachmentDao.updateIpUrAttachment(ipUrIpdetail.getUrNo(), ipUrAttachment.getId().getUrNo());
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}
	
	@Override
	public IpUrIpDetail updateDraft(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException {
		try {
			IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
			if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
					&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
					ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
				}
			}
			IpUrAction urAction = ipUrActionDao.findUrAction(ipUrIpdetail.getUrNo(), "NA");
			urAction.setLastUpdBy(ipUrIpdetail.getLastUpdBy());
			urAction.setSubject(ipUrIpdetail.getSubject());
			urAction.setLastUpd(null);
			ipUrActionDao.update(urAction);
		    ipUrIPDetailDao.update(ipUrIpdetail);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}

	@Override
	public IpUrIpDetail createSendForAssign(IpUrIpDetail ipUrIpdetail,IpUrAttachment ipUrAttachment) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null){
				IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
					}
				}
				ipUrIPDetailDao.insert(ipUrIpdetail);
				String olaDate = getOlaDate(new Date(), ipUrIpdetail.getUrType(), "N3");
				String slaDate = getSlaDate(new Date(), ipUrIpdetail.getUrType(), "NA");
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),IPFMConstant.ACTION_SENDFORASSIGN,olaDate,slaDate);
				//ipUrAttachmentDao.updateIpUrAttachment(ipUrIpdetail.getUrNo(), ipUrAttachment.getId().getUrNo());
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception  ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}

	@Override
	public IpUrIpDetail updateSendForAssign(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException, Exception {
		try {
			if(ipUrIpdetail != null){
				IpUser ipUser = checkNewUser(ipUrIpdetail.getSysOwnerId());
				if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipUrIpdetail.getSysOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipUrIpdetail.setSysOwnerTeamId(ipUser.getTeamId());
					}
				}
				ipUrIPDetailDao.update(ipUrIpdetail);
				String olaDate = getOlaDate(ipUrIpdetail.getReqDate(), ipUrIpdetail.getUrType(), "N3");
				String slaDate = getSlaDate(ipUrIpdetail.getReqDate(), ipUrIpdetail.getUrType(), "NA");
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),ipUrIpdetail.getUrType(),IPFMConstant.ACTION_SENDFORASSIGN,olaDate,slaDate);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}

	@Override
	public IpUrIpDetail deleteUR(IpUrIpDetail ipUrIpdetail,IpUrAttachment ipUrAttachment) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null && ipUrAttachment !=null){			
				ipUrIPDetailDao.delete(ipUrIpdetail);
				ipUrAttachmentDao.deleteIpUrAttachment(ipUrAttachment.getId().getUrNo());
				ipUrActionDao.deleteByUrNo(ipUrIpdetail.getUrNo());
/*				List<IpUrActionHistory> ipUrActionHistoryList = ipUrActionHistoryDao.getHistoryList(ipUrIpdetail.getUrNo());
				for(IpUrActionHistory   obj : ipUrActionHistoryList){
				  ipUrActionHistoryDao.delete(obj);
				}*/
				
				//planningPLDao.urIpTodoList(ipUrAttachment.getId().getUrNo(),IPFMConstant.URTYPE_PLANNING_REQ_IP,IPFMConstant.ACTION_DELETE,"","");
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return null;
	}	
	
	@Override
	public void  rejectUR(IpUrIpDetail ipUrIpdetail,IpUrAttachment ipUrAttachment) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail != null && ipUrAttachment !=null){
				ipUrIpdetail.setUrStatus(IPFMConstant.STATUS_REJECT_BY_CDN);
				ipUrIpdetail.setUrStatusName(IPFMConstant.STATUS_REJECT_BY_CDN_NAME);
				ipUrIPDetailDao.update(ipUrIpdetail);
				ipUrAttachmentDao.deleteIpUrAttachment(ipUrAttachment.getId().getUrNo());
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(),IPFMConstant.URTYPE_PLANNING_REQ_IP,IPFMConstant.ACTION_REJECT,"","");
				ipUrActionDao.deleteByUrNo(ipUrIpdetail.getUrNo());
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}	
	
	@Override
	public List<IpUser> getSystemOwnerList() throws IPFMBusinessException {
		List<IpUser> systemOwnerList = null;
		try {
			  systemOwnerList = iipUserDao.getSystemOwnerList();
			  
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return systemOwnerList;
	}
	
	@Override
	public List<IpTeam> getSystemOwnerTeamList() throws IPFMBusinessException {
		List<IpTeam> systemOwnerList = null;
		try {
			  systemOwnerList = iipUserDao.getSystemOwnerTeamTier2List();
		} catch (DataAccessException e) {
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
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urno;
	}
	
	public String  getTempURNo() throws IPFMBusinessException {
		String tempUrno = null;
		try {
			tempUrno = ipUrIPDetailDao.getTempURNo();
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return tempUrno;
	}
	
	@Override
	public IpUrIpDetail getIpUrIpDetail(String URNo) throws IPFMBusinessException {
		IpUrIpDetail ipUrIpDetail = null;
		try {
			if(URNo!=null){
				ipUrIpDetail = ipUrIPDetailDao.getIPUrIpDetail(URNo);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpDetail;
	}
	
	@Override
	public List<IpMasterTable> getMaster_activeStatus_List(String refTable) throws IPFMBusinessException {
		List<IpMasterTable> netWorkZoneList = null;
		try {
			if(refTable!=null){
			netWorkZoneList = commonDao.getMaster_activeStatus_List(refTable);
			}
		} catch (DataAccessException e) {
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
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urActionList;
	}

	@Override
	public List<IpUrAttachment> getIpUrAttachmentList(String urNo) throws IPFMBusinessException {
		List<IpUrAttachment> ipUrAttachmentList = null;
		try {
			ipUrAttachmentList = ipUrAttachmentDao.getIpUrAttachmentList(urNo);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrAttachmentList;
	}

	@Override
	public IpUrIpResult  checkIPRange(String  IpDigit1,String IpDigit2,String IpDigit3,String IpDigit4,String mask) throws IPFMBusinessException {
		IpUrIpResult obj =null;
		try {
			obj =  planningPLDao.checkIPRange(IpDigit1, IpDigit2, IpDigit3, IpDigit4, mask);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return obj;
	}

	@Override
	public String reCheck_ip_range(IpUrIpResult ipUrIpResult) throws IPFMBusinessException {
		String result=null;
		try {
			if(ipUrIpResult !=null){
			result = ipUrIPResultDao.reCheck_ip_range(ipUrIpResult) ;
			}

		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public void createAssignIP(IpUrIpResult ipUrIpResult, IpLevel1 ipLevel1) throws IPFMBusinessException {
		try {
			if(ipUrIpResult != null && ipLevel1 != null){ 
				String rowId = commonDao.getROW_ID();
				
				String urNo = ipUrIpResult.getId().getUrNo();
				BigDecimal seqNo = ipUrIPResultDao.getSeqNo(urNo);
							
				ipUrIpResult.getId().setSeq(seqNo);
				ipUrIpResult.setRowId(rowId);
				
				ipUrIPResultDao.insert(ipUrIpResult);
				ipLevel1.setRowId(rowId);
				ipLevel1.setIpStatus("P");
				ipLevel1Dao.insert(ipLevel1);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		
	}
	@Override
	public void createAssignIP(IpUrIpDetail ipUrIpDetail, List<IpUrIpResult> ipUrIpResultList, List<IpLevel1> ipLevel1List) throws IPFMBusinessException {
		try {
			for (IpLevel1 ipLevel1 : ipLevel1List) {
				String resultDup = checkDuplicateIP(ipLevel1.getBinary1Start(), ipLevel1.getBinary1End(), "1");
				if (!IPFMConstant.RESULT_SUCCESS.equalsIgnoreCase(resultDup)) {
					throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0060")));
				}
				String rowId = commonDao.getROW_ID();
				ipLevel1.setRowId(rowId);
				ipLevel1.setIpStatus("P");
				ipLevel1Dao.insert(ipLevel1);
			}
			for (IpUrIpResult ipUrIpResult : ipUrIpResultList) {
				String rowId = commonDao.getROW_ID();
				String urNo = ipUrIpResult.getId().getUrNo();
				BigDecimal seqNo = ipUrIPResultDao.getSeqNo(urNo);
				ipUrIpResult.getId().setSeq(seqNo);
				ipUrIpResult.setRowId(rowId);
				ipUrIPResultDao.insert(ipUrIpResult);				
			}
			
			updateIpDetail_genURIpToDoList(ipUrIpDetail);
		}catch (IPFMBusinessException ipfme){
			ipfme.printStackTrace();
			throw new IPFMBusinessException(ipfme.getMessage());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		
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
	public void updateIpDetail_genURIpToDoList(IpUrIpDetail ipUrIpdetail) throws IPFMBusinessException {
		try {
			if(ipUrIpdetail!=null){
				ipUrIPDetailDao.update(ipUrIpdetail);
				planningPLDao.urIpTodoList(ipUrIpdetail.getUrNo(), IPFMConstant.URTYPE_PLANNING_REQ_IP, IPFMConstant.ACTION_ASSIGN,"","");
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	
	@Override
	public String  getLevel1_ID() throws IPFMBusinessException {
		String level1Id = null;
		try {
			level1Id = ipLevel1Dao.getLevel1_ID();
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return level1Id;
	}

	@Override
	public IpUrIpResult getIpUrIpResult(String URNo) throws IPFMBusinessException {
		
		IpUrIpResult ipUrIpResult = null;
		try {
			if(URNo!=null){
				ipUrIpResult = ipUrIPResultDao.getIpUrIpResult(URNo);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpResult;
	}
	
	@Override
	public List<IpUrIpResult> getIpUrIpResultList(String URNo) throws IPFMBusinessException {
		
		List<IpUrIpResult> ipUrIpResult = null;
		try {
			if(URNo!=null){
				ipUrIpResult = ipUrIPResultDao.getIpUrIpResultList(URNo);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpResult;
	}

	@Override
	public String getUrStatusDesc(String urType, IpUrIpDetail ipUrIpDetail) throws IPFMBusinessException {
		String urStatusDesc = null;
		try {
			urStatusDesc = planningPLDao.getUrStatusDesc(urType, ipUrIpDetail.getUrStatus());
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urStatusDesc;
	}
	
	@Override
	public String getOlaDate(Date reqDate, String urType, String nodeId) throws IPFMBusinessException  {
		Date olaDate = ipUrActionDao.getOLADate(reqDate, urType, nodeId);
		String olaStr = IPFMDataUtility.toStringEngDateBySimpleFormat(olaDate, "yyyyMMdd");
		System.out.println("olaStr = " + olaStr);
		return olaStr;
	}
	
	@Override
	public String getSlaDate(Date reqDate, String urType, String pm) throws IPFMBusinessException  {
		Date slaDate = ipUrActionDao.getSLADate(reqDate, urType, pm);
		String slaStr = IPFMDataUtility.toStringEngDateBySimpleFormat(slaDate, "yyyyMMdd");
		System.out.println("slaStr = " + slaStr);
		return slaStr;
	}

	@Override
	public IpUrAction getIpUrAction(String urNo) throws IPFMBusinessException {
		IpUrAction urAction;
		try {
			urAction = ipUrActionDao.findUrAction(urNo,"NA");
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
	public BigDecimal checkIpLevel2TotalNotAvailable(String binarySt,String binaryEd) throws IPFMBusinessException {
		BigDecimal result = null; 
		try {
			result = ipLevel2Dao.checkIpLevel2TotalNotAvailable(binarySt,binaryEd);
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
	private IpUser checkNewUser(String userId) throws IPFMBusinessException {
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
			}else {
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
}
