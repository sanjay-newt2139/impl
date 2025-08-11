package th.co.ais.ipfm.service.impl;



import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IIPUserDao;
import th.co.ais.ipfm.dao.IPUserDao;
import th.co.ais.ipfm.dao.PlanningPLDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpUrIpResult;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.SearchIPService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;
import th.co.ais.ipfm.vo.SearchResult;


public class SearchIPServiceImp implements SearchIPService {

	private IIPInfoDAO  ipInfoDao;
	private IIPUserDao ipUserDao;
	private IPUserDao iipUserDao; 
	private PlanningPLDao planningPLDao;
	
	
	public IIPInfoDAO getIpInfoDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpInfoDao
		return ipInfoDao;
	}

	public void setIpInfoDao(IIPInfoDAO ipInfoDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpInfoDao
		this.ipInfoDao = ipInfoDao;
	}

	public PlanningPLDao getPlanningPLDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getPlanningPLDao
		return planningPLDao;
	}

	public void setPlanningPLDao(PlanningPLDao planningPLDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setPlanningPLDao
		this.planningPLDao = planningPLDao;
	}

	public IIPUserDao getIpUserDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpUserDao
		return ipUserDao;
	}

	public void setIpUserDao(IIPUserDao ipUserDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpUserDao
		this.ipUserDao = ipUserDao;
	}
	
	public IPUserDao getIipUserDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIipUserDao
		return iipUserDao;
	}

	public void setIipUserDao(IPUserDao iipUserDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIipUserDao
		this.iipUserDao = iipUserDao;
	}




	@Override
	public String getV_UserPermission(String userId)throws IPFMBusinessException {
		    String viewUr = ipUserDao.getV_UserPermission(userId);
		    
		return viewUr;
	}

	public IpUrIpResult  checkIPRange(String  IpDigit1,String IpDigit2,String IpDigit3,String IpDigit4,String mask) throws IPFMBusinessException {
		IpUrIpResult obj =null;
		try {
			if (IPFMUtils.ifBlank(IpDigit1, "").trim().length()==0 
					|| IPFMUtils.ifBlank(IpDigit2, "").trim().length()==0 
					|| IPFMUtils.ifBlank(IpDigit3, "").trim().length()==0
					|| IPFMUtils.ifBlank(IpDigit4, "").trim().length()==0) {
			 }else {
				 obj =  planningPLDao.checkIPRange(IpDigit1,IpDigit2,IpDigit3,IpDigit4, mask);
			 }
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		
		return obj;
	}

	@Override
	public SearchResult searchIP(IpInfo ipInfo, IpUser ipUser) throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
		try {
			//searchResult.setTotalResult(ipInfoDao.countSearchIP(ipInfo, ipUser));
			searchResult.setResultList(ipInfoDao.searchIP(ipInfo, ipUser));
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return searchResult;
	}

	@Override
	public IpInfo getIpInfo(String ipAddress) throws IPFMBusinessException {
		IpInfo ipInfo = null;
		try {
			if(ipAddress!=null){
				ipInfo = ipInfoDao.getIpInfo(ipAddress);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipInfo;
	}

	@Override
	public Integer getSeachIPTotalRec(IpInfo ipInfo, IpUser ipUser) throws IPFMBusinessException { // DMAP Comment : Dead Code Detected - The Following Method has no reference getSeachIPTotalRec
		Integer totalRec = 0;
		try {
			totalRec = ipInfoDao.searchIPTotalRec(ipInfo, ipUser);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return totalRec;
	}

	@Override
	public List<IpInfo> findIpInfoByHostName(String hostName)
			throws IPFMBusinessException {
		// TODO Auto-generated method stub
		return ipInfoDao.findIpInfoByHostName(hostName);
	}

	@Override
	public IpInfo findIpInfoByWYNodeName(String wyNodeName) // DMAP Comment : Dead Code Detected - The Following Method has no reference findIpInfoByWYNodeName
			throws IPFMBusinessException {
		// TODO Auto-generated method stub
		return ipInfoDao.findIpInfoByWCHUNodeName(wyNodeName);
	}
}
