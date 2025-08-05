package th.co.ais.ipfm.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPParameterDao;
import th.co.ais.ipfm.domain.Suggestion;
import th.co.ais.ipfm.domain1.IpMasterTable;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.CommonService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.vo.SearchResult;



public  class CommonServiceImpl implements CommonService{

	private CommonDao commonDao;
	private IIPParameterDao ipParameterDao;
	
	
	
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setIpParameterDao(IIPParameterDao ipParameterDao) {
		this.ipParameterDao = ipParameterDao;
	}

	@Override
	public List<IpMasterTable> getMasterList(String ABBr) throws IPFMBusinessException {
		List<IpMasterTable> ipStatusList = null;
		try {
			ipStatusList = commonDao.getMasterList(ABBr);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipStatusList;
	}

	
	@Override
	public List<IpMasterTable> getMasterRefKeyList(String refTable)throws IPFMBusinessException {

		List<IpMasterTable> masterRefKeyList = null;
		try {
			masterRefKeyList = commonDao.getMasterRefKeyList(refTable);
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return masterRefKeyList;
	}


	@Override
	public HashMap<String, Object> getAllMasterList() throws IPFMBusinessException {
		HashMap<String, Object> result = new HashMap<String, Object>();
		try { 
			result.put(IPFMConstant.MCODE_LOCAT, commonDao.getMasterList(IPFMConstant.MCODE_LOCAT));
			result.put(IPFMConstant.MCODE_NWTYPE, commonDao.getMasterList(IPFMConstant.MCODE_NWTYPE));
			result.put(IPFMConstant.MCODE_TIER2TEAM, commonDao.getMasterList(IPFMConstant.MCODE_TIER2TEAM));
			result.put(IPFMConstant.MCODE_ZONE, commonDao.getMasterList(IPFMConstant.MCODE_ZONE));
			//result.put(IPFMConstant.DEFAULT_IP_VERSION, ipParameterDao.getIPVersion().getNumericValue());
			result.put(IPFMConstant.MCODE_IPSTS, commonDao.getMasterList(IPFMConstant.MCODE_IPSTS));
			result.put(IPFMConstant.MCODE_COMPANY, commonDao.getMasterList(IPFMConstant.MCODE_COMPANY));
			result.put(IPFMConstant.MCODE_PRIORITY, commonDao.getMasterList(IPFMConstant.MCODE_PRIORITY));
			result.put(IPFMConstant.MCODE_INSTALLTYPE, commonDao.getMasterList(IPFMConstant.MCODE_INSTALLTYPE));
			result.put(IPFMConstant.MCODE_IPTYPELIST, commonDao.getMasterList(IPFMConstant.MCODE_IPTYPELIST));
			result.put(IPFMConstant.MCODE_IPCLASS, commonDao.getMasterList(IPFMConstant.MCODE_IPCLASS));
			result.put(IPFMConstant.MCODE_DATA_CEN, commonDao.getMasterList(IPFMConstant.MCODE_DATA_CEN));
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}



	@Override
	public List<IpMasterTable> searchMasterTable(String refTable, String refKey) throws IPFMBusinessException {
		List<IpMasterTable> masterTBLList = null;
		try {
			masterTBLList = commonDao.searchMasterTable(refTable, refKey);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return masterTBLList;
	}
	
	@Override
	public SearchResult searchMasterTable(String refTable, String refKey, int maxResult)
			throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
		try {
			searchResult.setTotalResult(commonDao.countSearchMasterTable(refTable, refKey));
			searchResult.setResultList(commonDao.searchMasterTable(refTable, refKey, maxResult));
			if(searchResult.getTotalResult()>maxResult){
				searchResult.setOverMaxResultLimit(true);
			}else{
				searchResult.setOverMaxResultLimit(false);
			}
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return searchResult;
	}

	@Override
	public IpMasterTable createMasterTBL(IpMasterTable masterTBL)throws IPFMBusinessException {
		try {
			if(masterTBL!=null){
				List<IpMasterTable> list =commonDao.searchMasterTableForADD(masterTBL.getId().getRefTable(), masterTBL.getId().getRefKey().trim(),"");				
				if(list!=null && list.size()>0){
					IpMasterTable master = list.get(0);
					if (master.getActiveStatus().equalsIgnoreCase("1")){
						throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0077")));
					}
					master.setActiveStatus("1");
					master.setHolidayDate(masterTBL.getHolidayDate());
					master.setId(masterTBL.getId());
					master.setOrderData(masterTBL.getOrderData());
					master.setOther(masterTBL.getOther());
					master.setRefType(masterTBL.getRefType());
					master.setShortDesc(masterTBL.getShortDesc());
					master.setTcp(masterTBL.getTcp());
					master.setUdp(masterTBL.getUdp());
					master.setRefDesc(masterTBL.getRefDesc());
					master.setLastUpd(masterTBL.getLastUpd());
					master.setLastUpdBy(masterTBL.getLastUpdBy());
					commonDao.update(master);
				}else{
					masterTBL.setRowId(commonDao.getROW_ID()); // May be insert from SYS_GUID() on db
					commonDao.insert(masterTBL);	
				}				
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0008")));
		}
		return masterTBL;
	}

	@Override
	public IpMasterTable getMasterTBL(String rowId)
			throws IPFMBusinessException {
		IpMasterTable masterTBL = null;
		try {
			if(rowId!=null){
//				masterTBL = commonDao.getByPrimaryKey(rowId);
				masterTBL = commonDao.getMasterById(rowId);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return masterTBL;
	}


	@Override
	public IpMasterTable updateMasterTBL(IpMasterTable masterTBL)
			throws IPFMBusinessException {
		try {

			if(masterTBL!=null){				
				if(masterTBL.getId().getRefTable().equals("NEWS")){
					commonDao.updateNews(masterTBL);
				}else{		
//					List list =commonDao.searchMasterTable(masterTBL.getId().getRefTable(), masterTBL.getId().getRefKey().trim());				
//					if(list.size()>0){
//							throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0013")));
//					}else{
						IpMasterTable obj = commonDao.getMasterById(masterTBL.getRowId());
						obj.setRefDesc(masterTBL.getRefDesc());
						obj.setShortDesc(masterTBL.getShortDesc());
						obj.setHolidayDate(masterTBL.getHolidayDate());
						obj.setTcp(masterTBL.getTcp());
						obj.setUdp(masterTBL.getUdp());
						obj.setOther(masterTBL.getOther());
						obj.setLastUpdBy(masterTBL.getLastUpdBy());
						obj.setLastUpd(new Timestamp(new Date().getTime()));
						obj.setOrderData(1);
						if(obj!=null){
							commonDao.update(obj);
//						}
					}						
				}			
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0010")));
		}
		return masterTBL;
	}
	
	@Override
	public void deleteMasterTable(IpMasterTable masterTBL)throws IPFMBusinessException {
		try {
			if(masterTBL!=null){
				commonDao.delete(masterTBL);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}

	@Override
	public List<IpMasterTable> deleteAndReSearchMasterTable(String rowId, String refTable, String refKey)
			throws IPFMBusinessException {
		List<IpMasterTable> masterTBLList = null;
		IpMasterTable masterTBL = commonDao.getMasterById(rowId);
		if(masterTBL!=null){
//			deleteMasterTable(masterTBL);
			commonDao.deleteByRowId(rowId);
			masterTBLList = searchMasterTable(refTable, refKey);
		}		
		return masterTBLList;
	}


	@Override
	public List<Suggestion> findSuggestion(String refID, String key) {
		List<IpMasterTable> listMaster = commonDao.searchMasterTable(refID, key);
		List<Suggestion> returnResult = new ArrayList<Suggestion>();
		for(IpMasterTable master : listMaster){
			returnResult.add(new Suggestion(master.getId().getRefKey(), master.getRefDesc()));
		}
		return returnResult;
	}

	@Override
	public List<String> getMasterRefTableList() throws IPFMBusinessException {
		List<String> masterRefTableList = null;
		try {
			masterRefTableList = commonDao.getMasterRefTableList();
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return masterRefTableList;
	}

	@Override
	public List<IpMasterTable> findMasterTableByRefKey(String ref) {
		return commonDao.getMasterRefKeyList(ref);
//		return commonDao.getMasterTableList(ref);
	}


	@Override
	public List<IpMasterTable> deleteAndListMasterTable(String rowId, String refTable, String refKey,String userId) throws IPFMBusinessException {
		List<IpMasterTable> masterTBLList = null;
		IpMasterTable masterTBL = commonDao.getMasterById(rowId);
		if(masterTBL!=null){
			try {
	            //deleteMasterTable(masterTBL);
				if("HOLIDAY".equals(refTable)){
				    commonDao.deleteByRowId(rowId);
				}else{
				    commonDao.updateFlag(masterTBL, refTable, refKey, userId);
				}
				masterTBLList = searchMasterTable(refTable, refKey);
			} catch (DataAccessException e) {
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
			}
		}		
		return masterTBLList;
	}

	public String callBatchGenReport()throws IPFMBusinessException{
		
		//gen_report_iprur001
		return commonDao.genReport();
	}
	
	public String callBatchUpdateUROverSlaOla()throws IPFMBusinessException{
		//update_ur_over_ola_sla
		return commonDao.updateUROverSlaOla();
	}
	
}
