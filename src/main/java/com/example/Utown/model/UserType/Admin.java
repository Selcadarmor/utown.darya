package com.example.Utown.model.UserType;

import com.example.Utown.model.Role;
import com.example.Utown.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

}
