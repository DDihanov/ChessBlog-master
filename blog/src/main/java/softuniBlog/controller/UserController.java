package softuniBlog.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.entity.Role;
import softuniBlog.entity.User;
import softuniBlog.repository.RoleRepository;
import softuniBlog.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class UserController{

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String registerUser(Model model) {
        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    @PostMapping("/register")
    public String registerUserProcess(@Valid UserBindingModel userBindingModel,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes){

        if(this.userRepository.findAll().stream().anyMatch(u->u.getEmail().equals(userBindingModel.getEmail()))){
            bindingResult.rejectValue("email", "messageCode", "User Email already taken.");
        }
        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            bindingResult.rejectValue("password", "messageCode", "Passwords must match");
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldError());
            return "redirect:/register";
        }


        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword())
        );

        Role userRole = this.roleRepository.findByName("ROLE_USER");

        user.addRole(userRole);

        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){

        model.addAttribute("view", "user/login");
        return "base-layout";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());


        model.addAttribute("user", user);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }


    @GetMapping("/profile/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String userEdit(Model model, @PathVariable Integer id){
        if(!this.userRepository.exists(id)){
            return "redirect:/profile";
        }
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());


        model.addAttribute("user", user);
        model.addAttribute("view", "user/edit");

        return "base-layout";
    }

    @PostMapping("/profile/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editUserProcess(@PathVariable Integer id,@Valid UserEditBindingModel userBindingModel,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes){
        if(!this.userRepository.exists(id)){
            return "redirect:/profile";
        }

        User user = this.userRepository.findOne(id);

        if(!StringUtils.isEmpty(userBindingModel.getPassword())
                && !StringUtils.isEmpty(userBindingModel.getConfirmPassword())){
            if(userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
            }
        }


        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            bindingResult.rejectValue("password", "messageCode", "Passwords must match");
        }

        if(userBindingModel.getPassword().isEmpty()){
            bindingResult.rejectValue("password", "messageCode", "Password cannot be left empty.");
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldError());
            return "redirect:/admin/users/edit/" + id;
        }

        user.setFullName(userBindingModel.getFullName());
        user.setEmail(userBindingModel.getEmail());

        this.userRepository.saveAndFlush(user);

        return "redirect:/profile";
    }
}

