package com.kau.dgscalender.service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kau.dgscalender.dao.PrivilegesDAO;
import com.kau.dgscalender.dao.UserDAO;
import com.kau.dgscalender.entity.Privileges;
import com.kau.dgscalender.entity.User;
import com.kau.dgscalender.exception.BusinessException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class JWTService {
	@Autowired
	private UserService userService;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PrivilegesDAO privilegesDAO;
	
	public String createJWT(Map<String, Object> headers, Map<String, String> claims) throws BusinessException {
		try {
			log.info("Enter createJWT Function...");
			// The JWT signature algorithm we will be using to sign the token
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

			String secertkey = "432a462d4a614e645267556b586e3272357538782f413f4428472b4b6250655368566d5971337336763979244226452948404d635166546a576e5a7234753777";

			// We will sign our JWT with our ApiKey secret
			byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secertkey);
			Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

			long nowMillis = System.currentTimeMillis();
			Date now = new Date(nowMillis);
			long expMillis = nowMillis +  getExpireTime(); // 3 hour 10800000 millesecond
			Date exp = new Date(expMillis);
			String jwtToken = Jwts.builder() // Build Token
					.setHeaderParams(headers)// Set headers
					.setClaims(claims)// Add claims
					.setExpiration(exp) // a java.util.Date
					.setIssuedAt(now) // add created time
					.signWith(signingKey) // Sign Token
					.compact(); //
			log.info("jwtToken: " + jwtToken);
			return jwtToken;
		} catch (Exception e) {
			throw new BusinessException("At  createJWT  Fun " + e.getMessage(), e);
		}

	}

	public Jws<Claims> decodeJWT(String jwt) throws BusinessException {
		try {
			log.info("Enter decodeJWT Function.with token =" + jwt);
			String secertkey = "432a462d4a614e645267556b586e3272357538782f413f4428472b4b6250655368566d5971337336763979244226452948404d635166546a576e5a7234753777";
            Jws<Claims> claim = Jwts.parserBuilder().setSigningKey(secertkey).build().parseClaimsJws(jwt);
			return claim;
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new BusinessException(e.getMessage(), e);
		}

	}
	
	private long getExpireTime() throws BusinessException {
		try {
			log.info("Enter getExpireTime Function...");
			String expireTime ="21600000";
			long time = Long.parseLong(expireTime);
			log.info("Expire Time In milleSeconds =" + time + " In mins = " + time / 60000);
			return time;
		} catch (Exception e) {
			throw new BusinessException("At  getExpireTime  Fun " + e.getMessage(), e);
		}
	}
	
	public String checkPrivilegesAccess(HttpServletRequest request, String userName) throws BusinessException {
		try {
			log.info("Enter checkPrivilegesAccess Function...");
			List<Privileges> systemPrivileges = new ArrayList<>();
			List<Privileges> parentSystemPrivileges = new ArrayList<>();

			// Get All Privilege of this User
			User user = userDAO.findByUsername(userName);
			systemPrivileges = getUserAllPrivileges(user);

			String backendurl = request.getRequestURI().split("/api/calender")[1];

			// Get Only First "/x" after "/admin or /merchant "
			int indexOfSlash;

			indexOfSlash = nthOccurrence(backendurl, "/", 3);

			if (indexOfSlash != -1)
				backendurl = backendurl.substring(0, indexOfSlash);

			// Fetch Privilege of this URL
			Privileges privilege = privilegesDAO.findByBackendUrl(backendurl);

//			log.info(systemPrivileges.stream().filter(item -> privilege.getCode().equals(item.getCode())).findAny()
//					.orElse(null));

			if ((privilege == null) || (systemPrivileges.stream()
					.filter(item -> privilege.getCode().equals(item.getCode())).findAny().orElse(null) == null))
				throw new BusinessException("Access Deined For this Privilage");
			return privilege.getCode();

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	public static int nthOccurrence(String str1, String str2, int n) {

		String tempStr = str1;
		int tempIndex = -1;
		int finalIndex = 0;
		for (int occurrence = 0; occurrence < n; ++occurrence) {
			tempIndex = tempStr.indexOf(str2);
			if (tempIndex == -1) {
				finalIndex = 0;
				break;
			}
			tempStr = tempStr.substring(++tempIndex);
			finalIndex += tempIndex;
		}
		return --finalIndex;
	}
	
	public List<Privileges> getUserAllPrivileges(User admin) throws BusinessException {
		try {
			log.info("Enter getUserAllPrivileges Function...");

			List<Privileges> systemPrivileges;

			systemPrivileges = privilegesDAO.findAllByUserIdOrderByPrivilegeorder(admin.getId());
			return systemPrivileges;

		} catch (Exception e) {
			throw new BusinessException("At  getAdminAllPrivileges  Fun " + e.getMessage(), e);
		}
	}
	

}
