package com.github.bzumhagen.parsely.server.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directives, ExceptionHandler, RejectionHandler, RequestEntityExpectedRejection}

object ParselyHandlers extends Directives {
  val rejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case RequestEntityExpectedRejection =>
          complete(HttpResponse(StatusCodes.BadRequest, entity = "Expected entity but found none"))
      }.result()
  val exceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case e: UnsupportedOperationException  =>
        complete(HttpResponse(StatusCodes.BadRequest, entity = e.getMessage))
    }
}
