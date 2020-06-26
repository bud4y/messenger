package edu.progmatic.messenger.modell;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class RegDto {

        @NotNull
        @Size(min = 3)
        private String username;
        @NotNull
        @Size(min = 8)
        private String password;

        @NotNull
        @Size(min = 8)
        private String passwordConfirm;

        @NotNull
        @NotBlank
        @Email
        private String email;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthday;


    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPasswordConfirm() {
            return passwordConfirm;
        }

        public void setPasswordConfirm(String passwordConfirm) {
            this.passwordConfirm = passwordConfirm;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public LocalDate getBirthday() {
            return birthday;
        }

        public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
        }

    }
