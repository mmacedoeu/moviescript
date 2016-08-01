package org.eu.mmacedo.movie.scripts.domain;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class MovieCharacter {
	private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
	private String name;
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	private Setting setting;
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "character")
	private List<Word> words;
	private transient Map<String, Word> index;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MovieCharacter other = (MovieCharacter) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (setting == null) {
			if (other.setting != null)
				return false;
		} else if (!setting.equals(other.setting))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((setting == null) ? 0 : setting.hashCode());
		return result;
	}

}
