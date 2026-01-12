package org.office.repository;

import org.office.model.Conversation;
import org.office.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    java.util.List<Message> findByConversation(Conversation conversation);
}

