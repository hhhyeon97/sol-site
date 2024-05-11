package com.sol.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

     var result = memberRepository.findByUsername(username);
     if(result.isEmpty()){
         throw new UsernameNotFoundException("해당 아이디 없음");
     }
        var user = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();


        if (user.getRole().equals("ROLE_ADMIN")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }

        var a = new CustomUser(user.getUsername(), user.getPassword(),authorities);
        a.displayName = user.getDisplayName();
        a.userId = user.getId();
        return a;
    }

}


