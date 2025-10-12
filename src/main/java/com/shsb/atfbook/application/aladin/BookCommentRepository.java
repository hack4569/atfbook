package com.shsb.atfbook.application.aladin;

import com.shsb.atfbook.domain.aladin.AladinBook;
import com.shsb.atfbook.domain.aladin.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {
    List<BookComment> findBookCommentsByAladinBookItemId(int aladinItemId);
}
