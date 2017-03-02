package processor

import akka.actor.Actor
import akka.actor.Actor.Receive
import model.Tweet

/**
  * Created by I338650 on 3/2/2017.
  */
class ActorWorker extends Actor{

  override def receive = {

    case input:Tweet => sender ! input.tag.size
    case _ =>
  }
}

//case class Work()