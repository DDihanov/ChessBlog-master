package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;
import softuniBlog.entity.Article;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.repository.TagRepository;
import softuniBlog.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Austin on 13/12/2016.
 */

@Controller
public class SearchController{

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value="/search")
    public String searchArticle(@RequestParam(value = "keywords") String keywords,@RequestParam(value="type") String type,
                         Model model, HttpServletRequest request) {

        String query = request.getParameter("keywords").trim();

        List<Article> articles = articleRepository.findAll();


        switch(type){
            case"tag":
                articles = articles.stream()
                        .filter(a->a.getTags().contains(StringUtils.capitalize(query.split("\\s+"))))
                        .collect(Collectors.toList());
                break;
            case"content":
                articles = articles.stream()
                    .filter(a->a.getContent().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
                break;
            case"category":
                articles = articles.stream()
                        .filter(a->a.getCategory().getName().equalsIgnoreCase(query))
                        .collect(Collectors.toList());
                break;
            case"title":
                articles = articles.stream()
                        .filter(a->a.getTitle().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());
                break;
            case"user":
                articles = articles.stream()
                        .filter(a->a.getAuthor().getFullName().equalsIgnoreCase(query))
                        .collect(Collectors.toList());
            default:
                articles = articles.stream()
                        .filter(a->a.getTitle().toLowerCase().contains(query.toLowerCase()))
                        .collect(Collectors.toList());
                break;
        }
        model.addAttribute("view", "search/search");
        model.addAttribute("query", query);
        model.addAttribute("articles", articles);

        return "base-layout";
    }

    public enum Categories{
        TAG, TITLE, CONTENT, CATEGORY,
    }
}
