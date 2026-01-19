package start.students.core.domain.entities;

import start.students.core.domain.valueobjects.CPF;
import start.students.core.domain.valueobjects.Email;
import start.students.core.domain.valueobjects.Status;
import start.students.core.domain.valueobjects.StudentId;

import java.time.LocalDateTime;

public class Student {
    private final StudentId id;
    private final String matricula;
    private String nomeCompleto;
    private Email email;
    private final CPF cpf;
    private String telefone;
    private String foto;
    private Status status;
    private final LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private boolean deletado;

    private Student(String matricula, String nomeCompleto, Email email,
                    CPF cpf, String telefone, String foto) {
        this.id = new StudentId();
        this.matricula = matricula;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.foto = foto;
        this.status = Status.ATIVO;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.deletado = false;
    }

    public static Student criar(String matricula, String nomeCompleto,
                                String email, String cpf, String telefone, String foto) {
        return new Student(matricula, nomeCompleto, new Email(email),
                new CPF(cpf), telefone, foto);
    }

    public void atualizar(String email, String telefone, Status status, String foto) {
        this.email = new Email(email);
        this.telefone = telefone;
        this.status = status;
        this.foto = foto;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void deletar() {
        this.deletado = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void restaurar() {
        this.deletado = false;
        this.atualizadoEm = LocalDateTime.now();
    }


    public StudentId getId() { return id; }
    public String getMatricula() { return matricula; }
    public String getNomeCompleto() { return nomeCompleto; }
    public Email getEmail() { return email; }
    public CPF getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getFoto() { return foto; }
    public Status getStatus() { return status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public boolean estaDeleted() { return deletado; }
}
