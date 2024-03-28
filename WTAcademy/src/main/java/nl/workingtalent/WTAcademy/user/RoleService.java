package nl.workingtalent.WTAcademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	
	@Autowired
	private IRoleRepository repository;
	
	// READ
	public List<Role> findAllRoles() {
		return repository.findAll();
	}
	
	public Optional<Role> findRoleWithId(long id) {
		return repository.findById(id);
	}

	// CREATE
	public void create(Role role) {
		repository.save(role);
	}
}
