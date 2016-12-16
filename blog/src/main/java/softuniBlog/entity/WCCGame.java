package softuniBlog.entity;

import javax.persistence.*;

@Entity
@Table(name = "WCCGames")
public class WCCGame {

    private Integer id;

    private String pgn;

    private User author;

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }


    public void setAuthor(User author) {
        this.author = author;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(columnDefinition = "text")
    public String getPgn() {
        return pgn;
    }

    public void setPgn(String pgn) {
        this.pgn = pgn;
    }

    public WCCGame() {
    }

    public WCCGame(String pgn, User author) {
        this.pgn = pgn;
        this.author = author;
    }
}
