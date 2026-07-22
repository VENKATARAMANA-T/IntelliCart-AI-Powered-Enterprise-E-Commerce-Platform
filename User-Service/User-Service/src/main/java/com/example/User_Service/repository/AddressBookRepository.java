package com.example.User_Service.repository;

import com.example.User_Service.model.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
    List<AddressBook> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
