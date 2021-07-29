package com.test.project.service.mapper.intf;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.test.project.service.vo.CmsFile;

@Repository
@Mapper
public interface CmsFileMapper {

	/* CMS파일 등록 */
    int insertCmsFile(CmsFile param);
    /* CMS파일 수정 */
    int updateCmsFile(CmsFile param);
    /* CMS파일 삭제 */
    void deleteCmsFile(int oid);
    /* CMS파일 목록 조회 */
    List<CmsFile> selectCmsFiles(CmsFile param);
}
