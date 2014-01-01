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
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class CouchbaseEndpoint extends DefaultEndpoint {

    public static final int DEFAULT_PORT = 8091;
    private static final String URI_ERROR = "Invalid URI. Format must be of the form couchbase:http[s]://hostname[:port]/bucket?[options...]";

    private String protocol;
    private String bucket;
    private String hostname;

    private String key;

    private boolean autoStartKeyForInserts = false;
    private long startKeyForInsertsFrom = 0;

    private String designDocumentName = "beer";
    private String viewName = "brewery_beers";
    private int limit = -1;
    private boolean descending = false;

    private int skip = -1;

    private String rangeStartKey = "";
    private String rangeEndKey = "";
    private String username = "";

    private String password = "";
    private int port;

    public CouchbaseEndpoint() {
    }

    public CouchbaseEndpoint(String uri, String remaining, CouchbaseComponent component) throws URISyntaxException {
        super(uri, component);
        URI remainingUri = new URI(remaining);

        protocol = remainingUri.getScheme();
        if (protocol == null) {
            throw new IllegalArgumentException(URI_ERROR);
        }

        port = remainingUri.getPort() == -1 ? DEFAULT_PORT : remainingUri.getPort();

        if (remainingUri.getPath() == null || remainingUri.getPath().trim().length() == 0) {
            throw new IllegalArgumentException(URI_ERROR);
        }
        bucket = remainingUri.getPath().substring(1);

        hostname = remainingUri.getHost();
        if (hostname == null) {
            throw new IllegalArgumentException(URI_ERROR);
        }
    }

    public CouchbaseEndpoint(String endpointUri) {
        super(endpointUri);
    }
    public Producer createProducer() throws Exception {
        return new CouchbaseProducer(this, createClient());
    }
    public Consumer createConsumer(Processor processor) throws Exception {
        return new CouchbaseConsumer(this, createClient(), processor);
    }
    public boolean isSingleton() {
        return true;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAutoStartKeyForInserts() {
        return autoStartKeyForInserts;
    }

    public void setAutoStartKeyForInserts(boolean autoStartKeyForInserts) {
        this.autoStartKeyForInserts = autoStartKeyForInserts;
    }

    public long getStartKeyForInsertsFrom() {
        return startKeyForInsertsFrom;
    }

    public void setStartKeyForInsertsFrom(long startKeyForInsertsFrom) {
        this.startKeyForInsertsFrom = startKeyForInsertsFrom;
    }

    public String getDesignDocumentName() {
        return designDocumentName;
    }

    public void setDesignDocumentName(String designDocumentName) {
        this.designDocumentName = designDocumentName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String getRangeStartKey() {
        return rangeStartKey;
    }

    public void setRangeStartKey(String rangeStartKey) {
        this.rangeStartKey = rangeStartKey;
    }

    public String getRangeEndKey() {
        return rangeEndKey;
    }

    public void setRangeEndKey(String rangeEndKey) {
        this.rangeEndKey = rangeEndKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String makeBootstrapURI() {
        return protocol + "://" + hostname + ":" + port + "/pools";
    }

    private CouchbaseClient createClient() throws IOException, URISyntaxException {
        List<URI> hosts = Arrays.asList(
                new URI(makeBootstrapURI())
        );

        return new CouchbaseClient(hosts, bucket, username, password);
    }

}
