package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MessageRepository extends BaseRepository<Message, String> {

    @Query("SELECT m FROM Message m LEFT JOIN FETCH m.fromShift LEFT JOIN FETCH m.toShift LEFT JOIN FETCH m.forShift")
    List<Message> findAllWithShifts();

    @Query("SELECT m FROM Message m WHERE :date BETWEEN m.fromDate AND m.toDate AND (m.fromShift.code = :shiftCode OR m.toShift.code = :shiftCode)")
    List<Message> findMessagesByDateAndShift(@Param("date") LocalDate date, @Param("shiftCode") int shiftCode);
}