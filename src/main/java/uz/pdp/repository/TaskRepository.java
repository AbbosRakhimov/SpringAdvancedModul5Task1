package uz.pdp.repository;

import uz.pdp.entity.Task;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	boolean existsByTaskName(String taskName);
	@Query(value = " select * from task t join worker w on t.worker_id=:id where t.task_name=:taskName ", nativeQuery = true)
	Optional<Task> findTaskByWorkerIdAndTaskName(UUID id, String taskName);
}
