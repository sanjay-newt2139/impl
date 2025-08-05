package th.co.ais.ipfm.service.impl;



import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IIPUserDao;
import th.co.ais.ipfm.dao.IPUrIPDetailDao;
import th.co.ais.ipfm.dao.IPUrStatusDao;
import th.co.ais.ipfm.dao.IPUserDao;
import th.co.ais.ipfm.domain1.IpUrIpDetail;
import th.co.ais.ipfm.domain1.IpUrStatus;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.SearchURService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.vo.SearchResult;


public class SearchURServiceImp implements SearchURService {

	private IPUrStatusDao  ipUrStatusDao;
	private IPUrIPDetailDao  ipUrIPDetailDao;
	private IIPUserDao ipUserDao;
	private IPUserDao iipUserDao;
	
	
	public IPUrStatusDao getIpUrStatusDao() {
		return ipUrStatusDao;
	}

	public void setIpUrStatusDao(IPUrStatusDao ipUrStatusDao) {
		this.ipUrStatusDao = ipUrStatusDao;
	}

	public IPUrIPDetailDao getIpUrIPDetailDao() {
		return ipUrIPDetailDao;
	}

	public void setIpUrIPDetailDao(IPUrIPDetailDao ipUrIPDetailDao) {
		this.ipUrIPDetailDao = ipUrIPDetailDao;
	}

	public IIPUserDao getIpUserDao() {
		return ipUserDao;
	}

	public void setIpUserDao(IIPUserDao ipUserDao) {
		this.ipUserDao = ipUserDao;
	}
	
	public IPUserDao getIipUserDao() {
		return iipUserDao;
	}

	public void setIipUserDao(IPUserDao iipUserDao) {
		this.iipUserDao = iipUserDao;
	}
	
	@Override
	public List<IpUrStatus> getSubUrStatusList(String urType) throws IPFMBusinessException {
		List<IpUrStatus> urStatusList = null;
		try {
			urStatusList = ipUrStatusDao.getSubUrStatusList(urType);
			  
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urStatusList;
	}

	@Override
	public List<IpUrStatus> getUrStatusList(String urType) throws IPFMBusinessException {
		List<IpUrStatus> urStatusList = null;
		try {
			urStatusList = ipUrStatusDao.getUrStatusList(urType);
			  
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urStatusList;
	}

	@Override
	public SearchResult searchUR(IpUrIpDetail ipUrIpDetail,IpUser ipUser) throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
		try {
			searchResult.setResultList(ipUrIPDetailDao.searchUR(ipUrIpDetail, ipUser));		
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return searchResult;
	}

	@Override
	public String getViewUR(String userId)throws IPFMBusinessException {
		    String viewUr = ipUserDao.getViewUR(userId);
		    
		return viewUr;
	}

	@Override
	public List<IpUser> getSystemOwnerList() throws IPFMBusinessException {
		List<IpUser> systemOwnerList = null;
		try {
			  systemOwnerList = iipUserDao.getSystemOwnerList();
			  
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return systemOwnerList;
	}
	


	@Override
	public SearchResult searchUR(IpUrIpDetail ipUrIpDetail, String subUrPack,
			IpUser ipUser) throws IPFMBusinessException {
		// TODO Auto-generated method stub
		SearchResult searchResult = new SearchResult();
		try {
			searchResult.setResultList(ipUrIPDetailDao.searchUR(ipUrIpDetail,subUrPack, ipUser));		
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return searchResult;

	}


}
