package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IIPPhysicalInterfaceDao;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpMasterTable;
import th.co.ais.ipfm.domain1.IpUrAccessListCdn;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrFirewall;
import th.co.ais.ipfm.domain1.IpUrPhysicalInterface;
import th.co.ais.ipfm.domain1.IpUrPhysicalInterfaceId;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.PhyInterfaceService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class PhyInterfaceServiceImpl implements PhyInterfaceService{
	private IIPPhysicalInterfaceDao physicalInterfaceDao;
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
	
	public void setPhysicalInterfaceDao(IIPPhysicalInterfaceDao physicalInterfaceDao) {
		this.physicalInterfaceDao = physicalInterfaceDao;
	}

	@Override
	public String genSubUrNo(String urNo) {
		return physicalInterfaceDao.getSubUrNo(urNo);
	}
	
	
	@Override
	public IpInfo getIPInfo(String ip) {
		return ipInfoDao.findIpInfo(ip);
	}
	
	
	@Override
	public IpInfo getIPInfoWithUsedStatus(String ip) {
		String[] status = {"U"};
		return ipInfoDao.findIpInfoWithStatus(ip, status);
	}
	
	
	@Override
	public void saveOrUpdate(IpUrPhysicalInterface phyIntf) {
		physicalInterfaceDao.saveOrUpdate(phyIntf);
	}
	
	
	@Override
	public List<IpUrPhysicalInterface> findByUrNo(String urNo) {
		return physicalInterfaceDao.findByURNo(urNo);
	}

	@Override
	public List<IpMasterTable> getProperty(String refTable){
		return physicalInterfaceDao.getProperty(refTable);
	}

	@Override
	public IpUrPhysicalInterface changeUrNo(IpUrPhysicalInterface tempUr,
			String urNO) {
		IpUrPhysicalInterface newInterface = tempUr;
		newInterface.getId().setUrNo(urNO);
		newInterface.setRowId(commonDao.getROW_ID());
		physicalInterfaceDao.saveOrUpdate(newInterface);
		return physicalInterfaceDao.getID(newInterface.getId());
	}

	@Override
	public void update(IpUrPhysicalInterface phyInterface) {
		physicalInterfaceDao.update(phyInterface);
		
	}

	@Override
	public void deleteByUrNo(String urno) throws IPFMBusinessException {
		try{
		  physicalInterfaceDao.deleteByUrNo(urno);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}

	@Override
	public IpUrPhysicalInterface findPhyInterface(String urNo, String subUrNo) {
		IpUrPhysicalInterfaceId id = new IpUrPhysicalInterfaceId(urNo, subUrNo);
		return physicalInterfaceDao.getID(id);
	}

	@Override
	public String getIntfNodeTypeId(String desc) {
		return physicalInterfaceDao.getIntfNodeTypeId(desc);
	}

	@Override
	public String getSpeedPortId(String desc) {
		return physicalInterfaceDao.getSpeedPortId(desc);
	}

	@Override
	public String getStatusId(String desc) {
		return physicalInterfaceDao.getStatusId(desc);
	}

	@Override
	public void delete(IpUrPhysicalInterface intf) {
		physicalInterfaceDao.delete(intf);
		
	}

	@Override
	public void delete(String urNo, String subUrNo) {
		physicalInterfaceDao.delete(urNo, subUrNo);
		
	}
	
	@Override
	public void cleanSubUR(String urNo) {
		physicalInterfaceDao.commitSubURDelete(urNo,"A");
		physicalInterfaceDao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) {
		physicalInterfaceDao.commitSubURUpdate(urNo,"A");
		List<IpUrPhysicalInterface> piList = physicalInterfaceDao.waitDeleteUr(urNo);
		for(IpUrPhysicalInterface pi : piList){
			IpUrAction action = ipUrActionDao.findUrAction(pi.getId().getUrNo(), pi.getId().getSubUrNo());
			IpUrActionHistory history = new IpUrActionHistory(action);
			history.setUrStatus(IPFMConstant.ACTION_DEL);
			history.setActionName("Requester Delete Sub UR");
			ipUrActionHistoryDao.insert(history);
		}
		physicalInterfaceDao.commitSubURDelete(urNo,"D");
	}

	@Override
	public boolean hasProperty(String type, String refTable) {
		return physicalInterfaceDao.hasProperty(type, refTable);
	}
	
	
	

}
