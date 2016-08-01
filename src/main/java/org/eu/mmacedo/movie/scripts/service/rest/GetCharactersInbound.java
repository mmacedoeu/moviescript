package org.eu.mmacedo.movie.scripts.service.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;

public class GetCharactersInbound implements Function<Message, Message> {
	@Autowired
	@Qualifier("getCharactersGateway")
	Function<Optional<Object>, JSONArray> getCharactersGateway;

	@Autowired
	@Qualifier("error")
	PollableChannel error;

	@Override
	public Message apply(final Message arg0) {
		final Map<String, Object> responseHeaderMap = new HashMap<>();
		responseHeaderMap.put("Content-Type", "application/json");
		try {
			final JSONArray apply = getCharactersGateway.apply(Optional.empty());

			final Message<?> receive = error.receive(1);
			if (receive != null && receive.getPayload() != null) {
				final String errormsg = receive.getPayload().toString();

				final JSONObject jsonObject = new JSONObject();
				jsonObject.put("message", errormsg);
				responseHeaderMap.put("http_statusCode", "403");
				responseHeaderMap.put("Reason-Phrase", jsonObject.toString());
				final Message<String> message = new GenericMessage<>(jsonObject.toString(), responseHeaderMap);
				return message;
			} else {

				responseHeaderMap.put("http_statusCode", "200");
				final Message<String> message = new GenericMessage<>(apply.toString(), responseHeaderMap);
				return message;
			}
		} catch (final Throwable t) {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("message", "Unexpected error");
			responseHeaderMap.put("http_statusCode", "403");
			responseHeaderMap.put("Reason-Phrase", jsonObject.toString());
			final Message<String> message = new GenericMessage<>(jsonObject.toString(), responseHeaderMap);
			return message;
		}
	}

}
