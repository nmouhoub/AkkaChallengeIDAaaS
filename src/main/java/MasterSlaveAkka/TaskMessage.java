/*
 * Copyright 2023 Noureddine Mouhoub 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package MasterSlaveAkka;

import java.io.Serializable;

/** 
* The "TaskMessage" class allows for the node of type "Coordinator" to send a task to an available node of type "Worker".
*/
public class TaskMessage implements Serializable {
	
    private static final long serialVersionUID = -826016682950753653L;
	private String filePath;

    public TaskMessage(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}