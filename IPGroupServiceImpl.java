package th.co.ais.ipfm.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPGroupDao;
import th.co.ais.ipfm.domain.IPGroup;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IIPGroupService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class IPGroupServiceImpl implements IIPGroupService {
	
	private IIPGroupDao ipGroupDao;
	private CommonDao commonDao;
	
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setIpGroupDao(IIPGroupDao ipGroupDao) {
		this.ipGroupDao = ipGroupDao;
	}

	@Override
	public List<String> getGroupIPVersion() throws IPFMBusinessException {
		List<String> groupIPVersion = null;
		try {
			groupIPVersion = ipGroupDao.getGroupIPVersion();
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return groupIPVersion;
	}


	@Override
	public List<IPGroup> searchIPGroup(String ipVersion, String tire1TeamID,String[] strartIP,String[] endIP)
			throws Exception {
		List<IPGroup> ipGroupList = null;
		try {
			ipGroupList = ipGroupDao.searchIPGroup(ipVersion,tire1TeamID, strartIP,endIP);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipGroupList;
	}
	/*public SearchResult searchIPGroup(String ipVersion,String tire1TeamID,String startIP,String endIP, int maxResult)
			throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
		try {
			searchResult.setTotalResult(ipGroupDao.countSearchIPGroup(ipVersion,tire1TeamID, startIP,endIP));
			searchResult.setResultList(ipGroupDao.searchIPGroup(ipVersion,tire1TeamID, startIP,endIP));
			if(searchResult.getTotalResult()>maxResult){
				searchResult.setOverMaxResultLimit(true);
			}else{
				searchResult.setOverMaxResultLimit(false);
			}
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return searchResult;
	}*/

	@Override
	public IPGroup addIPGroup(IPGroup obj) throws  Exception {
		IPGroup result = new IPGroup();
		try {
			if(obj != null){
				
				/*if(ipGroupDao.checkIPGroup(obj)){
					return null;
				}*/
								
				obj.setGrpID(ipGroupDao.getMaxGrpId());
				ipGroupDao.insert(obj);
				
				result = ipGroupDao.searchIPGroupById(null,obj.getGrpID());
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}

	@Override
	public IPGroup editIPGroup(IPGroup obj)
			throws Exception {
		IPGroup result = null;
		try {
			if(obj != null){
				result = ipGroupDao.searchIPGroupById(obj.getRowID(),null);
				result.setGrpDesc(obj.getGrpDesc());
				ipGroupDao.update(result);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	@Override
	public IPGroup deleteIPGroup(IPGroup obj)
			throws IPFMBusinessException {
		try {
			if(obj != null){
				ipGroupDao.delete(obj);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return obj;
	}
	@SuppressWarnings("unchecked")
	@Override
	public HashMap initGroupIPSearch(String tier1TeamID) throws IPFMBusinessException {
		HashMap result = new HashMap();
		try {
			result.put(IPFMConstant.LIST_IPCLASS, commonDao.getMasterList(IPFMConstant.MCODE_IPCLASS));
			result.put(IPFMConstant.LIST_COMPANY, commonDao.getMasterList(IPFMConstant.MCODE_COMPANY));
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}
		return result; 
	}

}
