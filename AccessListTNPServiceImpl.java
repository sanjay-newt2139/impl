package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPAccessListTNPDao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpUrAccessListTnp;
import th.co.ais.ipfm.domain1.IpUrAccessListTnpId;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrFirewall;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.AccessListTNPService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class AccessListTNPServiceImpl implements AccessListTNPService{
	private IIPAccessListTNPDao accessListTNPDao;
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
	
	public void setAccessListTNPDao(IIPAccessListTNPDao accessListTNPDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setAccessListTNPDao
		this.accessListTNPDao = accessListTNPDao;
	}

	public void setIpInfoDao(IIPInfoDAO ipInfoDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpInfoDao
		this.ipInfoDao = ipInfoDao;
	}

	@Override
	public String genSubUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference genSubUrNo
		return accessListTNPDao.getSubUrNo(urNo);
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
	public List<IpUrAccessListTnp> findByUrNo(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findByUrNo
		// TODO Auto-generated method stub
		return accessListTNPDao.findByURNo(urNo);
	}

	@Override
	public void saveOrUpdate(IpUrAccessListTnp tnp) {
		// TODO Auto-generated method stub
		accessListTNPDao.saveOrUpdate(tnp);
	}

	@Override
	public IpUrAccessListTnp changeUrNo(IpUrAccessListTnp tempUr, String urNO) { // DMAP Comment : Dead Code Detected - The Following Method has no reference changeUrNo
		IpUrAccessListTnp newTNP = tempUr;
		newTNP.getId().setUrNo(urNO);
		newTNP.setRowId(commonDao.getROW_ID());
		accessListTNPDao.saveOrUpdate(newTNP);
		return accessListTNPDao.getID(newTNP.getId());
	}

	@Override
	public void update(IpUrAccessListTnp tnp) {
		accessListTNPDao.update(tnp);
	}

	@Override
	public void deleteByUrNo(String urNo) throws IPFMBusinessException {
		try{
		  accessListTNPDao.deleteByUrNo(urNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	
	public IpUrAccessListTnp findAccessListTnp(String urNo, String subUrNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference findAccessListTnp
		IpUrAccessListTnpId id = new IpUrAccessListTnpId(urNo, subUrNo);
		System.out.println("urNo ==>  "+urNo);
		System.out.println("subUrNo ==>  "+subUrNo);
		return accessListTNPDao.getID(id);
		
	}

	@Override
	public void delete(IpUrAccessListTnp tnp) {
		accessListTNPDao.delete(tnp);
	}

	@Override
	public void delete(String urNo, String subUrNo) {
		accessListTNPDao.delete(urNo, subUrNo);
		
	}
	
	@Override
	public void cleanSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference cleanSubUR
		accessListTNPDao.commitSubURDelete(urNo,"A");
		accessListTNPDao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) { // DMAP Comment : Dead Code Detected - The Following Method has no reference commitSubUR
		accessListTNPDao.commitSubURUpdate(urNo,"A");
		List<IpUrAccessListTnp> atList = accessListTNPDao.waitDeleteUr(urNo);
		for(IpUrAccessListTnp at : atList){
			IpUrAction action = ipUrActionDao.findUrAction(at.getId().getUrNo(), at.getId().getSubUrNo());
			IpUrActionHistory history = new IpUrActionHistory(action);
			history.setUrStatus(IPFMConstant.ACTION_DEL);
			history.setActionName("Requester Delete Sub UR");
			ipUrActionHistoryDao.insert(history);
		}
		accessListTNPDao.commitSubURDelete(urNo,"D");
	}


}
