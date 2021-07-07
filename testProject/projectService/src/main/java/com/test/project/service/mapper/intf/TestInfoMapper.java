package com.test.project.service.mapper.intf;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestInfoMapper {

	HashMap<String, String> selectTest();
}
