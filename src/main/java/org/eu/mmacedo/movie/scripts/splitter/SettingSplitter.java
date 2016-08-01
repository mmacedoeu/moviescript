package org.eu.mmacedo.movie.scripts.splitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Function;

import org.eu.mmacedo.movie.scripts.domain.Setting;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SettingSplitter implements Function<String, Collection<Setting>> {

	/**
	 * For performance reasons we will consider name as always being in
	 * uppercase without checking it
	 */
	public Function<String, String> nameParser = (arg0) -> {
		int indexOf = arg0.indexOf(" -");
		indexOf = indexOf < 0 ? arg0.length() : indexOf;
		final String name = arg0.substring(5, indexOf).trim();
		return name;
	};

	public Function<String, String> nameParser2 = (arg0) -> {
		int indexOf = arg0.indexOf(" -");
		indexOf = indexOf < 0 ? arg0.length() : indexOf;
		final String name = arg0.substring(10, indexOf).trim();
		return name;
	};

	@Override
	public Collection<Setting> apply(final String arg0) {
		final Collection<Setting> output = new LinkedList<>();
		final BufferedReader bufReader = new BufferedReader(new StringReader(arg0));
		String line = null;
		boolean searching = true;
		Setting setting = null;
		try {
			while ((line = bufReader.readLine()) != null) {
				if (searching) {
					if (line.startsWith("INT./EXT.")) {
						setting = new Setting();
						setting.setName(nameParser2.apply(line));
						setting.setRaw(new LinkedList<>());
						setting.setCharacter(new HashSet<>());
						searching = false;
					} else if ((line.startsWith("INT.") || line.startsWith("EXT."))) {
						setting = new Setting();
						setting.setName(nameParser.apply(line));
						setting.setRaw(new LinkedList<>());
						setting.setCharacter(new HashSet<>());
						searching = false;
					} else {
						continue; // next line
					}
				} else {
					if (line.startsWith("INT./EXT.")) {
						setting = new Setting();
						setting.setName(nameParser2.apply(line));
						setting.setRaw(new LinkedList<>());
						setting.setCharacter(new HashSet<>());
						searching = false;
					} else if ((line.startsWith("INT.") || line.startsWith("EXT."))) {
						output.add(setting);
						setting = new Setting();
						setting.setName(nameParser.apply(line));
						setting.setRaw(new LinkedList<>());
						setting.setCharacter(new HashSet<>());
					} else {
						setting.getRaw().add(line);
					}
				}
			}
			if (setting != null) {
				output.add(setting);
			}
		} catch (final IOException e) {
			log.error("Error Splitting Settings", e);
		}
		return output;
	}

}
