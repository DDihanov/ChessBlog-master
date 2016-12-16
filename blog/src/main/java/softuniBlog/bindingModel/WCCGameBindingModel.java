package softuniBlog.bindingModel;

import javax.validation.constraints.Pattern;

/**
 * Created by Austin on 23/11/2016.
 */
public class WCCGameBindingModel {

    @Pattern(regexp = "(?<pgnGame>\\s*(?:\\[\\s*(?<tagName>\\w+)\\s*\"(?<tagValue>[^\"]*)\"\\s*\\]\\s*)+(?:(?<moveNumber>\\d+)(?<moveMarker>\\.|\\.{3})\\s*(?<moveValue>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?)(?:\\s*(?<moveValue2>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?))?\\s*(?:\\(\\s*(?<variation>(?:(?<varMoveNumber>\\d+)(?<varMoveMarker>\\.|\\.{3})\\s*(?<varMoveValue>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?)(?:\\s*(?<varMoveValue2>(?:[PNBRQK]?[a-h]?[1-8]?x?[a-h][1-8](?:\\=[PNBRQK])?|O(-?O){1,2})[\\+#]?(\\s*[\\!\\?]+)?))?\\s*(?:\\((?<varVariation>.*)\\)\\s*)?(?:\\{(?<varComment>[^\\}]*?)\\}\\s*)?)*)\\s*\\)\\s*)*(?:\\{(?<comment>[^\\}]*?)\\}\\s*)?)*(?<endMarker>1\\-?0|0\\-?1|1\\/2\\-?1\\/2|\\*)?\\s*)",
            message = "Please Enter Valid PGN.")
    private String pgn;

    public String getPgn() {
        return pgn;
    }

    public void setPgn(String pgn) {
        this.pgn = pgn;
    }

}
