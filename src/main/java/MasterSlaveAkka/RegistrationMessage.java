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
import akka.actor.ActorRef;

/** 
* The "RegistrationMessage" class allows for a node of type "Worker" to signal its availability to the node of type "Coordinator" in order to perform a task.
*/
public class RegistrationMessage implements Serializable {
	
    private static final long serialVersionUID = -6321548215370745928L;
	private ActorRef worker;

    public RegistrationMessage(ActorRef worker) {
        this.worker = worker;
    }

    public ActorRef getWorker() {
        return worker;
    }
}