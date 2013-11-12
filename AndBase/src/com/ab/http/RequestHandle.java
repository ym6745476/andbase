/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.http;

import java.util.concurrent.Future;

// TODO: Auto-generated Javadoc
/**
 * A Handle to an AsyncRequest which can be used to cancel a running request.
 * 
 */
public class RequestHandle {
	
	/** The request. */
	private final Future<?> request;
	
	/**
	 * Instantiates a new request handle.
	 *
	 * @param request the request
	 */
	public RequestHandle(Future<?> request) {
		this.request = request;
	}
	
	/**
	 * Attempts to cancel this request. This attempt will fail if the request has
	 * already completed, has already been cancelled, or could not be cancelled
	 * for some other reason. If successful, and this request has not started
	 * when cancel is called, this request should never run. If the request has
	 * already started, then the mayInterruptIfRunning parameter determines
	 * whether the thread executing this request should be interrupted in an
	 * attempt to stop the request.
	 * 
	 * After this method returns, subsequent calls to isDone() will always
	 * return true. Subsequent calls to isCancelled() will always return true
	 * if this method returned true.
	 * 
	 * @param mayInterruptIfRunning true if the thread executing this request should be interrupted; otherwise, in-progress requests are allowed to complete
	 * @return false if the request could not be cancelled, typically because it has already completed normally; true otherwise
	 */
	public boolean cancel(boolean mayInterruptIfRunning) {
		if (this.request == null) {
			return false;
		}
		return request.cancel(mayInterruptIfRunning);
	}
	
	/**
	 * Returns true if this task completed. Completion may be due to normal termination, an exception, or cancellation -- in all of these cases, this method will return true.
	 * @return true if this task completed
	 */
	public boolean isFinished() {
		if (this.request == null) {
			return true;
		}
		return request.isDone();
	}
	
	/**
	 * Returns true if this task was cancelled before it completed normally.
	 * @return true if this task was cancelled before it completed
	 */
	public boolean isCancelled() {
		if (this.request == null) {
			return false;
		}
		return request.isCancelled();
	}
}
