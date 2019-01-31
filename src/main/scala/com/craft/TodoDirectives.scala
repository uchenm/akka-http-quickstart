package com.craft

import akka.http.scaladsl.server.{Directive1, Directives}

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait TodoDirectives extends Directives with TodoJsonSupport {

  def handle[T]
  (f: Future[T])
  (e: Throwable => ApiError): Directive1[T] = onComplete(f) flatMap {
    case Success(t) =>
      provide(t)
    case Failure(error) =>
      val apiError = e(error)
      complete(apiError.statusCode, apiError.message)
  }

  def handleWithGeneric[T](f: Future[T]): Directive1[T] =
    handle[T](f)(_ => ApiError.generic)


  def handleWithNotfound[T](f: Future[T]): Directive1[T] =
    handle[T](f)((e) => e match {
      case TodoRepository.TodoNotFound(id) =>
        ApiError.todoNotFound(id)
      case _ =>
        ApiError.generic
    })

}
