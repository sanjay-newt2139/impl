package th.co.ais.ipfm.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.co.ais.ipfm.dao.IPLevel2Dao;
import th.co.ais.ipfm.domain1.ExcelErrorMsg;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.domain1.IpUrIpDetail;
import th.co.ais.ipfm.domain1.IpUrIpResult;
import th.co.ais.ipfm.domain1.IpUrIpResultId;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.ExcelService;
import th.co.ais.ipfm.service.IPUrIPResultService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.util.IPFMDataValidateUtil;
import th.co.ais.ipfm.util.IPFMExcelUtils;
import th.co.ais.ipfm.util.IPFMUtils;



public class ExcelServiceImpl implements ExcelService {
	
	private IPUrIPResultService ipUrIPResultService;
	private IPLevel2Dao ipLevel2Dao;

	public void setIpUrIPResultService(IPUrIPResultService ipUrIPResultService) {
		this.ipUrIPResultService = ipUrIPResultService;
	}
	public IPLevel2Dao getIpLevel2Dao() {
		return ipLevel2Dao;
	}
	public void setIpLevel2Dao(IPLevel2Dao ipLevel2Dao) {
		this.ipLevel2Dao = ipLevel2Dao;
	}
	@SuppressWarnings("unchecked")
	public Map userRequestIPImport(File file, String sheetName,IpUrIpDetail ipUrIpDetail, IpUrIpResult ipResult, String userTeamId, String userId) throws IPFMBusinessException {
		//Map key
		//1. result
		//2. errorMsg
		Map mapResult = new HashMap();
		List<IpUrIpResult> resultlist = new ArrayList<IpUrIpResult>();
		List<ExcelErrorMsg> errorlist = new ArrayList<ExcelErrorMsg>();
		List<IpInfo> ipInfolist;
		try {			
			List<Map> dataList = new ArrayList<Map>();
			dataList = new IPFMExcelUtils().getData(file,sheetName);
			IpUrIpResult ipUrIpResult;
			ExcelErrorMsg excelMsg;
			for(int rows=0 ; rows<dataList.size() ; rows++){
				Map map = dataList.get(rows);
				ipInfolist = new ArrayList<IpInfo>();
				excelMsg = new ExcelErrorMsg();
				excelMsg.setRowNo(String.valueOf(rows+3));			
				ipUrIpResult = (IpUrIpResult)ipResult.clone();
				IpUrIpResultId id = new IpUrIpResultId();
				id.setUrNo(ipUrIpDetail.getUrNo());
				ipUrIpResult.setId(id);
				String ipVersion = (String)map.get(0);
				String ipAddress = (String)map.get(1);
				String hostName = (String)map.get(2);
				String effectiveDate = (String)map.get(3);
				String expireDate = (String)map.get(4);
				//Set VlanId
				String vlanId = (String)map.get(9);
				ipUrIpResult.setVlanId(vlanId);
				//---------
				prepareIPVersion(ipUrIpResult, ipVersion.trim(), excelMsg);
				prepareIPAddress(ipUrIpResult, ipAddress.trim(), excelMsg);
				prepareEffExpDate(ipUrIpResult, effectiveDate.trim(), expireDate.trim(), excelMsg);
				ipUrIpResult.setVt2TeamId(ipUrIpDetail.getT2TeamId());
				ipUrIpResult.setCreatedBy(userId);
				ipUrIpResult.setLastUpdBy(userId);
				//ipUrIpResult.setSystemName((String)map.get(17));
				
				if (excelMsg.getErrorMsg()!=null && excelMsg.getErrorMsg().trim().length()>0) {
					errorlist.add(excelMsg);
					continue;
				}
				
				//No Input Error
				checkIPRange23_2(ipUrIpResult);
				if (!IPFMUtils.ifBlank(ipUrIpResult.getResult(),"").trim().equalsIgnoreCase(IPFMConstant.RESULT_SUCCESS)) {
					addErrorMsg(excelMsg, ipUrIpResult.getResult());
					errorlist.add(excelMsg);
				}else if (IPFMUtils.ifBlank(ipUrIpResult.getResult(),"").trim().equalsIgnoreCase(IPFMConstant.RESULT_SUCCESS)) {
					//Add IpInfo
					String[] strIpStart = ipUrIpResult.getLevel2Start().split("\\.");
		        	if (strIpStart.length==4) {
			        	int ipDigit1 = Integer.parseInt(strIpStart[0]);
			        	int ipDigit2 = Integer.parseInt(strIpStart[1]);
			        	int ipDigit3 = Integer.parseInt(strIpStart[2]);
			        	int ipDigit4 = Integer.parseInt(strIpStart[3]);
						for (int i=0;i<ipUrIpResult.getTotalIp();i++) {
							if (ipDigit4>255) { ipDigit4=0; ipDigit3++; }
							if (ipDigit3>255) { ipDigit3=0; ipDigit2++; }
							if (ipDigit2>255) { ipDigit2=0; ipDigit1++; }
							if (ipDigit1>255) break;
							String ipInfoipAddress = String.valueOf(ipDigit1)+"."+String.valueOf(ipDigit2)+"."+String.valueOf(ipDigit3)+"."+String.valueOf(ipDigit4);
							IpInfo ipInfo = newIpInfo(ipUrIpDetail,ipUrIpResult,ipInfoipAddress,hostName,userTeamId,userId);
							ipInfolist.add(ipInfo);
							ipDigit4++;
						}
		        	}
					ipUrIpResult.setIpInfoList(ipInfolist);
					if (ipUrIpResult.getIpInfoList().size()==1) ipUrIpResult.setRowShowIpInfo(true);
					resultlist.add(ipUrIpResult);
				}
			}
			System.out.println("resultlist = "+resultlist.size());
			System.out.println("errorlist = "+errorlist.size());
			mapResult.put("result", resultlist);
			mapResult.put("errorMsg", errorlist);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return mapResult;
	}

	private IpInfo newIpInfo(IpUrIpDetail ipUrIpDetail, IpUrIpResult result, String ipAddress, String hostName, String userTeamId, String userId) throws Exception {
		IpInfo bean = new IpInfo();
		bean.setIpVersion(result.getIpVersion());
		bean.setIpAddress(ipAddress);
		String[] ip = ipAddress.split("\\.");
		if (bean.getIpVersion()!=null && bean.getIpVersion().equalsIgnoreCase("4")) {
			bean.setIpv4digit1(new BigDecimal(ip[0])); bean.setIpv4digit2(new BigDecimal(ip[1]));
			bean.setIpv4digit3(new BigDecimal(ip[2])); bean.setIpv4digit4(new BigDecimal(ip[3]));
			String ipDeigit1Binary=""; String ipDeigit2Binary="";
			String ipDeigit3Binary=""; String ipDeigit4Binary="";
			ipDeigit1Binary = IPFMDataUtility.convertStringToBinary(ip[0]);
			ipDeigit2Binary = IPFMDataUtility.convertStringToBinary(ip[1]);
			ipDeigit3Binary = IPFMDataUtility.convertStringToBinary(ip[2]);
			ipDeigit4Binary = IPFMDataUtility.convertStringToBinary(ip[3]);
			bean.setBinaryIp(ipDeigit1Binary+ipDeigit2Binary+ipDeigit3Binary+ipDeigit4Binary);
			
		}else if (bean.getIpVersion()!=null && bean.getIpVersion().equalsIgnoreCase("6")) {
			bean.setIpv6digit1(new BigDecimal(ip[0])); bean.setIpv6digit2(new BigDecimal(ip[1]));
			bean.setIpv6digit3(new BigDecimal(ip[2])); bean.setIpv6digit4(new BigDecimal(ip[3]));
			bean.setIpv6digit5(new BigDecimal(ip[4])); bean.setIpv6digit6(new BigDecimal(ip[5]));
			String ipDeigit1Binary=""; String ipDeigit2Binary="";
			String ipDeigit3Binary=""; String ipDeigit4Binary="";
			String ipDeigit5Binary=""; String ipDeigit6Binary="";
			ipDeigit1Binary = IPFMDataUtility.convertStringToBinary(ip[0]);
			ipDeigit2Binary = IPFMDataUtility.convertStringToBinary(ip[1]);
			ipDeigit3Binary = IPFMDataUtility.convertStringToBinary(ip[2]);
			ipDeigit4Binary = IPFMDataUtility.convertStringToBinary(ip[3]);
			ipDeigit5Binary = IPFMDataUtility.convertStringToBinary(ip[4]);
			ipDeigit6Binary = IPFMDataUtility.convertStringToBinary(ip[5]);
			bean.setBinaryIp(ipDeigit1Binary+ipDeigit2Binary+ipDeigit3Binary+ipDeigit4Binary+ipDeigit5Binary+ipDeigit6Binary);
		}
		bean.setEffectiveDate(result.getEffectiveDate());
		bean.setExpiredDate(result.getExpireDate());
		bean.setSubmask(String.valueOf(result.getVmask()));
		bean.setHostName(hostName);
		
		return bean;
	}
	public void prepareIPVersion(IpUrIpResult ipUrIpResult, String ipVersion, ExcelErrorMsg excelMsg) {
		if (IPFMDataValidateUtil.validateIPVersion(ipVersion)){
			ipUrIpResult.setIpVersion(ipVersion);
		} else {
			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0038"), "IP Version", "4"));
		}
	}
	public void prepareIPAddress(IpUrIpResult ipUrIpResult, String ipAddress, ExcelErrorMsg excelMsg) {
		int maskIndex = ipAddress.indexOf("/");
		if (maskIndex>(-1)) {
			String[] ip = ipAddress.split("\\/");					
			if (IPFMDataValidateUtil.validateIPFormat(ip[0])) {
				String[] ipDigit = ip[0].split("\\.");
				ipUrIpResult.setIpDigit1(ipDigit[0]);
				ipUrIpResult.setIpDigit2(ipDigit[1]);
				ipUrIpResult.setIpDigit3(ipDigit[2]);
				ipUrIpResult.setIpDigit4(ipDigit[3]);
				if(IPFMDataValidateUtil.validateMaskFormat(ip[1])) {
					ipUrIpResult.setMask(ip[1]);
				}else{
					addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0059")));
				}
			}else{
				addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0031"), "IP Address"));
			}
		}else {
			if (IPFMDataValidateUtil.validateIPFormat(ipAddress)) {
				String[] ipDigit = ipAddress.split("\\.");
				ipUrIpResult.setIpDigit1(ipDigit[0]);
				ipUrIpResult.setIpDigit2(ipDigit[1]);
				ipUrIpResult.setIpDigit3(ipDigit[2]);
				ipUrIpResult.setIpDigit4(ipDigit[3]);
			}else{
				addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0038"), "IP Address", "172.0.0.1"));
			}
		}
	}
	public void prepareEffExpDate(IpUrIpResult ipUrIpResult, String effectiveDate, String expireDate, ExcelErrorMsg excelMsg) {
		boolean dateFormatFlag = true;
		if (effectiveDate!=null && effectiveDate.trim().length()==10) {
			try {
				int ind1 = effectiveDate.indexOf('/');
				int ind2 = effectiveDate.lastIndexOf('/');
				if ((ind1 == 2) && (ind2 == 5))
					ipUrIpResult.setEffectiveDate(IPFMUtils.cnvStringToDate(effectiveDate));
				else throw new Exception();
			}catch(Exception e){
				dateFormatFlag = false;
				addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0038"), "Effective Date", IPFMUtils.currentDate("dd/MM/yyyy")));
			}
		}else if (effectiveDate==null || effectiveDate.trim().length()==0){
			dateFormatFlag = false;
			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "Effective Date"));
		}else{
			dateFormatFlag = false;
			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0038"), "Effective Date", IPFMUtils.currentDate("dd/MM/yyyy")));
		}
		if (expireDate!=null && expireDate.trim().length()==10) {
			try {
				int ind1 = expireDate.indexOf('/');
				int ind2 = expireDate.lastIndexOf('/');
				if ((ind1 == 2) && (ind2 == 5))
					ipUrIpResult.setExpireDate(IPFMUtils.cnvStringToDate(expireDate));
				else throw new Exception();
			}catch(Exception e){
				dateFormatFlag = false;
				addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0038"), "Expire Date", IPFMUtils.currentDate("dd/MM/yyyy")));
			}
		}else if (expireDate==null || expireDate.trim().length()==0){
			dateFormatFlag = false;
			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "Expire Date"));
		}else{
			dateFormatFlag = false;
			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0038"), "Expire Date", IPFMUtils.currentDate("dd/MM/yyyy")));
		}
		if (dateFormatFlag) {
			if (IPFMUtils.ifBlank(effectiveDate, "").trim().length()>0 && IPFMUtils.ifBlank(expireDate, "").trim().length()>0) {
				if (ipUrIpResult.getEffectiveDate().compareTo(ipUrIpResult.getExpireDate())>0) {
					addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0023")));
				}
			}
		}
	}
	public void validateAssignInfo(IpUrIpResult ipUrIpResult, ExcelErrorMsg excelMsg) {
//		if (IPFMUtils.ifBlank(ipUrIpResult.getIpVersion(), "").trim().length()==0) {
//			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "IP Version"));
//		}
//		if (IPFMUtils.ifBlank(ipUrIpResult.getIpDigit1(), "").trim().length()==0 
//			&& IPFMUtils.ifBlank(ipUrIpResult.getIpDigit2(), "").trim().length()==0 
//			&& IPFMUtils.ifBlank(ipUrIpResult.getIpDigit3(), "").trim().length()==0
//			&& IPFMUtils.ifBlank(ipUrIpResult.getIpDigit4(), "").trim().length()==0 ) {
//			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "IP Address"));
//		}
//		if (ipUrIpResult.getEffectiveDate()==null) {
//			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "Effective Date"));
//		}
//		if (ipUrIpResult.getExpireDate()==null) {
//			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "Expire Date"));
//		}
		if (IPFMUtils.ifBlank(ipUrIpResult.getCompanyId(), "").trim().length()==0) {
			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "Company"));
		}
		if (IPFMUtils.ifBlank(ipUrIpResult.getIpTypeId(), "").trim().length()==0) {
			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "IP Type"));
		}
