package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IIPRoutingDao;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpMasterTable;
import th.co.ais.ipfm.domain1.IpUrAccessListCdn;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrFirewall;
import th.co.ais.ipfm.domain1.IpUrInterfaceGateway;
import th.co.ais.ipfm.domain1.IpUrInterfaceGatewayId;
import th.co.ais.ipfm.domain1.IpUrRouting;
import th.co.ais.ipfm.domain1.IpUrRoutingId;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.RoutingService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class RoutingServiceImpl implements RoutingService{
	private IIPRoutingDao routingDao;
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
	public void setRoutingDao(IIPRoutingDao routingDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setRoutingDao
		this.routingDao = routingDao;
	}

	@Override
	public String genSubUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference genSubUrNo
		return routingDao.getSubUrNo(urNo);
	}
	
	@Override
	public IpInfo getIPInfo(String ip) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIPInfo
		return ipInfoDao.findIpInfo(ip);
	}
	
	@Override
	public IpInfo getIPInfoUsedStatus(String ip) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIPInfoUsedStatus
		String[] status = {"U"};
		return ipInfoDao.findIpInfoWithStatus(ip, status);
	}
	
	@Override
	public void saveOrUpdate(IpUrRouting routing) throws IPFMBusinessException{
		try{
		  routingDao.saveOrUpdate(routing);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	@Override
	public List<IpUrRouting> findByUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findByUrNo
		return routingDao.findByURNo(urNo);
	}

	@Override
	public List<IpMasterTable> getProperty(String refTable){
		return routingDao.getProperty(refTable);
	}
	@Override
	public IpUrRouting changeUrNo(IpUrRouting tempUr, String urNO) { // DMAP Comment : Dead Code Detected - The Following Method has no reference changeUrNo
		IpUrRouting newRouting = tempUr;
		newRouting.getId().setUrNo(urNO);
		newRouting.setRowId(commonDao.getROW_ID());
		routingDao.saveOrUpdate(newRouting);
		return routingDao.getID(newRouting.getId());
	}
	@Override
	public void update(IpUrRouting routing) {
		routingDao.update(routing);
		
	}
	
	@Override
	public void deleteByUrNo(String urNo) throws IPFMBusinessException {
		try{
		   routingDao.deleteByUrNo(urNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}	
	}
	
	@Override
	public IpUrRouting findFirewall(String urNo, String subUrNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findFirewall
		IpUrRoutingId id = new IpUrRoutingId(urNo, subUrNo);
		return routingDao.getID(id);
	}
	@Override
	public void delete(IpUrRouting urNo) {
		routingDao.delete(urNo);
		
	}
	@Override
	public void delete(String urNo, String subUrNo) {
		routingDao.delete(urNo, subUrNo);
	}
	
	@Override
	public void cleanSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference cleanSubUR
		routingDao.commitSubURDelete(urNo,"A");
		routingDao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference commitSubUR
		routingDao.commitSubURUpdate(urNo,"A");
		List<IpUrRouting> rtList = routingDao.waitDeleteUr(urNo);
		for(IpUrRouting rt : rtList){
			IpUrAction action = ipUrActionDao.findUrAction(rt.getId().getUrNo(), rt.getId().getSubUrNo());
			IpUrActionHistory history = new IpUrActionHistory(action);
			history.setUrStatus(IPFMConstant.ACTION_DEL);
			history.setActionName("Requester Delete Sub UR");
			ipUrActionHistoryDao.insert(history);
		}
		routingDao.commitSubURDelete(urNo,"D");
	}

}
