package vn.com.buaansach.service.dto.auth;

import javax.validation.constraints.Size;

public class AdminPasswordChangeDTO {
    private String login;

    @Size(min = 4, max = 100)
    private String newPassword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "AdminChangePasswordDTO{" +
                "login='" + login + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
