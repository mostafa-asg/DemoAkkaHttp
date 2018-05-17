package com.github

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.github.db.repositories.UserRepository
import com.github.routes.Application

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import scala.io.StdIn

object Main {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    Await.ready(UserRepository.createTable , Duration.Inf)

    val interface = "0.0.0.0"
    val port = 8585
    val bindF = Http().bindAndHandle(Application.routes , interface , port)
    println(s"Server is up and run on [$interface:$port]")

    println("Press any key to exit ...")
    StdIn.readLine()
    Await.ready(bindF.flatMap( bind => bind.unbind()),10 seconds)
    Await.ready( system.terminate() , 10 seconds )
    println("by by")
  }

}
