package com.example.demo.boundedContext.member.entity;

import com.example.demo.base.Role;
import com.example.demo.base.convert.CustomConverter;
import com.example.demo.base.entity.BaseEntity;
import com.example.demo.boundedContext.member.dto.MemberModifyDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE member SET delete_date = NOW() WHERE id = ?")
@Entity
public class Member extends BaseEntity {

    private String username;
    private String email;
    private String password;
    private String phoneNumber;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<MbtiTest> tests = new ArrayList<>();

    @Convert(converter = CustomConverter.class)
    @Builder.Default
    private Set<Role> authorities = new HashSet<>();

    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(i -> new SimpleGrantedAuthority("ROLE_" + i.name())).toList();
    }

    public void addRole(Role role) {
        this.authorities.add(role);
    }

}
