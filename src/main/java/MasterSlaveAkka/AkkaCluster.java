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

import java.io.File;
import akka.actor.ActorSystem;

/** 
* The "Akka Cluster" class allows to create the actor system with a single node of type "Coordinator" and several nodes of type "Worker".
*/
public class AkkaCluster {
	
	public static void main(String[] args) {
		String role = args[0];
        ActorSystem system = ActorSystem.create("ClusterSystem");
        if (role.equals("master")) {
        	 File directoryPath = new File("src/main/resources/files");
             String filesPath[] = directoryPath.list();
        	 system.actorOf(Coordinator.props(filesPath), "coordinator");  	 
        } else if (role.equals("slave")) {
        	system.actorOf(Worker.props(), "worker");
        } else {
            System.err.println("Invalid role");
        }
    }
}