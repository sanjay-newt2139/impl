package th.co.ais.ipfm.service.impl;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IPDetailDao;
import th.co.ais.ipfm.domain.IPDetail;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.ManageDataIPService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;
import th.co.ais.ipfm.vo.IPDetailVO;
import th.co.ais.ipfm.vo.SearchResult;


public  class ManageDataIPServiceImpl implements ManageDataIPService{

	private IPDetailDao ipDetailDao;
	

	public void setIpDetailDao(IPDetailDao ipDetailDao) {
		this.ipDetailDao = ipDetailDao;
	}

	@Override
	public SearchResult search(IPDetailVO DTO, int maxResult)throws IPFMBusinessException {
		SearchResult searchResult = new SearchResult();
			try {
				searchResult.setTotalResult(ipDetailDao.countSearchIPDetail(DTO));
				searchResult.setResultList(ipDetailDao.search(DTO,maxResult));
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
	public IPDetail searchIPDetail(IPDetailVO DTO) throws IPFMBusinessException {
		 IPDetail ipDetail = null;
			try {
				ipDetail = ipDetailDao.searchIPDetail(DTO);
			} catch (DataAccessException e) {
				throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
			}
			return ipDetail;
	}

	@Override
	public IPDetail updateIpDetail(IPDetail ipDetail)
			throws IPFMBusinessException {
		try {
			if(ipDetail!=null){
				ipDetailDao.update(ipDetail);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0010")));
		}
		return null;
	}
	

	@Override
	public IPDetail getIPDetail(String rowId)
			throws IPFMBusinessException {
		IPDetail ipDetail = null;
		try {
			if(rowId!=null){
				ipDetail = ipDetailDao.getByPrimaryKey(rowId);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipDetail;
	}



}
