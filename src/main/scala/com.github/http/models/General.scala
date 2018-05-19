package com.github.http.models

trait Response
case class SuccessResponse(message: String="", success:Boolean=true) extends Response
case class ErrorResponse(message:String, success:Boolean=false) extends Response
