package softuniBlog.entity;

import javax.persistence.*;

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

    public Comment(String title, String content, User author, String localDateTime, Article article) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.localDateTime = localDateTime;
        this.article = article;
    }
}
