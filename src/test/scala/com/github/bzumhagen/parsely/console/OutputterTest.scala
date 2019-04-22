package com.github.bzumhagen.parsely.console

import java.io.{OutputStream, OutputStreamWriter}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.github.bzumhagen.parsely.{Female, Male, Other, Record}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class OutputterTest extends FlatSpec with Matchers with MockFactory {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

  "Outputter" should "output records in a table view" in {
    class MockOutputStreamWriter extends OutputStreamWriter(mock[OutputStream])
    val writer = mock[MockOutputStreamWriter]
    val outputter = new Outputter(writer)
    val records = Seq(
      Record("Jane", "Doeo", Female, "Red", LocalDate.parse("10/12/1980", formatter)),
      Record("Rain", "Doee", Other("Fluid"), "Black", LocalDate.parse("12/26/1992", formatter)),
      Record("John", "Doey", Male, "Blue", LocalDate.parse("01/02/1970", formatter))
    )
    val expectedTitle = "Test Records"
    val expectedTable =
      s"""|+----------+---------+------+--------------+-------------+
         ||First Name|Last Name|Gender|Favorite Color|Date of Birth|
         |+----------+---------+------+--------------+-------------+
         ||      ${records.head.firstName}|     ${records.head.lastName}|${records.head.gender}|           ${records.head.favoriteColor}|   ${formatter.format(records.head.dob)}|
         ||      ${records(1).firstName}|     ${records(1).lastName}| ${records(1).gender}|         ${records(1).favoriteColor}|   ${formatter.format(records(1).dob)}|
         ||      ${records(2).firstName}|     ${records(2).lastName}|  ${records(2).gender}|          ${records(2).favoriteColor}|   ${formatter.format(records(2).dob)}|
         |+----------+---------+------+--------------+-------------+""".stripMargin

    (writer.write(_: Int)).expects('\n').returns()
    (writer.write(_: String)).expects(expectedTitle).returns()
    (writer.write(_: Int)).expects('\n').returns()
    (writer.write(_: String)).expects(expectedTable).returns()
    (writer.write(_: Int)).expects('\n').returns()
    (writer.flush _).expects().returns()

    outputter.outputTable(expectedTitle, records)
    succeed
  }
}
