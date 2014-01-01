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
    private long startKey;

    public CouchbaseProducer(CouchbaseEndpoint endpoint, CouchbaseClient client) {
        super(endpoint);
        this.endpoint = endpoint;
        this.client = client;
        if (endpoint.isAutoStartKeyForInserts()) {
            this.startKey = endpoint.getStartKeyForInsertsFrom();
        }

    }

    public void process(Exchange exchange) throws Exception {

        Map<String, Object> headers = exchange.getIn().getHeaders();

        String key = (headers.containsKey(CouchbaseConstants.HEADER_KEY))
                ? exchange.getIn().getHeader(CouchbaseConstants.HEADER_KEY, String.class)
                : endpoint.getKey();

        if (endpoint.isAutoStartKeyForInserts()) {
            key = Long.toString(startKey);
            startKey++;
        } else if (key == null) {
            throw new CouchbaseException(CouchbaseConstants.HEADER_KEY + " is not specified in message header or endpoint URL.", exchange);
        }

        Object obj = exchange.getIn().getBody();
        client.set(key, obj).get();

        //cleanup the cache headers
        exchange.getIn().removeHeader(CouchbaseConstants.HEADER_KEY);

    }

}
