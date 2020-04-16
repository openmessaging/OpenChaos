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

package io.openmessaging.chaos.fault;

import io.openmessaging.chaos.ChaosControl;
import io.openmessaging.chaos.driver.mq.MQChaosNode;
import io.openmessaging.chaos.generator.FaultGenerator;
import io.openmessaging.chaos.generator.FaultOperation;
import io.openmessaging.chaos.generator.FixedFaultGenerator;
import io.openmessaging.chaos.generator.SingleFaultGenerator;
import io.openmessaging.chaos.recorder.Recorder;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseFault implements Fault {
    private volatile List<FaultOperation> faultOperations;

    private Map<String, MQChaosNode> nodesMap;

    private FaultGenerator faultGenerator;

    private String mode;

    private Recorder recorder;

    private static final Logger logger = LoggerFactory.getLogger(ChaosControl.class);

    public PauseFault(Map<String, MQChaosNode> nodesMap, String mode, Recorder recorder) {
        this.nodesMap = nodesMap;
        this.mode = mode;
        this.recorder = recorder;
        this.faultGenerator = new SingleFaultGenerator(nodesMap.keySet(), mode);
    }

    public PauseFault(Map<String, MQChaosNode> nodesMap, String mode, Recorder recorder, List<String> faultNodes) {
        this.nodesMap = nodesMap;
        this.mode = mode;
        this.recorder = recorder;
        this.faultGenerator = new FixedFaultGenerator(nodesMap.keySet(), faultNodes, mode);
    }

    @Override public synchronized void invoke() {
        logger.info("Invoke {} fault....", mode);
        recorder.recordFaultStart(mode, System.currentTimeMillis());
        faultOperations = faultGenerator.generate();
        for (FaultOperation operation : faultOperations) {
            logger.info("Suspend node {} processes...", operation.getNode());
            MQChaosNode mqChaosNode = nodesMap.get(operation.getNode());
            mqChaosNode.pause();
        }
    }

    @Override public synchronized void recover() {
        if (faultOperations == null)
            return;
        logger.info("Recover {} fault....", mode);
        recorder.recordFaultEnd(mode, System.currentTimeMillis());
        for (FaultOperation operation : faultOperations) {
            logger.info("Recovery node {} processes...", operation.getNode());
            MQChaosNode mqChaosNode = nodesMap.get(operation.getNode());
            mqChaosNode.resume();
        }
        faultOperations = null;
    }
}
