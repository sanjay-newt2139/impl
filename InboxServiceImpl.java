package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IPFunctionDao;
import th.co.ais.ipfm.dao.InboxDao;
import th.co.ais.ipfm.dao.IpvMonitorReqncDao;
import th.co.ais.ipfm.dao.IpvTodoPlanningReqDao;
import th.co.ais.ipfm.dao.IpvTodoReqncDao;
import th.co.ais.ipfm.dao.IpvTodoUserReqDao;
import th.co.ais.ipfm.dao.IpvWatchPlanningReqDao;
import th.co.ais.ipfm.dao.IpvWatchReqncDao;
import th.co.ais.ipfm.dao.IpvWatchUserReqDao;
import th.co.ais.ipfm.domain.TempInbox;
import th.co.ais.ipfm.domain1.IpFunction;
import th.co.ais.ipfm.domain1.IpvMonitorReqnc;
import th.co.ais.ipfm.domain1.IpvTodoPlanningReq;
import th.co.ais.ipfm.domain1.IpvTodoReqnc;
import th.co.ais.ipfm.domain1.IpvTodoUserReq;
import th.co.ais.ipfm.domain1.IpvWatchPlanningReq;
import th.co.ais.ipfm.domain1.IpvWatchReqnc;
import th.co.ais.ipfm.domain1.IpvWatchUserReq;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.InboxService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMConstant;
import th.co.ais.ipfm.util.IPFMDataUtility;



public  class InboxServiceImpl implements InboxService{
	private InboxDao inboxDao;
	private IpvTodoReqncDao ipvTodoReqncDao;
	private IpvTodoUserReqDao ipvTodoUserReqDao;
	private IpvTodoPlanningReqDao ipvTodoPlanningReqDao;
	private IpvWatchReqncDao ipvWatchReqncDao;
	private IpvWatchUserReqDao ipvWatchUserReqDao;
	private IpvWatchPlanningReqDao ipvWatchPlanningReqDao;
	private IpvMonitorReqncDao ipvMonitorReqncDao;	
	private IPFunctionDao ipFunctionDao;

	public void setIpvWatchReqncDao(IpvWatchReqncDao ipvWatchReqncDao) {
		this.ipvWatchReqncDao = ipvWatchReqncDao;
	}

	public void setIpvWatchUserReqDao(IpvWatchUserReqDao ipvWatchUserReqDao) {
		this.ipvWatchUserReqDao = ipvWatchUserReqDao;
	}

	public void setIpvWatchPlanningReqDao(
			IpvWatchPlanningReqDao ipvWatchPlanningReqDao) {
		this.ipvWatchPlanningReqDao = ipvWatchPlanningReqDao;
	}

	public void setIpvMonitorReqncDao(IpvMonitorReqncDao ipvMonitorReqncDao) {
		this.ipvMonitorReqncDao = ipvMonitorReqncDao;
	}

	public void setInboxDao(InboxDao inboxDao) {
		this.inboxDao = inboxDao;
	}

	public void setIpvTodoReqncDao(IpvTodoReqncDao ipvTodoReqncDao) {
		this.ipvTodoReqncDao = ipvTodoReqncDao;
	}

	public void setIpvTodoUserReqDao(IpvTodoUserReqDao ipvTodoUserReqDao) {
		this.ipvTodoUserReqDao = ipvTodoUserReqDao;
	}

	public void setIpvTodoPlanningReqDao(IpvTodoPlanningReqDao ipvTodoPlanningReqDao) {
		this.ipvTodoPlanningReqDao = ipvTodoPlanningReqDao;
	}

	public void setIpFunctionDao(IPFunctionDao ipFunctionDao) {
		this.ipFunctionDao = ipFunctionDao;
	}

