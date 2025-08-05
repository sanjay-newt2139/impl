package th.co.ais.ipfm.service.impl;

import java.util.Date;
import java.util.List;

import th.co.ais.ipfm.dao.IPEmailNotifyDao;
import th.co.ais.ipfm.dao.IPUrActionDao;
import th.co.ais.ipfm.domain1.IpEmailNotify;
import th.co.ais.ipfm.domain1.IpUrAction;
import th.co.ais.ipfm.domain1.IpUser;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IpUrActionService;

public class IpUrActionServiceImpl implements IpUrActionService {
	private IPUrActionDao ipUrActionDao;
	private IPEmailNotifyDao ipEmailNotify;

	public void setIpUrActionDao(IPUrActionDao ipUrActionDao) {
		this.ipUrActionDao = ipUrActionDao;
	}

	public void setIpEmailNotify(IPEmailNotifyDao ipEmailNotify) {
		this.ipEmailNotify = ipEmailNotify;
	}

	@Override
	public void saveEmailNotify(IpEmailNotify notify) throws IPFMBusinessException {
		try{
			ipEmailNotify.insert(notify);	
		} catch(Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(e);
		}
	}

	@Override
	public IpEmailNotify findByResponce(String urNo, String userId) {
		return ipEmailNotify.findByResponce(urNo, userId);
	}
	
	@Override
	public IpEmailNotify findByResponce(String emailCode) {
		return ipEmailNotify.findByResponce(emailCode);
	}

	@Override
	public void updateEmailNotify(IpEmailNotify notify) {
		ipEmailNotify.update(notify);
		
	}

	@Override
	public boolean isSendEmail(String type, String id) {
		return ipEmailNotify.isSendEmail(type, id);
	}
	
	@Override
	public Date getSLADate(Date reqDate,String urType,String pmTeam){
		return ipUrActionDao.getSLADate(reqDate,urType, pmTeam);
	}

	@Override
	public Date getOLADate(Date reqDate, String urType, String nodeId) {
		return ipUrActionDao.getOLADate(reqDate,urType,nodeId);
	}
	
	@Override
	public void deleteNotify(String urNo,String urStatus){
		if((urStatus!=null) && !(urStatus.equals(""))){
			ipEmailNotify.deleteNotibyByStatus(urNo, urStatus);
		} else {
			ipEmailNotify.deleteNotibyByUrNo(urNo);
		}
	}

	@Override
	public List<IpUrAction> listAction(String urNo, IpUser user, String mode) {
		return ipUrActionDao.listAction(urNo, user, mode);
	}

	@Override
	public List<IpUrAction> getIpUrAction(String urNo) {
		List<IpUrAction> ipUrActionList = null;
		ipUrActionList = ipUrActionDao.getURAction(urNo);
		
		return ipUrActionList;
	}
	@Override
	public List<IpUrAction> listAction(String urNo, String subUrNo) {
		List<IpUrAction> ipUrActionList = null;
		ipUrActionList = ipUrActionDao.listAction(urNo,subUrNo);
		
		return ipUrActionList;
	}
	

}
