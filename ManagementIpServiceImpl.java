package th.co.ais.ipfm.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CallProcedureDao;
import th.co.ais.ipfm.dao.IIPInfoDAO;
import th.co.ais.ipfm.dao.IIPInfoTempDAO;
import th.co.ais.ipfm.dao.IIPLogVerifyDAO;
import th.co.ais.ipfm.dao.IPFMCommondDao;
import th.co.ais.ipfm.dao.IPInfoHistoryDAO;
import th.co.ais.ipfm.dao.IPLevel1Dao;
import th.co.ais.ipfm.dao.IPLevel2Dao;
import th.co.ais.ipfm.dao.IPLevel2HistoryDao;
import th.co.ais.ipfm.dao.IPRoleMemberDao;
import th.co.ais.ipfm.dao.IPTeamDao;
import th.co.ais.ipfm.dao.IpvNetworkIpDao;
import th.co.ais.ipfm.dao.TempIPLevel2Dao;
import th.co.ais.ipfm.domain.IPLevel3NatHistory;
import th.co.ais.ipfm.domain.IPMasterValue;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpInfoHistory;
import th.co.ais.ipfm.domain1.IpInfoTemp;
import th.co.ais.ipfm.domain1.IpLevel1;
import th.co.ais.ipfm.domain1.IpLevel2;
import th.co.ais.ipfm.domain1.IpLevel2History;
import th.co.ais.ipfm.domain1.IpRoleMember;
import th.co.ais.ipfm.domain1.IpTeam;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.domain1.IpvNetworkIp;
import th.co.ais.ipfm.domain1.TempIpLevel2;
import th.co.ais.ipfm.domain1.IpLogVerify;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.ILoginService;
import th.co.ais.ipfm.service.ManagementIpService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMUtils;



public class ManagementIpServiceImpl implements ManagementIpService{
	private Logger logger = Logger.getLogger(ManagementIpServiceImpl.class);
	
