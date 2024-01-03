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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/** 
* The "Worker" class allows to perform tasks and send the results to node of type "Coordinator".
*/
public class Worker extends AbstractActor {

  ActorSelection coordinator = getContext().actorSelection("akka://ClusterSystem@127.0.0.1:2551/user/coordinator");

  
  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  Cluster cluster = Cluster.get(getContext().getSystem());
  
  @Override
  public void preStart() {
	log.info("Worker has started: {}", getSelf().path());
    cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class);
	coordinator.tell(new RegistrationMessage(getSelf()), getSelf());
    log.info("Worker send a registration message: {}", getSelf());
  }

  @Override
  public void postStop() {
	cluster.unsubscribe(getSelf());
	log.info("Worker has finished: {}", getSelf().path());
  }
  
  @Override
  public Receive createReceive() {
      return receiveBuilder()
    		  .match(MemberUp.class,memberUp -> {
    			  log.info("Worker is up: {}", memberUp.member().address());
    			  })
    		  .match(TaskMessage.class, task -> {
    			  log.info("Worker receive a task message: {}", getSelf());
                  Number[] result = calculateResult(task.getFilePath());
                  coordinator.tell(new ResultMessage(result[0].doubleValue(), result[1].intValue()), getSelf());
                  log.info("Worker send a result message: {}", getSelf());
                  coordinator.tell(new RegistrationMessage(getSelf()), getSelf());
                  log.info("Worker send a registration message: {}", getSelf());
              })
    		  .match(TerminateMessage.class, terminate -> {
                   log.info("Worker received a termination message: {}", getSelf());
                   getContext().stop(getSelf());
              })
              .build();
  }
  
  private Number[] calculateResult(String filePath) throws IOException {
	  Number[] result = new Number[2];
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
          String line;
          double sum = 0.0;
          int count = 0;
          while ((line = reader.readLine()) != null) {
              try {
                  double value = Double.parseDouble(line.trim());
                  sum += value;
                  count++;
              } catch (NumberFormatException e) {
                  System.err.println("Ignore an invalid value in this line: " + line);
              }
          }
          if (count == 0) {
              throw new IllegalArgumentException("No valid value found in the file");
          } 
          result[0] = sum;   
          result[1] = count;
          return result;
      }
  }

  public static Props props() {
      return Props.create(Worker.class);
  }
  
}