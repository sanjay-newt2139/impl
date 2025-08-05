package th.co.ais.ipfm.service.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IIPParameterDao;
import th.co.ais.ipfm.domain.IPParameter;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IIPParameterService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.vo.SearchResult;

public class IPParameterServiceImpl implements IIPParameterService{

	private IIPParameterDao ipParameterDao;
	
	public void setIpParameterDao(IIPParameterDao ipParameterDao) {
		this.ipParameterDao = ipParameterDao;
	}


	@Override
	public IPParameter createIPParameter(IPParameter ipParameter)
			throws IPFMBusinessException {
		try {
			if(ipParameter!=null){
				List list = ipParameterDao.searchIPParameter(ipParameter.getParameterGroup(), ipParameter.getParameterSubGroup());
				if(list!=null && list.size()>0){
					throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0013")));
				}else{
					ipParameterDao.insert(ipParameter);	
				}				
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0008")));
		}
		return ipParameter;
	}


	@Override
	public void deleteIPParameter(IPParameter ipParameter)
			throws IPFMBusinessException {
		try {
			if(ipParameter!=null){
				ipParameterDao.delete(ipParameter);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}


	@Override
	public IPParameter getIPParameter(String parameterId)
			throws IPFMBusinessException {
		IPParameter ipParameter = null;
		try {
			if(parameterId!=null){
				ipParameter = ipParameterDao.getByPrimaryKey(parameterId);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipParameter;
	}


	@Override
	public List<String> getParameterGroupList() throws IPFMBusinessException {
		List<String> parameterGroupList = null;
		try {
			parameterGroupList = ipParameterDao.getParameterGroupList();
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return parameterGroupList;
	}


	@Override
	public List<String> getParameterSubGroupList(String parameterGroup)
			throws IPFMBusinessException {
		List<String> parameterSubGroupList = null;
		try {
			parameterSubGroupList = ipParameterDao.getParameterSubGroupList(parameterGroup);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return parameterSubGroupList;
	}


	@Override
	public List<IPParameter> searchIPParameter(String parameterGroup,
			String parameterSubGroup) throws IPFMBusinessException {
		List<IPParameter> ipParameterList = null;
		try {
			ipParameterList = ipParameterDao.searchIPParameter(parameterGroup, parameterSubGroup);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipParameterList;
	}


	@Override
	public IPParameter updateIPParameter(IPParameter ipParameter)
			throws IPFMBusinessException {
		try {
			if(ipParameter!=null){
				ipParameterDao.update(ipParameter);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0010")));
		}
		return ipParameter;
	}


	@Override
	public List<IPParameter> deleteAndReSearchIPParameter(String rowId,
			String parameterGroup, String parameterSubGroup)
			throws IPFMBusinessException {
		List<IPParameter> ipParameterList = null;
		IPParameter ipParameter = ipParameterDao.getByPrimaryKey(rowId);
		if(ipParameter!=null){
			deleteIPParameter(ipParameter);
			ipParameterList = searchIPParameter(parameterGroup, parameterSubGroup);
		}		
		return ipParameterList;
	}


	@Override
	public SearchResult searchIPParameter(String parameterGroup,
			String parameterSubGroup, int maxResult)
			throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
		try {
			searchResult.setTotalResult(ipParameterDao.countSearchIPParameter(parameterGroup, parameterSubGroup));
			searchResult.setResultList(ipParameterDao.searchIPParameter(parameterGroup, parameterSubGroup, maxResult));
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
	public IPParameter getIPVersion() throws IPFMBusinessException {
		IPParameter ipParameter = null;
		try {
			ipParameter = ipParameterDao.getIPVersion();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return ipParameter;
	}


	@Override
	public String getDownloadDocumentURL() {
		// TODO Auto-generated method stub
		return ipParameterDao.getDownloadDocumentURL();
	}




}
