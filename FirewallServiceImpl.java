package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPFirewallDAO;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.dao.IPUrActionHistoryDao;
import th.co.ais.ipfm.domain.NCAssign;
import th.co.ais.ipfm.domain.NCData;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpMasterTable;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUrActionHistory;
import th.co.ais.ipfm.domain1.IpUrFirewall;
import th.co.ais.ipfm.domain1.IpUrFirewallId;
import th.co.ais.ipfm.domain1.IpUrNwConfig;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.FirewallService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;

public class FirewallServiceImpl implements FirewallService{
	private IIPFirewallDAO firewallDao;
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

	public void setFirewallDao(IIPFirewallDAO firewallDao) {
		this.firewallDao = firewallDao;
	}

	@Override
	public List<IpUrFirewall> listFirewall() {
		return firewallDao.listFirewall();
	}

	@Override
	public List<NCData> listResultByPage(String type, String page) {
		return firewallDao.listResultByPage(type, page);
	}

	@Override
	public List<NCAssign> listAssign(String page) {
		return firewallDao.listAssign(page);
	}

	@Override
	public NCData findUR(String type, String page, String urSubID) {
		return firewallDao.findUR(type, page, urSubID);
	}

	@Override
	public IpInfo getIPInfo(String ip) {
		return ipInfoDao.findIpInfo(ip);
	}
	
	public IpInfo getIPInfoWithUsedStatus(String ip) {
		String status[] = {"U"};
		return ipInfoDao.findIpInfoWithStatus(ip, status);
	}	

	@Override
	public List<IpMasterTable> getService() {
		return firewallDao.getServiceName();
	}

	@Override
	public String genSubUrNo(String urNo) {
		return firewallDao.genSubUrno(urNo);
	}

	@Override
	public void saveOrUpdate(IpUrFirewall firewall) {
		firewallDao.saveOrUpdate(firewall);
	}

	@Override
	public List<IpUrFirewall> findByUrNo(String urNo) {
		return firewallDao.findByUrNo(urNo);
	}

	@Override
	public void updateFirewall(IpUrFirewall firewall) {
		firewallDao.update(firewall);		
	}

	@Override
	public IpUrFirewall changeUrNo(IpUrFirewall tempUr, String urNO) {
		IpUrFirewall newFirewall = tempUr;
		newFirewall.getId().setUrNo(urNO);
		newFirewall.setRowId(commonDao.getROW_ID());
		firewallDao.saveOrUpdate(newFirewall);
		IpUrFirewall result = firewallDao.findByKey(newFirewall.getId()); 
		return result;
	}

	@Override
	public void deleteByUrNo(String urNo)throws IPFMBusinessException {
		try{
			firewallDao.deleteByUrNo(urNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}

	@Override
	public IpMasterTable getServiceByRefID(String refNo) {
		return firewallDao.getServiceNameByRefId(refNo);
	}

	@Override
	public IpUrFirewall findFirewall(String urNo, String subUrNo) {
		IpUrFirewallId id = new IpUrFirewallId(urNo, subUrNo);
		IpUrFirewall data = firewallDao.findByKey(id);
		if(data != null){
					if(data != null && data.getServiceTcpPort() != null){
						data.setServiceTcpPortStr(IPFMUtils.validateServicePort(data.getServiceTcpPort()));
					}else{
						data.setServiceTcpPortStr("");
					}
					
					if(data != null && data.getServiceUdpPort() != null){
						data.setServiceUdpPortStr(IPFMUtils.validateServicePort(data.getServiceUdpPort()));
					}else{
						data.setServiceUdpPortStr("");
					}
					
					if(data != null && data.getServiceOtherPort() != null){
						data.setServiceOtherPortStr(IPFMUtils.validateServicePort(data.getServiceOtherPort()));
					}else{
						data.setServiceOtherPortStr("");
					}

		}
		return data;
	}

	@Override
	public void delete(IpUrFirewall firewall) {
		firewallDao.delete(firewall);		
	}

	@Override
	public void delete(String urNo, String subUrNo) {
		firewallDao.delete(urNo,subUrNo);
		
	}

	@Override
	public void cleanSubUR(String urNo) {
		firewallDao.commitSubURDelete(urNo,"A");
		firewallDao.commitSubURUpdate(urNo, "D");
	}

	@Override
	public void commitSubUR(String urNo) {
		firewallDao.commitSubURUpdate(urNo,"A");
		List<IpUrFirewall> fwList = firewallDao.waitDeleteUr(urNo);
		for(IpUrFirewall fw : fwList){
			IpUrAction action = ipUrActionDao.findUrAction(fw.getId().getUrNo(), fw.getId().getSubUrNo());
			IpUrActionHistory history = new IpUrActionHistory(action);
			history.setUrStatus(IPFMConstant.ACTION_DEL);
			history.setActionName("Requester Delete Sub UR");
			ipUrActionHistoryDao.insert(history);
		}
		firewallDao.commitSubURDelete(urNo,"D");
	}
	
	@Override
	public void updateFirewallSubUrStatus(IpUrFirewall ipUrFirewall)  throws IPFMBusinessException {
		try{
			 firewallDao.update(ipUrFirewall);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	
	@Override
	public int  fineIpinfo(String ipStart,String ipEnd) {
		return ipInfoDao.findIpinfoWithUsedStatus(ipStart, ipEnd);
	}

	@Override
	public int  findTier2TeamID(String ipStart,String ipEnd) {
		return ipInfoDao.findTier2TeamIDWithUsedStatus(ipStart, ipEnd);
	}
	
	@Override
	public int  findSystemOwnerTeamID(String ipStart,String ipEnd) {
		return ipInfoDao.findSystemOwnerTeamIDWithUsedStatus(ipStart, ipEnd);
	}
	
	
	@Override
	public List<IpInfo> findIpInfoAssignIp(String binary2Start,
			String binary2End) throws IPFMBusinessException {
		
		return ipInfoDao.findIpInfoAssignIp(binary2Start, binary2End);
	}

	@Override
	public int countIpInfoStatusNotUsed(String binaryIpStart, String binaryIpEnd) {
		return ipInfoDao.countIpInfoStatusNotUsed(binaryIpStart, binaryIpEnd);
	}
	@Override
	public List<IpInfo> findIpInfoStatusNotUsed(String binary2Start,
			String binary2End) {
		return ipInfoDao.findIpInfoStatusNotUsed(binary2Start, binary2End);
	}
}
