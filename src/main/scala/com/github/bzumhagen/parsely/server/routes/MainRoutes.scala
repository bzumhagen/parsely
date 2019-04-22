package com.github.bzumhagen.parsely.server.routes

import akka.http.scaladsl.model.StatusCodes.Created
import akka.http.scaladsl.server.{Directives, Route}
import com.github.bzumhagen.parsely.server.RecordManager
import com.github.bzumhagen.parsely.server.json.ParselyJsonSupport
import spray.json._

import scala.concurrent.ExecutionContext

class MainRoutes(manager: RecordManager)(implicit ec: ExecutionContext) extends Directives with ParselyJsonSupport {
  import DefaultJsonProtocol._

  def routes: Route =
    pathPrefix("records") {
      post {
        requestEntityPresent {
          entity(as[String]) { row =>
            complete(manager.record(row).map(_ => Created))
          }
        }
      } ~
      pathPrefix("gender") {
        get {
          complete(manager.get().map(_.byGender))
        }
      } ~
      pathPrefix("birthdate") {
        get {
          complete(manager.get().map(_.byDob))
        }
      } ~
      pathPrefix("name") {
        get {
          complete(manager.get().map(_.byLastName))
        }
      }
    }

}
