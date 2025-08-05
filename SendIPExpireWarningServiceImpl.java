package th.co.ais.ipfm.service.impl;

import java.util.List;

import th.co.ais.ipfm.dao.IAutoSendMailDao;
import th.co.ais.ipfm.domain1.IpInfo;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.SendIPExpireWarningService;

public class SendIPExpireWarningServiceImpl implements SendIPExpireWarningService{
	private IAutoSendMailDao iAutoSendMailDao;
	public IAutoSendMailDao getiAutoSendMailDao() {
		return iAutoSendMailDao;
	}

	public void setiAutoSendMailDao(IAutoSendMailDao iAutoSendMailDao) {
		this.iAutoSendMailDao = iAutoSendMailDao;
	}

	@Override
	public String getTeamName(String teamId){
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getTeamName(teamId);
	}
	
	@Override
	public String getTeamNameLevel2(String teamId){
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getTeamNameLevel2(teamId);
	}
	
	@Override
	public String getTeamNameLevel2TeamAssign(String teamId){
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getTeamNameLevel2TeamAssign(teamId);
	}
	
	@Override
	public List<String> getListTeam(String conditionDate) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListTeam(conditionDate);
	}
	
	@Override
	public List<String> getListTeamLevel2(String conditionDate) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListTeamLevel2(conditionDate);
	}

	@Override
	public List<String> getListTeamLevel2ForAssignTeam(String conditionDate) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListTeamLevel2ForAssignTeam(conditionDate);
	}	
	
	@Override
	public String getConditionDate() {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getConditionDate();
	}
	
	@Override
	public String getExpireDate(String conditionDate, String teamId) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getExpireDate(conditionDate, teamId);
	}

	@Override
	public String getExpireDateLevel2(String conditionDate, String teamId) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getExpireDateLevel2(conditionDate, teamId);
	}	
	
	@Override
	public String getExpireDateLevel2TeamAssign(String conditionDate, String teamId) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getExpireDateLevel2TeamAssign(conditionDate, teamId);
	}	
	
	@Override
	public List<String> getListEmail(String teamId) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListEmail(teamId);
	}

	@Override
	public List<String> getListIP(String conditionDate, String teamId) {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListIP(conditionDate, teamId);
	}

	@Override
	public List<String> getListExpireIPInfo3Digit(String conditionDate,
			String teamId) throws IPFMBusinessException {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListExpireIPInfo3Digit(conditionDate, teamId);
	}
	
	@Override
	public List<String> getListExpireIPLevel2(String conditionDate,
			String teamId) throws IPFMBusinessException {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListExpireIPLevel2(conditionDate, teamId);
	}
	
	
	@Override
	public List<String> getListExpireIPLevel2ForAssignTeam(String conditionDate,
			String teamId) throws IPFMBusinessException {
		// TODO Auto-generated method stub
		return iAutoSendMailDao.getListExpireIPLevel2ForAssignTeam(conditionDate, teamId);
	}
	

}
