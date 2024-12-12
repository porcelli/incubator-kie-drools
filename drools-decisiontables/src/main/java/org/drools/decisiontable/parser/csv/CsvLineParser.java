/**
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
 */
package org.drools.decisiontable.parser.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser for CSV (Comma-Separated Values) format.
 * Supports quoted fields, escaped quotes, and custom delimiters.
 */
public class CsvLineParser {
    private final CsvStrategy parser;

    public CsvLineParser() {
        this.parser = new DefaultCsvStrategy();
    }

    /**
     * Parses a line of CSV text into a list of fields.
     *
     * @param input The CSV line to parse
     * @return List of fields extracted from the CSV line
     */
    public List<String> parse(CharSequence input) {
        return parser.parseLine(input.toString());
    }

    /**
     * Strategy interface for CSV parsing implementations.
     */
    interface CsvStrategy {
        List<String> parseLine(String input);
    }

    /**
     * Default implementation of CSV parsing strategy.
     * Handles quoted fields, escaped quotes, and custom delimiters.
     */
    static class DefaultCsvStrategy implements CsvStrategy {
        private final char delimiter;

        public DefaultCsvStrategy() {
            this(',');
        }

        public DefaultCsvStrategy(char delimiter) {
            this.delimiter = delimiter;
        }

        @Override
        public List<String> parseLine(String input) {
            List<String> fields = new ArrayList<>();
            if (input == null || input.isEmpty()) {
                fields.add("");
                return fields;
            }

            StringBuilder currentField = new StringBuilder();
            boolean inQuotes = false;
            boolean isEscaped = false;

            for (int i = 0; i < input.length(); i++) {
                char currentChar = input.charAt(i);

                if (isEscaped) {
                    currentField.append(currentChar);
                    isEscaped = false;
                    continue;
                }

                if (currentChar == '"') {
                    if (inQuotes && i + 1 < input.length() && input.charAt(i + 1) == '"') {
                        // Handle escaped quotes
                        isEscaped = true;
                    } else {
                        inQuotes = !inQuotes;
                    }
                    continue;
                }

                if (currentChar == delimiter && !inQuotes) {
                    fields.add(currentField.toString());
                    currentField.setLength(0);
                    continue;
                }

                currentField.append(currentChar);
            }

            // Add the last field
            fields.add(currentField.toString());

            return fields;
        }
    }
}