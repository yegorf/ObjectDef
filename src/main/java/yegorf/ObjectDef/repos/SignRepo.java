package yegorf.ObjectDef.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import yegorf.ObjectDef.entities.Sign;

import java.util.HashSet;

@Repository
public interface SignRepo extends CrudRepository<Sign, Integer> {
    HashSet<Sign> findAll();
}
