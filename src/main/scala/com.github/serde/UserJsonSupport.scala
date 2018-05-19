package com.github.serde

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.db.model.user.RowView
import com.github.http.models.user._
import spray.json.DefaultJsonProtocol

trait UserJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val userViewFormatter = jsonFormat3(RowView)
  implicit val registerUserRequestFormatter = jsonFormat3(RegisterUserRequest)
  implicit val registerUserResponseFormatter = jsonFormat2(RegisterUserResponse)
  implicit val changePassRequestFormatter = jsonFormat3(ChangePassRequest)

}
