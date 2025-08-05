package th.co.ais.ipfm.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import th.co.ais.ipfm.dao.IMainMenuDao;
import th.co.ais.ipfm.domain.MenuCaption;
import th.co.ais.ipfm.domain.Suggestion;
import th.co.ais.ipfm.domain1.IpFunction;
import th.co.ais.ipfm.service.MainMenuService;

public class MainMenuServiceImpl implements MainMenuService {
	private IMainMenuDao mainMenuDao;

	public void setMainMenuDao(IMainMenuDao mainMenuDao) {
		this.mainMenuDao = mainMenuDao;
	}

	@Override
	public Map listMenuByUserID(String userID) throws Exception {	
		List<IpFunction> funcList = mainMenuDao.listMenuByUserID(userID);
//		if(funcList.size()==0){
//			String groupName = mainMenuDao.findGroupByUser(userID);
//			funcList = mainMenuDao.listMenuByUserID(groupName);
//		}
		
		Map menuID = new LinkedHashMap<String,String>();
		Map menuList = new LinkedHashMap<String,ArrayList>();
		for(IpFunction func : funcList){
//			System.out.println("function ["+func.getRowId()+"]>> "+func.getFuncName()+" >> "+func.getFuncId());
			if(func.getMenuLevel().intValue()==0){
				String name = func.getFuncName();
				String id = func.getFuncId();
				menuID.put(id, name);
				List<MenuCaption> temp = new ArrayList<MenuCaption>();
				menuList.put(name, temp);
			} else {
				String parentMenu = func.getFuncMatherId();
				String parent = (String)menuID.get(parentMenu);
				List<MenuCaption> temp = (ArrayList<MenuCaption>)menuList.get(parent);
				MenuCaption caption = new MenuCaption();
				caption.setCaption(func.getFuncName());
				caption.setLinkURL(func.getActionUrl());
				caption.setProgramID(func.getProgramId());
				temp.add(caption);
				menuList.remove(parent);
				menuList.put(parent, temp);
			}
		}
		return menuList;
	}

	@Override
	public List<Suggestion> listProject() {
		return mainMenuDao.listProject();
	}

	@Override
	public List<IpFunction> findAllMenu() {
		List<IpFunction> menuList = mainMenuDao.findAllMenu();
		return menuList;
	}

	@Override
	public IpFunction findMenuByRowId(Integer rowId) {
		IpFunction ipFucntion = mainMenuDao.findMenuByRowId(rowId);
		return ipFucntion; 
	}

	@Override
	public List<IpFunction> findMenuLevel1ByUserId(String userId) throws Exception{
		List<IpFunction> menuList = mainMenuDao.findMenuLevel1ByUserId(userId);
		return menuList;
	}

}
