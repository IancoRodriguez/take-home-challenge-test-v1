package takehomechallenge.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = true, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // JPA needs
    public User() {}

    // Ctor for factory
    // ============================================

    private User(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public static User create(String email, String rawPassword) {
        // Validate email
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email no puede estar vacío");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email debe ser válido");
        }

        // Validate password
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password no puede estar vacío");
        }
        if (rawPassword.length() < 6) {
            throw new IllegalArgumentException("Password debe tener al menos 6 caracteres");
        }

        // Create user
        return new User(email.toLowerCase().trim(), rawPassword);
    }

    // Setter has password
    public void setPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // equals y hashCode by ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }



}
