package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IIPIntfGatewayDao;
import th.co.ais.ipfm.dao.IIPPhysicalInterfaceDao;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpMasterTable;
import th.co.ais.ipfm.domain1.IpUrAccessListCdn;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrFirewall;
import th.co.ais.ipfm.domain1.IpUrFirewallId;
import th.co.ais.ipfm.domain1.IpUrInterfaceGateway;
import th.co.ais.ipfm.domain1.IpUrInterfaceGatewayId;
import th.co.ais.ipfm.domain1.IpUrPhysicalInterface;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IntfGatewayService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class IntfGatewayServiceImpl implements IntfGatewayService{
	private IIPIntfGatewayDao intfGatewayDao;
	private IIPInfoDAO ipInfoDao;
	private CommonDao commonDao;
	private IPUrActionDao ipUrActionDao;
	private IPUrActionHistoryDao ipUrActionHistoryDao;

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpUrActionDao
		this.ipUrActionDao = ipUrActionDao;
	}

	public void setIpUrActionHistoryDao(IPUrActionHistoryDao ipUrActionHistoryDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpUrActionHistoryDao
		this.ipUrActionHistoryDao = ipUrActionHistoryDao;
	}

	public void setCommonDao(CommonDao commonDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setCommonDao
		this.commonDao = commonDao;
	}
	
	public void setIpInfoDao(IIPInfoDAO ipInfoDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpInfoDao
		this.ipInfoDao = ipInfoDao;
	}
	
	

	public void setIntfGatewayDao(IIPIntfGatewayDao intfGatewayDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIntfGatewayDao
		this.intfGatewayDao = intfGatewayDao;
	}



	@Override
	public String genSubUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference genSubUrNo
		return intfGatewayDao.getSubUrNo(urNo);
	}
	
	
	@Override
	public IpInfo getIPInfo(String ip) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIPInfo
		return ipInfoDao.findIpInfo(ip);
	}
	
	
	@Override
	public IpInfo getIPInfoWithUsedStatus(String ip) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIPInfoWithUsedStatus
		String[] status = {"U"};
		return ipInfoDao.findIpInfoWithStatus(ip, status);
	}
	
	
	@Override
	public void saveOrUpdate(IpUrInterfaceGateway gateway) {
		intfGatewayDao.saveOrUpdate(gateway);
	}
	
	
	@Override
	public List<IpUrInterfaceGateway> findByUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findByUrNo
		return intfGatewayDao.findByURNo(urNo);
	}

	@Override
	public List<IpMasterTable> getProperty(String refTable){
		return intfGatewayDao.getProperty(refTable);
	}



	@Override
	public IpUrInterfaceGateway changeUrNo(IpUrInterfaceGateway tempUr, // DMAP Comment : Dead Code Detected - The Following Method has no reference changeUrNo
			String urNO) {
		IpUrInterfaceGateway newGateway = tempUr;
		newGateway.getId().setUrNo(urNO);
		newGateway.setRowId(commonDao.getROW_ID());
		intfGatewayDao.saveOrUpdate(newGateway);
		return intfGatewayDao.getID(newGateway.getId());
	}



	@Override
	public void update(IpUrInterfaceGateway gateway) {
		intfGatewayDao.update(gateway);
		
	}

	@Override
	public void deleteByUrNo(String urNo)  throws IPFMBusinessException {
		try{
			intfGatewayDao.deleteByUrNo(urNo);	
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	
	@Override
	public IpUrInterfaceGateway findFirewall(String urNo, String subUrNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findFirewall
		IpUrInterfaceGatewayId id = new IpUrInterfaceGatewayId(urNo, subUrNo);
		return intfGatewayDao.getID(id);
	}



	@Override
	public String getNetworkTypeId(String desc) {
		return intfGatewayDao.getNetworkTypeId(desc);
	}



	@Override
	public void delete(IpUrInterfaceGateway gateway) {
		intfGatewayDao.delete(gateway);
		
	}



	@Override
	public void delete(String urNo, String subUrNo) {
		intfGatewayDao.delete(urNo, subUrNo);
		
	}

	@Override
	public boolean hasNetworkType(String type) {
		return intfGatewayDao.hasNetworkType(type);		
	}
	
	@Override
	public void cleanSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference cleanSubUR
		intfGatewayDao.commitSubURDelete(urNo,"A");
		intfGatewayDao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference commitSubUR
		intfGatewayDao.commitSubURUpdate(urNo,"A");
		List<IpUrInterfaceGateway> igList = intfGatewayDao.waitDeleteUr(urNo);
		for(IpUrInterfaceGateway ig : igList){
			IpUrAction action = ipUrActionDao.findUrAction(ig.getId().getUrNo(), ig.getId().getSubUrNo());
			IpUrActionHistory history = new IpUrActionHistory(action);
			history.setUrStatus(IPFMConstant.ACTION_DEL);
			history.setActionName("Requester Delete Sub UR");
			ipUrActionHistoryDao.insert(history);
		}
		intfGatewayDao.commitSubURDelete(urNo,"D");
	}

}
