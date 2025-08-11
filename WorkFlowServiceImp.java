package th.co.ais.ipfm.service.impl;



import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IPUrWfConfigDetailDao;
import th.co.ais.ipfm.dao.IPWfConfigDao;
import th.co.ais.ipfm.domain1.IpWfConfig;
import th.co.ais.ipfm.domain1.IpWfConfigDetail;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.WorkFlowService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;


public class WorkFlowServiceImp implements WorkFlowService {


	private IPUrWfConfigDetailDao ipUrWfConfigDetailDao;
	private IPWfConfigDao ipWfConfigDao;
	 private CommonDao commonDao;


	public IPUrWfConfigDetailDao getIpUrWfConfigDetailDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpUrWfConfigDetailDao
		return ipUrWfConfigDetailDao;
	}

	public void setIpUrWfConfigDetailDao(IPUrWfConfigDetailDao ipUrWfConfigDetailDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpUrWfConfigDetailDao
		this.ipUrWfConfigDetailDao = ipUrWfConfigDetailDao;
	}

	public IPWfConfigDao getIpWfConfigDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpWfConfigDao
		return ipWfConfigDao;
	}

	public void setIpWfConfigDao(IPWfConfigDao ipWfConfigDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpWfConfigDao
		this.ipWfConfigDao = ipWfConfigDao;
	}
	
	public CommonDao getCommonDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getCommonDao
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setCommonDao
		this.commonDao = commonDao;
	}

	@Override
	public List<IpWfConfigDetail> getIpWfConfigDetailList(String urType) throws IPFMBusinessException {
		List<IpWfConfigDetail> ipWfConfigDetailList = null;
		try {
			ipWfConfigDetailList = ipUrWfConfigDetailDao.getIpWfConfigDetailList(urType);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipWfConfigDetailList;
	}

	@Override
	public IpWfConfig getIpWfConfig(String urType) throws IPFMBusinessException {
		IpWfConfig ipWfConfig = null;
		try {
			ipWfConfig = ipWfConfigDao.getIpWfConfig(urType);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		
		return ipWfConfig;
	}
	
	@Override
	public void save(IpWfConfig ipWfConfig, List<IpWfConfigDetail> ipWfConfigDetailList,String userId) throws IPFMBusinessException { // DMAP Comment : Dead Code Detected - The Following Method has no reference save
		try {			
			if("NC".equals(ipWfConfig.getUrType())){
				ipWfConfigDao.updateIpWfConfig(ipWfConfig,userId); 
			}else{
			    ipWfConfigDao.update(ipWfConfig);
			}
			
			for(IpWfConfigDetail obj : ipWfConfigDetailList){
				obj.setLastUpdBy(userId);
				ipUrWfConfigDetailDao.update(obj);
			}
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
	}

	@Override
	public int maxOla() throws DataAccessException {
		 int  macOla =  ipUrWfConfigDetailDao.maxOla();
		return macOla;
	}
	
	
}
