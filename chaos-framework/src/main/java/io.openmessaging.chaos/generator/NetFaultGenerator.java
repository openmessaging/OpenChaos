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

package io.openmessaging.chaos.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class NetFaultGenerator implements FaultGenerator {

    private Collection<String> nodes;

    private String mode;

    private Random random = new Random();

    public NetFaultGenerator(Collection<String> nodes, String mode) {
        this.nodes = nodes;
        this.mode = mode;
    }

    @Override public List<FaultOperation> generate() {
        switch (mode) {
            case "random-partition":
                return partitionOperationsGenerate();
            case "random-delay":
            case "random-loss":
                return otherOperationsGenerate();
            default:
                break;
        }
        return Collections.emptyList();
    }

    private List<FaultOperation> otherOperationsGenerate() {
        int num = random.nextInt(nodes.size()) + 1;
        List<FaultOperation> operations = new ArrayList<>();
        List<String> shuffleNodes = new ArrayList<>(nodes);
        Collections.shuffle(shuffleNodes);
        for (int i = 0; i < num; i++) {
            FaultOperation operation = new FaultOperation(mode, shuffleNodes.get(i));
            operations.add(operation);
        }
        return operations;
    }

    private List<FaultOperation> partitionOperationsGenerate() {
        int num = random.nextInt(nodes.size() - 1) + 1;
        List<FaultOperation> operations = new ArrayList<>();
        List<String> shuffleNodes = new ArrayList<>(nodes);
        Collections.shuffle(shuffleNodes);
        Set<String> partition1 = new HashSet<>();
        Set<String> partition2 = new HashSet<>();
        for (int i = 0; i < shuffleNodes.size(); i++) {
            if (i < num) {
                partition1.add(shuffleNodes.get(i));
            } else {
                partition2.add(shuffleNodes.get(i));
            }
        }

        nodes.forEach(node -> {
            if (partition1.contains(node)) {
                operations.add(getPartitionOperation(node, partition2));
            } else {
                operations.add(getPartitionOperation(node, partition1));
            }
        });

        return operations;
    }

    private FaultOperation getPartitionOperation(String node, Set<String> partitionNodes) {
        List<String> invokeArgs = new ArrayList<>(partitionNodes);
        List<String> recoverArgs = new ArrayList<>(partitionNodes);
        FaultOperation operation = new FaultOperation(mode, node, invokeArgs, recoverArgs);
        return operation;
    }

}
