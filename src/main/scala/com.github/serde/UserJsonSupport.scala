package com.github.serde

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.db.model.user.UserView
import com.github.http.models.user._
import spray.json.DefaultJsonProtocol

trait UserJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val userViewFormatter = jsonFormat3(UserView)
  implicit val registerRequestFormatter = jsonFormat3(RegisterRequest)
  implicit val registerResponseFormatter = jsonFormat2(RegisterResponse)
  implicit val updateRequest = jsonFormat2(UpdateRequest)
  implicit val changePassRequestFormatter = jsonFormat3(ChangePassRequest)

}
