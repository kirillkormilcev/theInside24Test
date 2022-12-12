package info.theinside.server.repository;

import info.theinside.server.model.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByName(String name, PageRequest pageRequest);
}