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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Recode the request/response to history file
 */
public class Recorder {

    private static final Logger log = LoggerFactory.getLogger(Recorder.class);

    private BufferedWriter bufferedWriter;

    private File historyFile;

    private Recorder(File historyFile, BufferedWriter bufferedWriter) {
        this.historyFile = historyFile;
        this.bufferedWriter = bufferedWriter;
    }

    public static Recorder newRecorder(String historyFileName) {

        File historyFile = new File(historyFileName);
        if (historyFile.exists()) {
            log.error("{} file already exist.", historyFileName);
            return null;
        }
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(historyFileName));
        } catch (IOException e) {
            log.error("New {} writer failed", historyFileName, e);
        }

        if (bufferedWriter != null) {
            return new Recorder(historyFile, bufferedWriter);
        } else {
            return null;
        }
    }


    public void recordRequest(RequestLogEntry requestLogEntry) {
        recordToHistoryFile(requestLogEntry.toString());

        String logLine = "client" + requestLogEntry.clientId + " request " + requestLogEntry.operation + ", data is " + requestLogEntry.value;
        log.info(logLine);
    }

    public void recordResponse(ResponseLogEntry responseLogEntry) {
        recordToHistoryFile(responseLogEntry.toString());

        String logLine = "client" + responseLogEntry.clientId + " " + responseLogEntry.operation + " response " + responseLogEntry.result + ", data is " + responseLogEntry.value;
        log.info(logLine);
    }

    public void recordFault(FaultLogEntry faultLogEntry) {
        recordToHistoryFile(String.format("fault\t%s\t%s\t%d\n", faultLogEntry.faultName, faultLogEntry.operation, faultLogEntry.timestamp));
    }

    private synchronized void recordToHistoryFile(String recordLine) {
        try {
            bufferedWriter.write(recordLine);
        } catch (IOException e) {
            log.error("Record to history file fail", e);
        }
    }

    public void flush() {
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            log.error("Recorder shutdown fail", e);
        }
    }

    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("Recorder shutdown fail", e);
        }
    }

    public void delete() {
        try {
            bufferedWriter.close();
            if (historyFile.exists()) {
                historyFile.delete();
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
