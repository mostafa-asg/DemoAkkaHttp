package com.github.services.validation

import com.github.http.models.Response
import com.github.services.validation.Validate.ValidateFunc

import scala.concurrent.Future

class Validate[IN,OUT <: Response](input: IN, valFunctions: List[ValidateFunc[IN,OUT]]=List.empty) {

  def > (f: ValidateFunc[IN,OUT]) = new Validate(input,valFunctions :+ f)

  def andThen(f: IN => Future[OUT]): Future[OUT] = {
    val it = valFunctions.iterator
    while (it.hasNext){
      val nextFunc = it.next
      nextFunc(input) match {
        case Some(result) => return Future.successful(result)
        case _ => // Do nothing
      }
    }

    f(input)
  }

}

object Validate {

  type ValidateFunc[IN,OUT] = IN => Option[OUT]
  def apply[IN,R](input: IN) = new Validate[IN,Response](input)

}
