package edu.progmatic.messenger.security;

import edu.progmatic.messenger.modell.User;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Authotity")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long authorityId;

    @Column(name = "authoritiyName")
    private String authorityName;

    @ManyToMany
    private Set<User> userDataSet;

    public Authority(Long authorityId, String authorityName, Set<User> userDataSet) {
        this.authorityId = authorityId;
        this.authorityName = authorityName;
        this.userDataSet = userDataSet;
    }

    public Authority() {
    }

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public Set<User> getUserDataSet() {
        return userDataSet;
    }

    public void setUserDataSet(Set<User> userDataSet) {
        this.userDataSet = userDataSet;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}