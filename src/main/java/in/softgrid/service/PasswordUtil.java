package in.softgrid.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    private static final PasswordEncoder passwordEncoderr = new BCryptPasswordEncoder();

    // Hash a plain password
    public static String hashPassword(String plainPassword) {
        return passwordEncoderr.encode(plainPassword);
    }

    // Check if a plain password matches the hashed password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return passwordEncoderr.matches(plainPassword, hashedPassword);
    }
}

