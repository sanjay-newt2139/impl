package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPAccessListDao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpUrAccessListCdn;
import th.co.ais.ipfm.domain1.IpUrAccessListCdnId;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.AccessListService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class AccessListServiceImpl implements AccessListService{
	private IIPAccessListDao accessListDao;
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
	
	public void setAccessListDao(IIPAccessListDao accessListDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setAccessListDao
		this.accessListDao = accessListDao;
	}
	public void setIpInfoDao(IIPInfoDAO ipInfoDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpInfoDao
		this.ipInfoDao = ipInfoDao;
	}
	
	@Override
	public String genSubUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference genSubUrNo
		return accessListDao.getSubUrNo(urNo);
	}
	
	@Override
	public IpInfo getIPInfo(String ip) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIPInfo
		return ipInfoDao.findIpInfo(ip);
	}
	
	@Override
	public IpInfo getIPInfoWithUsedStatus(String ip) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getIPInfoWithUsedStatus
		// TODO Auto-generated method stub
		String[] status = {"U"};
		return ipInfoDao.findIpInfoWithStatus(ip, status);
	}
	
	@Override
	public IpUrAccessListCdn saveOrUpdate(IpUrAccessListCdn cdn) {
		accessListDao.saveOrUpdate(cdn);
		return accessListDao.getID(cdn.getId());
	}
	@Override
	public List<IpUrAccessListCdn> findByUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findByUrNo
		// TODO Auto-generated method stub
		return accessListDao.findByURNo(urNo);
	}
	@Override
	public IpUrAccessListCdn changeUrNo(IpUrAccessListCdn tempUr, String urNO) { // DMAP Comment : Dead Code Detected - The Following Method has no reference changeUrNo
		IpUrAccessListCdn newCDN = tempUr;
		newCDN.getId().setUrNo(urNO);
		newCDN.setRowId(commonDao.getROW_ID());
		accessListDao.saveOrUpdate(newCDN);
		return accessListDao.getID(newCDN.getId());

	}
	@Override
	public void update(IpUrAccessListCdn cdn) throws IPFMBusinessException{
		try{
			accessListDao.update(cdn);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	
	@Override
	public void deleteByUrNo(String urNo) throws IPFMBusinessException {
		try{
			accessListDao.deleteByUrNo(urNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	@Override
	public IpUrAccessListCdn findAccessListCdn(String urNo, String subUrNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findAccessListCdn
		IpUrAccessListCdnId id = new IpUrAccessListCdnId(urNo, subUrNo);
		return accessListDao.getID(id);
		
	}
	@Override
	public void save(IpUrAccessListCdn cdn) { // DMAP Comment : Dead Code Detected - The Following Method has no reference save
		accessListDao.insert(cdn);
	}
	@Override
	public void delete(IpUrAccessListCdn cdn) {
		accessListDao.delete(cdn);
		
	}
	@Override
	public void delete(String urNo, String subUrNo) {
		accessListDao.delete(urNo, subUrNo);
		
		
	}
	
	@Override
	public void cleanSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference cleanSubUR
		accessListDao.commitSubURDelete(urNo,"A");
		accessListDao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference commitSubUR
		accessListDao.commitSubURUpdate(urNo,"A");
		List<IpUrAccessListCdn> alList = accessListDao.waitDeleteUr(urNo);
		for(IpUrAccessListCdn al : alList){
			IpUrAction action = ipUrActionDao.findUrAction(al.getId().getUrNo(), al.getId().getSubUrNo());
			IpUrActionHistory history = new IpUrActionHistory(action);
			history.setUrStatus(IPFMConstant.ACTION_DEL);
			history.setActionName("Requester Delete Sub UR");
			ipUrActionHistoryDao.insert(history);
		}
		accessListDao.commitSubURDelete(urNo,"D");
	}
	
	@Override
	public int  fineIpinfo(String ipStart,String ipEnd) { // DMAP Comment : Dead Code Detected - The Following Method has no reference fineIpinfo
		return ipInfoDao.findIpinfo(ipStart, ipEnd);
	}

	@Override
	public int  findTier2TeamID(String ipStart,String ipEnd) {
		return ipInfoDao.findTier2TeamID(ipStart, ipEnd);
	}
	
	@Override
	public int  findSystemOwnerTeamID(String ipStart,String ipEnd) {
		return ipInfoDao.findSystemOwnerTeamID(ipStart, ipEnd);
	}

	@Override
	public List<IpInfo> findIpInfoAssignIp(String binary2Start,
			String binary2End) throws IPFMBusinessException {
		
		return ipInfoDao.findIpInfoAssignIp(binary2Start, binary2End);
	}

}
