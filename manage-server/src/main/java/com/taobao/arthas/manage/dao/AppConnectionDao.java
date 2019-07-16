package com.taobao.arthas.manage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taobao.arthas.manage.dao.domain.AppConnectionDo;

public interface AppConnectionDao extends JpaRepository<AppConnectionDo, Long> {
	
	@Query("select * from AppConnectionDo where appIp = :appId and pid = :pid limit 1")
	AppConnectionDo getByAppipAndPid(@Param("appId") String appId, @Param("pid") Integer pid);
	

	@Modifying
	@Query("update AppConnectionDo set lastConnectionTime = :time where id = :id")
	AppConnectionDo updateConnectionTime( @Param("pid") Long id,  @Param("time") Long time);
}
