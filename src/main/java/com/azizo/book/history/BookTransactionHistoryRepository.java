package com.azizo.book.history;

import com.azizo.book.book.Book;
import com.azizo.book.book.BookController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

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


    @Query("""
      SELECT transaction
      FROM BookTransactionHistory transaction
      WHERE transaction.users.id = :userId
      AND transaction.book.id = :bookId
      AND transaction.returned = false
      AND transaction.returnApproved = false
      
""")
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer id);


    @Query("""
      SELECT transaction
      FROM BookTransactionHistory transaction
      WHERE transaction.book.owner.id = :userId
      AND transaction.book.id = :bookId
      AND transaction.returned = true
      AND transaction.returnApproved = false
    

""")
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer id);
}
