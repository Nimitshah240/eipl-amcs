package com.eipl.amcs.sync.repository;

import com.eipl.amcs.sync.model.NavigationBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavigationBookRepository extends JpaRepository<NavigationBook, Integer> {

    List<NavigationBook> findByNavForAndFlag(short navFor, short flag);

}
