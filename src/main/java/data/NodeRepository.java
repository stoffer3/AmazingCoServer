package data;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NodeRepository extends CrudRepository<Node, UUID> {
}
