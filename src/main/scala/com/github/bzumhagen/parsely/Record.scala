package com.github.bzumhagen.parsely

import java.time.LocalDate

sealed trait Gender
case object Gender {
  def apply(value: String): Gender = value.toLowerCase match {
    case "male"   => Male
    case "female" => Female
    case _        => Other(value)
  }
}
case object Male extends Gender
case object Female extends Gender
case class Other(gender: String) extends Gender {
  override def toString: String = gender
}

case class Record(
  firstName: String,
  lastName: String,
  gender: Gender,
  favoriteColor: String,
  dob: LocalDate
)

case class Records(set: Set[Record]) {
  def byGender: Seq[Record] =
    set.toSeq.sortBy(r => (r.gender.toString.toLowerCase, r.lastName))
  def byDob: Seq[Record] =
    set.toSeq.sortWith((first, second) => first.dob.isBefore(second.dob))
  def byLastName: Seq[Record] =
    set.toSeq.sortWith(_.lastName > _.lastName)
}