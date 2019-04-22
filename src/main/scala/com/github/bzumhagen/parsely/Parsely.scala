package com.github.bzumhagen.parsely

import java.io.OutputStreamWriter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler}
import akka.stream.ActorMaterializer
import better.files.File
import com.github.bzumhagen.parsely.console.Outputter
import com.github.bzumhagen.parsely.server.RecordManager
import com.github.bzumhagen.parsely.server.routes.{MainRoutes, ParselyHandlers}
import com.github.bzumhagen.parsely.server.storage.InMemoryStorage
import scopt.OParser

import scala.io.StdIn

object Parsely extends App {
  case class Arguments(fileToParse: Option[File] = None)

  override def main(args: Array[String]): Unit = {
    val builder = OParser.builder[Arguments]
    val parser = {
      import builder._
      OParser.sequence(
        programName(BuildInfo.name),
        head(BuildInfo.name, BuildInfo.version),
        opt[String]('f', "file")
          .action((path, arguments) => arguments.copy(fileToParse = Some(File(path))))
          .text("path to the input file")
      )
    }

    OParser.parse(parser, args, Arguments()) match {
      case Some(arguments) =>
        arguments.fileToParse match {
          case Some(file) => processFile(file)
          case None       => startServer()
        }
      case _ =>
        // arguments are bad, error message will have been displayed
    }
  }

  private def processFile(file: File): Unit = {
    val parser = new Parser
    val records = parser.parse(file)
    val outputter = new Outputter(new OutputStreamWriter(System.out))

    outputter.outputTable("By Gender (ASC)", records.byGender)
    outputter.outputTable("By Date of Birth (ASC)", records.byDob)
    outputter.outputTable("By Last Name (DESC)", records.byLastName)
  }

  private def startServer(): Unit = {
    implicit val exceptionHandler: ExceptionHandler = ParselyHandlers.exceptionHandler
    implicit val rejectionHandler: RejectionHandler = ParselyHandlers.rejectionHandler

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val bindingFuture = Http().bindAndHandle(new MainRoutes(new RecordManager(new InMemoryStorage, new Parser)).routes, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
