package com.taobao.arthas.manage.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taobao.arthas.manage.dao.domain.OptTaskDo;

public interface OptTaskDao extends JpaRepository<OptTaskDo, Long> {

	@Query("select t from OptTaskDo t where appConnectionId = :appConnectionId and taskStatusCode = :taskStatusCode")
	List<OptTaskDo> getByConnectionIdAndStatus(@Param("appConnectionId") Long appConnectionId, @Param("taskStatusCode") Integer taskStatusCode);
	
	@Modifying
	@Query("update OptTaskDo set taskStatusCode = :id where id = :taskStatusCode")
	int updateStatus( @Param("id") Long id, @Param("taskStatusCode")  Integer taskStatusCode);

}
