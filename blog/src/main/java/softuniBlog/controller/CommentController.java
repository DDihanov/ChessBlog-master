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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.CommentBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Comment;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CommentRepository;
import softuniBlog.repository.UserRepository;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Austin on 16/12/2016.
 */



@Controller
public class CommentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    ArticleRepository articleRepository;

    private boolean isUserAuthorOrAdmin(Comment comment){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(comment);
    }

    @GetMapping("comments/{id}/addComment")
    @PreAuthorize("isAuthenticated()")
    public String addComment(Model model, @PathVariable Integer id){

        if(!this.articleRepository.exists(id)){
            return "redirect:/";
        }

        if(!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)){
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            User userEntity = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", userEntity);
        }

        Article article = this.articleRepository.findOne(id);
        Set<Comment> comments = this.articleRepository.findOne(id).getComments();

        model.addAttribute("comments", comments);
        model.addAttribute("view", "comments/addComment");
        model.addAttribute("article", article);

        return "base-layout";
    }

    @PostMapping("comments/{id}/addComment")
    @PreAuthorize("isAuthenticated()")
    public String addCommentProcess(@PathVariable Integer id, @Valid CommentBindingModel commentBindingModel, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors());
            return "redirect:/comments/{id}/addComment#scroll";
        }


        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User userEntity = this.userRepository.findByEmail(principal.getUsername());
        Article article = this.articleRepository.findOne(id);
        HashSet<Integer> likes = new HashSet<>();
        HashSet<Integer> dislikes = new HashSet<>();

        Comment comment = new Comment(commentBindingModel.getTitle(),
                commentBindingModel.getContent(),
                userEntity,
                commentBindingModel.getLocalDateTime(),
                article,
                likes,
                dislikes);

        this.commentRepository.saveAndFlush(comment);

        return"redirect:/article/" + id;
    }

    @GetMapping("comments/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editComment(@PathVariable Integer id, Model model){
        if (!this.commentRepository.exists(id)) {
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)) {
            return "redirect:/comments/edit" + id;
        }

        model.addAttribute("view", "comments/editComment");
        model.addAttribute("comment", comment);

        return "base-layout";
    }

    @PostMapping("comments/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editCommentProcess(@PathVariable Integer id,
                                     @Valid CommentBindingModel commentBindingModel,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors());
            return "redirect:/comments/edit/" + id;
        }

        Comment comment = this.commentRepository.findOne(id);


        if (!isUserAuthorOrAdmin(comment)) {
            return "redirect:/comments/edit" + id;
        }

        String newTitle = commentBindingModel.getTitle();
        String newContent = commentBindingModel.getContent();

        if(!newTitle.equals(comment.getTitle()) || !newContent.equals(comment.getContent())){
            comment.setContent(newContent);
            comment.setTitle(newTitle);
            comment.setLastEditDate(commentBindingModel.getLocalDateTime());

            this.commentRepository.saveAndFlush(comment);
        }

        return "redirect:/article/" + comment.getArticle().getId();
    }


    @GetMapping("comments/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(Model model, @PathVariable Integer id){

        if (!this.commentRepository.exists(id)) {
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);

        if (!isUserAuthorOrAdmin(comment)) {
            return "redirect:/article/" + comment.getArticle().getId();
        }

        model.addAttribute("comment", comment);
        model.addAttribute("view", "comments/deleteComment");
        model.addAttribute("article", comment);

        return "base-layout";
    }

    @PostMapping("comments/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteCommentProcess(@PathVariable Integer id){
        if (!this.commentRepository.exists(id)) {
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);
        Integer articleId = comment.getArticle().getId();

        if (!isUserAuthorOrAdmin(comment)) {
            return "redirect:/article/" + articleId;
        }

        this.commentRepository.delete(comment);

        return "redirect:/article/" + articleId;
    }

    @RequestMapping("article/comments/like/{id}")
    @PreAuthorize("isAuthenticated()")
    public String likeProcess(@PathVariable Integer id){
        if (!this.commentRepository.exists(id)) {
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);
        Integer articleId = comment.getArticle().getId();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Integer userId = this.userRepository.findByEmail(principal.getUsername()).getId();

        comment.getLikes().add(userId);
        comment.getDislikes().remove(userId);

        this.commentRepository.saveAndFlush(comment);
        return "redirect:/article/" + articleId+"#"+id;
    }

    @RequestMapping("article/comments/dislike/{id}")
    @PreAuthorize("isAuthenticated()")
    public String dislikeProcess(@PathVariable Integer id){
        if (!this.commentRepository.exists(id)) {
            return "redirect:/";
        }

        Comment comment = this.commentRepository.findOne(id);
        Integer articleId = comment.getArticle().getId();
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Integer userId = this.userRepository.findByEmail(principal.getUsername()).getId();

        comment.getDislikes().add(userId);
        comment.getLikes().remove(userId);

        this.commentRepository.saveAndFlush(comment);
        return "redirect:/article/" + articleId + "#" +id;
    }
}
