package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.entity.Tag;
import softuniBlog.repository.TagRepository;

/**
 * Created by Austin on 06/12/2016.
 */

@Controller
public class TagController{

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/tag/{name}")
    public String articlesWithTag(Model model, @PathVariable String name) {
        Tag tag = this.tagRepository.findByName(name);

        if (tag == null) {
            return "redirect:/";
        }

        model.addAttribute("view", "tag/articles");
        model.addAttribute("tag", tag);

//        List<Tag> tags = this.tagRepository.findAll().stream().sorted(Comparator.comparing(a->a.getArticles().size())).collect(Collectors.toList());
//        List<Tag> tags = this.tagRepository.findAll().stream().sorted((a,b)->
//                Integer.valueOf(a.getArticles().size()).compareTo(Integer.valueOf(b.getArticles().size()))).collect(Collectors.toList());
        return "base-layout";
    }
}
