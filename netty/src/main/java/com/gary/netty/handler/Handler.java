package com.gary.netty.handler;

import com.gary.netty.event.Event;

public interface Handler {

	public void handle(final Event event) throws Exception;
}
