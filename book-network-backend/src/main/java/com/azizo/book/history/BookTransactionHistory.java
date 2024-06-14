package com.azizo.book.history;

import com.azizo.book.book.Book;
import com.azizo.book.common.BaseEntity;
import com.azizo.book.user.Users;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookTransactionHistory extends BaseEntity {


    @ManyToOne
    @JoinColumn(name= "users_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name= "book_id")
    private Book book;


    private boolean returned;
    private boolean returnApproved ;

}