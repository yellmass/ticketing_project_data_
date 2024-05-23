package com.cydeo.repository;

import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTaskStatusNot(Status taskStatus);
    List<Task> findAllByTaskStatus(Status taskStatus);

    @Query("select t from Task t where t.project.id=?1 and t.taskStatus!='COMPLETE'")
    List<Task> totalNonCompletedTasks(Long projectId);
    @Query(value = "select * from tasks where project_id=?1 and task_status='COMPLETE'",nativeQuery = true)
    List<Task> totalCompletedTasks(Long projectId);

    List<Task> findAllByProject_Id(Long projectId);
}
