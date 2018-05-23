package com.github.routes

import akka.http.scaladsl.server.Directives._

object Application {
  val routes = pathPrefix("api") {
    pathPrefix("v1") {
      UserRoutes.routes
    } ~
    pathPrefix("v2") {
      complete("Version 2 will be available soon")
    }
  }
}
