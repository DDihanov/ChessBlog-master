package softuniBlog.bindingModel;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Austin on 23/11/2016.
 */
public class ArticleBindingModel {
    @NotBlank(message = "Title cannot be empty!")
    @Size(min = 3, message = "Title must be more than 3 characters.")
    private String title;

    @NotBlank(message = "Post Content cannot be empty!")
    @Size(min = 50, message = "Post content must be more than 50 characters.")
    private String content;

    @Pattern(regexp = "(?<pgnGame>\\s*(?:\\[\\s*(?<tagName>\\w+)\\s*\"(?<tagValue>[^\"]*)\"\\s*\\]\\s*)+(?:(?<moveNumber>\\d+)(?<moveMarker>\\.|\\.{3})\\s*(?<moveValue>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?)(?:\\s*(?<moveValue2>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?))?\\s*(?:\\(\\s*(?<variation>(?:(?<varMoveNumber>\\d+)(?<varMoveMarker>\\.|\\.{3})\\s*(?<varMoveValue>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?)(?:\\s*(?<varMoveValue2>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?))?\\s*(?:\\((?<varVariation>.*)\\)\\s*)?(?:\\{(?<varComment>[^\\}]*?)\\}\\s*)?)*)\\s*\\)\\s*)*(?:\\{(?<comment>[^\\}]*?)\\}\\s*)?)*(?<endMarker>1\\-?0|0\\-?1|1\\/2\\-?1\\/2|\\*)?\\s*)|(^$)",
            message = "Please Enter Valid PGN.")
    private String pgn;

    private Integer categoryId;

    @NotBlank(message = "Please enter at least one tag.")
    private String tagString;

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getPgn() {
        return pgn;
    }

    public void setPgn(String pgn) {
        this.pgn = pgn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
