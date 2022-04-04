package uz.pdp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import uz.pdp.entity.Worker;
import uz.pdp.entity.enums.RoleName;

public interface WorkerRepository extends JpaRepository<Worker, UUID> {
	
	boolean existsByRolesAndCompanyId(RoleName rolename, Integer id);
	
//	@Query(value="select * from worker k join worker_roles wk on k.id=wk.worker_id join role r on wk.roles_id=r.id where r.role_name=:roleName", nativeQuery = true)
//	List<Worker> getWorker(RoleName roleName);
	
	Optional<Worker> findByEmail(String email);
	
	boolean existsByEmailOrCompanyId(String email, Integer id);
	
	boolean existsByEmail(String email);
	
	@Query(value = "select w.full_name, w.company_id, w.email, tu.entry, tu.exit, ta.task_name,"
			+ "ta.data_for_finish_work, ta.taskstatus from worker w join task ta on w.id=ta.worker_id join company c on c.id=w.company_id "
			+ "join turmiket tu on tu.id=c.turniket_id ", nativeQuery = true)
	List<Worker> getworker(Integer workerId);
}
