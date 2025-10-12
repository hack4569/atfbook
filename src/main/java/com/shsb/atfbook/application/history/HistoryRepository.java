package com.shsb.atfbook.application.history;

import com.shsb.atfbook.domain.history.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findHistoriesByMemberId(long memberId);
    int deleteHistoriesByMemberId(long memberId);
}
