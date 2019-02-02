package com.craft

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.Directive._

trait Router {
  def route: Route
}

class TodoRouter(todoRepository: TodoRepository) extends Router
  with Directives with TodoDirectives with ValidatorDirectives with TodoJsonSupport {

  override def route: Route = pathPrefix("todos") {
    pathEndOrSingleSlash {
      get {
        handleWithGeneric(todoRepository.all()) { todos =>
          complete(todos)
        }
      } ~ post {
        entity(as[CreateTodo]) { createTodo =>
          validateWith(CreateTodoValidator)(createTodo) {
            handleWithGeneric(todoRepository.save(createTodo)) { todos =>
              complete(todos)
            }
          }
        }
      }
    } ~ path(Segment) { id: String =>
      put {
        entity(as[UpdateTodo]) { updateTodo =>
          validateWith(UpdateTodoValidator)(updateTodo) {
            handle(todoRepository.update(id, updateTodo)) {
              case TodoRepository.TodoNotFound(_) =>
                ApiError.todoNotFound(id)
              case _ =>
                ApiError.generic
            } { todo =>
              complete(todo)
            }
          }
        }
      }
    } ~ path("done") {
      get {
        handleWithGeneric(todoRepository.done()) { todos =>
          complete(todos)
        }
      }
    } ~ path("pending") {
      get {
        handleWithGeneric(todoRepository.pending()) { todos =>
          complete(todos)
        }
      }
    }
  }
}
