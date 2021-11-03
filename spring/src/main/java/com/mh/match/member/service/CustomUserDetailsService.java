package com.mh.match.member.service;

import com.mh.match.member.repository.MemberRepository;
import com.mh.match.member.entity.CustomUserDetails;
import com.mh.match.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Member member = memberRepository.findByEmail(username)
//                .map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .id(Long.toString(member.getId()))
                .password(member.getPassword())
                .email(member.getEmail())
                .is_active(member.getIs_active())
                .banned(member.getBanned())
                .nickname(member.getNickname())
                .isCredentialsNonExpired(true)
                .authorities(Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().toString())))
                .build();
        return customUserDetails;
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}