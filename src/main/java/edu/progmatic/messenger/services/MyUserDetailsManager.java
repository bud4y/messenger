package edu.progmatic.messenger.services;

import edu.progmatic.messenger.modell.User;
import edu.progmatic.messenger.security.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;

@Service
public class MyUserDetailsManager implements UserDetailsManager {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MyUserDetailsManager my;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void createUser(UserDetails userDetails) {
        User user = new User();

        user.setUserName(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setEmail(((User) userDetails).getEmail());
        user.setBirthDate(((User) userDetails).getBirthDate());
        user.setAuthorities(new HashSet<>());
        user.addAuthority(my.getAuthority("ROLE_USER"));
        user.isEnabled();

        em.persist(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {

    }

    @Override
    public void deleteUser(String s) {

    }

    @Override
    public void changePassword(String s, String s1) {

    }

    @Override
    public boolean userExists(String s) {
        try {
            em.createQuery("select u from User u where u.userName = :user", User.class)
                    .setParameter("user", s).getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return em.createQuery("SELECT u FROM User u JOIN FETCH u.authorities WHERE u.userName = :user", User.class)
                .setParameter("user", name).getSingleResult();
    }

    @Transactional
    public Authority getAuthority(String authType) {
        return em.createQuery("SELECT a FROM Authority a WHERE a.authority = :authName ", Authority.class)
                .setParameter("authName", authType).getSingleResult();
    }
}
