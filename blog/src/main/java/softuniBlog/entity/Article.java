package softuniBlog.entity;

import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {

    private Integer id;

    private String title;

    private String content;

    private String pgn;

    private User author;

    private Category category;

    private Set<Tag> tags;

    private Set<Comment> comments;

    @OneToMany(mappedBy = "article")
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @ManyToMany()
    @JoinColumn(table = "article_tags")
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "categoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(columnDefinition = "text")
    public String getPgn() {
        return pgn;
    }

    public void setPgn(String pgn) {
        this.pgn = pgn;
    }

    @Transient
    public String getSummary(){
        int pos = this.getContent().length();
        String text = this.getContent();
        for (int i = pos/2; i < pos; i++) {
            if(StringUtils.isEmptyOrWhitespace(String.valueOf(text.charAt(i)))){
                return text.substring(0, i) + "...";
            }
        }
        return this.getContent().substring(0, this.getContent().length()/2)+"...";
    }

    public Article() {
    }

    public Article(String title, String pgn, String content, User author, Category category, HashSet<Tag> tags, LinkedHashSet<Comment> comments) {
        this.pgn = pgn;
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.tags = tags;
        this.comments = comments;
    }
}
