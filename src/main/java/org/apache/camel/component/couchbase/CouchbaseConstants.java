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

public interface CouchbaseConstants {

    static int COUCHBASE_DEFAULT_PORT = 8091;
    static String COUCHBASE_URI_ERROR = "Invalid URI. Format must be of the form couchbase:http[s]://hostname[:port]/bucket?[options...]";
    static String DEFAULT_DESIGN_DOCUMENT_NAME = "beer";
    static String DEFAULT_VIEWNAME = "brewery_beers";
    static String HEADER_KEY = "KEY";
    static String HEADER_ID = "ID";
    static String HEADER_DESIGN_DOCUMENT_NAME = "DDN";
    static String HEADER_VIEWNAME = "VN";

}