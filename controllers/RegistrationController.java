import com.web_project.zayavki.models.ClientModel;
import com.web_project.zayavki.models.RoleEnum;
import com.web_project.zayavki.models.UserModel;
import com.web_project.zayavki.repository.UserRepository;
import com.web_project.zayavki.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    /**
     * UserRepository for create new user
     */
    @Autowired
    private UserRepository userRepository;
    /**
     * PassowrdEncoder for hash password
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * ApiService for work with api methods
     */
    @Autowired
    private ApiService apiService;
    /**
     * Method for view reg form
     * @return html page
     */
    @GetMapping("/registration")
    public String registrationView(){
        return "registration";
    }
    /**
     * Method for register user
     * @param user object of Class UserModel
     * @param model object of class Model
     * @return html login page
     */
    @PostMapping("/registration")
    public String registrationUser(UserModel user, Model model){
        if(userRepository.existsByUsername(user.getUsername())){
            model.addAttribute("message", "Пользователь уже существует");
            return "registration";

        }
        user.setActive(true);
        user.setRoles(Collections.singleton(RoleEnum.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setClient(new ClientModel());
        String json = apiService.setDataToApi("/user", user);
        return "redirect:/login";
    }
}
