package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Room r "
            + "SET r.cols = :cols, r.rows = :rows "
            + "WHERE lower(r.name) = lower(:name)")
    void update(@Param("name") String name, @Param("cols") Integer numberOfColumns,
                @Param("rows") Integer numberOfRows);

    @Transactional
    void deleteByNameContainingIgnoreCase(String name);

    boolean existsByNameContainingIgnoreCase(String name);

    Room findByNameContainingIgnoreCase(String name);
}
