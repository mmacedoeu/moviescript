package org.eu.mmacedo.movie.scripts.repository;

import java.util.Optional;

import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {
	Optional<Setting> findByName(String name);

	Optional<Setting> findById(Long id);
}
