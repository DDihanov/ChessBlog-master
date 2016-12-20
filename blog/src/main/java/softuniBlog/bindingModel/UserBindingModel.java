package softuniBlog.bindingModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserBindingModel {
    @NotNull(message = "Email cannot be empty!")
    @Pattern(regexp = "^[a-zA-Z]+[^@]*@[a-zA-Z]+[^@]*\\.[a-zA-Z]{2,}[^@]*$", message = "Please enter a valid E-mail address!")
    private String email;

    @NotNull(message = "Name cannot be empty!")
    private String fullName;

    @NotNull(message = "Password cannot be empty!")
    @Size(min = 5, message = "Password must be more than 5 characters.")
    private String password;

    @NotNull(message = "Please confirm password!")
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
