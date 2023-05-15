package com.testing.a2z.identity.adapter.out.persistence;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private UUID id;

    @Column(unique = true)
    private String username;

    // NOTE: Inlined for simplicity, alternatively we could use @Embedded or @OneToOne
    private String passwordSalt;

    private String passwordHash;

}
