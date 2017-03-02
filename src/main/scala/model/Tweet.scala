package model

/**
  * Created by I338650 on 3/2/2017.
  */
case class Tweet(tag:List[String], msg:String)

object TweetClient {

  val tweets: List[Tweet]  = List( Tweet(List("akka","scala"),"this is 1") , Tweet(List("akka","reactive"),"this is 1"), Tweet(List("scala"),"this is 1") )
  def fetchTweet(): List[Tweet] ={
    tweets
  }

}
