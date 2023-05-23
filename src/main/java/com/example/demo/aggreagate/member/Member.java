package com.example.demo.aggreagate.member;

import com.example.demo.base.Role;
import com.example.demo.base.convert.CustomConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String Test_id;

    @Convert(converter = CustomConverter.class)
    @Builder.Default
    private Set<Role> authorities = new HashSet<>();

    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(i -> new SimpleGrantedAuthority("ROLE_"+i.name())).toList();
    }

    public void addRole(Role role) {
        this.authorities.add(role);
    }
}
