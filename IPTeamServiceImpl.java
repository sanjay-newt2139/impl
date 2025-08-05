package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.IPTeamDao;
import th.co.ais.ipfm.domain1.IpTeam;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IPTeamService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class IPTeamServiceImpl implements IPTeamService {
	private IPTeamDao ipTeamDao;

	public void setIpTeamDao(IPTeamDao ipTeamDao) {
		this.ipTeamDao = ipTeamDao;
	}

	@Override
	public List<IpTeam> getTier2TeamList() throws IPFMBusinessException {
		List<IpTeam> result = new ArrayList<IpTeam>();
	
		try {
			result = ipTeamDao.getTier2TeamList();
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return result;
	}
	
	@Override
	public IpTeam findIpTeamById(String teamId) throws IPFMBusinessException {
		IpTeam result = null;
		try {
			if (teamId!=null) {
				if (teamId.trim().equalsIgnoreCase("NA")) {
					result = ipTeamDao.getIpTeamByTeamName(teamId);
				}else{
					result = ipTeamDao.getIpTeam(teamId);
				}
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} catch (Exception e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		} 
		return result;
	}
}
