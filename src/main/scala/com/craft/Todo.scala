package com.craft

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsBoolean, JsField, JsObject, JsString, JsValue, JsonFormat, RootJsonFormat}

case class Todo(id: String, title: String, description: String, done: Boolean)

case class CreateTodo(title: String, description: String)

case class UpdateTodo(title: Option[String], description: Option[String], done: Option[Boolean])

trait TodoJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val todo = jsonFormat4(Todo)
  implicit val createTodo = jsonFormat2(CreateTodo)

  //  implicit val updateTodo = jsonFormat3(UpdateTodo)

  implicit object UpdateTodoFormat extends RootJsonFormat[UpdateTodo] {
    def  read(value: JsValue) = value.asJsObject.getFields("title", "description", "done") match {
      case Seq(JsString(title), JsString(description), JsBoolean(done)) =>
        UpdateTodo(Some(title), Some(description), Some(done))
      case Seq(JsString(title), JsString(description)) =>
        UpdateTodo(Some(title), Some(description), None)
      case Seq(JsString(title), JsBoolean(done)) =>
        UpdateTodo(Some(title), None, Some(done))
      case Seq(JsString(description), JsBoolean(done)) =>
        UpdateTodo(None, Some(description), Some(done))
      case Seq(JsString(title)) =>
        UpdateTodo(Some(title), None, None)
      case Seq(JsString(description)) =>
        UpdateTodo(None, Some(description), None)
      case Seq(JsBoolean(done)) =>
        UpdateTodo(None, None, Some(done))

      case _ =>
        throw new DeserializationException("UpdateTodo expected")
    }

    def write(obj: UpdateTodo) = JsObject("title" -> JsString(obj.title),
      "title" -> JsString(obj.description), "title" -> JsBoolean(obj.done))


  }

}