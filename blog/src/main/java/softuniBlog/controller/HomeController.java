package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    WCCRepository wccRepository;

    @Autowired
    RoleRepository roleRepository;

//    @GetMapping("/")
//    public String index(Model model) {
//        List<Article> articles = this.articleRepository.findAll();
//
//        model.addAttribute("view", "home/index");
//        model.addAttribute("articles", articles);
//
//        return "base-layout";
//    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<Category> categories = this.categoryRepository.findAll();

        long totalUsers = this.userRepository.count();
        long totalArticles = this.articleRepository.count();
        long totalCategories = this.categoryRepository.count();

        session.setAttribute("totalUsers", totalUsers);
        session.setAttribute("totalArticles", totalArticles);
        session.setAttribute("totalCategories", totalCategories);

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/";
        }

        Category category = this.categoryRepository.findOne(id);
        Set<Article> articles = category.getArticles();

        model.addAttribute("articles", articles);
        model.addAttribute("category", category);
        model.addAttribute("view", "home/list-articles");

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        model.addAttribute("view", "error/403");

        return "base-layout";
    }
}
