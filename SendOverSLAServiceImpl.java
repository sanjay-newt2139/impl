package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.List;

import th.co.ais.ipfm.dao.IAutoSendSMSDao;
import th.co.ais.ipfm.service.SendOverSLAService;

public class SendOverSLAServiceImpl implements SendOverSLAService{
	private IAutoSendSMSDao iAutoSendSMSDao;

	@Override
	public String getSMSNo(String urType) {
		String smsNoList = "";
		try{
			if(urType != null && urType.trim().length()>0){
				smsNoList = iAutoSendSMSDao.getSMSNo(urType);			
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return smsNoList;
	}

	@Override
	public String getSMSWording(String refKey) {
		String wording = "";
		try{
			if(refKey != null && refKey.trim().length()>0){
				wording = iAutoSendSMSDao.getSMSWording(refKey);			
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return wording;
	}

	@Override
	public List<String> listUROverSLA(String refKey, String urType) {		
		List<String> listUR = new ArrayList<String>();
		try {
			if(refKey!=null && refKey.equals("NC")){
				listUR = iAutoSendSMSDao.getNCUROverSLAList();
			}else{

				listUR = iAutoSendSMSDao.getIPUROverSLAList(urType);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return listUR;
	}

	@Override
	public void updateSendSMS(String urType, String urNo) {
		
		try {
			if(urType!=null && urType.equals("NC")){
				iAutoSendSMSDao.updateNCURNo(urNo);
			}else{
				iAutoSendSMSDao.updateIPURNo(urNo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public IAutoSendSMSDao getiAutoSendSMSDao() {
		return iAutoSendSMSDao;
	}

	public void setiAutoSendSMSDao(IAutoSendSMSDao iAutoSendSMSDao) {
		this.iAutoSendSMSDao = iAutoSendSMSDao;
	}

	

}
