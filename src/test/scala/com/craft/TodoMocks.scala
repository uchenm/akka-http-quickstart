package com.craft

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future

trait TodoMocks {

  class FailingRepository extends TodoRepository {
    override def all(): Future[Seq[Todo]] = Future.failed(new Exception("Mocked exception"))

    override def done(): Future[Seq[Todo]] = Future.failed(new Exception("Mocked exception"))

    override def pending(): Future[Seq[Todo]] = Future.failed(new Exception("Mocked exception"))

    override def save(createTodo: CreateTodo): Future[Todo] = Future.failed(new Exception("Mocked exception"))

    override def update(id: String, updateTodo: UpdateTodo): Future[Todo] = Future.failed(new Exception("Mocked exception"))
  }

}

