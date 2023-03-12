package ru.itmo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;;
import ru.itmo.model.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
