package com.test.project.service.vo;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import net.sf.json.JSONObject;

@Component
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class CmsFileRevision {

    private String                        fileOid;
    private int                           versionCode;
    private String                        mineType;
    private String                        diskFilename;
    private long                          fileSize;
    private String                        filePath;
    private String                        cmsInfo;
    @JsonIgnore
    private Date                          createdOn;

    @JsonIgnore
    private LinkedHashMap<String, Object> metadata;

    @JsonIgnore
    private File                          file;

    public LinkedHashMap<String, Object> addMetadata(String key, Object value) {
        if (metadata == null) {
            metadata = new LinkedHashMap<String, Object>();
        }

        metadata.put(key, value);
        return metadata;
    }

    @SuppressWarnings("unchecked")
    public <E> E getMetadata(String key, Class<E> clazz) {
        if (metadata == null) {
            return null;
        }
        return ((E) metadata.get(key));
    }

    public boolean containsKeyMetadataKey(String key) {
        if (metadata == null) {
            return false;
        }
        return this.metadata.containsKey(key);
    }

    public String toMetadataString() {
        return JSONObject.fromObject(metadata).toString();
    }

    public CmsFileRevision() {}

    public CmsFileRevision(String fileOid, int versionCode) {
        this.fileOid = fileOid;
        this.versionCode = versionCode;
    }

    public String getUrl(){
    	if(cmsInfo != null)
    		return JSONObject.fromObject(cmsInfo).getString("cdn");

    	return null;
    }
}
