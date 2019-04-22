package com.github.bzumhagen.parsely.server.json

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.github.bzumhagen.parsely.{Female, Gender, Male, Other, Record}
import spray.json._

object ParselyJsonSupport extends DefaultJsonProtocol {
  implicit object GenderJsonFormat extends RootJsonFormat[Gender] {
    def write(gender: Gender): JsValue =
      JsString(gender.toString)

    def read(value: JsValue): Gender = value match {
      case JsString(gender) =>
        gender.toLowerCase match {
          case "male"   => Male
          case "female" => Female
          case _        => Other(gender)
        }
      case _ => deserializationError("Gender expected")
    }
  }

  implicit object LocalDateFormat extends RootJsonFormat[LocalDate] {
    private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    def write(localDate: LocalDate): JsValue =
      JsString(localDate.format(formatter))

    def read(value: JsValue): LocalDate = value match {
      case JsString(localDate) =>
        LocalDate.parse(localDate, formatter)
      case _ => deserializationError("Date in a MM/dd/yyyy format expected")
    }
  }
}

trait ParselyJsonSupport extends SprayJsonSupport {
  import ParselyJsonSupport._

  implicit val recordFormat: RootJsonFormat[Record] = jsonFormat5(Record)

  type FromResponseEntityUnmarshaller[T] = Unmarshaller[ResponseEntity, T]
}
