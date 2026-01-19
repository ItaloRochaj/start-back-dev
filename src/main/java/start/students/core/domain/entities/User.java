package start.students.core.domain.entities;

import start.students.core.domain.valueobjects.Email;

public class User {

    private final String id;
    private final String username;
    private final String password;
    private final Email email;
    private final boolean ativo;

    public User(String id, String username, String password, Email email, boolean ativo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.ativo = ativo;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Email getEmail() { return email; }
    public boolean isAtivo() { return ativo; }
}