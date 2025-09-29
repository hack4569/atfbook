package com.shsb.atfbook.application.history;

import com.shsb.atfbook.domain.history.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findHistoriesByLoginId(String loginId);
}
