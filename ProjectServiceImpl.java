package th.co.ais.ipfm.service.impl;




import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.ProjectDao;
import th.co.ais.ipfm.domain1.Project;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.ProjectService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;


public class ProjectServiceImpl implements ProjectService {
	private ProjectDao projectDao;
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
	
	@Override
	public List<Project> getProjectList() throws IPFMBusinessException {
		List<Project> projectList = new ArrayList<Project>(); 
		try{
			projectList = projectDao.getProjectList();
		}catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
		return projectList;
	}
	
}
