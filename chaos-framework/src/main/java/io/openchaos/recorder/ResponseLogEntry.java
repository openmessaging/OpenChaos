/*
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

package io.openchaos.recorder;

import io.openchaos.common.InvokeResult;

public class ResponseLogEntry {
    public int clientId;
    public String operation;
    public LogEntryType type = LogEntryType.RESPONSE;
    public InvokeResult result;
    public String value;
    public String shardingKey;
    public long timestamp;
    public long sendLatency;
    public long endToEndLatency;
    public String extraInfo;

    public ResponseLogEntry(int clientId, String operation, InvokeResult result, String value, long timestamp,
        long sendLatency) {
        this.clientId = clientId;
        this.operation = operation;
        this.result = result;
        this.value = value;
        this.timestamp = timestamp;
        this.sendLatency = sendLatency;
    }

    public ResponseLogEntry(int clientId, String operation, InvokeResult result, String value, long timestamp,
        long sendLatency, String extraInfo) {
        this.clientId = clientId;
        this.operation = operation;
        this.result = result;
        this.value = value;
        this.timestamp = timestamp;
        this.sendLatency = sendLatency;
        this.extraInfo = extraInfo;
    }

    public ResponseLogEntry(int clientId, String operation, InvokeResult result, String shardingKey, String value,
        long timestamp, long sendLatency) {
        this.clientId = clientId;
        this.operation = operation;
        this.result = result;
        this.value = value;
        this.shardingKey = shardingKey;
        this.timestamp = timestamp;
        this.sendLatency = sendLatency;
    }

    public ResponseLogEntry(int clientId, String operation, InvokeResult result, String shardingKey, String value,
        long timestamp, long sendLatency, String extraInfo) {
        this.clientId = clientId;
        this.operation = operation;
        this.result = result;
        this.value = value;
        this.shardingKey = shardingKey;
        this.timestamp = timestamp;
        this.sendLatency = sendLatency;
        this.extraInfo = extraInfo;
    }

    public ResponseLogEntry(int clientId, String operation, InvokeResult result, String shardingKey, String value,
        long timestamp, long sendLatency, String extraInfo, long endToEndLatency) {
        this.clientId = clientId;
        this.operation = operation;
        this.result = result;
        this.value = value;
        this.shardingKey = shardingKey;
        this.timestamp = timestamp;
        this.sendLatency = sendLatency;
        this.extraInfo = extraInfo;
        this.endToEndLatency = endToEndLatency;
    }

    @Override
    public String toString() {
        return String.format("%d\t%s\t%s\t%s\t%s\t%s\t%d\t%d\t%s\t%d\n", clientId, operation, type, result, value, shardingKey, timestamp, sendLatency, extraInfo, endToEndLatency);
    }
}
