package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.WCCGameBindingModel;
import softuniBlog.entity.User;
import softuniBlog.entity.WCCGame;
import softuniBlog.repository.UserRepository;
import softuniBlog.repository.WCCRepository;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Austin on 23/11/2016.
 */

@Controller
public class WCCGamesController{

    @Autowired
    private WCCRepository wccRepository;

    @Autowired
    UserRepository userRepository;

    private boolean isUserAdmin(WCCGame game){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin();
    }


    @GetMapping("currentWccGames/currentWccGames")
    public String index(Model model) {
        List<WCCGame> wccGameList = this.wccRepository.findAll();

        if(!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)){
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            User userEntity = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", userEntity);
        }


        model.addAttribute("view", "currentWccGames/currentWccGames");
        model.addAttribute("games", wccGameList);

        return "base-layout";
    }

    @GetMapping("currentWccGames/add")
    @PreAuthorize("isAuthenticated()")
    public String addGame(Model model){

        model.addAttribute("view", "currentWccGames/add");

        return "base-layout";
    }

    @PostMapping("currentWccGames/add")
    @PreAuthorize("isAuthenticated()")
    public String addGameProcess(@Valid WCCGameBindingModel wccGameBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();


        User userEntity = this.userRepository.findByEmail(principal.getUsername());


        WCCGame wccGame = new WCCGame(wccGameBindingModel.getPgn(), userEntity);

        if(!isUserAdmin(wccGame)){
            return "redirect:/currentWccGames/";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors());
            return"redirect:/currentWccGames/add";
        }

        if(!this.wccRepository.exists(wccGame.getId())){
            redirectAttributes.addFlashAttribute("errors", "WCC Game Already Exists");
            return"redirect:/currentWccGames/add";
        }

        this.wccRepository.saveAndFlush(wccGame);

        return"redirect:/currentWccGames/currentWccGames";
    }

}
