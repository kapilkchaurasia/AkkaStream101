import scala.concurrent._
import akka._
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.util._
import model.{Tweet, TweetClient}
import akka.pattern.ask
import processor.ActorWorker
import scala.concurrent.duration._

object AkkaStream101 {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("TestSystem")
    implicit val materializer = ActorMaterializer()
    val workerActorRef = system.actorOf(Props[ActorWorker])
    implicit val timeout = Timeout(5.seconds)

    print("akka stream 101")

    // akka stream 101
    val source101 = Source(1 to 3)
    val flow101 = Flow[Int].map(x => x*2)
    val sink101 = Sink.foreach[Int](x => System.out.print(x))
//    (source101 via flow101 to sink101).run()


    //Linear stream processing
     val source102:Source[Tweet,NotUsed] = Source(TweetClient.fetchTweet())
     val tweetFilter1 :Flow[Tweet,Tweet,NotUsed] = Flow[Tweet].filter(tweet => {
       tweet.tag.contains("akka") == true
     })
    val sink102: Sink[Tweet,Future[Done]] = Sink.foreach(tweet => println(tweet.msg) )
    // (s1 via tweetFilter to sink).run()

    //Graph stream processing
    val tweetFilter2 :Flow[Tweet,Tweet,NotUsed] = Flow[Tweet].filter(tweet => {
      tweet.tag.contains("scala") == true
    })
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val bcast = b.add(Broadcast[Tweet](2))
      source102 ~> bcast.in

      bcast.out(0) ~> tweetFilter1 ~> sink102
      bcast.out(1) ~> tweetFilter2 ~> sink102
      ClosedShape
    })
    // g.run()


    //using ACTOR in akka stream
     source102.mapAsync(2)(elem => (workerActorRef ? elem).mapTo[Int] ).runWith(sink101)

  }


}
