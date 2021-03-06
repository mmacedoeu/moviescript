package org.eu.mmacedo.movie.scripts.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.transaction.Transactional;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.domain.Word;
import org.eu.mmacedo.movie.scripts.repository.SettingRepository;
import org.eu.mmacedo.movie.scripts.repository.WordRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class GetSettings implements Function<Optional<Object>, JSONArray> {

	@Autowired
	private WordRepository wordRepository;

	@Autowired
	private SettingRepository settingRepository;

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

	private final Function<Collection<MovieCharacter>, JSONArray> getMovieCharacter = c -> {
		final JSONArray jsonArray = new JSONArray();
		for (final MovieCharacter movieCharacter : c) {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", movieCharacter.getId());
			jsonObject.put("name", movieCharacter.getName());
			jsonObject.put("wordCounts", this.getWordCounts.apply(movieCharacter.getId()));

			jsonArray.put(jsonObject);
		}
		return jsonArray;
	};

	@Transactional
	@Override
	public JSONArray apply(final Optional<Object> arg0) {
		final List<Setting> findAll = settingRepository.findAll();
		final JSONArray array = new JSONArray();
		for (final Setting setting : findAll) {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", setting.getId());
			jsonObject.put("name", setting.getName());
			jsonObject.put("characters", this.getMovieCharacter.apply(setting.getCharacter()));

			array.put(jsonObject);
		}
		return array;
	}

}
