package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Movie m "
            + "SET m.genre = :genre, m.runningTime = :runningTime "
            + "WHERE lower(m.title) = lower(:title)")
    void update(@Param("title") String title, @Param("genre") String genre, @Param("runningTime") Integer length);

    @Transactional
    void deleteByTitle(String title);

    boolean existsByTitle(String title);

    Movie findByTitle(String title);
}
