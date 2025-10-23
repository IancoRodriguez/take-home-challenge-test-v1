package takehomechallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import takehomechallenge.model.User;

@Repository
public interface IUsuarioRepository extends JpaRepository<User, Integer> {
}
