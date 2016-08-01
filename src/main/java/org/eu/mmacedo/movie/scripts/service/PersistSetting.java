package org.eu.mmacedo.movie.scripts.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import javax.transaction.Transactional;

import org.eu.mmacedo.movie.scripts.domain.MovieCharacter;
import org.eu.mmacedo.movie.scripts.domain.Setting;
import org.eu.mmacedo.movie.scripts.domain.Word;
import org.eu.mmacedo.movie.scripts.repository.MovieCharacterRepository;
import org.eu.mmacedo.movie.scripts.repository.SettingRepository;
import org.eu.mmacedo.movie.scripts.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersistSetting implements Consumer<Setting> {

	// @PersistenceContext
	// private EntityManager entityManager;

	@Autowired
	private SettingRepository settingRepository;

	@Autowired
	private MovieCharacterRepository charRepository;

	@Autowired
	private WordRepository wordRepository;

	/**
	 * Used to find in process MovieCharacter by name per Setting
	 */
	public final BiFunction<Collection<MovieCharacter>, String, Optional<MovieCharacter>> findByName = (c, s) -> {
		if (s == null || s.isEmpty())
			return Optional.empty();
		for (final MovieCharacter movieCharacter : c) {
			if (s.equals(movieCharacter.getName())) {
				return Optional.of(movieCharacter);
			}
		}
		return Optional.empty();
	};

	/**
	 * Used to find in process Word by name per Character
	 */
	public final BiFunction<Collection<Word>, String, Optional<Word>> findWordByName = (c, s) -> {
		if (s == null || s.isEmpty())
			return Optional.empty();
		for (final Word w : c) {
			if (s.equals(w.getWord())) {
				return Optional.of(w);
			}
		}
		return Optional.empty();
	};

	@Transactional
	@Override
	public void accept(final Setting arg0) {
		for (final MovieCharacter movieCharacter : arg0.getCharacter()) {
			for (final Word word : movieCharacter.getIndex().values()) {

				final Optional<Setting> findByNameSetting = settingRepository.findByName(arg0.getName());
				if (log.isTraceEnabled()) {
					log.trace(arg0.getName() + " is present :" + findByNameSetting.isPresent());
				}

				Optional<MovieCharacter> findByNameCharacter = Optional.empty();
				if (findByNameSetting.isPresent()) {
					findByNameCharacter = this.findByName.apply(findByNameSetting.get().getCharacter(),
							movieCharacter.getName());
				}
				if (log.isTraceEnabled()) {
					log.trace(movieCharacter.getName() + " is present :" + findByNameCharacter.isPresent());
				}

				final List<Word> findBySettingByMovieCharacterByword = wordRepository
						.findBySettingByMovieCharacterByword(word.getSetting(), word.getCharacter().getName(),
								word.getWord());
				if (log.isTraceEnabled()) {
					log.trace(word.getWord() + " is present :" + !findBySettingByMovieCharacterByword.isEmpty());
				}

				if (!findBySettingByMovieCharacterByword.isEmpty()) { // update
					final Word next = findBySettingByMovieCharacterByword.get(0);
					final Long oldcounter = next.getCounter();
					next.setCounter(oldcounter + word.getCounter());
					wordRepository.saveAndFlush(next);

					if (log.isDebugEnabled()) {
						log.debug("Setting(" + next.getCharacter().getSetting().getName() + ") - Character("
								+ next.getCharacter().getName() + ") - updated word[" + word.getWord() + "] New "
								+ next.getCounter() + " Old: " + oldcounter + " Counter: " + word.getCounter());
					}

				} else { // insert
					if (findByNameCharacter.isPresent()) {
						final MovieCharacter m = findByNameCharacter.get();
						word.setCharacter(m);
						m.getWords().add(word);
						charRepository.saveAndFlush(m);

						if (log.isDebugEnabled()) {
							log.debug("Setting(" + m.getSetting().getName() + ")[" + m.getSetting().getId()
									+ "] - Character(" + m.getName() + ") - new word[" + word.getWord() + "] "
									+ " Counter: " + word.getCounter());
						}

					} else if (findByNameSetting.isPresent()) {
						final Setting setting = findByNameSetting.get();
						movieCharacter.getWords().add(word);
						movieCharacter.setSetting(setting);
						setting.getCharacter().add(movieCharacter);
						settingRepository.saveAndFlush(setting);

						if (log.isDebugEnabled()) {
							log.debug("Setting(" + setting.getName() + ")[" + setting.getId() + "] - New Character("
									+ movieCharacter.getName() + ") - new word[" + word.getWord() + "] " + " Counter: "
									+ word.getCounter());
						}

					} else {
						// final Setting save = settingRepository.save(arg0);
						//
						// if (log.isTraceEnabled()) {
						// log.trace("Setting id:" + save.getId());
						// }

						movieCharacter.getWords().add(word);
						movieCharacter.setSetting(arg0);
						arg0.getCharacter().add(movieCharacter);
						settingRepository.saveAndFlush(arg0);

						if (log.isDebugEnabled()) {
							log.debug("New Setting(" + arg0.getName() + ")" + " - New Character("
									+ movieCharacter.getName() + ") - new word[" + word.getWord() + "] " + " Counter: "
									+ word.getCounter());
						}
					}
				}

				final List<MovieCharacter> findRootByName = charRepository.findRootByName(movieCharacter.getName());
				Optional<MovieCharacter> findRootByNameOpt = Optional.empty();
				if (findRootByName != null && !findRootByName.isEmpty()) {
					findRootByNameOpt = Optional.of(findRootByName.get(0));
				}
				if (log.isTraceEnabled()) {
					log.trace("Root: " + movieCharacter.getName() + " is present :" + findRootByNameOpt.isPresent());
				}

				// character consolidation
				if (findRootByNameOpt.isPresent()) { // Character exists
					final MovieCharacter m = findRootByNameOpt.get();
					// final Optional<Word> apply =
					// this.findWordByName.apply(m.getWords(), word.getWord());
					final List<Word> findByMovieCharacterByword = wordRepository.findByMovieCharacterByword(m.getName(),
							word.getWord());
					// Word exists them sum
					if (!findByMovieCharacterByword.isEmpty()) {
						final Word w = findByMovieCharacterByword.get(0);
						final Long oldcounter = w.getCounter();
						w.setCounter(oldcounter + word.getCounter());
						wordRepository.saveAndFlush(w);

						if (log.isDebugEnabled()) {
							log.debug("ROOT - Character(" + m.getName() + ") - updated word[" + word.getWord()
									+ "] New " + w.getCounter() + " Old: " + oldcounter + " Counter: "
									+ word.getCounter());
						}
					} else { // Save new Word
						final Word w = new Word();
						w.setWord(word.getWord());
						w.setCharacter(m);
						w.setCounter(word.getCounter());
						m.getWords().add(w);
						charRepository.saveAndFlush(m);
						if (log.isDebugEnabled()) {
							log.debug("ROOT - Character(" + m.getName() + ") - new word[" + word.getWord() + "] "
									+ " Counter: " + word.getCounter());
						}
					}
				} else { // Save new Character
					final MovieCharacter m = new MovieCharacter();
					m.setName(movieCharacter.getName());
					m.setWords(new LinkedList<>());
					final Word w = new Word();
					w.setWord(word.getWord());
					w.setCharacter(m);
					w.setCounter(word.getCounter());
					m.getWords().add(w);
					charRepository.saveAndFlush(m);

					if (log.isDebugEnabled()) {
						log.debug("ROOT - New Character(" + m.getName() + ") - new word[" + w.getWord() + "] "
								+ " Counter: " + w.getCounter());
					}

				}

			}
		}
	}

}
