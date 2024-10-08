package com.example.useraccountmanager.model;

import com.example.useraccountmanager.tools.UserStatusEnum;
import com.example.useraccountmanager.tools.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "firebase_uid")
    private String firebaseUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRoleEnum userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Account> accounts = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatusEnum userStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    private void prePersist() {
        LocalDateTime date = LocalDateTime.now();
        this.createdAt = date;
        this.updatedAt = date;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

}
