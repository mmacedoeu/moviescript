package org.eu.mmacedo.movie.scripts.repository;

import java.util.List;
import java.util.Optional;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieCharacterRepository extends JpaRepository<MovieCharacter, Long> {
	@Query("select u from MovieCharacter u where u.name = :name and u.setting is null")
	List<MovieCharacter> findRootByName(@Param("name") String name);

	@Query("select u from MovieCharacter u where u.setting is null")
	List<MovieCharacter> findAllRoot();

	@Query("select u from MovieCharacter u where u.setting is null and u.id = :id")
	Optional<MovieCharacter> findRootById(@Param("id") Long id);
}
