package com.Comp_Emp_Manage.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.*;

import java.time.LocalDate;
import com.Comp_Emp_Manage.entity.UserAuth;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String department;
    private String designation;
    private LocalDate joiningDate;
    private Double salary;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean fresher = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_auth_id", referencedColumnName = "id", unique = true)
    private UserAuth userAuth;
}
