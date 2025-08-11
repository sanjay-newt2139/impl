 /* This application remediation was done for embedded Oracle SQL to make it compatible with PostgreSQL with Newt DMAP Version: v1.1.4.3_v8.3.5.2 on Date: 05-Aug-2025 */
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


	public void setMainMenuDao(IMainMenuDao mainMenuDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setMainMenuDao
		this.mainMenuDao = mainMenuDao;
	}

	public void setIpfmCommondDao(IPFMCommondDao ipfmCommondDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpfmCommondDao
		this.ipfmCommondDao = ipfmCommondDao;
	}
	
	public void setIpvMonitorReqncDao(IpvMonitorReqncDao ipvMonitorReqncDao) { // DMAP Comment : Dead Code Detected - The Following Method has no reference setIpvMonitorReqncDao
		this.ipvMonitorReqncDao = ipvMonitorReqncDao;
	}

	@Override
	public ArrayList<TreeMenu> getToDoListByUser(String userName) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getToDoListByUser
		// TODO Auto-generated method stub
		ArrayList<TreeMenu> returnResult = new ArrayList<TreeMenu>();
		ArrayList params = new ArrayList();
		params.add(userName); 
/**
DMAP TAG: Query converted: Identifier4
DMAP ConvertedQuery - call LIST_TODO_LIST (?,?)
**/

//		List<Map> result = ipfmCommondDao.callStore("{call LIST_TODO_LIST (?,?)}", params);
		List<Map> result = ipfmCommondDao.callStore("call LIST_TODO_LIST (?,?)", params);
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
	public ArrayList<TreeMenu> getWatchListByUser(String userName) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getWatchListByUser
		// TODO Auto-generated method stub
		ArrayList<TreeMenu> returnResult = new ArrayList<TreeMenu>();
		ArrayList params = new ArrayList();
		params.add(userName);
/**
DMAP TAG: Query converted: Identifier3
DMAP ConvertedQuery - call LIST_WATCH_LIST(?,?)
**/

//		List<Map> result = ipfmCommondDao.callStore("{call LIST_WATCH_LIST(?,?)}", params);
		List<Map> result = ipfmCommondDao.callStore("call LIST_WATCH_LIST(?,?)", params);
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
	public ArrayList<TreeMenu> getMornitorListByUser(String userName) { // DMAP Comment : Dead Code Detected - The Following Method has no reference getMornitorListByUser
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
/**
DMAP TAG: Query converted: Identifier2
DMAP ConvertedQuery - call LIST_MONITOR_LIST(?,?)
**/

//		List<Map> result = ipfmCommondDao.callStore("{call LIST_MONITOR_LIST(?,?)}", params);
		List<Map> result = ipfmCommondDao.callStore("call LIST_MONITOR_LIST(?,?)", params);
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
