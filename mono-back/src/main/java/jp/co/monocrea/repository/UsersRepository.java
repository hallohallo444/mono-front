package jp.co.monocrea.repository;

import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jp.co.monocrea.entity.Users;

public class UsersRepository implements PanacheRepository<Users> {
    
}
