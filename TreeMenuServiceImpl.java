package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import th.co.ais.ipfm.dao.IMainMenuDao;
import th.co.ais.ipfm.dao.IPFMCommondDao;
import th.co.ais.ipfm.dao.IpvMonitorReqncDao;
import th.co.ais.ipfm.domain.TreeMenu;
import th.co.ais.ipfm.service.TreeMenuService;

public class TreeMenuServiceImpl implements TreeMenuService{
	private IMainMenuDao mainMenuDao;
	private IPFMCommondDao ipfmCommondDao;
	private IpvMonitorReqncDao ipvMonitorReqncDao;


	public void setMainMenuDao(IMainMenuDao mainMenuDao) {
		this.mainMenuDao = mainMenuDao;
	}

	public void setIpfmCommondDao(IPFMCommondDao ipfmCommondDao) {
		this.ipfmCommondDao = ipfmCommondDao;
	}
	
	public void setIpvMonitorReqncDao(IpvMonitorReqncDao ipvMonitorReqncDao) {
		this.ipvMonitorReqncDao = ipvMonitorReqncDao;
	}

	@Override
	public ArrayList<TreeMenu> getToDoListByUser(String userName) {
		// TODO Auto-generated method stub
		ArrayList<TreeMenu> returnResult = new ArrayList<TreeMenu>();
		ArrayList params = new ArrayList();
		params.add(userName); 
		List<Map> result = ipfmCommondDao.callStore("{call LIST_TODO_LIST (?,?)}", params);
		//List<Map> result = ipfmCommondDao.callStore("{call LIST_TODO_LIST (?,?)}", params);
//		System.out.println(result.size());
		for(Map map : result){
			//System.out.println("(String)map.get(LV2)" + (String)map.get("LV2"));
			TreeMenu menu = new TreeMenu();
			menu.setLevel1((String)map.get("LV1"));
			menu.setLevel2((String)map.get("LV2"));
			menu.setLevel3((String)map.get("LV3"));
			menu.setLevel4((String)map.get("LV4"));
			menu.setActionName((String)map.get("ACTION_NAME"));
			menu.setActionCode((String)map.get("ACTION_CODE"));
			menu.setUrType((String)map.get("UR_TYPE"));
			returnResult.add(menu);
		}
		return returnResult;
	}

	@Override
	public ArrayList<TreeMenu> getWatchListByUser(String userName) {
		// TODO Auto-generated method stub
		ArrayList<TreeMenu> returnResult = new ArrayList<TreeMenu>();
		ArrayList params = new ArrayList();
		params.add(userName);
		List<Map> result = ipfmCommondDao.callStore("{call LIST_WATCH_LIST(?,?)}", params);
		for(Map map : result){
			TreeMenu menu = new TreeMenu();
			menu.setLevel1((String)map.get("LV1"));
			menu.setLevel2((String)map.get("LV2"));
			menu.setLevel3((String)map.get("LV3"));
			menu.setLevel4((String)map.get("LV4"));
			menu.setActionName((String)map.get("ACTION_NAME"));
			menu.setActionCode((String)map.get("ACTION_CODE"));
			menu.setUrType((String)map.get("UR_TYPE"));
			returnResult.add(menu);
		}
		return returnResult;
	}
	@Override
	public ArrayList<TreeMenu> getMornitorListByUser(String userName) {
		// TODO Auto-generated method stub
		ArrayList<TreeMenu> returnResult = new ArrayList<TreeMenu>();
		ArrayList params = new ArrayList();
		int count = 0;
		try {
			count = ipvMonitorReqncDao.countMonitorReqNC(userName);
		} catch(Exception e) {
			e.printStackTrace();
		}
		params.add(userName);
		List<Map> result = ipfmCommondDao.callStore("{call LIST_MONITOR_LIST(?,?)}", params);
		for(Map map : result){
			TreeMenu menu = new TreeMenu();
			menu.setLevel1((String)map.get("LV1"));// + " (" + count + ")");
			menu.setLevel2((String)map.get("LV2"));// + " (" + count + ")");
			menu.setLevel3((String)map.get("LV3"));
			menu.setLevel4((String)map.get("LV4"));
			menu.setActionName((String)map.get("ACTION_NAME"));
			menu.setActionCode((String)map.get("ACTION_CODE"));
			menu.setUrType((String)map.get("UR_TYPE"));
			returnResult.add(menu);
		}
		return returnResult;
	}

}
