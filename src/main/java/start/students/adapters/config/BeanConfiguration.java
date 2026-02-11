package start.students.adapters.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import start.students.adapters.outbound.persistence.adapters.StudentPersistenceAdapter;
import start.students.adapters.outbound.persistence.adapters.UserPersistenceAdapter;
import start.students.adapters.outbound.external.JwtTokenAdapter;
import start.students.adapters.outbound.external.PasswordEncoderAdapter;
import start.students.adapters.outbound.repositories.StudentJpaRepository;
import start.students.adapters.outbound.repositories.UserJpaRepository;
import start.students.core.application.mappers.StudentMapper;
import start.students.core.application.usecases.*;
import start.students.core.ports.*;

@Configuration
public class BeanConfiguration {

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    // =================== Adapters (Ports Implementation) ===================

    @Bean
    public StudentRepositoryPort studentRepositoryPort(StudentJpaRepository repository) {
        return new StudentPersistenceAdapter(repository);
    }

    @Bean
    public UserRepositoryPort userRepositoryPort(UserJpaRepository repository) {
        return new UserPersistenceAdapter(repository);
    }

    @Bean
    public JwtTokenPort jwtTokenPort() {
        return new JwtTokenAdapter(jwtSecret, jwtExpiration);
    }

    @Bean
    public PasswordEncoderPort passwordEncoderPort(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderAdapter(passwordEncoder);
    }

    // =================== Mappers ===================

    @Bean
    public StudentMapper studentMapper(StudentJpaRepository studentJpaRepository) {
        // Implementar um serviço de matrícula que usa o repositório
        return new StudentMapper(new StudentMapper.MatriculaService() {
            @Override
            public String generateMatricula() {
                int currentYear = java.time.Year.now().getValue();
                Long maxMatricula = studentJpaRepository.findMaxMatricula();
                
                long sequence;
                if (maxMatricula == null) {
                    // Primeira matrícula do ano
                    sequence = 1;
                } else {
                    // Extrair sequência da matrícula anterior e incrementar
                    String maxMatriculaStr = String.valueOf(maxMatricula);
                    if (maxMatriculaStr.startsWith(String.valueOf(currentYear))) {
                        // Matrícula do ano atual, incrementar sequência
                        sequence = maxMatricula % 100000 + 1;
                    } else {
                        // Matrícula de ano anterior, começar nova sequência
                        sequence = 1;
                    }
                }
                
                return String.format("%d%05d", currentYear, sequence);
            }
        });
    }

    // =================== Use Cases ===================

    @Bean
    public CreateStudentUseCase createStudentUseCase(
            StudentRepositoryPort studentRepository,
            StudentMapper studentMapper) {
        return new CreateStudentUseCase(studentRepository, studentMapper);
    }

    @Bean
    public UpdateStudentUseCase updateStudentUseCase(
            StudentRepositoryPort studentRepository,
            StudentMapper studentMapper) {
        return new UpdateStudentUseCase(studentRepository, studentMapper);
    }

    @Bean
    public DeleteStudentUseCase deleteStudentUseCase(
            StudentRepositoryPort studentRepository) {
        return new DeleteStudentUseCase(studentRepository);
    }

    @Bean
    public GetStudentDetailUseCase getStudentDetailUseCase(
            StudentRepositoryPort studentRepository,
            StudentMapper studentMapper) {
        return new GetStudentDetailUseCase(studentRepository, studentMapper);
    }

    @Bean
    public ListStudentsUseCase listStudentsUseCase(
            StudentRepositoryPort studentRepository,
            StudentMapper studentMapper) {
        return new ListStudentsUseCase(studentRepository, studentMapper);
    }

    @Bean
    public AuthenticationUseCase authenticationUseCase(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            JwtTokenPort jwtTokenPort) {
        return new AuthenticationUseCase(userRepository, passwordEncoder, jwtTokenPort);
    }
}
