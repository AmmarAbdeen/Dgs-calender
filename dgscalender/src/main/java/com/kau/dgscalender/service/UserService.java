package com.kau.dgscalender.service;

import java.security.SecureRandom;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import com.google.gson.Gson;
import com.kau.dgscalender.dao.PrivilegesDAO;
import com.kau.dgscalender.dao.UserDAO;
import com.kau.dgscalender.dto.PrivilegesDTO;
import com.kau.dgscalender.dto.UserDTO;
import com.kau.dgscalender.entity.Privileges;
import com.kau.dgscalender.entity.User;
import com.kau.dgscalender.exception.BusinessException;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class UserService extends BaseService {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PrivilegesDAO privilegesDAO;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private FreeMakerService freeMakerService;

	public UserDTO addUser(UserDTO request) throws BusinessException {
		try {
			log.info("Enter addUser Function..with Username= " + request.getUsername());
			if (request.getUsername() == null || request.getUsername().isEmpty()) {
				throw new BusinessException("Username is required");
			}
			if (request.getFullName() == null || request.getFullName().isEmpty()) {
				throw new BusinessException("Full Name is required");
			}
			if (request.getPassword() == null || request.getPassword().isEmpty()) {
				throw new BusinessException("Password is required");
			}
			if (request.getEmail() == null || request.getEmail().isEmpty()) {
				throw new BusinessException("Email is required");
			}

			User userAlreadyExist = userDAO.findByUsernameContainingIgnoreCase(request.getUsername());
			if (userAlreadyExist != null) {
				throw new BusinessException("This Username already Exist Please write another username");
			}

			userAlreadyExist = userDAO.findByEmail(request.getEmail());
			if (userAlreadyExist != null) {
				throw new BusinessException("This Email already Exist");
			}
			if (request.isAdmin()) {
				if (request.getPrivileges() == null)
					throw new BusinessException("At creation : no privileges chosen ");
			}

			User user = mapToEntity(request, request.isAdmin());
			user.setNotificationNumber(0);		
			// user = addDefaultPrivilegesToUser(user);
			userDAO.save(user);
			return request;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}
	@Async
	public void verifyToSendEmail(UserDTO request) throws BusinessException {
		try {
			log.info("Enter addUser Function..with Username= " + request.getUsername());
			if (request.getUsername() == null || request.getUsername().isEmpty()) {
				throw new BusinessException("Username is required");
			}
			if (request.getEmail() == null || request.getEmail().isEmpty()) {
				throw new BusinessException("Email is required");
			}

			User user = userDAO.findByUsernameAndEmail(request.getUsername(),request.getEmail());
			if (user == null) {
				throw new BusinessException("Check your username or email");
			}
			String password = generateRandomNumberWithLength(8);
			user.setPassword(password);
			userDAO.save(user);
			sendMailForPassword(user.getUsername(),password,"ammar.basuny@cashcall.com.eg");

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}
	
	private void sendMailForPassword(String username,String password,String toMail) {
		String formMail = "abdeenammar14@gmail.com";
		String subject = "UserName and Password";
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		String message = freeMakerService.mappingMessageForSendPasswordMail(username, password,"email_file_for_send_password");
		// log.info(message);
		mailMessage.setFrom(formMail);
		mailMessage.setTo(toMail);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		javaMailSender.send(mailMessage);
	}
	
	private  String generateRandomNumberWithLength(int len) {
		// ASCII range - alphanumeric (0-9)
		final String chars = "0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		return sb.toString();
	}

	public UserDTO editUser(UserDTO request) throws BusinessException {
		try {
			log.info("Enter addUser Function..with Username= " + request.getUsername());
			if (request.getUsername() == null || request.getUsername().isEmpty()) {
				throw new BusinessException("Username is required");
			}
			if (request.getFullName() == null || request.getFullName().isEmpty()) {
				throw new BusinessException("Full Name is required");
			}
			if (request.getEmail() == null || request.getEmail().isEmpty()) {
				throw new BusinessException("Email is required");
			}

			User userAlreadyExist = userDAO.findByUsernameContainingIgnoreCaseAndIdNot(request.getUsername(),
					request.getId());
			if (userAlreadyExist != null) {
				throw new BusinessException("This Username already Exist Please write another username");
			}

			userAlreadyExist = userDAO.findByEmailAndIdNot(request.getEmail(), request.getId());
			if (userAlreadyExist != null) {
				throw new BusinessException("This Email already Exist");
			}

			User user = userDAO.findById(request.getId()).get();
			user.setEmail(request.getEmail());
			user.setFullName(request.getFullName());
			user.setUsername(request.getUsername());
			userDAO.save(user);
			return request;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	public void changePassword(String username,UserDTO userDTO)
			throws BusinessException {
		try {

			log.info("Enter changePassword Function...with request " + userDTO);
			if (userDTO == null)
				throw new BusinessException("Missing data not found");

			if (userDTO.getPassword() == null)
				throw new BusinessException("Missing current Password");
			if (userDTO.getNewPassword() == null)
				throw new BusinessException("Missing new Password");
			if (userDTO.getConfirmPassword() == null)
				throw new BusinessException("Missing confirm Password");
			if (!(userDTO.getNewPassword().equals(userDTO.getConfirmPassword()))) {
				throw new BusinessException("Missing new password not equal confirm password");
			}

			User user = userDAO.findByUsernameAndPassword(username,userDTO.getPassword());
			if (user == null) {
				throw new BusinessException("This Password is not correct");
			}
			user.setPassword(userDTO.getNewPassword());
			userDAO.save(user);
			log.info("Returned successfully from changePassword ....");

			}catch(Exception e){
				throw new BusinessException("At  changePassword Fun " + e.getMessage(), e);
			}
	}

	public String getUserByUserName(String username) throws BusinessException {
		User user = userDAO.findByUsername(username);
		return new Gson().toJson(mapToDTO(user));

	}

	public String getAllUsers() throws BusinessException {
		try {
			List<User> users = userDAO.findAll();
			return new Gson().toJson(mapToDTOList(users));
		} catch (Exception e) {
			throw new BusinessException("At  getAllUsers Fun " + e.getMessage(), e);
		}
	}

	public User addDefaultPrivilegesToUser(User user) {
		if (user.isAdmin()) {
			user.setPrivilege(privilegesDAO.findAll());
		} else {
			user.setPrivilege(privilegesDAO.findAllByAdminPrivilege(false));
		}
		return user;
	}

	public String getAllPrivilegesParentWithChildren(Long userId) throws BusinessException {
		try {
			List<PrivilegesDTO> privilegesDTOs = mapToDTOPrivilegesList(
					privilegesDAO.findAllByUserIdOrderByPrivilegeorder(userId));
			List<PrivilegesDTO> parentprivileges = new ArrayList<>();

			for (PrivilegesDTO privilegeDTO : privilegesDTOs) {
				if (privilegeDTO.getParentPrivilegeId() == null) {
					privilegeDTO = getChildrenprivileges(privilegeDTO, privilegesDTOs);
					parentprivileges.add(privilegeDTO);
				}

			}

			return new Gson().toJson(parentprivileges);
		} catch (Exception e) {
			throw new BusinessException("At  getprivileges Fun " + e.getMessage(), e);
		}
	}

	public String getAllPrivileges(Long userId) throws BusinessException {
		try {
			List<PrivilegesDTO> privilegesDTOs = mapToDTOPrivilegesList(
					privilegesDAO.findAllByUserIdOrderByPrivilegeorder(userId));
			return new Gson().toJson(privilegesDTOs);
		} catch (Exception e) {
			throw new BusinessException("At  getprivileges Fun " + e.getMessage(), e);
		}

	}

	public PrivilegesDTO getChildrenprivileges(PrivilegesDTO privilegeDTO, List<PrivilegesDTO> privilegesDTOs)
			throws BusinessException {

		List<PrivilegesDTO> childrenprivileges = new ArrayList<>();
		for (PrivilegesDTO privilege : privilegesDTOs) {
			if (privilege.getParentPrivilegeId() != null
					&& privilege.getParentPrivilegeId().equals(privilegeDTO.getId())) {
				childrenprivileges.add(privilege);
			}
		}
		sortChildrenPrivileges(childrenprivileges);
		for (PrivilegesDTO childrenprivilege : childrenprivileges) {
			childrenprivilege = getChildrenprivileges(childrenprivilege, privilegesDTOs);
		}
		privilegeDTO.setChildrenPrivilege(childrenprivileges);
		return privilegeDTO;
	}

	private void sortChildrenPrivileges(List<PrivilegesDTO> childrenprivileges) {
		if (childrenprivileges.size() > 1) {
			Collections.sort(childrenprivileges,
					(childrenprivilege1, childrenprivilege2) -> (int) (childrenprivilege1.getPrivilegeorder()
							- childrenprivilege2.getPrivilegeorder()));
		}
	}

	public List<PrivilegesDTO> mapToDTOPrivilegesList(List<Privileges> privileges) {
		List<PrivilegesDTO> privilegesDTOs = new ArrayList<>();
		for (Privileges privilege : privileges) {
			privilegesDTOs.add(mapPrivilegToDTO(privilege));
		}
		return privilegesDTOs;
	}

	public List<UserDTO> mapToDTOList(List<User> users) {
		List<UserDTO> userDTOs = new ArrayList<>();
		for (User user : users) {
			userDTOs.add(mapToDTO(user));
		}
		return userDTOs;
	}

	public UserDTO mapToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		userDTO.setFullName(user.getFullName());
		userDTO.setEmail(user.getEmail());
		userDTO.setAdmin(user.isAdmin());
		userDTO.setNotificationNumber(user.getNotificationNumber());
		List<PrivilegesDTO> privilegesDTO = new ArrayList<>();
		for (Privileges privilege : user.getPrivilege()) {
			privilegesDTO.add(mapPrivilegToDTO(privilege));
		}
		userDTO.setPrivileges(privilegesDTO);

		return userDTO;
	}

	public PrivilegesDTO mapPrivilegToDTO(Privileges privilege) {
		PrivilegesDTO privilegeDTO = new PrivilegesDTO();
		privilegeDTO.setId(privilege.getId());
		privilegeDTO.setCode(privilege.getCode());
		privilegeDTO.setArabicName(privilege.getArabicName());
		privilegeDTO.setEnglishName(privilege.getEnglishName());
		privilegeDTO.setIconPath(privilege.getIconPath());
		privilegeDTO.setRouterLink(privilege.getRouterLink());
		privilegeDTO.setBackendUrl(privilege.getBackendUrl());
		privilegeDTO.setMainPrivilege(privilege.isMainPrivilege());
		privilegeDTO.setMenuItem(privilege.isMenuItem());
		privilegeDTO.setAdminPrivilege(privilege.isAdminPrivilege());
		privilegeDTO.setPrivilegeorder(privilege.getPrivilegeorder());
		privilegeDTO.setParentPrivilegeId(
				privilege.getParentPrivilege() != null ? privilege.getParentPrivilege().getId() : null);
		return privilegeDTO;
	}

	public User mapToEntity(UserDTO userDTO, boolean isAdmin) throws ParseException {
		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setFullName(userDTO.getFullName());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		user.setAdmin(userDTO.isAdmin());
		if (isAdmin) {
			List<Privileges> privileges = new ArrayList<>();
			for (PrivilegesDTO privilegesDTO : userDTO.getPrivileges()) {
				privileges.add(mapPrivilegDTOToEntity(privilegesDTO));
			}
			user.setPrivilege(privileges);
		} else {
			user.setPrivilege(privilegesDAO.findAllByAdminPrivilege(false));
		}

		return user;
	}

	public Privileges mapPrivilegDTOToEntity(PrivilegesDTO privilegeDTO) {
		Privileges privilege = new Privileges();
		privilege.setId(privilegeDTO.getId());
		privilege.setCode(privilegeDTO.getCode());
		privilege.setArabicName(privilegeDTO.getArabicName());
		privilege.setEnglishName(privilegeDTO.getEnglishName());
		privilege.setIconPath(privilegeDTO.getIconPath());
		privilege.setRouterLink(privilegeDTO.getRouterLink());
		privilege.setBackendUrl(privilegeDTO.getBackendUrl());
		privilege.setMainPrivilege(privilegeDTO.isMainPrivilege());
		privilege.setMenuItem(privilegeDTO.isMenuItem());
		privilege.setAdminPrivilege(privilegeDTO.isAdminPrivilege());
		privilege.setPrivilegeorder(privilegeDTO.getPrivilegeorder());
		if (privilegeDTO.getParentPrivilegeId() != null) {
			Privileges privileges = privilegesDAO.findById(privilegeDTO.getParentPrivilegeId()).get();
			privilege.setParentPrivilege(privileges);
		}
		return privilege;
	}

}
