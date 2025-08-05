package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPGroupDao;
import th.co.ais.ipfm.dao.IIPRangeDao;
import th.co.ais.ipfm.domain.IPRange;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.RangeIPService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.vo.SearchResult;


public  class RangeIPServiceImpl implements RangeIPService{
	
	private CommonDao commonDao;
	private IIPRangeDao rangeIPDao;
	private IIPGroupDao ipGroupDao;
	
	public void setRangeIPDao(IIPRangeDao rangeIPDao) {
		this.rangeIPDao = rangeIPDao;
	}

	public void setIpGroupDao(IIPGroupDao ipGroupDao) {
		this.ipGroupDao = ipGroupDao;
	}
	
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public SearchResult search(IPRange ipRange,int maxResult)throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
			try {
				//searchResult.setTotalResult(rangeIPDao.countSearch(ipRange));
				searchResult.setResultList(rangeIPDao.search(ipRange,maxResult));
			} catch (DataAccessException e) {
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
			}
			return searchResult;
	}
	
	public List<IPRange> search(IPRange ipRange)throws IPFMBusinessException {
		List<IPRange> ipRangeList = null;
			try {
				ipRangeList = rangeIPDao.search(ipRange);
			} catch (DataAccessException e) {
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
			}
			return ipRangeList;
	}
	
	@Override
	public void deleteIPRang(IPRange ipRange)
			throws IPFMBusinessException {
		try {
			if(ipRange!=null){
				rangeIPDao.delete(ipRange);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}
	
	@Override
	public List<IPRange> deleteAndReSearchIPRange(String rowId, IPRange ipRange)throws IPFMBusinessException {
		List<IPRange> ipRangeList = null;
		IPRange ip = rangeIPDao.getByPrimaryKey(rowId);
		if(ipRange!=null){
			
			deleteIPRang(ip);
			
			ipRangeList = search(ipRange);
		}		
		return ipRangeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap initRangeIPSearch(String tier1TeamID) throws IPFMBusinessException {
		HashMap result = new HashMap();
		try {
			result.put(IPFMConstant.LIST_IPVERSION  , rangeIPDao.getIPVersionListByTier1TeamID(tier1TeamID));
			result.put(IPFMConstant.LIST_IPGROUP, ipGroupDao.getGroupIPList());
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result; 
	}
	
	public List<Map> checkIPRange(String ipVersion, String mask, String effectiveDate, String expireDate) throws IPFMBusinessException{
		//Map key
		//1. result
		//2. errorMsg
		List<Map> resultList = new ArrayList<Map>();
		try {
			resultList = rangeIPDao.checkIPRange(ipVersion, mask, effectiveDate, expireDate);
			
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return resultList;
	}

	public List<Map> checkIPRange23_2(){
		//Map key
		//1. result
		//2. errorMsg
		return null;
	}
	
	public List<Map> checkIPRangeEdit(){
		//Map key
		//1. result
		//2. errorMsg
		return null;
	}
}
