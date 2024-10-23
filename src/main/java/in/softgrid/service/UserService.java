package in.softgrid.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.softgrid.entity.User;
import in.softgrid.repositary.userRepository;

@Service
public class UserService {

    @Autowired
    private userRepository userRepo;

    public void registerUser(User user) {
        String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepo.save(user);
    }

    public User validateUser(User user1) {
        User founduser = userRepo.findByEmail(user1.getEmail());
        if (founduser != null && PasswordUtil.checkPassword(user1.getPassword(), founduser.getPassword())) {
            return founduser; 
        }
        return null; 
    }
}


