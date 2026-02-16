package start.students.adapters.outbound.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import start.students.adapters.outbound.persistence.entities.StudentJpaEntity;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, String> {

    Page<StudentJpaEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<StudentJpaEntity> findByCpfContaining(String cpf, Pageable pageable);

    Page<StudentJpaEntity> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    @Query("SELECT s FROM StudentJpaEntity s WHERE CAST(s.matricula AS string) LIKE :matricula")
    Page<StudentJpaEntity> findByIdContaining(@Param("matricula") String matricula, Pageable pageable);

    Page<StudentJpaEntity> findByMatriculaContaining(String matricula, Pageable pageable);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM StudentJpaEntity s WHERE LOWER(s.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    @Query(value = "SELECT MAX(CAST(matricula AS INTEGER)) FROM students", nativeQuery = true)
    Long findMaxMatricula();
}
