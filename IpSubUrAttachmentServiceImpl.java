package th.co.ais.ipfm.service.impl;

import java.math.BigDecimal;
import java.util.List;

import th.co.ais.ipfm.dao.IPSubUrAttachmentDao;
import th.co.ais.ipfm.domain1.IpSubUrAttachment;
import th.co.ais.ipfm.domain1.IpSubUrAttachmentDto;
import th.co.ais.ipfm.exception.IPFMBusinessException;
import th.co.ais.ipfm.service.IpSubUrAttachmentService;

public class IpSubUrAttachmentServiceImpl implements IpSubUrAttachmentService {
	private IPSubUrAttachmentDao ipSubUrAttachmentDao;
	
	
	
	public void setIpSubUrAttachmentDao(IPSubUrAttachmentDao ipSubUrAttachmentDao) {
		this.ipSubUrAttachmentDao = ipSubUrAttachmentDao;
	}
	@Override
	public BigDecimal getSeqNo(String urNo, String subUrNo, String catagory) throws Exception{
		try{
			return ipSubUrAttachmentDao.getSeqNo(urNo, catagory);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	@Override
	public IpSubUrAttachment findByFileName(String urNo,String fileName, String catagory)  throws Exception{
		try{
			return ipSubUrAttachmentDao.findByFileName(urNo, fileName, catagory);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	@Override
	public List<IpSubUrAttachment> findByUrNo(String urNo) {
		try{
			return ipSubUrAttachmentDao.findByUrNo(urNo);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	@Override
	public List<IpSubUrAttachment> getIpUrAttachmentList(String urNo)  throws Exception {
		try{
			return ipSubUrAttachmentDao.getIpUrAttachmentList(urNo);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	@Override
	public void deleteIpUrAttachment(String urNo, String fileName, String catagory)  throws Exception {
		try{
			ipSubUrAttachmentDao.deleteIpUrAttachment(urNo,fileName, catagory);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	@Override
	public String findSubUrByUrNoAndSeq(String urNo, String seq)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IpSubUrAttachment findAttachmentSubUr(String urNo, String filename,String catagory) {
		return ipSubUrAttachmentDao.findAttachmentSubUr(urNo, filename, catagory);
	}

	@Override
	public void deleteSubAttachmentFile(IpSubUrAttachment attachFile) throws IPFMBusinessException {
		ipSubUrAttachmentDao.delete(attachFile);
		
	}
	@Override
	public void saveAttachFile(IpSubUrAttachment attach) {
	String urNo = attach.getId().getUrNo();
	BigDecimal seqNo = ipSubUrAttachmentDao.getSeqNo(urNo, attach.getId().getCategory());
	attach.getId().setSeq(seqNo);
	ipSubUrAttachmentDao.insert(attach);}
	@Override
	public List<IpSubUrAttachmentDto> getByUrNo(String urNo) {
		// TODO Auto-generated method stub
		return ipSubUrAttachmentDao.getByUrNo(urNo);
	}
	
}
