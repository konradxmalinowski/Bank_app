import org.mindrot.jbcrypt.BCrypt;

public class Utils {
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
