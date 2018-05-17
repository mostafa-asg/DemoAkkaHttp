package com.github.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.github.db.model.User
import com.github.db.repositories.UserRepository
import spray.json.DefaultJsonProtocol

object UserRoutes extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val userFormatter = jsonFormat4(User)

  def getById(id: Long) = get {
    onSuccess(UserRepository.getById(id)) {
      case Some(user) => complete(user)
      case None => complete(StatusCodes.NotFound)
    }
  }

  def updateById(id: Long) = put {
    entity(as[User]) { user =>
      onSuccess( UserRepository.update(id,user) ) { returnValue =>
        if (returnValue>0)
          complete(StatusCodes.OK)
        else
          complete(StatusCodes.NotFound)
      }
    }
  }

  def deleteById(id: Long) = delete {
    onSuccess(UserRepository.deleteById(id)) { returnValue =>
      if (returnValue > 0) {
        complete(StatusCodes.OK)
      } else {
        complete(StatusCodes.NotFound)
      }
    }
  }

  val getAll = get {
    onSuccess( UserRepository.getAll ) { users =>
      complete( users )
    }
  }

  val insert = post {
    entity(as[User]) { user =>
      onSuccess( UserRepository.insert(user) ) { returnValue =>
        complete(StatusCodes.Created)
      }
    }
  }

  val routes = pathPrefix("users") {
    path(LongNumber) { id =>
      getById(id) ~ updateById(id) ~ deleteById(id)
    } ~ getAll ~ insert
  }

}
