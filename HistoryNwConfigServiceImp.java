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


	public IPUrActionDao getIpUrActionDao() {
		return ipUrActionDao;
	}

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) {
		this.ipUrActionDao = ipUrActionDao;
	}

	public IPUrNwConfigDao getIpUrNwConfigDao() {
		return ipUrNwConfigDao;
	}

	public void setIpUrNwConfigDao(IPUrNwConfigDao ipUrNwConfigDao) {
		this.ipUrNwConfigDao = ipUrNwConfigDao;
	}


	public IPUrActionHistoryDao getIpUrActionHistoryDao() {
		return ipUrActionHistoryDao;
	}

	public void setIpUrActionHistoryDao(IPUrActionHistoryDao ipUrActionHistoryDao) {
		this.ipUrActionHistoryDao = ipUrActionHistoryDao;
	}

	@Override
	public List<IpUrAction> getIpUrActionList(String urNo) throws IPFMBusinessException {
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
