package com.test.project.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.redjframework.tika.Tika;
import com.test.project.service.mapper.intf.CmsFileMapper;
import com.test.project.service.mapper.intf.CmsFileRevisionMapper;
import com.test.project.service.vo.CmsFile;
import com.test.project.service.vo.CmsFileRevision;

import net.sf.json.JSONObject;

@Service
public class CmsFileService {

    private static final String   CDN_URL       = "cdn";

    private static final String   ALLNG_CMS_URL = "allng_cms_url";    

    private final Logger          log        = LoggerFactory.getLogger(getClass());

    @Autowired
    private CmsFileMapper         cmsFileMapper;

    @Autowired
    private CmsFileRevisionMapper cmsFileRevisionMapper;

    @Autowired
    private NasFileManageService  nasFileManageService;

	@Autowired
	ThreadPoolTaskExecutor cmsUploadExecutor;

    public CmsFileRevision newCmsUploadFile(String name, MultipartFile multipartFile) throws Exception {
    	return writeFile(null, name, multipartFile);
    }

    /**
     * CMS 최초 파일 등록 (MultipartFile)
     *
     * @param name
     * @param file
     * @return
     * @throws Exception
     */
    public String newUploadFile(String name, MultipartFile multipartFile) throws Exception {

    	CmsFileRevision cmsFile = writeFile(null, name, multipartFile);

        return cmsFile.getFileOid();
    }
    
    /**
     * CMS 최초 파일 등록 (File)
     *
     * @param name
     * @param file
     * @return
     * @throws Exception
     */
    public String newUploadFile(String name, File file) throws Exception {
        CmsFileRevision cmsFile = writeFile(null, name, file);
        return cmsFile.getFileOid();
    }

    /**
     * CMS 등록 파일 갱신
     *
     * @param fileOid
     * @param file
     * @return
     * @throws Exception
     */
    public void changeUploadFile(String fileOid, MultipartFile multipartFile) throws Exception {
        writeFile(fileOid, null, multipartFile);
    }

    /**
     * 파일 처리 메인 (MultipartFile)
     * @param fileOid
     * @param name
     * @param multipartFile
     * @return
     * @throws Exception
     */
    private CmsFileRevision writeFile(String fileOid, String name, MultipartFile multipartFile) throws Exception {

    	int versionCode = 1;
    	CmsFile cmsFile = null;

        /* 파일 첫 등록 일 경우 처리 */
    	if (fileOid == null) {
            cmsFile = new CmsFile();
            cmsFile.setName(name);
            cmsFileMapper.insertCmsFile(cmsFile);
            cmsFile.getOid();
        }
    	/* 파일 수정 일 경우 처리 */
    	else {
    		cmsFile = getCmsFIle(fileOid);
            if (cmsFile == null) {
                throw new Exception("CMS 파일정보 오류!!");
            }
            versionCode = cmsFile.getLastVersionCode() + 1;
        }

    	// Nas Insert
        CmsFileRevision cmsFileRevision = new CmsFileRevision(cmsFile.getOid(), versionCode);
        File file = writeFileToNas(multipartFile, cmsFileRevision);


        // Allng CMS 연동(비동기)
        /*final CmsFile cmsUploadFile = cmsFile;
        cmsUploadExecutor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					writeFileToAllngCms((fileOid == null), cmsUploadFile, cmsFileRevision, file);
				} catch (Exception e) {
			        log.error("Asynchronous cms upload task error", e);
				}
			}
		});*/

        // CMS File Revision Insert
        cmsFileRevision.setCmsInfo(cmsFileRevision.toMetadataString());
        cmsFileRevisionMapper.insertCmsFileRevision(cmsFileRevision);

        // CMS File Update
        cmsFile.setLastVersionCode(cmsFileRevision.getVersionCode());
        cmsFile.setCmsInfo(cmsFileRevision.getCmsInfo());
        cmsFileMapper.updateCmsFile(cmsFile);

        return cmsFileRevision;
    }
    
