package th.co.ais.ipfm.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IIPUrIPDao;
import th.co.ais.ipfm.domain.IPUrIP;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IIPUrIPService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class IPUrIPServiceImpl implements IIPUrIPService {
	private IIPUrIPDao ipUrIPDao;
	private CommonDao commonDao;
	
	DateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",new Locale("en","EN"));
	
	@Override
	public String addIPUrIP(IPUrIP obj) throws IPFMBusinessException,Exception {
		try {
			if(obj != null){
								
				obj.setUrNo(ipUrIPDao.getMaxId());
				obj.setReqDate( dfm.parse(ipUrIPDao.getSysdate()));
				ipUrIPDao.insert(obj);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return obj.getUrNo();
	}
	
	@Override
	public IPUrIP editIPUrIP(IPUrIP obj) throws IPFMBusinessException,Exception {
		//IPUrIP result = new IPUrIP();
		try {
			if(obj != null){					
				//result = ipUrIPDao.searchIPUrIPByID(obj.getUrNo());
				ipUrIPDao.update(obj);
				
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return obj;
	}
	
	@Override
	public IPUrIP findIPUrIPByUrNo(String urNo) throws IPFMBusinessException,Exception {
		IPUrIP result = new IPUrIP();
		try {
			//if(!Utils.ifBlank(urNo,"").trim().equalIgnoreCase("")){					
			if(urNo!=null && !urNo.trim().equalsIgnoreCase("")){	
				result = ipUrIPDao.searchIPUrIPByID(urNo);
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	
	public IIPUrIPDao getIpUrIPDao() {
		return ipUrIPDao;
	}
	public void setIpUrIPDao(IIPUrIPDao ipUrIPDao) {
		this.ipUrIPDao = ipUrIPDao;
	}
	public CommonDao getCommonDao() {
		return commonDao;
	}
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}


	
}
