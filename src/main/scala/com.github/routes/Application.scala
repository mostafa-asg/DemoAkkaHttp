package com.github.routes

import akka.http.scaladsl.server.Directives._

object Application {
  val routes = pathPrefix("api") {
    pathPrefix("v1") {
      UserRoutes.routes
    }
  }
}
