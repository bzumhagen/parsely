package com.github.bzumhagen.parsely.server.json

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.github.bzumhagen.parsely.{Female, Male, Other, Record}
import org.scalatest.{FlatSpec, Matchers}
import spray.json._

class ParselyJsonSupportTest extends FlatSpec with Matchers with ParselyJsonSupport {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  "ParselyJsonSupport" should "serialize and deserialize records" in {
    val recordMale = Record("John", "Doe", Male, "Blue", LocalDate.parse("01/02/1990", formatter))
    val recordFemale = Record("Jane", "Doe", Female, "Blue", LocalDate.parse("01/02/1990", formatter))
    val recordOther = Record("Rain", "Doe", Other("Fluid"), "Blue", LocalDate.parse("01/02/1990", formatter))

    Seq(recordMale, recordFemale, recordOther).map { record =>
      val serialized = record.toJson.prettyPrint
      val deserialized = serialized.parseJson.convertTo[Record]
      record shouldBe deserialized
    }
  }
}
