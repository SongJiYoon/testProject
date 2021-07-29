package com.test.project.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.test.project.service.common.KyowonException;
import com.test.project.service.common.KyowonOS;
import com.test.project.service.common.Tools;

@Service
public class NasFileManageService {

    private final Logger          logger    = LoggerFactory.getLogger(getClass());

    private static final String[] IMAGE_EXT = { "jpg", "jpeg", "png", "bmp", "gif" };

    @Value("${nas.mount.path}")
    private String                nasMountPath;
    @Value("${nas.cdn.baseurl}")
    private String                nasCdnBaseURL;
    @Value("${nas.cdn.videobaseurl}")
    private String                nasCdnVideoBaseURL;
//    @Value("#{serviceProp['nas.upload.server.address']}")
//    private String                serverAddress;
//    @Value("#{serviceProp['nas.upload.server.userid']}")
//    private String                serverUserID;
//    @Value("#{serviceProp['nas.upload.server.password']}")
//    private String                serverPassword;
    
    public NasFileManageService() {}

//    private boolean sendSFtp(String localfile, String remotefile) {
//        JSch jsch = new JSch();
//        Session session = null;
//
//        try {
//            session = jsch.getSession(serverUserID, serverAddress, 22);
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.setPassword(serverPassword);
//            session.connect();
//
//            Channel channel = session.openChannel("sftp");
//            channel.connect();
//
//            ChannelSftp sftpChannel = (ChannelSftp) channel;
//
//            String[] pathArray = remotefile.split("/");
//            logger.debug("sendSFtp ... localfile = {}, remotefile = {}, pathArray Length = {}", localfile, remotefile, pathArray.length);
//
//            sftpChannel.cd("/");
//
//            for (int i = 1; i < pathArray.length - 1; i++) {
//                try {
//                    sftpChannel.mkdir(pathArray[i]);
//                    sftpChannel.cd(pathArray[i]);
//                } catch (Exception e) {
//                    sftpChannel.cd(pathArray[i]);
//                }
//            }
//
//            sftpChannel.put(localfile, remotefile);
//            sftpChannel.exit();
//            session.disconnect();
//
//        } catch (JSchException e) {
//            e.printStackTrace();
//            return false;
//        } catch (SftpException e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            if (session != null && session.isConnected()) {
//                session.disconnect();
//            }
//        }
//
//        return true;
//    }
    
    /**
     *  writeFile (MultipartFile)
     */
    private File writeFile(MultipartFile multipartFile, String targetFilePath) throws Exception {
        String dirPath = this.nasMountPath + targetFilePath;
        logger.debug("dirPath = " + dirPath);
        File dir = new File(dirPath);
        
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destFile = new File(dir.getPath() + "/" + multipartFile.getOriginalFilename().replace(" ", "_"));
        logger.debug("QQQQQQQQQQQQQQQQQQQQQQQ멈추지마!!");
        multipartFile.transferTo(destFile);
        logger.debug("QQQQQQQQQQQQQQQQQQQQQQQ통과통과통과통과");

        /*
        if ("local".equalsIgnoreCase(System.getProperty("serverType")) && this.serverAddress != null && !this.serverAddress.isEmpty()) {
            if (!sendSFtp(destFile.getAbsolutePath(), dirPath + "/" + destFile.getName())) {
                destFile.delete();
                throw new Exception("파일 생성 오류!!");
            }
        }*/

        //String nasFileUrl = getDownloadUrl(targetFilePath + "/" + destFile.getName());
        //logger.info("writeFile 저장 => {}, cdn url => {}", destFile.getPath(), nasFileUrl);

        return destFile;
    }
   
    /**
     *  writeFile (File)
     */
    private File writeFile(File file, String targetFilePath) throws Exception {
		String dirPath = this.nasMountPath + targetFilePath;
		logger.debug("copy Path: {}", dirPath);
		File dir = new File(dirPath);
		
		if (!dir.exists()) { dir.mkdirs(); }
		
		File destFile = new File(dir.getPath() + "/" + file.getName());
		
		logger.debug("org File: {}", file.getAbsolutePath());
		logger.debug("dest File: {}", destFile.getAbsolutePath());
		
		FileCopyUtils.copy(file, destFile);
		logger.debug("copy complet!");
		
		return destFile;
    }

    /**
     * 파일 복사
     *
     * @param tmpFile
     * @return
     * @throws Exception
     */
    public static void fileCopy(String inFileName, String outFileName) {
     try {
      FileInputStream fis = new FileInputStream(inFileName);
      FileOutputStream fos = new FileOutputStream(outFileName);
      
      int data = 0;
      while((data=fis.read())!=-1) {
       fos.write(data);
      }
      fis.close();
      fos.close();
      
     } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
     }
    }

   
    
    /**
     * 파일 저장 (MultipartFile)
     *
     * @param tmpFile
     * @return
     * @throws Exception
     */
    public File saveFile(MultipartFile multipartFile, final String targetFilePath) throws Exception {
        File file = this.writeFile(multipartFile, targetFilePath);
        return file;
    }
    
    /**
     * 파일 저장 (File)
     *
     * @param tmpFile
     * @return
     * @throws Exception
     */
    public File saveFile(File file, final String targetFilePath) throws Exception {
        File file1 = this.writeFile(file, targetFilePath);
        return file1;
    }

    
    /**
     * 파일 삭제
     *
     * @param nasFilePath
     * @throws KyowonException
     */
    public void deleteFile(String filePath) throws KyowonException {
        String path = nasMountPath + filePath.replaceAll(nasCdnBaseURL, "").trim();
        logger.debug("Delete File path => {}", path);

        KyowonOS.rmfile(path);
    }

    public void deleteFile(String filePath, String fileName) throws KyowonException {
        String path = nasMountPath + filePath.replaceAll(nasCdnBaseURL, "").replaceAll("/" + fileName, "");
        logger.debug("path => {}", path);

        KyowonOS.rmdir(path);
    }

    /**
     * CDN을 사용한 파일 다운로드 URL
     *
     * @param nasFilePath
     * @return
     */
    public String getDownloadUrl(String nasFilePath) {
        return nasCdnBaseURL + nasFilePath;
    }

    public String getVideoDownloadUrl(String nasFilePath) {
        return nasCdnVideoBaseURL + nasFilePath;
    }

    public String saveFilePath(String sequence) {
        String targetFilePath = String.format("/%s/%s/%s/%s", System.getProperty("serverType"), "SServerNm", Tools.getDateString(System.currentTimeMillis(), "yyyy/MM/dd/HH/mm"), Long.valueOf(System.currentTimeMillis()) + "_" + sequence);
        logger.debug("targetFilePath : {}", targetFilePath);
        return targetFilePath;
    }
}
