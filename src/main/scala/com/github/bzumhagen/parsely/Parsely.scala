package com.github.bzumhagen.parsely

import better.files.File
import com.github.bzumhagen.parsely.parse.Parser
import scopt.OParser

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
          case None       => println("No file provided")
        }
      case _ =>
        // arguments are bad, error message will have been displayed
    }
  }

  private def processFile(file: File): Unit = {
    val parser = new Parser
    parser.parse(file).foreach(println)
  }
}
