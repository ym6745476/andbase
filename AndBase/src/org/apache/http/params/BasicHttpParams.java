/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.params;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpParams;

/**
 * Default implementation of {@link HttpParams} interface.
 * <p>
 * Please note access to the internal structures of this class is not
 * synchronized and therefore this class may be thread-unsafe.
 *
 * @since 4.0
 */
@NotThreadSafe
public class BasicHttpParams extends AbstractHttpParams implements Serializable, Cloneable {

    private static final long serialVersionUID = -7086398485908701455L;

    /** Map of HTTP parameters that this collection contains. */
    private final HashMap<String, Object> parameters = new HashMap<String, Object>();

    public BasicHttpParams() {
        super();
    }

    public Object getParameter(final String name) {
        return this.parameters.get(name);
    }

    public HttpParams setParameter(final String name, final Object value) {
        this.parameters.put(name, value);
        return this;
    }

    public boolean removeParameter(String name) {
        //this is to avoid the case in which the key has a null value
        if (this.parameters.containsKey(name)) {
            this.parameters.remove(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Assigns the value to all the parameter with the given names
     *
     * @param names array of parameter names
     * @param value parameter value
     */
    public void setParameters(final String[] names, final Object value) {
        for (int i = 0; i < names.length; i++) {
            setParameter(names[i], value);
        }
    }

    /**
     * Is the parameter set?
     * <p>
     * Uses {@link #getParameter(String)} (which is overrideable) to
     * fetch the parameter value, if any.
     * <p>
     * Also @see {@link #isParameterSetLocally(String)}
     *
     * @param name parameter name
     * @return true if parameter is defined and non-null
     */
    public boolean isParameterSet(final String name) {
        return getParameter(name) != null;
    }

    /**
     * Is the parameter set in this object?
     * <p>
     * The parameter value is fetched directly.
     * <p>
     * Also @see {@link #isParameterSet(String)}
     *
     * @param name parameter name
     * @return true if parameter is defined and non-null
     */
    public boolean isParameterSetLocally(final String name) {
        return this.parameters.get(name) != null;
    }

    /**
     * Removes all parameters from this collection.
     */
    public void clear() {
        this.parameters.clear();
    }

    /**
     * Creates a copy of these parameters.
     * This implementation calls {@link #clone()}.
     *
     * @return  a new set of params holding a copy of the
     *          <i>local</i> parameters in this object.
     *
     * @deprecated (4.1)
     * @throws UnsupportedOperationException if the clone() fails
     */
    @Deprecated
    public HttpParams copy() {
        try {
            return (HttpParams) clone();
        } catch (CloneNotSupportedException ex) {
            throw new UnsupportedOperationException("Cloning not supported");
        }
    }

    /**
     * Clones the instance.
     * Uses {@link #copyParams(HttpParams)} to copy the parameters.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        BasicHttpParams clone = (BasicHttpParams) super.clone();
        copyParams(clone);
        return clone;
    }

    /**
     * Copies the locally defined parameters to the argument parameters.
     * This method is called from {@link #clone()}.
     *
     * @param target    the parameters to which to copy
     * @since 4.2
     */
    public void copyParams(HttpParams target) {
        Iterator<Map.Entry<String, Object>> iter = this.parameters.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Object> me = iter.next();
            if (me.getKey() instanceof String)
                target.setParameter(me.getKey(), me.getValue());
        }
    }

    /**
     * Returns the current set of names.
     *
     * Changes to the underlying HttpParams are not reflected
     * in the set - it is a snapshot.
     *
     * @return the names, as a Set<String>
     * @since 4.2
     */
    @Override
    public Set<String> getNames() {
        return new HashSet<String>(this.parameters.keySet());
    }
}
