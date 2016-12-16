package softuniBlog.bindingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Austin on 27/11/2016.
 */
public class UserEditBindingModel extends UserBindingModel{
    private List<Integer> roles;

    public UserEditBindingModel() {
        this.roles = new ArrayList<>();
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
}
