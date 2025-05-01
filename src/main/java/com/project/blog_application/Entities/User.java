package com.project.blog_application.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 4, message = "User name must be greater than or equal to 4 characters!!")
    private String name;

    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @Column(nullable = false)
    @NotEmpty
    @Size(min = 5, max = 10, message = "Password must be more than 4 characters and less than 11 characters!!")
    private String password;

    @Size(min = 5, message = "About section should contain at least 5 characters!!")
    private String about;
}
