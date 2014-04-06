/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.messaging.simp;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.util.Assert;

/**
 * A base class for working with message headers in simple messaging protocols that
 * support basic messaging patterns. Provides uniform access to specific values common
 * across protocols such as a destination, message type (e.g. publish, subscribe, etc),
 * session id, and others.
 *
 * <p>Use one of the static factory method in this class, then call getters and setters,
 * and at the end if necessary call {@link #toMap()} to obtain the updated headers.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class SimpMessageHeaderAccessor extends NativeMessageHeaderAccessor {

	private static final SimpMessageHeaderAccessorFactory factory = new DefaultSimpMessageHeaderAccessorFactory();

	// SiMP header names

	public static final String CONNECT_MESSAGE_HEADER = "simpConnectMessage";

	public static final String DESTINATION_HEADER = "simpDestination";

	public static final String MESSAGE_TYPE_HEADER = "simpMessageType";

	public static final String SESSION_ID_HEADER = "simpSessionId";

	public static final String SESSION_ATTRIBUTES = "simpSessionAttributes";

	public static final String SUBSCRIPTION_ID_HEADER = "simpSubscriptionId";

	public static final String USER_HEADER = "simpUser";

	/**
	 * For internal use.
	 *
	 * <p>The original destination used by a client when subscribing. Such a
	 * destination may have been modified (e.g. user destinations) on the server
	 * side. This header provides a hint so messages sent to clients may have
	 * a destination matching to their original subscription.
	 */
	public static final String ORIGINAL_DESTINATION = "simpOrigDestination";


	/**
	 * A constructor for creating new message headers.
	 * This constructor is protected. See factory methods in this and sub-classes.
	 */
	protected SimpMessageHeaderAccessor(SimpMessageType messageType, Map<String, List<String>> externalSourceHeaders) {
		super(externalSourceHeaders);
		Assert.notNull(messageType, "MessageType must not be null");
		setHeader(MESSAGE_TYPE_HEADER, messageType);
	}

	/**
	 * A constructor for accessing and modifying existing message headers. This
	 * constructor is protected. See factory methods in this and sub-classes.
	 */
	protected SimpMessageHeaderAccessor(Message<?> message) {
		super(message);
	}


	/**
	 * Create an instance with
	 * {@link org.springframework.messaging.simp.SimpMessageType} {@code MESSAGE}.
	 */
	public static SimpMessageHeaderAccessor create() {
		return factory.create();
	}

	/**
	 * Create an instance with the given
	 * {@link org.springframework.messaging.simp.SimpMessageType}.
	 */
	public static SimpMessageHeaderAccessor create(SimpMessageType messageType) {
		return factory.create(messageType);
	}

	/**
	 * Create an instance from the payload and headers of the given Message.
	 */
	public static SimpMessageHeaderAccessor wrap(Message<?> message) {
		return factory.wrap(message);
	}


	@Override
	protected MessageHeaderAccessor createAccessor(Message<?> message) {
		return factory.wrap(message);
	}

	public void setMessageTypeIfNotSet(SimpMessageType messageType) {
		if (getMessageType() == null) {
			setHeader(MESSAGE_TYPE_HEADER, messageType);
		}
	}

	/**
	 * @return the message type
	 */
	public SimpMessageType getMessageType() {
		return (SimpMessageType) getHeader(MESSAGE_TYPE_HEADER);
	}

	/**
	 * A static alternative for access to the message type.
	 */
	public static SimpMessageType getMessageType(Map<String, Object> headers) {
		return (SimpMessageType) headers.get(MESSAGE_TYPE_HEADER);
	}

	public void setDestination(String destination) {
		Assert.notNull(destination, "Destination must not be null");
		setHeader(DESTINATION_HEADER, destination);
	}

	/**
	 * @return the message destination
	 */
	public String getDestination() {
		return (String) getHeader(DESTINATION_HEADER);
	}

	/**
	 * A static alternative for access to the destination header.
	 */
	public static String getDestination(Map<String, Object> headers) {
		return (String) headers.get(DESTINATION_HEADER);
	}

	public void setSubscriptionId(String subscriptionId) {
		setHeader(SUBSCRIPTION_ID_HEADER, subscriptionId);
	}

	/**
	 * @return the subscription id (if any) of the message
	 */
	public String getSubscriptionId() {
		return (String) getHeader(SUBSCRIPTION_ID_HEADER);
	}

	/**
	 * A static alternative for access to the subscription id header.
	 */
	public static String getSubscriptionId(Map<String, Object> headers) {
		return (String) headers.get(SUBSCRIPTION_ID_HEADER);
	}

	public void setSessionId(String sessionId) {
		setHeader(SESSION_ID_HEADER, sessionId);
	}

	/**
	 * @return the id of the current session
	 */
	public String getSessionId() {
		return (String) getHeader(SESSION_ID_HEADER);
	}

	/**
	 * A static alternative for access to the session id header.
	 */
	public static String getSessionId(Map<String, Object> headers) {
		return (String) headers.get(SESSION_ID_HEADER);
	}

	/**
	 * A static alternative for access to the session attributes header.
	 */
	public void setSessionAttributes(Map<String, Object> attributes) {
		setHeader(SESSION_ATTRIBUTES, attributes);
	}

	/**
	 * @return attributes associated with the current session.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSessionAttributes() {
		return (Map<String, Object>) getHeader(SESSION_ATTRIBUTES);
	}

	/**
	 * A static alternative for access to the session attributes header.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSessionAttributes(Map<String, Object> headers) {
		return (Map<String, Object>) headers.get(SESSION_ATTRIBUTES);
	}

	public void setUser(Principal principal) {
		setHeader(USER_HEADER, principal);
	}

	/**
	 * @return the user associated with the current session.
	 */
	public Principal getUser() {
		return (Principal) getHeader(USER_HEADER);
	}

	/**
	 * A static alternative for access to the user header.
	 */
	public static Principal getUser(Map<String, Object> headers) {
		return (Principal) headers.get(USER_HEADER);
	}

}
