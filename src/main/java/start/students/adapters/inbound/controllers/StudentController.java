package start.students.adapters.inbound.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import start.students.adapters.inbound.http.ApiResponse;
import start.students.core.application.dtos.*;
import start.students.core.application.usecases.*;
import start.students.core.domain.exceptions.DomainException;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;
    private final GetStudentDetailUseCase getStudentDetailUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    private final DeleteStudentUseCase deleteStudentUseCase;
    private final ListStudentsUseCase listStudentsUseCase;

    public StudentController(CreateStudentUseCase createStudentUseCase,
                           GetStudentDetailUseCase getStudentDetailUseCase,
                           UpdateStudentUseCase updateStudentUseCase,
                           DeleteStudentUseCase deleteStudentUseCase,
                           ListStudentsUseCase listStudentsUseCase) {
        this.createStudentUseCase = createStudentUseCase;
        this.getStudentDetailUseCase = getStudentDetailUseCase;
        this.updateStudentUseCase = updateStudentUseCase;
        this.deleteStudentUseCase = deleteStudentUseCase;
        this.listStudentsUseCase = listStudentsUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentOutputDTO>> create(@Valid @RequestBody CreateStudentInputDTO input) {
        try {
            StudentOutputDTO result = createStudentUseCase.execute(input);
            return ResponseEntity.ok(ApiResponse.success("Aluno criado com sucesso", result));
        } catch (DomainException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            // Log para debug
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error("Erro ao criar aluno: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentOutputDTO>> getDetail(@PathVariable String id) {
        try {
            StudentOutputDTO result = getStudentDetailUseCase.execute(id);
            return ResponseEntity.ok(ApiResponse.success("Aluno encontrado", result));
        } catch (DomainException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentOutputDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateStudentInputDTO input) {
        try {
            StudentOutputDTO result = updateStudentUseCase.execute(id, input);
            return ResponseEntity.ok(ApiResponse.success("Aluno atualizado com sucesso", result));
        } catch (DomainException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        try {
            deleteStudentUseCase.execute(id);
            return ResponseEntity.ok(ApiResponse.success("Aluno removido com sucesso", null));
        } catch (DomainException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<StudentPagedOutputDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String searchType) {
        try {
            StudentPagedOutputDTO result = listStudentsUseCase.execute(page, size, search, searchType);
            return ResponseEntity.ok(ApiResponse.success("Alunos listados", result));
        } catch (DomainException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/validate/cpf")
    public ResponseEntity<ApiResponse<Boolean>> validateCpf(@RequestParam String cpf) {
        boolean exists = listStudentsUseCase.validateCpfExists(cpf);
        if (exists) {
            return ResponseEntity.ok(ApiResponse.success("CPF já cadastrado", true));
        }
        return ResponseEntity.ok(ApiResponse.success("CPF disponível", false));
    }

    @GetMapping("/validate/email")
    public ResponseEntity<ApiResponse<Boolean>> validateEmail(@RequestParam String email) {
        boolean exists = listStudentsUseCase.validateEmailExists(email);
        if (exists) {
            return ResponseEntity.ok(ApiResponse.success("Email já cadastrado", true));
        }
        return ResponseEntity.ok(ApiResponse.success("Email disponível", false));
    }
}
