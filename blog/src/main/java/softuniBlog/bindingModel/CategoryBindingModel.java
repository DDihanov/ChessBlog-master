package softuniBlog.bindingModel;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Austin on 06/12/2016.
 */
public class CategoryBindingModel {

    @NotBlank(message = "Category cannot be empty.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
