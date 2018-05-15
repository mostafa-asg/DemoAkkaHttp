package com.github.model.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.db.User
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val userFormatter = jsonFormat3(User)

}
