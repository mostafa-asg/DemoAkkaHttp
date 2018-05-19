package com.github.services.validation

import com.github.http.models.Response
import com.github.services.validation.Validate.ValFunc

import scala.concurrent.Future

class Validate[IN,R <: Response](input: IN, valFunctions: List[ValFunc[IN,R]]=List.empty) {

  def > (f: ValFunc[IN,R]) = new Validate(input,valFunctions :+ f)

  def andThen(f: IN => Future[R]): Future[R] = {
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

  type ValFunc[IN,R] = IN => Option[R]
  def apply[IN,R](input: IN) = new Validate[IN,Response](input)

}
