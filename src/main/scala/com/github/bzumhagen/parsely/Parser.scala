package com.github.bzumhagen.parsely

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import better.files.File

sealed trait Delimiter { val value: Char }
case object Pipe extends Delimiter { val value = '|' }
case object Comma extends Delimiter { val value = ',' }
case object Space extends Delimiter { val value = ' ' }

class Parser {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  private def parseRecord(line: String, delimiter: Delimiter): Record = {
    val rawRecord =
      delimiter match {
        case Pipe  => line.split(s" [${Pipe.value}] ")
        case Comma => line.split(s"${Comma.value} ")
        case Space => line.split(s"${Space.value}")
      }
    if (rawRecord.size != 5) {
      throw new UnsupportedOperationException(s"Parsed record doesn't have all required fields [$line]")
    } else {
      Record(
        lastName = rawRecord(0),
        firstName = rawRecord(1),
        gender = Gender(rawRecord(2)),
        favoriteColor = rawRecord(3),
        dob = LocalDate.parse(rawRecord(4), formatter)
      )
    }
  }

  def parse(line: String): Record = {
    identifyDelimiter(line).map { delimiter =>
      parseRecord(line, delimiter)
    } getOrElse (throw new UnsupportedOperationException(s"Unable to identify a supported delimiter [$line]"))
  }

  def parse(file: File): Records = {
    val iterator = file.lineIterator
    if (iterator.isEmpty) {
      Records(Set.empty)
    } else {
      val firstLine = iterator.next()
      identifyDelimiter(firstLine).map { delimiter =>
        val records = (Seq(firstLine) ++ iterator).map(parseRecord(_, delimiter)).toSet
        Records(records)
      } getOrElse (throw new UnsupportedOperationException(s"Unable to identify a supported delimiter [$firstLine]"))
    }
  }

  private def identifyDelimiter(sample: String): Option[Delimiter] = {
    def parseForDelimiter(index: Int = 0, delimiter: Option[Delimiter] = None) : Option[Delimiter] = {
      if (index >= sample.length) {
        None
      } else {
        sample(index) match {
          case Pipe.value  => Some(Pipe)
          case Comma.value => Some(Comma)
          case Space.value => delimiter.orElse(parseForDelimiter(index + 1, Some(Space)))
          case _           => parseForDelimiter(index + 1, delimiter)
        }
      }
    }
    parseForDelimiter()
  }
}