	private IPLevel1Dao ipLevel1Dao;
	private IPLevel2Dao ipLevel2Dao;
	private TempIPLevel2Dao tempIpLevel2Dao;
	private IPLevel2HistoryDao ipLevel2HistoryDao;
	private IIPInfoDAO ipInfoDao;
	private IPInfoHistoryDAO ipInfoHistoryDao;
	private IpvNetworkIpDao ipvNetworkIpDao;
	private CallProcedureDao callProcedureDao;
	private IPFMCommondDao ipfmCommondDao;
	private IPRoleMemberDao ipRoleMemberDao;
	private ILoginService loginService;
	private IPTeamDao ipTeamDao;
	private IIPInfoTempDAO ipInfoTempDao;
	private IIPLogVerifyDAO ipLogVerifyDao;
	
	
	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}

	public void setIpTeamDao(IPTeamDao ipTeamDao) {
		this.ipTeamDao = ipTeamDao;
	}

	public void setTempIpLevel2Dao(TempIPLevel2Dao tempIpLevel2Dao) {
		this.tempIpLevel2Dao = tempIpLevel2Dao;
	}

	public void setIpLevel2HistoryDao(IPLevel2HistoryDao ipLevel2HistoryDao) {
		this.ipLevel2HistoryDao = ipLevel2HistoryDao;
	}

	public void setIpInfoHistoryDao(IPInfoHistoryDAO ipInfoHistoryDao) {
		this.ipInfoHistoryDao = ipInfoHistoryDao;
	}

	public void setIpvNetworkIpDao(IpvNetworkIpDao ipvNetworkIpDao) {
		this.ipvNetworkIpDao = ipvNetworkIpDao;
	}

	public void setIpLevel1Dao(IPLevel1Dao ipLevel1Dao) {
		this.ipLevel1Dao = ipLevel1Dao;
	}

	public void setIpLevel2Dao(IPLevel2Dao ipLevel2Dao) {
		this.ipLevel2Dao = ipLevel2Dao;
	}

	public void setIpInfoDao(IIPInfoDAO ipInfoDao) {
		this.ipInfoDao = ipInfoDao;
	}

	public void setCallProcedureDao(CallProcedureDao callProcedureDao) {
		this.callProcedureDao = callProcedureDao;
	}
	
	public void setIpfmCommondDao(IPFMCommondDao ipfmCommondDao) {
		this.ipfmCommondDao = ipfmCommondDao;
	}
	
	public void setIpRoleMemberDao(IPRoleMemberDao ipRoleMemberDao) {
		this.ipRoleMemberDao = ipRoleMemberDao;
	}

	public void setIpInfoTempDao(IIPInfoTempDAO ipInfoTempDao){
		this.ipInfoTempDao = ipInfoTempDao;
	}
	
	public void setIpLogVerifyDao(IIPLogVerifyDAO ipLogVerifyDao){
		this.ipLogVerifyDao = ipLogVerifyDao;
	}
	@Override
	public String getSysdate() throws IPFMBusinessException {
		String sysDate;
		try {
			sysDate = ipfmCommondDao.getSysdate();
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return sysDate;
	}

	@Override
	public List<IpLevel1> findAllIpLevel1(Integer maxSearchResult) throws IPFMBusinessException {
		List<IpLevel1> ipLevel1List = new ArrayList<IpLevel1>();
		try {
			ipLevel1List = ipLevel1Dao.findAllIpLevel1(maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipLevel1List;
	}
	
	@Override
	public List<IpLevel2> findAllIpLevel2(Integer maxSearchResult) throws IPFMBusinessException {
		List<IpLevel2> ipLevel2List = new ArrayList<IpLevel2>();
		try {
			ipLevel2List = ipLevel2Dao.findAllIpLevel2(maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipLevel2List;
	}
	
	@Override
	public List<IpLevel2> findIpLevel2ByTeam(String teamId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpLevel2> ipLevel2List = new ArrayList<IpLevel2>();
		try {
			ipLevel2List = ipLevel2Dao.findIpLevel2ByTeam(teamId,maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipLevel2List;
	}

	@Override
	public List<IpLevel1> searchIpManagementLevel1(String ipVersion, String ip1, String ip2, String ip3, String ip4, String mask,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpLevel1> result = new ArrayList<IpLevel1>();
		try {
			IpLevel1 ipLevel1 = callProcedureDao.getIpLevel1StartEnd(ip1, ip2, ip3, ip4, mask);
			String binaryStart = "";
			String binaryEnd = "";
			List<IpLevel1> ipLevel1StartList = ipLevel1Dao.searchBinaryStartBetweenDate(ipLevel1.getIp1StartBinary()+ipLevel1.getIp2StartBinary()+ipLevel1.getIp3StartBinary()+ipLevel1.getIp4StartBinary(),"");
			if (ipLevel1StartList.size()>0) {
				binaryStart = ipLevel1StartList.get(0).getBinary1Start();
			}else{
				binaryStart = ipLevel1.getIp1StartBinary()+ipLevel1.getIp2StartBinary()+ipLevel1.getIp3StartBinary()+ipLevel1.getIp4StartBinary();
			}
			List<IpLevel1> ipLevel1EndList = ipLevel1Dao.searchBinaryEndBetweenDate(ipLevel1.getIp1EndBinary()+ipLevel1.getIp2EndBinary()+ipLevel1.getIp3EndBinary()+ipLevel1.getIp4EndBinary(),"");
			if (ipLevel1EndList.size()>0) {
				binaryEnd = ipLevel1EndList.get(ipLevel1EndList.size()-1).getBinary1End();
			}else{
				binaryEnd = ipLevel1.getIp1EndBinary()+ipLevel1.getIp2EndBinary()+ipLevel1.getIp3EndBinary()+ipLevel1.getIp4EndBinary();
			}
			result = ipLevel1Dao.searchBinaryRange(binaryStart,binaryEnd,"");
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	@Override
	public List<IpLevel1> searchIpManagementLevel1ByTeam(String ipVersion, String ip1, String ip2, String ip3, String ip4, String mask, String teamId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpLevel1> result = new ArrayList<IpLevel1>();
		try {
			IpLevel1 ipLevel1 = callProcedureDao.getIpLevel1StartEnd(ip1, ip2, ip3, ip4, mask);
			String binaryStart = "";
			String binaryEnd = "";
			List<IpLevel1> ipLevel1StartList = ipLevel1Dao.searchBinaryStartBetweenDate(ipLevel1.getIp1StartBinary()+ipLevel1.getIp2StartBinary()+ipLevel1.getIp3StartBinary()+ipLevel1.getIp4StartBinary(),teamId);
			if (ipLevel1StartList.size()>0) {
				binaryStart = ipLevel1StartList.get(0).getBinary1Start();
			}else{
				binaryStart = ipLevel1.getIp1StartBinary()+ipLevel1.getIp2StartBinary()+ipLevel1.getIp3StartBinary()+ipLevel1.getIp4StartBinary();
			}
			List<IpLevel1> ipLevel1EndList = ipLevel1Dao.searchBinaryEndBetweenDate(ipLevel1.getIp1EndBinary()+ipLevel1.getIp2EndBinary()+ipLevel1.getIp3EndBinary()+ipLevel1.getIp4EndBinary(),teamId);
			if (ipLevel1EndList.size()>0) {
				binaryEnd = ipLevel1EndList.get(ipLevel1EndList.size()-1).getBinary1End();
			}else{
				binaryEnd = ipLevel1.getIp1EndBinary()+ipLevel1.getIp2EndBinary()+ipLevel1.getIp3EndBinary()+ipLevel1.getIp4EndBinary();
			}
			result = ipLevel1Dao.searchBinaryRange(binaryStart,binaryEnd,teamId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public IpLevel1 saveIpLevel1(IpLevel1 ipLevel1,String updateBy) throws IPFMBusinessException {
		try {
			Date updateDate = new Timestamp(new Date().getTime());	
			if (ipLevel1.getIpStatusOld().equalsIgnoreCase("P") && ipLevel1.getIpStatus().equalsIgnoreCase("N")) {
				ipInfoDao.insertIpInfoHistoryByLevel1Id(ipLevel1.getLevel1Id(),updateDate,updateBy);
				ipInfoDao.deleteIpLevel3ByLevel1Id(ipLevel1.getLevel1Id());
				ipLevel2Dao.insertHistoryByLevel1Id(ipLevel1.getLevel1Id(),updateDate,updateBy);
				ipLevel2Dao.deleteIpLevel2ByLevel1Id(ipLevel1.getLevel1Id());
				ipLevel1Dao.insertHistoryByLevel1Id(ipLevel1.getLevel1Id(),updateDate,updateBy);
				ipLevel1Dao.delete(ipLevel1);
			}else{
				ipLevel1.setLastUpd(null);
				ipLevel1.setLastUpdBy(updateBy);
				ipLevel1Dao.update(ipLevel1);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipLevel1;
	}
	
	@Override
	public List<IpLevel2> searchIpManagementLevel2(String ipVersion, String ip1, String ip2, String ip3, String ip4, String mask,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpLevel2> result = new ArrayList<IpLevel2>();
		try {
			
//			callProcedureDao.processIpLevel2(ip1, ip2, ip3, ip4, mask, "All");
//			List<TempIpLevel2> ipLevel2Temp = tempIpLevel2Dao.findTempIpLevel2(maxSearchResult);
			List<TempIpLevel2> ipLevel2Temp = tempIpLevel2Dao.searchIpLevel2(ip1, ip2, ip3, ip4, mask, "All", maxSearchResult);
			result.addAll(prepareIpLevel2(ipLevel2Temp));
//			IpLevel2 ipLevel2 = callProcedureDao.getIpLevel2StartEnd(ip1, ip2, ip3, ip4, mask);
//			String binaryStart = "";
//			String binaryEnd = "";
//			List<IpLevel2> ipLevel2StartList = ipLevel2Dao.searchBinaryStartBetweenDate(ipLevel2.getIp1StartBinary()+ipLevel2.getIp2StartBinary()+ipLevel2.getIp3StartBinary()+ipLevel2.getIp4StartBinary());
//			if (ipLevel2StartList.size()>0) {
//				binaryStart = ipLevel2StartList.get(0).getBinary2Start();
//			}else{
//				binaryStart = ipLevel2.getIp1StartBinary()+ipLevel2.getIp2StartBinary()+ipLevel2.getIp3StartBinary()+ipLevel2.getIp4StartBinary();
//			}
//			List<IpLevel2> ipLevel2EndList = ipLevel2Dao.searchBinaryEndBetweenDate(ipLevel2.getIp1EndBinary()+ipLevel2.getIp2EndBinary()+ipLevel2.getIp3EndBinary()+ipLevel2.getIp4EndBinary());
//			if (ipLevel2EndList.size()>0) {
//				binaryEnd = ipLevel2EndList.get(ipLevel2EndList.size()-1).getBinary2End();
//			}else{
//				binaryEnd = ipLevel2.getIp1EndBinary()+ipLevel2.getIp2EndBinary()+ipLevel2.getIp3EndBinary()+ipLevel2.getIp4EndBinary();
//			}
//			result = ipLevel2Dao.searchBinaryRange(binaryStart,binaryEnd,maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	private Collection<? extends IpLevel2> prepareIpLevel2( List<TempIpLevel2> ipLevel2Temp) {
		List<IpLevel2> result = new ArrayList<IpLevel2>();
		IpLevel2 level2;
		for (TempIpLevel2 temp : ipLevel2Temp) {
			level2 = new IpLevel2();
			level2.setAssignIp(temp.getAssignIp());
			level2.setAvailableIp(temp.getAvailableIp());
			level2.setBinary2End(temp.getBinary2End());
			level2.setBinary2Start(temp.getBinary2Start());
			level2.setCompanyId(temp.getCompanyId());
			level2.setCompanyName(temp.getCompanyName());
			level2.setCreated(temp.getCreated());
			level2.setCreatedBy(temp.getCreatedBy());
			level2.setDnsName(temp.getDnsName());
			level2.setEffectiveDate(temp.getEffectiveDate());
			level2.setExpiredDate(temp.getExpiredDate());
			level2.setGateway(temp.getGateway());
			level2.setInstallTypeId(temp.getInstallTypeId());
			level2.setInstallTypeName(temp.getInstallTypeName());
			level2.setIpStatus(temp.getIpStatus());
			level2.setIpSubmask(temp.getIpSubmask());
			level2.setIpTypeId(temp.getIpTypeId());
			level2.setIpTypeName(temp.getIpTypeName());
			level2.setIpVersion(temp.getIpVersion());
			level2.setLastUpd(temp.getLastUpd());
			level2.setLastUpdBy(temp.getLastUpdBy());
			level2.setLevel1Id(temp.getLevel1Id());
			level2.setLevel2End(temp.getLevel2End());
			level2.setLevel2Id(temp.getLevel2Id());
			level2.setLevel2Start(temp.getLevel2Start());
			level2.setLocationId(temp.getLocationId());
			level2.setLocationName(temp.getLocationName());
			level2.setNetworkIp(temp.getNetworkIp());
			level2.setNetworkTypeId(temp.getNetworkTypeId());			
			level2.setNetworkTypeName(temp.getNetworkTypeName());
			level2.setNetworkZoneId(temp.getNetworkZoneId());
			level2.setNetworkZoneName(temp.getNetworkZoneName());
			level2.setReserveIp(temp.getReserveIp());
			level2.setRowId(temp.getRowId());
			level2.setSubmask(temp.getSubmask());
			level2.setSystemName(temp.getSystemName());
			level2.setT1Remark(temp.getT1Remark());
			level2.setT1TeamId(temp.getT1TeamId());
			level2.setT1TeamName(temp.getT1TeamName());
			level2.setT2Remark(temp.getT2Remark());
			level2.setT2TeamId(temp.getT2TeamId());
			level2.setT2TeamName(temp.getT2TeamName());
			level2.setTerminateIp(temp.getTerminateIp());
			level2.setTotalIp(temp.getTotalIp());
			level2.setUseIp(temp.getUseIp());
			level2.setVersion(temp.getVersion());
			level2.setVip(temp.getVip());
			level2.setVlanId(temp.getVlanId());
			result.add(level2);
		}
		return result;
	}

	@Override
	public List<IpLevel2> searchIpManagementLevel2ByTeam(String ipVersion, String ip1, String ip2, String ip3, String ip4, String mask, String teamId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpLevel2> result = new ArrayList<IpLevel2>();
		try {
//			callProcedureDao.processIpLevel2(ip1, ip2, ip3, ip4, mask, teamId);
//			List<TempIpLevel2> ipLevel2Temp = tempIpLevel2Dao.findTempIpLevel2(maxSearchResult);
			List<TempIpLevel2> ipLevel2Temp = tempIpLevel2Dao.searchIpLevel2(ip1, ip2, ip3, ip4, mask, teamId, maxSearchResult);
			result.addAll(prepareIpLevel2(ipLevel2Temp));
			
//			IpLevel2 ipLevel2 = callProcedureDao.getIpLevel2StartEnd(ip1, ip2, ip3, ip4, mask);
//			String binaryStart = "";
//			String binaryEnd = "";
//			List<IpLevel2> ipLevel2StartList = ipLevel2Dao.searchBinaryStartBetweenDate(ipLevel2.getIp1StartBinary()+ipLevel2.getIp2StartBinary()+ipLevel2.getIp3StartBinary()+ipLevel2.getIp4StartBinary(),teamId);
//			if (ipLevel2StartList.size()>0) {
//				binaryStart = ipLevel2StartList.get(0).getBinary2Start();
//			}else{
//				binaryStart = ipLevel2.getIp1StartBinary()+ipLevel2.getIp2StartBinary()+ipLevel2.getIp3StartBinary()+ipLevel2.getIp4StartBinary();
//			}
//			List<IpLevel2> ipLevel2EndList = ipLevel2Dao.searchBinaryEndBetweenDate(ipLevel2.getIp1EndBinary()+ipLevel2.getIp2EndBinary()+ipLevel2.getIp3EndBinary()+ipLevel2.getIp4EndBinary(),teamId);
//			if (ipLevel2EndList.size()>0) {
//				binaryEnd = ipLevel2EndList.get(ipLevel2EndList.size()-1).getBinary2End();
//			}else{
//				binaryEnd = ipLevel2.getIp1EndBinary()+ipLevel2.getIp2EndBinary()+ipLevel2.getIp3EndBinary()+ipLevel2.getIp4EndBinary();
//			}
//			result = ipLevel2Dao.searchBinaryRange(binaryStart,binaryEnd,teamId,maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	@Override
	public IpLevel2 saveIpLevel2(IpLevel2 ipLevel2,String updateBy) throws IPFMBusinessException {
		try {
			Date updateDate = new Timestamp(new Date().getTime());			
			if (ipLevel2.getIpStatus().equalsIgnoreCase("N")) {
				IpLevel1 ipLevel1 = ipLevel1Dao.findIpLevel1ByLevel1Id(ipLevel2.getLevel1Id());
				ipLevel1.setTotalT2Assign(ipLevel1.getTotalT2Assign().subtract(ipLevel2.getTotalIp()));
				ipLevel1.setLastUpdBy(updateBy);
				ipLevel1.setLastUpd(null);
				ipInfoDao.insertIpInfoHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
				ipInfoDao.deleteIpLevel3ByLv2(ipLevel2.getLevel2Id());
				ipLevel2Dao.insertHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
				ipLevel2Dao.delete(ipLevel2);
				ipLevel1Dao.update(ipLevel1);
			}else if (ipLevel2.getIpStatus().equalsIgnoreCase("P")) {
//				ipLevel2.setAvailableIp(ipLevel2.getAssignIp().add(ipLevel2.getTerminateIp()));
//				ipLevel2.setAssignIp(new BigDecimal(0));
//				ipLevel2.setTerminateIp(new BigDecimal(0));
//				ipInfoDao.insertIpInfoHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
//				ipInfoDao.deleteIpLevel3ByLv2(ipLevel2.getLevel2Id());
//				ipLevel2Dao.update(ipLevel2);
				
				IpLevel1 ipLevel1 = ipLevel1Dao.findIpLevel1ByLevel1Id(ipLevel2.getLevel1Id());
				ipLevel1.setTotalT2Assign(ipLevel1.getTotalT2Assign().subtract(ipLevel2.getTotalIp()));
				ipLevel1.setLastUpdBy(updateBy);
				ipLevel1.setLastUpd(null);
				ipInfoDao.insertIpInfoHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
				ipInfoDao.deleteIpLevel3ByLv2(ipLevel2.getLevel2Id());
				ipLevel2Dao.insertHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
				ipLevel2Dao.delete(ipLevel2);
				ipLevel1Dao.update(ipLevel1);
			}else{
//				updateStatusValue(ipLevel2, ipLevel2.getIpStatusOld(), ipLevel2.getIpStatus());
				ipLevel2.setLastUpdBy(updateBy);
				ipLevel2.setLastUpd(null);
				ipLevel2Dao.update(ipLevel2);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipLevel2;
	}
	
	public void saveIpLevel2(List<IpLevel2> ipLevel2List, String updateBy) throws IPFMBusinessException {
		try {
			Date updateDate = new Timestamp(new Date().getTime());			
			for (IpLevel2 ipLevel2 : ipLevel2List) {
				if (ipLevel2.getIpStatus().equalsIgnoreCase("N")) {
					IpLevel1 ipLevel1 = ipLevel1Dao.findIpLevel1ByLevel1Id(ipLevel2.getLevel1Id());
					ipLevel1.setTotalT2Assign(ipLevel1.getTotalT2Assign().subtract(ipLevel2.getTotalIp()));
					ipLevel1.setLastUpdBy(updateBy);
					ipLevel1.setLastUpd(null);
					ipInfoDao.insertIpInfoHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
					ipInfoDao.deleteIpLevel3ByLv2(ipLevel2.getLevel2Id());
					ipLevel2Dao.insertHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
					ipLevel2Dao.delete(ipLevel2);
					ipLevel1Dao.update(ipLevel1);
				}else if (ipLevel2.getIpStatus().equalsIgnoreCase("P")) {
					IpLevel1 ipLevel1 = ipLevel1Dao.findIpLevel1ByLevel1Id(ipLevel2.getLevel1Id());
					ipLevel1.setTotalT2Assign(ipLevel1.getTotalT2Assign().subtract(ipLevel2.getTotalIp()));
					ipLevel1.setLastUpdBy(updateBy);
					ipLevel1.setLastUpd(null);
					ipInfoDao.insertIpInfoHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
					ipInfoDao.deleteIpLevel3ByLv2(ipLevel2.getLevel2Id());
					ipLevel2Dao.insertHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
					ipLevel2Dao.delete(ipLevel2);
					ipLevel1Dao.update(ipLevel1);
				}else{
					ipLevel2.setLastUpdBy(updateBy);
					ipLevel2.setLastUpd(null);
					ipLevel2Dao.update(ipLevel2);
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	
	private IpUser checkNewUser(String userId) throws IPFMBusinessException {
		IpUser ipUser = null;
		try {
			ipUser = loginService.getIPUser(userId);
			if (ipUser==null){
				ipUser = loginService.getEhrByUserId(userId);
				loginService.addUser(ipUser);
				loginService.addIpRoleMemberByUser(ipUser);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUser;
	}
	@Override
	public IpInfo updateIpInfo(String oldIpStatus, IpInfo ipInfo, String updateBy) throws IPFMBusinessException {
		try {
			IpLevel2 ipLevel2 = ipLevel2Dao.findIpLevel2ByLevel2Id(ipInfo.getLevel2Id());
			updateStatusValue(ipLevel2,oldIpStatus,ipInfo.getIpStatus());
			if (!IPFMUtils.ifBlank(ipInfo.getIpStatus(),"").equalsIgnoreCase("P")) {
				IpUser ipUser = checkNewUser(ipInfo.getSystemOwner());
				if (IPFMUtils.ifBlank(ipInfo.getSystemOwnerTeamName(),"").length()>0 
						&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
					if (IPFMUtils.ifBlank(ipInfo.getSystemOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
						ipInfo.setSystemOwnerTeamId(ipUser.getTeamId());
					}
				}
				ipLevel2.setLastUpd(null);
				ipLevel2.setLastUpdBy(updateBy);
				ipInfo.setLastUpd(null);
				ipInfo.setLastUpdBy(updateBy);
				ipLevel2Dao.update(ipLevel2);
				ipInfoDao.update(ipInfo);
			}else if (IPFMUtils.ifBlank(ipInfo.getIpStatus(),"").equalsIgnoreCase("P")) {				
				if (ipLevel2.getAvailableIp().intValue()==ipLevel2.getTotalIp().intValue()) {
					Date updateDate = new Timestamp(new Date().getTime());
					IpLevel1 ipLevel1 = ipLevel1Dao.findIpLevel1ByLevel1Id(ipLevel2.getLevel1Id());
					ipLevel1.setTotalT2Assign(ipLevel1.getTotalT2Assign().subtract(ipLevel2.getTotalIp()));
					ipLevel1.setLastUpdBy(updateBy);
					ipLevel1.setLastUpd(null);
					ipInfoDao.insertIpInfoHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
					ipInfoDao.deleteIpLevel3ByLv2(ipLevel2.getLevel2Id());
					ipLevel2Dao.insertHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
					ipLevel2Dao.delete(ipLevel2);
					ipLevel1Dao.update(ipLevel1);
				}else{
					ipLevel2.setLastUpd(null);
					ipLevel2.setLastUpdBy(updateBy);
					ipInfo.setLastUpd(null);
					ipInfo.setLastUpdBy(updateBy);
					IpInfoHistory history = copyIpInfoHistory(ipInfo);
					history.setIpStatus(oldIpStatus);
					ipInfoHistoryDao.insert(history);
					ipLevel2Dao.update(ipLevel2);
					ipInfoDao.delete(ipInfo);
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipInfo;
	}
	
	private void updateStatusValue(IpLevel2 ipLevel2,String oldStatus,String newStatus){
		if (oldStatus.equalsIgnoreCase("A")) {
			ipLevel2.setAssignIp(ipLevel2.getAssignIp().subtract(new BigDecimal(1)));
		}else if (oldStatus.equalsIgnoreCase("U")){
			ipLevel2.setUseIp(ipLevel2.getUseIp().subtract(new BigDecimal(1)));
		}else if (oldStatus.equalsIgnoreCase("R")){
			ipLevel2.setReserveIp(ipLevel2.getReserveIp().subtract(new BigDecimal(1)));
		}else if (oldStatus.equalsIgnoreCase("T")){
			ipLevel2.setTerminateIp(ipLevel2.getTerminateIp().subtract(new BigDecimal(1)));
		}
		
		if (newStatus.equalsIgnoreCase("A")) {
			ipLevel2.setAssignIp(ipLevel2.getAssignIp().add(new BigDecimal(1)));
		}else if (newStatus.equalsIgnoreCase("U")){
			ipLevel2.setUseIp(ipLevel2.getUseIp().add(new BigDecimal(1)));
		}else if (newStatus.equalsIgnoreCase("R")){
			ipLevel2.setReserveIp(ipLevel2.getReserveIp().add(new BigDecimal(1)));
		}else if (newStatus.equalsIgnoreCase("T")){
			ipLevel2.setTerminateIp(ipLevel2.getTerminateIp().add(new BigDecimal(1)));
		}else if (newStatus.equalsIgnoreCase("P")){
			ipLevel2.setAvailableIp(ipLevel2.getAvailableIp().add(new BigDecimal(1)));
		}
	}

	@Override
	public List<IpRoleMember> findIpRoleMemberByUserId(String userId) throws IPFMBusinessException {
		List<IpRoleMember> resultList = new ArrayList<IpRoleMember>();
		try {
			resultList = ipRoleMemberDao.findIpRoleMembersByUserId(userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}

	@Override
	public List<IpInfo> searchIpManagementLevel3(String ipAddress, String tier2TeamId, String sysOwnerTeamId, String sysOwnerId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpInfo> resultList = new ArrayList<IpInfo>();
		try {
//			System.out.println("--- searchIpManagementLevel3 ---");
			resultList = ipInfoDao.searchIpInfo(ipAddress, tier2TeamId, sysOwnerTeamId, sysOwnerId, maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	
	@Override
	public List<IpvNetworkIp> searhNetworkIpByTier1(Integer maxSearchResult) throws IPFMBusinessException {
		List<IpvNetworkIp> resultList = new ArrayList<IpvNetworkIp>();
		try {
//			System.out.println("--- searhNetworkIpByTier1 ---");
			resultList = ipvNetworkIpDao.searhNetworkIpByTier1(maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	
	@Override
	public List<IpvNetworkIp> searhNetworkIpByTier2(String tier2TeamId, String userId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpvNetworkIp> resultList = new ArrayList<IpvNetworkIp>();
		try {
//			System.out.println("--- searhNetworkIpByTier2 ---");
			resultList = ipvNetworkIpDao.searhNetworkIpByTier2(tier2TeamId,userId,maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	
	@Override
	public List<IpvNetworkIp> searhNetworkIpByTier3(String sysOwnerTeamId, String sysOwnerId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpvNetworkIp> resultList = new ArrayList<IpvNetworkIp>();
		try {
//			System.out.println("--- searhNetworkIpByTier3 ---");
			resultList = ipvNetworkIpDao.searhNetworkIpByTier3(sysOwnerTeamId, sysOwnerId, maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	@Override
	public List<IpvNetworkIp> searhNetworkIpRangeByTier1(String ipDigit1, String ipDigit2, String ipDigit3, String ipDigit4, String mask, Integer maxSearchResult) throws IPFMBusinessException {
		List<IpvNetworkIp> resultList = new ArrayList<IpvNetworkIp>();
		try {
			IpInfo ipInfo = callProcedureDao.getIpInfoStartEnd(ipDigit1, ipDigit2, ipDigit3, ipDigit4, mask);
			ipInfo.setBinaryIpStart(IPFMDataUtility.convertIpStringToBinary(ipInfo.getBinaryIpStart()));
			ipInfo.setBinaryIpEnd(IPFMDataUtility.convertIpStringToBinary(ipInfo.getBinaryIpEnd()));
			resultList = ipvNetworkIpDao.searhNetworkIpRangeByTier1(ipInfo.getBinaryIpStart(),ipInfo.getBinaryIpEnd(),maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	@Override
	public List<IpvNetworkIp> searhNetworkIpRangeByTier2(String ipDigit1, String ipDigit2, String ipDigit3, String ipDigit4, String mask, String tier2TeamId, String userId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpvNetworkIp> resultList = new ArrayList<IpvNetworkIp>();
		try {
			IpInfo ipInfo = callProcedureDao.getIpInfoStartEnd(ipDigit1, ipDigit2, ipDigit3, ipDigit4, mask);
			ipInfo.setBinaryIpStart(IPFMDataUtility.convertIpStringToBinary(ipInfo.getBinaryIpStart()));
			ipInfo.setBinaryIpEnd(IPFMDataUtility.convertIpStringToBinary(ipInfo.getBinaryIpEnd()));			
			resultList = ipvNetworkIpDao.searhNetworkIpRangeByTier2(ipInfo.getBinaryIpStart(),ipInfo.getBinaryIpEnd(),tier2TeamId,userId,maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	
	public List<IpvNetworkIp> searhNetworkIpRangeByTier3(String ipDigit1, String ipDigit2, String ipDigit3, String ipDigit4, String mask, String sysOwnerTeamId, String sysOwnerId,Integer maxSearchResult) throws IPFMBusinessException {
		List<IpvNetworkIp> resultList = new ArrayList<IpvNetworkIp>();
		try {
			IpInfo ipInfo = callProcedureDao.getIpInfoStartEnd(ipDigit1, ipDigit2, ipDigit3, ipDigit4, mask);
			ipInfo.setBinaryIpStart(IPFMDataUtility.convertIpStringToBinary(ipInfo.getBinaryIpStart()));
			ipInfo.setBinaryIpEnd(IPFMDataUtility.convertIpStringToBinary(ipInfo.getBinaryIpEnd()));			
			
			resultList = ipvNetworkIpDao.searhNetworkIpRangeByTier3(ipInfo.getBinaryIpStart(),ipInfo.getBinaryIpEnd(),sysOwnerTeamId, sysOwnerId, maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}
	
	
	@Override
	public void updateIpLevel2ExpireDate(List<IpLevel2> ipLevel2List) throws IPFMBusinessException {
		try {
			for (IpLevel2 lv2 : ipLevel2List) {
				ipLevel2Dao.update(lv2);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}

	@Override
	public void updateIpInfoExpireDate(List<IpInfo> ipInfoList) throws IPFMBusinessException {
		try {
			for (IpInfo ipInfo : ipInfoList) {
				ipInfoDao.update(ipInfo);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	@Override
	public void ipInfoAssignToPlanning(IpInfo ipInfo,String updateBy) throws IPFMBusinessException{
		try {
			IpLevel2 ipLevel2 = ipLevel2Dao.findIpLevel2ByLevel2Id(ipInfo.getLevel2Id());
			ipLevel2.setTerminateIp(ipLevel2.getTerminateIp().add(new BigDecimal(1)));
			ipLevel2.setAssignIp(ipLevel2.getAssignIp().subtract(new BigDecimal(1)));
			ipLevel2.setLastUpd(null);
			ipLevel2.setLastUpdBy(updateBy);
			IpInfoHistory history = copyIpInfoHistory(ipInfo);
			ipInfoHistoryDao.insert(history);
			ipLevel2Dao.update(ipLevel2);
			ipInfoDao.delete(ipInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	@Override
	public IpLevel2History copyIpLevel2History(IpLevel2 bean) throws IPFMBusinessException{
		IpLevel2History history = null;
		try {
			history = new IpLevel2History();
			history.setIpVersion(bean.getIpVersion());
			history.setAssignIp(bean.getAssignIp());
			history.setAvailableIp(bean.getAvailableIp());
			history.setBinary2End(bean.getBinary2End());
			history.setBinary2Start(bean.getBinary2Start());
			history.setCompanyId(bean.getCompanyId());
			history.setCompanyName(bean.getCompanyName());
			history.setDnsName(bean.getDnsName());
			history.setEffectiveDate(bean.getEffectiveDate());
			history.setExpiredDate(bean.getExpiredDate());
			history.setGateway(bean.getGateway());
			history.setInstallTypeId(bean.getInstallTypeId());
			history.setInstallTypeName(bean.getInstallTypeName());
			history.setIpStatus(bean.getIpStatusOld());
			history.setIpSubmask(bean.getIpSubmask());
			history.setIpTypeId(bean.getIpTypeId());
			history.setIpTypeName(bean.getIpTypeName());
			history.setLevel1Id(bean.getLevel1Id());
			history.setLevel2End(bean.getLevel2End());
			history.setLevel2Id(bean.getLevel2Id());
			history.setLevel2Start(bean.getLevel2Start());
			history.setLocationId(bean.getLocationId());
			history.setLocationName(bean.getLocationName());
			history.setNetworkIp(bean.getNetworkIp());
			history.setNetworkTypeId(bean.getNetworkTypeId());
			history.setNetworkTypeName(bean.getNetworkTypeName());
			history.setNetworkZoneId(bean.getNetworkZoneId());
			history.setNetworkZoneName(bean.getNetworkZoneName());
			history.setReserveIp(bean.getReserveIp());
			history.setSubmask(bean.getSubmask());
			history.setSystemName(bean.getSystemName());
			history.setT1Remark(bean.getT1Remark());
			history.setT1TeamId(bean.getT1TeamId());
			history.setT1TeamName(bean.getT1TeamName());
			history.setT2Remark(bean.getT2Remark());
			history.setT2TeamId(bean.getT2TeamId());
			history.setT2TeamName(bean.getT2TeamName());
			history.setTerminateIp(bean.getTerminateIp());
			history.setTotalIp(bean.getTotalIp());
			history.setUseIp(bean.getUseIp());
			history.setVip(bean.getVip());
			history.setVlanId(bean.getVlanId());
			history.setCreated(bean.getCreated());
			history.setCreatedBy(bean.getCreatedBy());
			history.setLastUpd(bean.getLastUpd());
			history.setLastUpdBy(bean.getLastUpdBy());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return history;
	}
	@Override
	public IpInfoHistory copyIpInfoHistory(IpInfo ipInfo) throws IPFMBusinessException{
		IpInfoHistory history = null;
		try {
			history = new IpInfoHistory();
			history.setAvailableIP(ipInfo.getAvailableIP());
			history.setBinaryIp(ipInfo.getBinaryIp());
			history.setBinaryIpEnd(ipInfo.getBinaryIpEnd());
			history.setBinaryIpStart(ipInfo.getBinaryIpStart());
			history.setCompanyId(ipInfo.getCompanyId());
			history.setCompanyName(ipInfo.getCompanyName());			
			history.setDnsName(ipInfo.getDnsName());
			history.setEffectiveDate(ipInfo.getEffectiveDate());
			history.setExpiredDate(ipInfo.getExpiredDate());
			history.setGateway(ipInfo.getGateway());
			history.setHostName(ipInfo.getHostName());
			history.setInstallTypeId(ipInfo.getInstallTypeId());
			history.setInstallTypeName(ipInfo.getInstallTypeName());
			history.setIpAddress(ipInfo.getIpAddress());
			history.setIpStatus(ipInfo.getIpStatus());
			history.setIpSubmask(ipInfo.getIpSubmask());
			history.setIpTypeId(ipInfo.getIpTypeId());
			history.setIpTypeName(ipInfo.getIpTypeName());
			history.setIpv4digit1(ipInfo.getIpv4digit1());
			history.setIpv4digit2(ipInfo.getIpv4digit2());
			history.setIpv4digit3(ipInfo.getIpv4digit3());
			history.setIpv4digit4(ipInfo.getIpv4digit4());
			history.setIpv6digit1(ipInfo.getIpv6digit1());
			history.setIpv6digit2(ipInfo.getIpv6digit2());
			history.setIpv6digit3(ipInfo.getIpv6digit3());
			history.setIpv6digit4(ipInfo.getIpv6digit4());
			history.setIpv6digit5(ipInfo.getIpv6digit5());
			history.setIpv6digit6(ipInfo.getIpv6digit6());
			history.setIpVersion(ipInfo.getIpVersion());
			history.setLevel1Id(ipInfo.getLevel1Id());
			history.setLevel2Id(ipInfo.getLevel2Id());
			history.setLocationId(ipInfo.getLocationId());
			history.setLocationName(ipInfo.getLocationName());
			history.setMacAddress(ipInfo.getMacAddress());
			history.setNetworkIp(ipInfo.getNetworkIp());
			history.setNetworkTypeId(ipInfo.getNetworkTypeId());
			history.setNetworkTypeName(ipInfo.getNetworkTypeName());
			history.setNetworkZoneId(ipInfo.getNetworkZoneId());
			history.setNetworkZoneName(ipInfo.getNetworkZoneName());
			history.setProjectId(ipInfo.getProjectId());
			history.setProjectName(ipInfo.getProjectName());
			history.setProjectManager(ipInfo.getProjectManager());
			history.setSubmask(ipInfo.getSubmask());
			history.setSubNet(ipInfo.getSubNet());
			history.setSystemName(ipInfo.getSystemName());
			history.setSystemOwner(ipInfo.getSystemOwner());
			history.setSystemOwnerName(ipInfo.getSystemOwnerName());
			history.setSystemOwnerTeamId(ipInfo.getSystemOwnerTeamId());
			history.setSystemOwnerTeamName(ipInfo.getSystemOwnerTeamName());
			history.setT1Remark(ipInfo.getT1Remark());
			history.setT1TeamId(ipInfo.getT1TeamId());
			history.setT1TeamName(ipInfo.getT1TeamName());
			history.setT2Remark(ipInfo.getT2Remark());
			history.setT2TeamId(ipInfo.getT2TeamId());
			history.setT2TeamName(ipInfo.getT2TeamName());
			history.setT3Remark(ipInfo.getT3Remark());
			history.setTotalIP(ipInfo.getTotalIP());
			history.setUrRefer(ipInfo.getUrRefer());
			history.setVip(ipInfo.getVip());
			history.setVlanId(ipInfo.getVlanId());
			history.setWyNodeName(ipInfo.getWyNodeName());
			history.setCreated(ipInfo.getCreated());
			history.setCreatedBy(ipInfo.getCreatedBy());
			history.setLastUpd(ipInfo.getLastUpd());
			history.setLastUpdBy(ipInfo.getLastUpdBy());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return history;
	}
	@Override
	public void ipLevel2AssignToAvailable(IpLevel2 ipLevel2) throws IPFMBusinessException{
		try {
			IpLevel1 ipLevel1 = ipLevel1Dao.findIpLevel1ByLevel1Id(ipLevel2.getLevel1Id());
			ipLevel1.setTotalT2Assign(ipLevel1.getTotalT2Assign().subtract(ipLevel2.getTotalIp()));
			ipLevel1Dao.update(ipLevel1);
			ipLevel2Dao.delete(ipLevel2);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	@Override
	public List<IpLevel1> findIpLevel1ByTeam(String teamId,Integer maxSearchResult) throws IPFMBusinessException{
		List<IpLevel1> ipLevel1List = new ArrayList<IpLevel1>();
		try {
			ipLevel1List = ipLevel1Dao.findIpLevel1ByTeam(teamId,maxSearchResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipLevel1List;
	}
	
	@Override
	public IpTeam findIpTeamById(String teamId) throws IPFMBusinessException {
		IpTeam ipTeam = null;
		try {
			if (teamId!=null && !teamId.trim().equalsIgnoreCase("NA")) {
				ipTeam = ipTeamDao.getIpTeam(teamId);
			}else{
				ipTeam = ipTeamDao.getIpTeamByTeamName(teamId);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipTeam;
	}

	@Override
	public boolean validateOverExpire(String level2Id, String expiredDate) throws IPFMBusinessException {
		boolean overExpired = false;
		try {
			String overExpiredStr = ipInfoDao.validateOverExpire(level2Id, expiredDate);
			if (overExpiredStr!= null && overExpiredStr.equalsIgnoreCase("Y")) overExpired = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return overExpired;
	}

	@Override
	public boolean validateOverExpireLevel1(String level1Id, String expiredDate) throws IPFMBusinessException {
		boolean overExpired = false;
		try {
			String overExpiredStr = ipLevel2Dao.validateOverExpireLevel1(level1Id, expiredDate);
			if (overExpiredStr!= null && overExpiredStr.equalsIgnoreCase("Y")) overExpired = true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return overExpired;
	}
	
	public void updateIpInfo(List<IpInfo> ipInfoList, String updateBy) throws IPFMBusinessException {
		IpUser tempIpUser = null;
		IpUser ipUser = null;
		String tempSystemOwner = null;
		try {
			for(IpInfo ipInfo:ipInfoList){
				IpLevel2 ipLevel2 = ipLevel2Dao.findIpLevel2ByLevel2Id(ipInfo.getLevel2Id());
				updateStatusValue(ipLevel2,ipInfo.getIpStatusOld(),ipInfo.getIpStatus());
				if (!IPFMUtils.ifBlank(ipInfo.getIpStatus(),"").equalsIgnoreCase("P")) {
					if(tempSystemOwner!=null && tempSystemOwner.equals(ipInfo.getSystemOwner())){
						ipUser = tempIpUser; 
					}else{
						tempSystemOwner = ipInfo.getSystemOwner();
						ipUser = checkNewUser(ipInfo.getSystemOwner());
						tempIpUser = ipUser;
					}
					if (IPFMUtils.ifBlank(ipInfo.getSystemOwnerTeamName(),"").length()>0 
							&& IPFMUtils.ifBlank(ipUser.getTeamName(),"").length()>0 ) {
						if (IPFMUtils.ifBlank(ipInfo.getSystemOwnerTeamName(),"").equalsIgnoreCase(ipUser.getTeamName())) {
							ipInfo.setSystemOwnerTeamId(ipUser.getTeamId());
						}
					}
					ipLevel2.setLastUpd(null);
					ipLevel2.setLastUpdBy(updateBy);
					ipInfo.setLastUpd(null);
					ipInfo.setLastUpdBy(updateBy);
					ipLevel2Dao.update(ipLevel2);
					ipInfoDao.update(ipInfo);
				}else if (IPFMUtils.ifBlank(ipInfo.getIpStatus(),"").equalsIgnoreCase("P")) {				
					if (ipLevel2.getAvailableIp().intValue()==ipLevel2.getTotalIp().intValue()) {
						Date updateDate = new Timestamp(new Date().getTime());
						IpLevel1 ipLevel1 = ipLevel1Dao.findIpLevel1ByLevel1Id(ipLevel2.getLevel1Id());
						ipLevel1.setTotalT2Assign(ipLevel1.getTotalT2Assign().subtract(ipLevel2.getTotalIp()));
						ipLevel1.setLastUpdBy(updateBy);
						ipLevel1.setLastUpd(null);
						ipInfoDao.insertIpInfoHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
						ipInfoDao.deleteIpLevel3ByLv2(ipLevel2.getLevel2Id());
						ipLevel2Dao.insertHistoryByLevel2Id(ipLevel2.getLevel2Id(),updateDate,updateBy);
						ipLevel2Dao.delete(ipLevel2);
						ipLevel1Dao.update(ipLevel1);
					}else{
						ipLevel2.setLastUpd(null);
						ipLevel2.setLastUpdBy(updateBy);
						ipInfo.setLastUpd(null);
						ipInfo.setLastUpdBy(updateBy);
						IpInfoHistory history = copyIpInfoHistory(ipInfo);
						history.setIpStatus(ipInfo.getIpStatusOld());
						ipInfoHistoryDao.insert(history);
						ipLevel2Dao.update(ipLevel2);
						ipInfoDao.delete(ipInfo);
					}
				}
				
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}	
	
	public List<IpInfo> checkDupVlan(IpInfo ipInfo) throws IPFMBusinessException {
		List<IpInfo> ipInfoList;
		try {
			ipInfoList = ipInfoDao.findIpInfoByVlan(ipInfo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipInfoList;
	}	
	
	public boolean deleteAndInsertIpInfoTemp(List<IpInfoTemp> ipInfoTempList,String username) throws IPFMBusinessException {
		boolean result = false;
		try {
			ipInfoTempDao.deleteIpInfoTempByCreateBy(ipInfoTempList.get(0).getCreatedBy());
			
			IpInfoTemp ipInfoTemp;
			for(Iterator it=ipInfoTempList.iterator();it.hasNext();){
				ipInfoTemp = (IpInfoTemp)it.next();
				ipInfoTempDao.insert(ipInfoTemp);
			}
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public HashMap verifyIpInfoTemp(List<IpInfoTemp> ipInfoTempList,String username,String userRole) throws IPFMBusinessException {
		Map resultMap;
		int error = 0;
		int warn = 0;
		HashMap responseMap = new HashMap();	
		try {
			if(ipInfoTempList != null && ipInfoTempList.size() > 0){
				
				resultMap = callProcedureDao.verifyImportLv3(username,userRole);
				if(resultMap != null){
					error = (Integer)resultMap.get("error");
					warn = (Integer)resultMap.get("warn");
				}
				responseMap.put("error", error);
				responseMap.put("warn", warn);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return responseMap;
	}
	
	public List<IpLogVerify> findIpLogVerifyByUsername(String username) throws IPFMBusinessException{
		if(StringUtils.isNotEmpty(username)){
			return ipLogVerifyDao.findIpLogVerify(username);
		}
		return null;
	}
	
	public void saveImportIpLv3(String username)  throws IPFMBusinessException{
		try{
			if(StringUtils.isNotEmpty(username)){
				callProcedureDao.updateImportLv3(username);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkPermissionUpdateNatIP(String userId, String ip) throws IPFMBusinessException {
		boolean result = false;
		try{
			if(StringUtils.isNotEmpty(userId)){
				
				List<IPMasterValue> masterLV = ipfmCommondDao.getMasterValue("UPDATE_NAT");
				if(masterLV != null && masterLV.size() > 0){
					for(IPMasterValue lvBean : masterLV){
						if(lvBean != null && lvBean.getValueName() != null && !(lvBean.getValueName().equals(""))   && lvBean.getValueData() != null && !(lvBean.getValueData().equals(""))){
							if(lvBean.getValueName().equalsIgnoreCase("USER_LV1") && lvBean.getValueData().equalsIgnoreCase("Y")){
									List<String> data = ipInfoDao.getLv1TeamMemberByIP(ip);								
									if(data != null && data.size() > 0){
										for(String tmp:data){
											if(StringUtils.isNotEmpty(tmp) && tmp.equals(userId)){
												result = true;
												break;
											}
										}
									}
							}
							
							if(lvBean.getValueName().equalsIgnoreCase("USER_LV2") && lvBean.getValueData().equalsIgnoreCase("Y")){
								List<String> data = ipInfoDao.getLv2TeamMemberByIP(ip);								
								if(data != null && data.size() > 0){
									for(String tmp:data){
										if(StringUtils.isNotEmpty(tmp) && tmp.equals(userId)){
											result = true;
											break;
										}
									}
								}
							}
							
							if(lvBean.getValueName().equalsIgnoreCase("USER_LV3") && lvBean.getValueData().equalsIgnoreCase("Y")){
								IpUser ipUser = loginService.getIPUser(userId);
								IpInfo ipInfo =   ipInfoDao.findIpInfo(ip);						
								if(ipUser != null && ipInfo != null){
									if(ipUser.getTeamId() != null && ipUser.getUserId() != null){
										   if(ipInfo.getSystemOwnerId() != null && ipInfo.getSystemOwnerId().equals(ipUser.getUserId())){
									    	   result = true;
									       }
									       
									       if(ipInfo.getSystemOwnerTeamId() != null && ipInfo.getSystemOwnerTeamId().equals(ipUser.getTeamId())){
									    	   result = true;
									       }
									}
								}	
							}
							
							if(result){
								break;
							}
						}
						
					}

				}
												
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void insertLogVerify(String ip, String type, String msg,String username) throws IPFMBusinessException {
		try{
			if(StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(msg)){
				ipInfoTempDao.insertErrorLog(ip, type, msg, username);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void updateNatIP(String ip, String natIP) throws IPFMBusinessException {
		try{
			if(StringUtils.isNotEmpty(ip)){
				ipInfoDao.updateNatIP(ip, natIP);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public IPLevel3NatHistory validateInsertNatHis(String ip, String natIP,String username) throws IPFMBusinessException {
		IPLevel3NatHistory result = new IPLevel3NatHistory();
		try{
			IpInfo ipInfo =   ipInfoDao.findIpInfo(ip);	
			if(ipInfo != null && ipInfo.getIpAddress() != null){
				String ip1 = ipInfo.getNatIp()==null?"":ipInfo.getNatIp().trim();
				String ip2 = natIP==null?"":natIP.trim();

				if(ip1.equals(ip2)){
					return null;
				}else{
					result.setNatIP(ip2);
					result.setNatIPOld(ip1);
					result.setIpAddress(ip);
					result.setCreatedBy(username);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	@Override
	public void insertNatHistory(List<IPLevel3NatHistory> natHisList) throws IPFMBusinessException {
		
		try{
			if(natHisList != null && natHisList.size() > 0){
				ipInfoDao.insertNatHistory(natHisList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public List<IPMasterValue> getMasterList(String group) throws IPFMBusinessException {		
		List<IPMasterValue> result = new ArrayList<IPMasterValue>();
		try{
			if(group != null){
				result = ipfmCommondDao.getMasterValue("UPDATE_NAT");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
