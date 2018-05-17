package com.github.marshallers.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.db.model.User
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val userFormatter = jsonFormat4(User)

}