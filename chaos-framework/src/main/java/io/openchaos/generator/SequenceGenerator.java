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

package io.openchaos.generator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SequenceGenerator {

    private static AtomicLong stagger = new AtomicLong(0);

    private static Random random = new Random();

    public static Operation generateQueueOperation(boolean isUsePull) {
        if (isUsePull) {
            if (random.nextDouble() < 0.5) {
                return new Operation("enqueue", String.valueOf(stagger.getAndIncrement()));
            } else {
                return new Operation("dequeue");
            }
        } else {
            return new Operation("enqueue", String.valueOf(stagger.getAndIncrement()));
        }
    }

    public static Operation generateKVOperation() {
        return new Operation("put", String.valueOf(stagger.getAndIncrement()));
    }
}
