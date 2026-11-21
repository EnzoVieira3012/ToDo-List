package com.todo.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, UUID> {

    Optional<TaskJpaEntity> findById(UUID id);

    List<TaskJpaEntity> findByStatus(String status);

    List<TaskJpaEntity> findByPriority(String priority);

    List<TaskJpaEntity> findByStatusAndPriority(String status, String priority);

    @Query("SELECT t FROM TaskJpaEntity t WHERE t.status = :status")
    Page<TaskJpaEntity> findByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT t FROM TaskJpaEntity t WHERE t.priority = :priority")
    Page<TaskJpaEntity> findByPriority(@Param("priority") String priority, Pageable pageable);

    @Query("SELECT t FROM TaskJpaEntity t WHERE t.status = :status AND t.priority = :priority")
    Page<TaskJpaEntity> findByStatusAndPriority(@Param("status") String status, @Param("priority") String priority, Pageable pageable);

    @Query("SELECT t FROM TaskJpaEntity t")
    Page<TaskJpaEntity> findAllPaged(Pageable pageable);

    boolean existsById(UUID id);

    long count();

    long countByStatus(String status);
}