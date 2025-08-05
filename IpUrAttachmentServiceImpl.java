package th.co.ais.ipfm.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.dao.DataAccessException;

import th.co.ais.ipfm.dao.CommonDao;
import th.co.ais.ipfm.dao.IPSubUrAttachmentDao;
import th.co.ais.ipfm.dao.IPUrAttachmentDao;
import th.co.ais.ipfm.domain1.IpSubUrAttachment;
import th.co.ais.ipfm.domain1.IpUrAttachment;
import th.co.ais.ipfm.domain1.IpUrAttachmentId;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IpUrAttachmentService;
import th.co.ais.ipfm.util.ErrorMessageUtil;
import th.co.ais.ipfm.util.IPFMDataUtility;

public class IpUrAttachmentServiceImpl implements IpUrAttachmentService {
	private IPUrAttachmentDao ipUrAttachmentDao;
	private CommonDao commonDao;

	public void setIpUrAttachmentDao(IPUrAttachmentDao ipUrAttachmentDao) {
		this.ipUrAttachmentDao = ipUrAttachmentDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	@Override
	public void saveAttachFile(IpUrAttachment attach) {
		String urNo = attach.getId().getUrNo();
		BigDecimal seqNo = ipUrAttachmentDao.getSeqNo(urNo);
		attach.getId().setSeq(seqNo);
		attach.setRowId(commonDao.getROW_ID());
		ipUrAttachmentDao.insert(attach);
	}

	@Override
	public IpUrAttachment findAttachment(String urNo, String filename,
			String catagory) {
		return ipUrAttachmentDao.findByFileName(urNo, filename, catagory);
	}

	@Override
	public void deleteAttachmentFile(IpUrAttachment attachFile) throws IPFMBusinessException {
		try{
			ipUrAttachmentDao.delete(attachFile);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}catch(Exception ex){
			ex.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0009")));
		}
	}

	@Override
	public void updateAttachFileName(String tempUrNo,String urNo) throws IPFMBusinessException {
		try{
			List<IpUrAttachment> listAttach = ipUrAttachmentDao.findByUrNo(tempUrNo);
			boolean failed = false;
			for(IpUrAttachment attach : listAttach){
				if(movefile(attach,urNo)){
					ipUrAttachmentDao.updateIpUrAttachment(urNo,tempUrNo);
					/*IpUrAttachment copy = (IpUrAttachment)attach.clone();
					IpUrAttachmentId id = new IpUrAttachmentId(urNo, attach.getId().getSeq(), attach.getId().getCategory());  
					copy.setId(id);
					int pos = copy.getFileLocation().lastIndexOf("/");
			        String savePath = copy.getFileLocation().substring(0, pos);
			        String destPath = savePath+"/"+urNo;
			        copy.setFileLocation(destPath);
					ipUrAttachmentDao.insert(copy);*/
				} else {
					failed=true;
				}
			}
			if(!failed) {
				if(!tempUrNo.equalsIgnoreCase(urNo)){
					ipUrAttachmentDao.deleteIpUrAttachment(tempUrNo);
				}
				
			}
		} catch (DataAccessException e) {
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}catch(Exception ex){
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0011")));
		}
	}
	
	@Override
	public void updateAttachFileNameByCategory(String tempUrNo,String urNo) throws IPFMBusinessException {
		try{
			List<IpUrAttachment> listAttach = ipUrAttachmentDao.findByUrNo(tempUrNo);
			boolean failed = false;
			for(IpUrAttachment attach : listAttach){
				if(movefileByCategory(attach,urNo)){
					IpUrAttachment copy = (IpUrAttachment)attach.clone();
					IpUrAttachmentId id = new IpUrAttachmentId(urNo, attach.getId().getSeq(), attach.getId().getCategory());  
					copy.setId(id);
					ipUrAttachmentDao.insert(copy);
				} else {
					failed=true;
				}
			}
			if(failed) ipUrAttachmentDao.deleteIpUrAttachment(tempUrNo);
		}catch(Exception e){
			e.printStackTrace();
			throw new IPFMBusinessException(IPFMDataUtility.buildMessage(ErrorMessageUtil.getMessage("ER0008")));
		}
	}


	@Override
	public void attachFile(IpUrAttachment ipUrAttachment) throws IPFMBusinessException {
		IpUrAttachment attach = findAttachment(ipUrAttachment.getId().getUrNo(), ipUrAttachment.getFileName(), ipUrAttachment.getId().getCategory());
	    if(attach!=null){
	    	String deleteStr = attach.getFileLocation()+"/"+attach.getFileName();
	    	File deleteFile = new File(deleteStr);
	    	System.out.println("I will Delete >>"+deleteStr);
	    	if(deleteFile.exists()){
	    		if(deleteFile.delete()){
	    			System.out.println("Delete >>"+deleteFile.getName());
	    			deleteAttachmentFile(attach);
	    		}
	    	}
	    }
        IpUrAttachment attachFile = new IpUrAttachment();
        //IpUrAttachmentId id = new IpUrAttachmentId(urNO,null,catagory);
        saveAttachFile(attachFile);
		
	}
	
	@Override
	public void updateAttachFile(String tempUrNo,String urNo,String updateBy) {
		List<IpUrAttachment> listAttach = ipUrAttachmentDao.findByUrNo(tempUrNo);
		boolean failed = false;
		for(IpUrAttachment attach : listAttach){
			boolean flag = false;
			try {
				flag = movefile(attach, urNo);
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
			if(flag){
					IpUrAttachment newAttachment = new IpUrAttachment();
					IpUrAttachmentId id = new IpUrAttachmentId(urNo, attach.getId().getSeq(), attach.getId().getCategory());
					newAttachment.setFileLocation(attach.getFileLocation().replace(tempUrNo, urNo));
					newAttachment.setDescription(attach.getDescription());
					newAttachment.setFileName(attach.getFileName());
					newAttachment.setRowId(commonDao.getROW_ID());
					newAttachment.setCreatedBy(attach.getCreatedBy());
					newAttachment.setLastUpdBy(updateBy);
					newAttachment.setId(id);
					try{
						ipUrAttachmentDao.insert(newAttachment);
						ipUrAttachmentDao.deleteIpUrAttachment(tempUrNo);
					}catch(Exception ex) {
						ex.printStackTrace();
					}
			} else {
				failed=true;
			}
		}
	}
	
	@Override
	public void updateAttachFileByCategory(String tempUrNo,String urNo,String updateBy) throws IPFMBusinessException{
		List<IpUrAttachment> listAttach = ipUrAttachmentDao.findByUrNo(tempUrNo);
		boolean failed = false;
		for(IpUrAttachment attach : listAttach){
			boolean flag = false;
			try {
				flag = movefileByCategory(attach, urNo);
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
			if(flag){
					IpUrAttachment newAttachment = new IpUrAttachment();
					IpUrAttachmentId id = new IpUrAttachmentId(urNo, attach.getId().getSeq(), attach.getId().getCategory());
					newAttachment.setFileLocation(attach.getFileLocation().replace(tempUrNo, urNo));
					newAttachment.setDescription(attach.getDescription());
					newAttachment.setFileName(attach.getFileName());
					newAttachment.setRowId(commonDao.getROW_ID());
					newAttachment.setCreatedBy(attach.getCreatedBy());
					newAttachment.setLastUpdBy(updateBy);
					newAttachment.setId(id);
					try{
						ipUrAttachmentDao.insert(newAttachment);
						ipUrAttachmentDao.deleteIpUrAttachment(tempUrNo);
					}catch(Exception ex) {
						ex.printStackTrace();
					}
			} else {
				failed=true;
			}
		}
	}

	@Override
	public List<IpUrAttachment> listFilesByUrNo(String urNo) {
		return ipUrAttachmentDao.findByUrNo(urNo);		
	}

	@Override
	public IpUrAttachment findAttatchment(String urNo, String seq) {
		return ipUrAttachmentDao.findByUrNoSeq(urNo, new BigDecimal(seq));
	}

	@Override
	public List<IpUrAttachment> listFilesByCategory(String urNo, String category) {
		return ipUrAttachmentDao.findByCategory(urNo,category);		
	}
	
	private boolean movefile(IpUrAttachment attach,String urNo){
		String filepath = attach.getFileLocation();
        String filename = attach.getFileName();
        String newPath = urNo;

        int pos = filepath.lastIndexOf("/");
        String savePath = filepath.substring(0, pos);
        String destPath = savePath+"/"+newPath;
        File fp = new File(destPath);
        File old = new File(filepath);
        if(!fp.exists()) fp.mkdir();

        File source = new File(filepath+"/"+filename);
        File dest = new File(destPath+"/"+filename);
        System.out.println("copy "+source.getPath()+" to "+dest.getPath());
        boolean success = source.renameTo(dest);

//		if(success) old.delete();
	    return success;
	}
	
	private boolean movefileByCategory(IpUrAttachment attach,String urNo){
		String filepath = attach.getFileLocation();
        String filename = attach.getFileName();
        String newPath = urNo;
        
        int pos = filepath.lastIndexOf("/");        
        String savePath = filepath.substring(0, pos);
        String categoryPath = filepath.substring(pos);   
        pos = savePath.lastIndexOf("/");
        String oldPath = savePath;
        savePath = savePath.substring(0, pos);
        String destPath = savePath+"/"+newPath;
        
        File fp = new File(destPath);
        File old = new File(oldPath);
        if(!fp.exists()) fp.mkdir();
        destPath = savePath+"/"+newPath+categoryPath;
        fp = new File(destPath);
        if(!fp.exists()) fp.mkdir();
        File source = new File(filepath+"/"+filename);
        File dest = new File(destPath+"/"+filename);
        
        System.out.println("copy "+source.getPath()+" to "+dest.getPath());
        
        boolean success = source.renameTo(dest);
        System.out.println("old.getAbsolutePath() = "+old.getAbsolutePath());
        old.delete();
	    return success;
	}
	
	public boolean deleteDir(File dir) {
		System.out.println("path = " + dir.getAbsolutePath());
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return true;
	}
}
