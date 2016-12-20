package softuniBlog.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment{

    private Integer id;

    private String title;

    private String content;

    private User author;

    private String localDateTime;

    private Article article;

    private String lastEditDate;

    private Set<Integer> likes;

    private Set<Integer> dislikes;

    private boolean liked;

    private boolean disliked;


    @Transient
    public boolean isLiked(Integer id) {
        return this.likes.contains(id);
    }

    @Transient
    public boolean isDisliked(Integer id) {
        return this.dislikes.contains(id);
    }

    @Column(nullable = true)
    @ElementCollection(targetClass=Integer.class)
    public Set<Integer> getLikes() {
        return likes;
    }

    public void setLikes(Set<Integer> likes) {
        this.likes = likes;
    }

    @Column(nullable = true)
    @ElementCollection(targetClass=Integer.class)
    public Set<Integer> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Set<Integer> dislikes) {
        this.dislikes = dislikes;
    }

    @Column(nullable = true, columnDefinition = "text")
    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
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

    @Column(nullable = false, columnDefinition="text")
    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    @ManyToOne()
    @JoinColumn(name = "articleId", nullable = false)
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Comment() {
    }

    public Comment(String title, String content, User author, String localDateTime, Article article,
                   HashSet<Integer> likes, HashSet<Integer> dislikes) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.localDateTime = localDateTime;
        this.article = article;
        this.likes = likes;
        this.dislikes = dislikes;
    }
}
