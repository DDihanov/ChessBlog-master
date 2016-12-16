package softuniBlog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuniBlog.bindingModel.CategoryBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Austin on 06/12/2016.
 */

@Controller
@RequestMapping("/admin/categories")
public class CategoryController{
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String listCategories(Model model){
        model.addAttribute("view", "admin/category/list");

        List<Category> categories = this.categoryRepository.findAll();

        categories = categories.stream()
                    .sorted(Comparator.comparingInt(Category::getId))
                    .collect(Collectors.toList());

        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @GetMapping("/create")
    public String createCategory(Model model){
        model.addAttribute("view", "admin/category/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createCategoryProcess(@Valid CategoryBindingModel categoryBindingModel, BindingResult bindingResult,RedirectAttributes redirectAttributes){
            if(this.categoryRepository.findAll().stream().anyMatch(c -> c.getName().equals(categoryBindingModel.getName()))) {
                bindingResult.rejectValue("name", "errorCode", "Category already exists.");
            }
            if(bindingResult.hasErrors()){
                redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors());
                return "redirect:/admin/categories/create";
            }

        Category category = new Category(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editCategoryProcess(@PathVariable Integer id,
                              @Valid CategoryBindingModel categoryBindingModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        if(!this.categoryRepository.exists(id)) {
            bindingResult.rejectValue("category", "errorCode", "Category does not exist.");
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addAttribute("errors", bindingResult.getFieldErrors());
            return "redirect:/admin/categories/edit/"+id;
        }

        Category category = this.categoryRepository.findOne(id);

        category.setName(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }


    @GetMapping("/delete/{id}")
    public String deleteCategory(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategoryProcess(@PathVariable Integer id,
                              CategoryBindingModel categoryBindingModel){
        if(!this.categoryRepository.exists(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.findOne(id);

        for (Article article :
                category.getArticles()) {
            this.articleRepository.delete(article);
        }

        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";
    }
}
