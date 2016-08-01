package org.eu.mmacedo.movie.scripts.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.domain.Word;
import org.eu.mmacedo.movie.scripts.repository.MovieCharacterRepository;
import org.eu.mmacedo.movie.scripts.repository.WordRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class GetCharacter implements Function<Long, Optional<JSONObject>> {

	@Autowired
	private WordRepository wordRepository;

	@Autowired
	private MovieCharacterRepository charRepository;

	private final Function<Long, JSONArray> getWordCounts = id -> {
		final JSONArray jsonArray = new JSONArray();
		final PageRequest pageRequest = new PageRequest(0, 10, Sort.Direction.DESC, "counter");
		final List<Word> findByCharacterId = wordRepository.findByCharacterId(id, pageRequest);
		for (final Word word : findByCharacterId) {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("word", word.getWord());
			jsonObject.put("count", word.getCounter());

			jsonArray.put(jsonObject);
		}
		return jsonArray;
	};

	@Override
	public Optional<JSONObject> apply(final Long arg0) {
		final Optional<MovieCharacter> findRootById = charRepository.findRootById(arg0);
		if (findRootById.isPresent()) {
			final MovieCharacter movieCharacter = findRootById.get();
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", movieCharacter.getId());
			jsonObject.put("name", movieCharacter.getName());
			jsonObject.put("wordCounts", this.getWordCounts.apply(movieCharacter.getId()));
			return Optional.of(jsonObject);
		}

		return Optional.empty();
	}

}
