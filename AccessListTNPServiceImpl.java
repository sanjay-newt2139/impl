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

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) {
		this.ipUrActionDao = ipUrActionDao;
	}

	public void setIpUrActionHistoryDao(IPUrActionHistoryDao ipUrActionHistoryDao) {
		this.ipUrActionHistoryDao = ipUrActionHistoryDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
	public void setAccessListTNPDao(IIPAccessListTNPDao accessListTNPDao) {
		this.accessListTNPDao = accessListTNPDao;
	}

	public void setIpInfoDao(IIPInfoDAO ipInfoDao) {
		this.ipInfoDao = ipInfoDao;
	}

	@Override
	public String genSubUrNo(String urNo) {
		return accessListTNPDao.getSubUrNo(urNo);
	}
	
	@Override
	public IpInfo getIPInfo(String ip) {
		return ipInfoDao.findIpInfo(ip);
	}
	
	@Override
	public IpInfo getIPInfoWithUsedStatus(String ip) {
		// TODO Auto-generated method stub
		String[] status = {"U"};
		return ipInfoDao.findIpInfoWithStatus(ip, status);
	}
	
	
	@Override
	public List<IpUrAccessListTnp> findByUrNo(String urNo) {
		// TODO Auto-generated method stub
		return accessListTNPDao.findByURNo(urNo);
	}

	@Override
	public void saveOrUpdate(IpUrAccessListTnp tnp) {
		// TODO Auto-generated method stub
		accessListTNPDao.saveOrUpdate(tnp);
	}

	@Override
	public IpUrAccessListTnp changeUrNo(IpUrAccessListTnp tempUr, String urNO) {
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
	
	public IpUrAccessListTnp findAccessListTnp(String urNo, String subUrNo) {
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
	public void cleanSubUR(String urNo) {
		accessListTNPDao.commitSubURDelete(urNo,"A");
		accessListTNPDao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) {
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
