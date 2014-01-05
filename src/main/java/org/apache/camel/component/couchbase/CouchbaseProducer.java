/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.camel.component.couchbase;

import com.couchbase.client.CouchbaseClient;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CouchbaseProducer extends DefaultProducer {

    private static final Logger LOG = LoggerFactory.getLogger(CouchbaseProducer.class);
    private CouchbaseEndpoint endpoint;
    private CouchbaseClient client;
    private long startId;

    public CouchbaseProducer(CouchbaseEndpoint endpoint, CouchbaseClient client) {
        super(endpoint);
        this.endpoint = endpoint;
        this.client = client;
        if (endpoint.isAutoStartIdForInserts()) {
            this.startId = endpoint.getStartingIdForInsertsFrom();
        }

    }

    public void process(Exchange exchange) throws Exception {

        Map<String, Object> headers = exchange.getIn().getHeaders();

        String id = (headers.containsKey(CouchbaseConstants.HEADER_ID))
                ? exchange.getIn().getHeader(CouchbaseConstants.HEADER_ID, String.class)
                : endpoint.getId();

        if (endpoint.isAutoStartIdForInserts()) {
            id = Long.toString(startId);
            startId++;
        } else if (id == null) {
            throw new CouchbaseException(CouchbaseConstants.HEADER_ID + " is not specified in message header or endpoint URL.", exchange);
        }

        Object obj = exchange.getIn().getBody();
        client.set(id, obj).get();

        //cleanup the cache headers
        exchange.getIn().removeHeader(CouchbaseConstants.HEADER_ID);

    }

}
