package com.example.Utown.model.UserType;

import com.example.Utown.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;



@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {

}
