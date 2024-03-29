package nl.workingtalent.WTAcademy.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
public class RoleController {
	
	@Autowired
	private RoleService service;
	
	// READ
	@RequestMapping("role/all")
	public List<Role> allRoles() {
		return service.findAllRoles();
	}
	
	@RequestMapping("role/{id}")
	public Optional<Role> findRoleById(@PathVariable("id") long id) {
		return service.findRoleWithId(id);
	}
	
	// CREATE
	@RequestMapping(method = RequestMethod.POST, value = "role/create")
	public void createRole(@RequestBody Role role) {
		service.create(role);
	}
}
