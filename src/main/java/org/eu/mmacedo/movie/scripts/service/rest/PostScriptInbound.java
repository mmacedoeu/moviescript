package org.eu.mmacedo.movie.scripts.service.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

public class PostScriptInbound implements Function<Message, Message> {
	@Autowired
	@Qualifier("scriptRequest")
	MessageChannel scriptRequest;

	@Autowired
	@Qualifier("error")
	PollableChannel error;

	private static AtomicBoolean received = new AtomicBoolean(false);

	@Override
	public Message apply(final Message t) {
		final Map<String, Object> responseHeaderMap = new HashMap<>();
		responseHeaderMap.put("Content-Type", "application/json");
		if (!received.get()) { // never received
			final Object payload = t.getPayload();
			if (payload instanceof String) {
				final String text = (String) payload;
				// final String text = new String(b,
				// java.nio.charset.StandardCharsets.UTF_8);
				final Message<String> reqmsg = new GenericMessage<>(text);
				// send message to process script in text
				scriptRequest.send(reqmsg);

				try {
					// It is not specified timeout behavior so a 30 second and
					// checks for errors
					TimeUnit.SECONDS.sleep(20);

					final Message<?> receive = error.receive(1);
					if (receive != null && receive.getPayload() != null) {
						final String errormsg = receive.getPayload().toString();

						final JSONObject jsonObject = new JSONObject();
						jsonObject.put("message", errormsg);
						responseHeaderMap.put("status", "403");
						responseHeaderMap.put("Return-Status-Msg", jsonObject.toString());
						final Message<String> message = new GenericMessage<>(jsonObject.toString(), responseHeaderMap);
						return message;
					} else {

						final JSONObject jsonObject = new JSONObject();
						jsonObject.put("message", "Movie script successfully received");
						responseHeaderMap.put("status", "200");
						responseHeaderMap.put("Return-Status-Msg", jsonObject.toString());
						final Message<String> message = new GenericMessage<>(jsonObject.toString(), responseHeaderMap);

						received.set(true);
						return message;
					}
				} catch (final InterruptedException e) { //
					final JSONObject jsonObject = new JSONObject();
					jsonObject.put("message", "InterruptedException error: \t" + e.getMessage());
					responseHeaderMap.put("status", "403");
					responseHeaderMap.put("Return-Status-Msg", jsonObject.toString());
					final Message<String> message = new GenericMessage<>(jsonObject.toString(), responseHeaderMap);
					return message;
				}
			} else { // default error
				final JSONObject jsonObject = new JSONObject();
				jsonObject.put("message", "Unexpected error");
				responseHeaderMap.put("status", "403");
				responseHeaderMap.put("Return-Status-Msg", jsonObject.toString());
				final Message<String> message = new GenericMessage<>(jsonObject.toString(), responseHeaderMap);
				return message;
			}
		} else { // received once
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", "Movie script already received");
			responseHeaderMap.put("status", "403");
			responseHeaderMap.put("Return-Status-Msg", jsonObject.toString());
			final Message<String> message = new GenericMessage<>(jsonObject.toString(), responseHeaderMap);
			return message;
		}
	}

}
