package com.test.project.service.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CmsFile {

    private String                oid;
    private String                name;
    private int                   lastVersionCode = 1;
    private String                cmsInfo;
    private Date                  createdOn;
    private Date                  updatedOn;
    private Date                  deletedOn;

    private CmsFileRevision       lastVersionFile;
    private List<CmsFileRevision> revisions;

    public CmsFile() {}

    public CmsFile(String oid) {
        this.oid = oid;
    }

    public void AddRevisions(CmsFileRevision file) {
        if (this.revisions == null) {
            this.revisions = new ArrayList<CmsFileRevision>();
        }

        file.setFileOid(this.oid);

        this.revisions.add(file);
        this.lastVersionFile = file;
    }

}
