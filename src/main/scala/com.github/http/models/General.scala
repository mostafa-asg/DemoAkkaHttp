package com.github.http.models

trait Response
case class ErrorResponse(message:String, success:Boolean=false) extends Response