    /**
     * 파일 처리 메인 (File)
     * @param fileOid
     * @param name
     * @param file
     * @return
     * @throws Exception
     */
    private CmsFileRevision writeFile(String fileOid, String name, File file) throws Exception {
        int versionCode = 1;
        CmsFile cmsFile = null;

        /* 파일 첫 등록 일 경우 처리 */
        if (fileOid == null) {
            cmsFile = new CmsFile();
            cmsFile.setName(name);
            cmsFileMapper.insertCmsFile(cmsFile);
            cmsFile.getOid();
        }
        /* 파일 수정 일 경우 처리 */
        else {
            cmsFile = getCmsFIle(fileOid);
            if (cmsFile == null) {
                throw new Exception("CMS 파일정보 오류!!");
            }
            versionCode = cmsFile.getLastVersionCode() + 1;
        }

        // Nas Insert
        CmsFileRevision cmsFileRevision = new CmsFileRevision(cmsFile.getOid(), versionCode);
        File file1 = writeFileToNas(file, cmsFileRevision);

        // CMS File Revision Insert
        cmsFileRevision.setCmsInfo(cmsFileRevision.toMetadataString());
        cmsFileRevisionMapper.insertCmsFileRevision(cmsFileRevision);

        // CMS File Update
        cmsFile.setLastVersionCode(cmsFileRevision.getVersionCode());
        cmsFile.setCmsInfo(cmsFileRevision.getCmsInfo());
        cmsFileMapper.updateCmsFile(cmsFile);

        return cmsFileRevision;
    }

    /**
     * 파일 수정 시 기존 파일 정보 조회
     *
     * @param oid
     * @return
     */
    private CmsFile getCmsFIle(String oid) {
        List<CmsFile> list = cmsFileMapper.selectCmsFiles(new CmsFile(oid));
        if (list.size() < 1) {
            return null;
        }
        return list.get(0);
    }

    /**
     * writeFileToNas (MultipartFile)
     *
     * @param 
     * @return
     */
    private File writeFileToNas(MultipartFile file, final CmsFileRevision cmsFileRevision) throws Exception {
        String targetFilePath = nasFileManageService.saveFilePath(cmsFileRevision.getFileOid());
        File targetFile = nasFileManageService.saveFile(file, targetFilePath);

        Tika tika = new Tika();
        String mineType = tika.detect(targetFile);

        log.debug("mineType = " + mineType);

        if(mineType.contains("video")){
        	cmsFileRevision.addMetadata(CDN_URL, nasFileManageService.getVideoDownloadUrl(targetFilePath + "/" + targetFile.getName()));
        } else{
        	cmsFileRevision.addMetadata(CDN_URL, nasFileManageService.getDownloadUrl(targetFilePath + "/" + targetFile.getName()));
        }

        log.info("writeFileToNas 저장 : {}, {}", targetFile.getPath(), cmsFileRevision.getMetadata(CDN_URL, String.class));

        cmsFileRevision.setMineType(mineType);
        cmsFileRevision.setDiskFilename(targetFile.getName());
        cmsFileRevision.setFileSize(targetFile.length());
        cmsFileRevision.setFilePath(targetFilePath);
   
        return targetFile;
    }
   
    /**
     * writeFileToNas (File)
     *
     * @param 
     * @return
     */
    private File writeFileToNas(File file, final CmsFileRevision cmsFileRevision) throws Exception {
        String targetFilePath = nasFileManageService.saveFilePath(cmsFileRevision.getFileOid());
        File targetFile = nasFileManageService.saveFile(file, targetFilePath);

        Tika tika = new Tika();
        String mineType = tika.detect(targetFile);

        log.debug("mineType = " + mineType);

        if(mineType.contains("video")){
            cmsFileRevision.addMetadata(CDN_URL, nasFileManageService.getVideoDownloadUrl(targetFilePath + "/" + targetFile.getName()));
        } else{
            cmsFileRevision.addMetadata(CDN_URL, nasFileManageService.getDownloadUrl(targetFilePath + "/" + targetFile.getName()));
        }

        log.info("writeFileToNas 저장 : {}, {}", targetFile.getPath(), cmsFileRevision.getMetadata(CDN_URL, String.class));

        cmsFileRevision.setMineType(mineType);
        cmsFileRevision.setDiskFilename(targetFile.getName());
        cmsFileRevision.setFileSize(targetFile.length());
        cmsFileRevision.setFilePath(targetFilePath);
   
        return targetFile;
    }

    /**
     * 파일 URL 다운로드
     *
     * @param fileOid
     * @param versionCode
     * @param isNas
     * @return
     */
    public String getDownloadUrl(String fileOid, int versionCode, boolean isNas) {
        String cmsinfo = cmsFileRevisionMapper.selectCmsFileUrl(fileOid, versionCode);
        return getURL(cmsinfo, isNas);
    }


    public String getURL(String cmsInfo, boolean isNas) {
        if (cmsInfo == null || cmsInfo.isEmpty()) {
            return null;
        }
        return JSONObject.fromObject(cmsInfo).getString(isNas ? CDN_URL : ALLNG_CMS_URL);

    }

    /**
     * 파일 최종 이력 조회(파일명 조회시 사용)
     * @param oid
     * @return
     */
    public CmsFileRevision getMaxCmsFileRevision(String fileOid){

    	CmsFileRevision maxFileRevision = cmsFileRevisionMapper.selectMaxFileRevision(fileOid);

    	return maxFileRevision;
    }
}
