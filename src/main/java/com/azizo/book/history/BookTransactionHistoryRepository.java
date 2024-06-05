package com.azizo.book.history;

import com.azizo.book.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

@Query("""
    SELECT history
    FROM BookTransactionHistory history
    where history.users.id = :userId
""")
Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);


    @Query("""
    SELECT history
    FROM BookTransactionHistory history
    where history.book.owner.id = :userId
""")
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);


    @Query("""
         SELECT (COUNT(*) > 0) AS isBorrowed
         FROM BookTransactionHistory bookTransactionHistory
         WHERE bookTransactionHistory.users.id = :bookId
         AND bookTransactionHistory.book.id = :bookId
         AND bookTransactionHistory.returnApproved = false
""")
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer id);
}