	@Override
	public List<TempInbox> findInbox(String userId, String title1, String title2, String title3, String urNo) {
		List<TempInbox> inboxList = new ArrayList<TempInbox>();
		try {
			inboxList = inboxDao.findInbox(userId, title1, title2, title3, urNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		return inboxList;
	}
	@Override
	public String getNews() throws IPFMBusinessException{
		String news = "";
		try {
			news = inboxDao.getNews();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}	
		return news;
	}

	@Override
	public List<IpvTodoReqnc> findTodoReqNC(String userId, String actionCode) throws IPFMBusinessException {
		List<IpvTodoReqnc> inboxList = new ArrayList<IpvTodoReqnc>();
		try {
			if (actionCode==null || !actionCode.equalsIgnoreCase("01")) {
				inboxList = ipvTodoReqncDao.findTodoReqNC(userId, actionCode);
			}else{
				inboxList = ipvTodoReqncDao.findTodoReqNCDraft(userId, actionCode);
			}
//			for (IpvTodoReqnc nc : inboxList) {
//				System.out.println("nc.getReqDate()= "+ nc.getReqDate()+ " :: created = "+nc.getCreated());
//			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}
	@Override
	public List<IpvTodoReqnc> findTodoReqNCNoRemark(String userId, String actionCode) throws IPFMBusinessException {
		List<IpvTodoReqnc> inboxList = new ArrayList<IpvTodoReqnc>();
		try {
			inboxList = ipvTodoReqncDao.findTodoReqNCNoRemark(userId, actionCode);
//			for (IpvTodoReqnc nc : inboxList) {
//				System.out.println("nc.getReqDate()= "+ nc.getReqDate()+ " :: created = "+nc.getCreated());
//			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}
	@Override
	public List<IpvTodoUserReq> findTodoUserReq(String userId, String actionCode) throws IPFMBusinessException {
		List<IpvTodoUserReq> inboxList = new ArrayList<IpvTodoUserReq>();
		try {
			inboxList = ipvTodoUserReqDao.findTodoUserReq(userId, actionCode);
//			System.out.println("inboxList size = " + inboxList.size());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}
	@Override
	public List<IpvTodoPlanningReq> findTodoPlanningReq(String userId, String actionCode) throws IPFMBusinessException {
		List<IpvTodoPlanningReq> inboxList = new ArrayList<IpvTodoPlanningReq>();
		try {
			inboxList = ipvTodoPlanningReqDao.findTodoPlanningReq(userId, actionCode);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}

	@Override
	public List<String> getUrTypeList(String urType) throws IPFMBusinessException {
		List<String> urTypeList = new ArrayList<String>();
		try {
			if (urType==null || urType.trim().length()==0) {
				urTypeList.add("User Request IP");
				urTypeList.add("Planning Request IP");
				urTypeList.add("Request Network Config");
			}else if (urType.equalsIgnoreCase(IPFMConstant.URTYPE_USER_REQ_IP)){
				urTypeList.add("User Request IP");
			}else if (urType.equalsIgnoreCase(IPFMConstant.URTYPE_PLANNING_REQ_IP)){
				urTypeList.add("Planning Request IP");
			}else if (urType.equalsIgnoreCase(IPFMConstant.URTYPE_NETWORK_CONFIG)){
				urTypeList.add("Request Network Config");
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return urTypeList;
	}
	
	@Override
	public IpFunction getIpFunction(String funcId) throws IPFMBusinessException{
		IpFunction ipFunction = new IpFunction();
		try{
			ipFunction = ipFunctionDao.findIPFunctionByFuncID(funcId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return ipFunction;
	}

	@Override
	public List<IpvMonitorReqnc> findMonitorReqNC(String userId) throws IPFMBusinessException {
		List<IpvMonitorReqnc> inboxList = new ArrayList<IpvMonitorReqnc>();
		try {
			inboxList = ipvMonitorReqncDao.findMonitorReqNC(userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}

	@Override
	public List<IpvWatchPlanningReq> findWatchPlanningReq(String userId) throws IPFMBusinessException {
		List<IpvWatchPlanningReq> inboxList = new ArrayList<IpvWatchPlanningReq>();
		try {
			inboxList = ipvWatchPlanningReqDao.findWatchPlanningReq(userId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}

	@Override
	public List<IpvWatchReqnc> findWatchReqNC(String userId) throws IPFMBusinessException {
		List<IpvWatchReqnc> inboxList = new ArrayList<IpvWatchReqnc>();
		try {
			inboxList = ipvWatchReqncDao.findWatchReqNC(userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}

	@Override
	public List<IpvWatchUserReq> findWatchUserReq(String userId) throws IPFMBusinessException {
		List<IpvWatchUserReq> inboxList = new ArrayList<IpvWatchUserReq>();
		try {
			inboxList = ipvWatchUserReqDao.findWatchUserReq(userId);
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return inboxList;
	}
}
