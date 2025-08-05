package th.co.ais.ipfm.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IPUrIPResultDao;
import th.co.ais.ipfm.domain1.IpUrIpResult;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IPUrIPResultService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;


public class IPUrIPResultServiceImpl implements IPUrIPResultService {
	private IPUrIPResultDao ipUrIPResultDao;
	
	public void setIpUrIPResultDao(IPUrIPResultDao ipUrIPResultDao) {
		this.ipUrIPResultDao = ipUrIPResultDao;
	}
	
	public IpUrIpResult checkIPRange(IpUrIpResult ipUrIpResult) throws IPFMBusinessException{
		IpUrIpResult result = new IpUrIpResult();
		try {
			result = ipUrIPResultDao.checkIPRange(ipUrIpResult);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	public IpUrIpResult checkIPRange23(IpUrIpResult ipUrIpResult) throws IPFMBusinessException{
		IpUrIpResult result = new IpUrIpResult();
		try {
			result = ipUrIPResultDao.checkIPRange23(ipUrIpResult);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	public IpUrIpResult checkIPRange23_2(IpUrIpResult ipUrIpResult) throws IPFMBusinessException{
		IpUrIpResult result = new IpUrIpResult();
		try {
			result = ipUrIPResultDao.checkIPRange23_2(ipUrIpResult);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return result;
	}
	public IpUrIpResult addIpUrIpResult(IpUrIpResult ipUrIpResult) throws IPFMBusinessException{
		try {
			String urNo = ipUrIpResult.getId().getUrNo();
			BigDecimal seqNo = ipUrIPResultDao.getSeqNo(urNo);
			ipUrIpResult.getId().setSeq(seqNo);
			ipUrIPResultDao.insert(ipUrIpResult);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpResult;
	}
	public List<IpUrIpResult> addIpUrIpResult(List<IpUrIpResult> ipUrIpResultList) throws IPFMBusinessException{
		try {
			for (IpUrIpResult ipUrIpResult : ipUrIpResultList){
				String urNo = ipUrIpResult.getId().getUrNo();
				BigDecimal seqNo = ipUrIPResultDao.getSeqNo(urNo);
				ipUrIpResult.getId().setSeq(seqNo);
				ipUrIPResultDao.insert(ipUrIpResult);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipUrIpResultList;
	}
}
