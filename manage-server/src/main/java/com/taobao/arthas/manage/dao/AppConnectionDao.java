package com.taobao.arthas.manage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taobao.arthas.manage.dao.domain.AppConnectionDo;

public interface AppConnectionDao extends JpaRepository<AppConnectionDo, Long> {
	
	@Query("select a from AppConnectionDo a where appIp = :appIp and pid = :pid")
	AppConnectionDo getByAppipAndPid(@Param("appIp") String appIp, @Param("pid") Integer pid);
	

	@Modifying
	@Query("update AppConnectionDo set lastConnectionTime = :time where id = :id")
	AppConnectionDo updateConnectionTime( @Param("id") Long id,  @Param("time") Long time);
}
