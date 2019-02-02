package com.craft

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsBoolean, JsField, JsObject, JsString, JsValue, JsonFormat, RootJsonFormat}

case class Todo(id: String, title: String, description: String, done: Boolean)

case class CreateTodo(title: String, description: String)

case class UpdateTodo(title: Option[String], description: Option[String], done: Option[Boolean])

trait TodoJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val todoFormat = jsonFormat4(Todo)
  implicit val createTodoFormat = jsonFormat2(CreateTodo)
  implicit object UpdateTodoFormat extends RootJsonFormat[UpdateTodo] {
    override def read(value: JsValue) = value.asJsObject.getFields("title", "description", "done") match {
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

    override def write(obj: UpdateTodo) ={

      var jsonMap:Map[String,JsValue]= Map()

      obj.description match{
        case Some(description)=> jsonMap=jsonMap.updated("description",JsString(description))
        case None=>
      }
      obj.title match{
        case Some(title) => jsonMap=jsonMap.updated("title",JsString(title))
        case None =>
      }
      obj.done match {
        case Some(done) => jsonMap=jsonMap.updated("done",JsBoolean(done))
        case None =>
      }
      JsObject(jsonMap)
    }
  }

}