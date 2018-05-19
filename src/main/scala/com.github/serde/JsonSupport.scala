package com.github.serde

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.http.models.ErrorResponse
import spray.json.DefaultJsonProtocol

class JsonSupport extends SprayJsonSupport
                  with DefaultJsonProtocol
                  with UserJsonSupport {

  implicit val errorResponseFormatter = jsonFormat2(ErrorResponse)

}
