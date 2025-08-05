package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPF5Dao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpMasterTable;
import th.co.ais.ipfm.domain1.IpUrAccessListCdn;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrF5;
import th.co.ais.ipfm.domain1.IpUrF5Id;
import th.co.ais.ipfm.domain1.IpUrFirewall;
import th.co.ais.ipfm.domain1.IpUrInterfaceGateway;
import th.co.ais.ipfm.domain1.IpUrInterfaceGatewayId;
import th.co.ais.ipfm.domain1.IpvRetriveF5;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.F5Service;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class F5ServiceImpl implements F5Service{
	private IIPF5Dao f5Dao;
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
	
	public void setIpInfoDao(IIPInfoDAO ipInfoDao) {
		this.ipInfoDao = ipInfoDao;
	}


	public void setF5Dao(IIPF5Dao f5Dao) {
		this.f5Dao = f5Dao;
	}


	@Override
	public String genSubUrNo(String urNo) {
		return f5Dao.getSubUrNo(urNo);
	}
	
	@Override
	public IpInfo getIPInfo(String ip) {
		return ipInfoDao.findIpInfo(ip);
	}
	
	@Override
	public IpInfo getIPInfoWithStatus(String ip) {
		String[] status = {"A","U","R"};
		return ipInfoDao.findIpInfoWithStatus(ip, status);
	}	
	
	@Override
	public void saveOrUpdate(IpUrF5 f5) {
		f5Dao.saveOrUpdate(f5);
	}
	@Override
	public List<IpUrF5> findByUrNo(String urNo) {
		return f5Dao.findByURNo(urNo);
	}

	@Override
	public List<IpMasterTable> getProperty(String refTable){
		return f5Dao.getProperty(refTable);
	}


	@Override
	public List<IpvRetriveF5> retriveF5(String vip, String port, String urNo) {
		return f5Dao.retriveF5(vip, port, urNo);
	}


	@Override
	public IpUrF5 getF5ById(IpUrF5Id id) {
		// TODO Auto-generated method stub
		return f5Dao.getID(id);
	}


	@Override
	public IpUrF5 changeUrNo(IpUrF5 tempUr, String urNO) {
		IpUrF5 newF5 = tempUr;
		newF5.getId().setUrNo(urNO);
		newF5.setRowId(commonDao.getROW_ID());
		f5Dao.saveOrUpdate(newF5);
		return f5Dao.getID(newF5.getId());
	}


	@Override
	public void update(IpUrF5 f5) {
		f5Dao.update(f5);
		
	}


	@Override
	public void deleteByUrNo(String urNo) throws IPFMBusinessException {
		try{
		  f5Dao.deleteByUrNo(urNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}

	@Override
	public IpUrF5 findFirewall(String urNo, String subUrNo) {
		IpUrF5Id id = new IpUrF5Id(urNo, subUrNo);
		return f5Dao.getID(id);
	}


	@Override
	public void delete(IpUrF5 f5) {
		f5Dao.delete(f5);
	}


	@Override
	public void delete(String urNo, String subUrNo) {
		f5Dao.delete(urNo, subUrNo);		
	}
	
	@Override
	public void cleanSubUR(String urNo) {
		f5Dao.commitSubURDelete(urNo,"A");
		f5Dao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) {
		f5Dao.commitSubURUpdate(urNo,"A");
		List<IpUrF5> f5List = f5Dao.waitDeleteUr(urNo);
		for(IpUrF5 f5 : f5List){
			IpUrAction action = ipUrActionDao.findUrAction(f5.getId().getUrNo(), f5.getId().getSubUrNo());
			IpUrActionHistory history = new IpUrActionHistory(action);
			history.setUrStatus(IPFMConstant.ACTION_DEL);
			history.setActionName("Requester Delete Sub UR");
			ipUrActionHistoryDao.insert(history);
		}
		f5Dao.commitSubURDelete(urNo,"D");
	}

	

}
