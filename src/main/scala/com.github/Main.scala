package com.github

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.github.db.model.User
import com.github.db.repositories.UserRepository
import com.github.marshallers.json.JsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import scala.io.StdIn

object Main extends JsonSupport {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    Await.ready(UserRepository.createTable , Duration.Inf)

    val route = pathPrefix("users") {
      get {
        onSuccess( UserRepository.getAll ) { users =>
          complete( users )
        }
      } ~
      post {
        entity(as[User]) { user =>
          onSuccess( UserRepository.insert(user) ) { returnValue =>
            complete(StatusCodes.Created)
          }
        }
      } ~
      put {
        entity(as[User]) { user =>

          user.id match {
            case Some(id) => {
              onSuccess( UserRepository.update(id,user) ) { returnValue =>
                if (returnValue>0)
                  complete(StatusCodes.Created)
                else
                  complete(StatusCodes.NotFound)
              }
            }
            case None => complete(StatusCodes.NotFound)
          }

        }
      }
    }

    val interface = "0.0.0.0"
    val port = 8585
    val bindF = Http().bindAndHandle(route , interface , port)
    println(s"Server is up and run on [$interface:$port]")

    println("Press any key to exit ...")
    StdIn.readLine()
    Await.ready(bindF.flatMap( bind => bind.unbind()),10 seconds)
    Await.ready( system.terminate() , 10 seconds )
    println("by by")
  }

}
