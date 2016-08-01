package org.eu.mmacedo.movie.scripts.repository;

import java.util.List;

import org.eu.mmacedo.movie.scripts.domain.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends JpaRepository<Word, Long> {
	@Query("select u from Word u where u.character.setting.name = :settingName and u.character.name = :movieCharacterName and u.word = :word")
	List<Word> findBySettingByMovieCharacterByword(@Param("settingName") String settingName,
			@Param("movieCharacterName") String movieCharacterName, @Param("word") String word);

	@Query("select u from Word u where u.character.setting is null and u.character.name = :movieCharacterName and u.word = :word")
	List<Word> findByMovieCharacterByword(@Param("movieCharacterName") String movieCharacterName,
			@Param("word") String word);

	@Query("select u from Word u where u.character.id = :id")
	List<Word> findByCharacterId(@Param("id") Long id, Pageable pageRequest);
}
