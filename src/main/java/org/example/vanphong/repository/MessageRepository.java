package org.example.vanphong.repository;

import org.example.vanphong.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    java.util.List<Message> findByConversation(org.example.vanphong.model.Conversation conversation);
}

