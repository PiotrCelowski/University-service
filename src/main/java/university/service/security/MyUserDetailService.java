package university.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import university.service.data.identity.UserRepository;
import university.service.domain.identity.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }

    public class MyUserPrincipal implements UserDetails {
        private UserEntity user;

        public MyUserPrincipal(UserEntity user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<GrantedAuthority> roles= new ArrayList<>();
            roles.add(new SimpleGrantedAuthority(user.getRole()));
            return roles;
        }

        @Override
        public String getPassword() {
            return user.getUserPassword();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    @PostConstruct
    void createSomeUsers() {
        BaseUserDecorator user = new Student(new BaseUser("user"));
        BaseUserDecorator admin = new Admin(new BaseUser("admin"));
        BaseUserDecorator worker = new Teacher(new BaseUser("worker"));

        userRepository.saveAndFlush(user.getBaseUser());
        userRepository.saveAndFlush(admin.getBaseUser());
        userRepository.saveAndFlush(worker.getBaseUser());
    }
}
