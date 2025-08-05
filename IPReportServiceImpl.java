package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IPReportListDao;
import th.co.ais.ipfm.domain1.IpReportList;
import th.co.ais.ipfm.domain1.Iprur001Result1;
import th.co.ais.ipfm.domain1.Iprur001Result2;
import th.co.ais.ipfm.domain1.Iprur001Result4;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IPReportService;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class IPReportServiceImpl implements IPReportService {
	
	private IPReportListDao iPReportListDao;
	private CommonDao commonDao;

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	public void setiPReportListDao(IPReportListDao iPReportListDao) {
		this.iPReportListDao = iPReportListDao;
	}

	@Override
	public List<IpReportList> getIpReportCritier()
			throws Exception {
		// TODO Auto-generated method stub
		List<IpReportList> result = new ArrayList<IpReportList>();		
		try{
			result = iPReportListDao.getIpReportCritier();
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}				
		return result;
	}
	

	@Override
	public List<IpReportList> addReportCriteria(IpReportList obj)
			throws Exception {
		// TODO Auto-generated method stub
		List<IpReportList> result = new ArrayList<IpReportList>();
		
		try{
			if(obj != null){
				obj.setRowId(commonDao.getROW_ID());
				obj.setReqDatetime(new Date());
				obj.setReportId(iPReportListDao.getReportId());
			}
			iPReportListDao.addCritier(obj);
			result = iPReportListDao.getIpReportCritier();
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}				
		return result;
	}

	@Override
	public List<IpReportList> deleteReportCriteria(String reportId)throws Exception {
		// TODO Auto-generated method stub
		List<IpReportList> result = new ArrayList<IpReportList>();
		
		try{
			iPReportListDao.deleteReportList(reportId);
			result = iPReportListDao.getIpReportCritier();
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}
				
		return result;
	}

	@Override
	public List<IpReportList> getIpReportList()throws Exception {
		// TODO Auto-generated method stub
		List<IpReportList> result = new ArrayList<IpReportList>();		
		try{
			result = iPReportListDao.getIpReportList();
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}				
		return result;
	}

	
	public List<Iprur001Result1> getIprur001Result1(String  reportId) throws Exception {
		// TODO Auto-generated method stub
		List<Iprur001Result1> result = new ArrayList<Iprur001Result1>();		
		try{
			result = iPReportListDao.getIprur001Result1(reportId);
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}				
		return result;
	}
	
	public List<Iprur001Result4> getIprur001Result4(String  reportId) throws Exception {
		// TODO Auto-generated method stub
		List<Iprur001Result4> result = new ArrayList<Iprur001Result4>();		
		try{
			result = iPReportListDao.getIprur001Result4(reportId);
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}				
		return result;
	}
	
	public List<Iprur001Result2> getTotalURRequest(String  reportId, String  urType) throws Exception {
		// TODO Auto-generated method stub
		List<Iprur001Result2> result = new ArrayList<Iprur001Result2>();		
		try{
			result = iPReportListDao.getTotalURRequest(reportId, urType);
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}				
		return result;
	}
	
	public List<String> getURList(String reportId, String urType, String seq, String level, String userId, String tab, String action, String status) throws Exception {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<String>();
		
		try{	
			if(urType!=null && urType.equals("NC")){
				result = iPReportListDao.getURNCList(reportId, seq, level, userId, tab, action, status);
			}else{//IP
				result = iPReportListDao.getURIPList(reportId, seq, level, userId, action, status);
			}
		}catch (DataAccessException e) {
			// TODO: handle exception
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(e.getMessage()));
		}				
		return result;
	}
	
	public IpReportList getReportCritierByReportId(String reportId) throws Exception{
		return iPReportListDao.getReportCritierByReportId(reportId);
	}
	
}