//		if (IPFMUtils.ifBlank(ipUrIpResult.getVip(), "").trim().length()==0) {
//			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "VIP"));
//		}
//		if (IPFMUtils.ifBlank(ipUrIpResult.getInstallTypeId(), "").trim().length()==0) {
//			addErrorMsg(excelMsg, IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0001"), "Install Type"));
//		}
	}
	public void checkIPRange(IpUrIpResult ipUrIpResult) throws IPFMBusinessException{
		ipUrIPResultService.checkIPRange(ipUrIpResult);
	}
	public void checkIPRange23_2(IpUrIpResult ipUrIpResult) throws IPFMBusinessException{
		ipUrIPResultService.checkIPRange23_2(ipUrIpResult);
	}
	
	public void addErrorMsg(ExcelErrorMsg excelMsg, String errorMsg){
		
		if (excelMsg.getErrorMsg()==null || excelMsg.getErrorMsg().trim().length()==0) {
			excelMsg.setErrorMsg(errorMsg);
		}else{
			excelMsg.setErrorMsg(excelMsg.getErrorMsg() + "<br>" + errorMsg);
		}
	}

	@SuppressWarnings("unchecked")
	public IpUrIpResult getIpUrIpResultData(IpUrIpDetail ipUrIpDetail,String userName, Map map) throws Exception{
		IpUrIpResult ipUrIpResult = new IpUrIpResult();
		IpUrIpResultId id = new IpUrIpResultId();
		id.setUrNo(ipUrIpDetail.getUrNo());
		ipUrIpResult.setId(id);
		ipUrIpResult.setIpVersion((String)map.get(0));
		ipUrIpResult.setIpAddress((String)map.get(1));
		ipUrIpResult.setEffectiveDate(IPFMUtils.cnvStringToDate((String)map.get(3)));
		ipUrIpResult.setExpireDate(IPFMUtils.cnvStringToDate((String)map.get(4)));
		ipUrIpResult.setCreatedBy(userName);
		ipUrIpResult.setLastUpdBy(userName);
		return ipUrIpResult;
	}
}
