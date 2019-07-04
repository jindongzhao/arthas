package com.taobao.arthas.manage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taobao.arthas.manage.dao.domain.OptTaskDo;

public interface OptTaskDao extends JpaRepository<OptTaskDo, Long> {

	List<OptTaskDo> getByAppId(@Param("appId") String appId);
	
	@Modifying
	@Query("update OptTaskDo set taskStatusCode = ?2 where id = ?1")
	int updateStatus(Long id, Integer taskStatusCode);

}
