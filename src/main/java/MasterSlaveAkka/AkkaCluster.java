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
import java.util.Scanner;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/** 
* The "Akka Cluster" class allows to create the actor system with a single node of type "Coordinator" and several nodes of type "Worker".
*/
public class AkkaCluster {

    public static void main(String[] args) {
    	
        int numberOfWorkers = 2; 
        Scanner scanner = new Scanner(System.in);
        System.out.print("Veuillez saisir le nombre de Worker : ");
        if (scanner.hasNextInt()) {
        	numberOfWorkers = scanner.nextInt();
            ActorSystem system = ActorSystem.create("ClusterSystem"); 
            File directoryPath = new File("src/main/resources/files");
            String filesPath[] = directoryPath.list();
            ActorRef coordinator = system.actorOf(Coordinator.props(filesPath), "coordinator");
            for (int i = 0; i < numberOfWorkers; i++) {
            	system.actorOf(Worker.props(coordinator), "worker" + i);
            }
        } else {
            System.err.println("Entrée invalide ! Veuillez saisir un nombre entier !");
        }
        scanner.close();
    }
}