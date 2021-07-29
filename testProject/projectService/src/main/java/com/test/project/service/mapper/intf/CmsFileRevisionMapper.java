package com.test.project.service.mapper.intf;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.test.project.service.vo.CmsFileRevision;

@Repository
@Mapper
public interface CmsFileRevisionMapper {

	/* CMS파일 버전 등록*/
    int insertCmsFileRevision(CmsFileRevision param);
    /* CMS파일 버전 삭제 */
    void deleteCmsFileRevision(@Param("fileOid") String fileOid, @Param("versionCode") int versionCode);
    /* CMS파일 버전 상세 */
    List<CmsFileRevision> selectCmsFileRevisions(CmsFileRevision param);
    /* CMS파일 URL 조회 */
    String selectCmsFileUrl(@Param("fileOid") String fileOid, @Param("versionCode") int versionCode);
    /* CMS파일 최신버전 조회 */
    CmsFileRevision selectMaxFileRevision(@Param("fileOid") String fileOid);
}
