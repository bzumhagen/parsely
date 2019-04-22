package com.github.bzumhagen.parsely.server.routes

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import akka.Done
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler, Route}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.github.bzumhagen.parsely.server.RecordManager
import com.github.bzumhagen.parsely.server.json.ParselyJsonSupport
import com.github.bzumhagen.parsely.{Female, Male, Record, Records}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

class MainRoutesTest extends FlatSpec with Matchers with ScalatestRouteTest with MockFactory with ParselyJsonSupport {
  implicit val exceptionHandler: ExceptionHandler = ParselyHandlers.exceptionHandler
  implicit val rejectionHandler: RejectionHandler = ParselyHandlers.rejectionHandler
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  "MainRoutes" should "return a 201 created for POST requests to /records with valid string input" in {
    val manager = mock[RecordManager]
    val mainRoutes = new MainRoutes(manager)
    val expectedInput = "some record"

    (manager.record _).expects(expectedInput).returns(Future.successful(Done))

    Post("/records", expectedInput) ~> mainRoutes.routes ~> check {
      status shouldEqual StatusCodes.Created
    }
  }

  it should "return a 400 bad request for POST requests to /records with missing input" in {
    val manager = mock[RecordManager]
    val mainRoutes = new MainRoutes(manager)

    Post("/records") ~> Route.seal(mainRoutes.routes) ~> check {
      status shouldEqual StatusCodes.BadRequest
    }
  }

  it should "return a 400 bad request for POST requests to /records with empty input" in {
    val manager = mock[RecordManager]
    val mainRoutes = new MainRoutes(manager)

    Post("/records", "") ~> Route.seal(mainRoutes.routes) ~> check {
      status shouldEqual StatusCodes.BadRequest
    }
  }

  it should "return records sorted by gender for GET requests to /records/gender" in {
    val manager = mock[RecordManager]
    val mainRoutes = new MainRoutes(manager)
    val expectedRecords =
      Seq(
        Record("Jane", "Doey", Female, "Blue", LocalDate.parse("01/02/1990", formatter)),
        Record("John", "Doe", Male, "Blue", LocalDate.parse("01/02/1990", formatter))
      )

    (manager.get _).expects().returns(Future.successful(Records(expectedRecords.toSet)))

    Get("/records/gender") ~> mainRoutes.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[Seq[Record]] shouldEqual expectedRecords
    }
  }

  it should "return records sorted by date of birth for GET requests to /records/birthdate" in {
    val manager = mock[RecordManager]
    val mainRoutes = new MainRoutes(manager)
    val expectedRecords =
      Seq(
        Record("Jane", "Doey", Female, "Blue", LocalDate.parse("01/02/1990", formatter)),
        Record("John", "Doe", Male, "Blue", LocalDate.parse("01/02/1990", formatter))
      )

    (manager.get _).expects().returns(Future.successful(Records(expectedRecords.toSet)))

    Get("/records/birthdate") ~> mainRoutes.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[Seq[Record]] shouldEqual expectedRecords
    }
  }

  it should "return records sorted by last name for GET requests to /records/name" in {
    val manager = mock[RecordManager]
    val mainRoutes = new MainRoutes(manager)
    val expectedRecords =
      Seq(
        Record("Jane", "Doey", Female, "Blue", LocalDate.parse("01/02/1990", formatter)),
        Record("John", "Doe", Male, "Blue", LocalDate.parse("01/02/1990", formatter))
      )

    (manager.get _).expects().returns(Future.successful(Records(expectedRecords.toSet)))

    Get("/records/name") ~> mainRoutes.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[Seq[Record]] shouldEqual expectedRecords
    }
  }

}
