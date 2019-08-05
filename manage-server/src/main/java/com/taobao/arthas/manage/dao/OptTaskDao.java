/*package com.taobao.arthas.manage.dao;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OptTaskDao extends JpaRepository {

	@Query("select t from OptTaskDo t where appConnectionId = :appConnectionId and taskStatusCode = :taskStatusCode")
	List<OptTaskDo> getByConnectionIdAndStatus(@Param("appConnectionId") Long appConnectionId, @Param("taskStatusCode") Integer taskStatusCode);
	
	@Transactional
	@Modifying
	@Query("update OptTaskDo set taskStatusCode = :taskStatusCode where id = :id")
	int updateStatus( @Param("id") Long id, @Param("taskStatusCode")  Integer taskStatusCode);

}
*/