package mx.cenidet.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptPassword {

	public static void main(String[] args) {
		var password = "1234";
		System.out.println("Password: " + password);
		System.out.println("Password encriptado: " + encriptarPassword(password));
	}

	public static String encriptarPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
}