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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.*;
import softuniBlog.repository.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Austin on 23/11/2016.
 */

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    CommentRepository commentRepository;

    private HashSet<Tag> findTagsFromString(String tagString) {
        HashSet<Tag> tags = new HashSet<>();

        String[] tagNames = tagString.split(",\\s*");

        for (String tagName :
                tagNames) {
            Tag currentTag = this.tagRepository.findByName(tagName);

            if (currentTag == null) {
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }

            tags.add(currentTag);
        }

        return tags;
    }

    @GetMapping("article/create")
    @PreAuthorize("isAuthenticated()")
    public String createArticle(Model model) {
        model.addAttribute("view", "article/create");
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("categories", categories);
        return "base-layout";
    }

    private boolean isUserAuthorOrAdmin(Article article) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }

    @PostMapping("article/create")
    @PreAuthorize("isAuthenticated()")
    public String createArticleProcess(@Valid ArticleBindingModel articleBindingModel, BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors());
            return "redirect:/article/create";
        }
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();


        User userEntity = this.userRepository.findByEmail(principal.getUsername());
        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());
        LinkedHashSet<Comment> comments = new LinkedHashSet<>();

        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getPgn(),
                articleBindingModel.getContent(),
                userEntity,
                category,
                tags,
                comments
        );

        this.articleRepository.saveAndFlush(articleEntity);

        return "redirect:/";
    }

    @GetMapping("article/{id}")
    public String articleDetails(Model model, @PathVariable Integer id) {

        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        if (!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)) {
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            User userEntity = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", userEntity);
        }


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Article article = this.articleRepository.findOne(id);


        List<Comment> comments = this.articleRepository.findOne(id).getComments()
                .stream().sorted((c1,c2)->compareDate(c2.getLocalDateTime(),c1.getLocalDateTime()))
                .collect(Collectors.toList());


        model.addAttribute("view", "article/details");
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("pgn", article.getPgn());

        return "base-layout";
    }

    public static int compareDate(String sdate1, String sdate2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(sdate1);
            Date date2 = simpleDateFormat.parse(sdate2);
            return date1.compareTo(date2);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1;
    }

    @GetMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editArticle(@PathVariable Integer id, Model model) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }


        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        List<Category> categories = this.categoryRepository.findAll();

        String tagString = article.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));


        model.addAttribute("tags", tagString);
        model.addAttribute("categories", categories);
        model.addAttribute("view", "article/edit");
        model.addAttribute("article", article);

        return "base-layout";
    }


    @PostMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editArticleProcess(@Valid ArticleBindingModel articleBindingModel, BindingResult bindingResult, @PathVariable Integer id
            , RedirectAttributes redirectAttributes) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors());
            return "redirect:/article/edit/" + id;
        }

        Article article = this.articleRepository.findOne(id);


        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        Category category = this.categoryRepository.findOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());


        article.setTags(tags);
        article.setCategory(category);
        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());
        article.setPgn(articleBindingModel.getPgn());

        this.articleRepository.saveAndFlush(article);

        return "redirect:/article/" + article.getId();
    }

    @GetMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteArticle(@PathVariable Integer id, Model model) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);


        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }

        model.addAttribute("view", "article/delete");
        model.addAttribute("article", article);

        return "base-layout";
    }


    @PostMapping("article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteArticleProcess(@PathVariable Integer id) {
        if (!this.articleRepository.exists(id)) {
            return "redirect:/";
        }

        Article article = this.articleRepository.findOne(id);

        if (!isUserAuthorOrAdmin(article)) {
            return "redirect:/article/" + id;
        }
        this.articleRepository.delete(article);

        return "redirect:/";
    }
}
