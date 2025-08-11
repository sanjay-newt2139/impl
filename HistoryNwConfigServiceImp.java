package th.co.ais.ipfm.service.impl;


import java.util.List;

import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.dao.IPUrNwConfigDao;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrNwConfig;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.HistoryNwConfigService;


public class HistoryNwConfigServiceImp implements HistoryNwConfigService {

	private IPUrActionDao ipUrActionDao;  
	private IPUrNwConfigDao ipUrNwConfigDao; 
	private IPUrActionHistoryDao ipUrActionHistoryDao;


	public IPUrActionDao getIpUrActionDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpUrActionDao
		return ipUrActionDao;
	}

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpUrActionDao
		this.ipUrActionDao = ipUrActionDao;
	}

	public IPUrNwConfigDao getIpUrNwConfigDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpUrNwConfigDao
		return ipUrNwConfigDao;
	}

	public void setIpUrNwConfigDao(IPUrNwConfigDao ipUrNwConfigDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpUrNwConfigDao
		this.ipUrNwConfigDao = ipUrNwConfigDao;
	}


	public IPUrActionHistoryDao getIpUrActionHistoryDao() { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpUrActionHistoryDao
		return ipUrActionHistoryDao;
	}

	public void setIpUrActionHistoryDao(IPUrActionHistoryDao ipUrActionHistoryDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpUrActionHistoryDao
		this.ipUrActionHistoryDao = ipUrActionHistoryDao;
	}

	@Override
	public List<IpUrAction> getIpUrActionList(String urNo) throws IPFMBusinessException { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIpUrActionList
		List<IpUrAction> ipUrActionList = null;
		ipUrActionList  = ipUrActionDao.getURActionList(urNo);
		return ipUrActionList;
	}

	@Override
	public IpUrNwConfig findByUrNo(String urNo)throws IPFMBusinessException {
		IpUrNwConfig ipUrNwConfig =null; 
		ipUrNwConfig =  ipUrNwConfigDao.findByUrNo(urNo);
		return ipUrNwConfig;
	}

	@Override
	public List<IpUrActionHistory> getHistoryList(String urNo)throws IPFMBusinessException {
		List<IpUrActionHistory> ipUrActionHistoryList = null;
		ipUrActionHistoryList  = ipUrActionHistoryDao.getHistoryList(urNo);
		return ipUrActionHistoryList;
	}

	@Override
	public List<IpUrAction> findIpUrActionList(String urNo) throws IPFMBusinessException {
		List<IpUrAction> ipUrActionList = null;
		ipUrActionList  = ipUrActionDao.findIpUrActionList(urNo);
		return ipUrActionList;
	}

	
	
	
	
	
	
	
	
}
