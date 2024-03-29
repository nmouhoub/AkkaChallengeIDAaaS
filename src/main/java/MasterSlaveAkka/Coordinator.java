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

import java.util.LinkedList;
import java.util.Queue;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/** 
* The "Coordinator" class allows to send tasks to available nodes of type "Worker" and receive the result of each task.
*/
public class Coordinator extends AbstractActor {
	
  private Queue<ActorRef> workers = new LinkedList<>();
  private Queue<String> tasks = new LinkedList<>();
  private int totalTasks = 0;
  private int totalResults = 0;
  private int sizeResults = 0;
  private double sumResults = 0.0;
	
  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  Cluster cluster = Cluster.get(getContext().getSystem());
  
  public Coordinator(String[] filesPath) {
	  for(int i=0; i < filesPath.length; i++) {
	         tasks.add("src/main/resources/files/" + filesPath[i]);
	         totalTasks++; 
	  }
  }

  @Override
  public void preStart() {
	log.info("Coordinator has started: {}", getSelf().path());
    cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class);
  }

  @Override
  public void postStop() {
	log.info("Coordinator has finished: {}", getSelf().path());
    cluster.unsubscribe(getSelf());
    double finalResult = sumResults / sizeResults;
    System.out.println("Final result : " + finalResult);
  }
  
  @Override
  public Receive createReceive() {
      return receiveBuilder()
    		  .match(MemberUp.class,memberUp -> {
    			  log.info("Coordinator is up: {}", memberUp.member().address());
    		  })
    		  .match(RegistrationMessage.class, registration -> {
    			  log.info("Coordinator receive a registration message: {}", getSelf());
                  workers.add(registration.getWorker());
                  sendTask();
              })
              .match(ResultMessage.class, result -> {
            	  log.info("Coordinator receive a result message: {}", getSelf());
            	  totalResults++;
            	  sumResults += result.getSum();
            	  sizeResults += result.getSize();
              })
              .build();
  }
    
  private void sendTask() {
      while (!tasks.isEmpty() && !workers.isEmpty()) {
          String filePath = tasks.poll();
          ActorRef worker = workers.poll();
          worker.tell(new TaskMessage(filePath), getSelf());
          log.info("Coordinator send a task message: {}", getSelf());
      }

      if (tasks.isEmpty() && (totalTasks == totalResults)) {
          while (!workers.isEmpty()) {
              ActorRef worker = workers.poll();
              worker.tell(new TerminateMessage(), getSelf());
              log.info("Coordinator send a termination message: {}", getSelf());
          }
    	  getContext().stop(getSelf());
      }
  }
 

  public static Props props(String [] filesPath) {
      return Props.create(Coordinator.class, () -> new Coordinator(filesPath));
  }
  
}