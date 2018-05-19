package com.github.routes

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.github.db.model.user.RowView
import com.github.http.models.{ErrorResponse, Response, SuccessResponse}
import com.github.http.models.user.{ChangePassRequest, RegisterUserRequest, RegisterUserResponse}
import com.github.serde.JsonSupport
import com.github.services.UserService

object UserRoutes extends JsonSupport {

  val getAll = get {
    onSuccess( UserService.getAll ) { users =>
      complete( users )
    }
  }

  def getById(id: Long) = get {
    onSuccess(UserService.getById(id)) {
      case Some(user) => complete(user)
      case None => complete(StatusCodes.NotFound)
    }
  }

  def updateById(id: Long) = put {
    entity(as[RowView]) { user =>
      onSuccess( UserService.update(id,user) ) { returnValue =>
        if (returnValue>0)
          complete(StatusCodes.OK)
        else
          complete(StatusCodes.NotFound)
      }
    }
  }

  def changePass(id: Long) = put {
    entity(as[ChangePassRequest]) { req =>
      onSuccess( UserService.changePassword(id,req) ) { response =>
        complete(toProperResponse(response))
      }
    }
  }

  def deleteById(id: Long) = delete {
    onSuccess(UserService.delete(id)) { returnValue =>
      if (returnValue > 0) {
        complete(StatusCodes.OK)
      } else {
        complete(StatusCodes.NotFound)
      }
    }
  }

  val insert = post {
    entity(as[RegisterUserRequest]) { user =>
      onSuccess( UserService.registerNewUser(user) ) { response =>
        complete(toProperResponse(response))
      }
    }
  }

  val routes = pathPrefix("users") {
    pathPrefix(LongNumber) { id =>
      getById(id) ~ deleteById(id) ~ updateById(id) ~
      path("changePass") {
        changePass(id)
      }
    } ~
      getAll ~ insert
  }

  private def toProperResponse(response: Response):ToResponseMarshallable = response match {
    case RegisterUserResponse(_,_) => ToResponseMarshallable(response.asInstanceOf[RegisterUserResponse])
    case ErrorResponse(_,_) =>  ToResponseMarshallable(response.asInstanceOf[ErrorResponse])
    case SuccessResponse(_,_) => ToResponseMarshallable(response.asInstanceOf[SuccessResponse])
    case _ => throw new RuntimeException("Invalid reponse type")
  }

}
