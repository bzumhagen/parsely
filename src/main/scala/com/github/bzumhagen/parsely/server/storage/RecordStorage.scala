package com.github.bzumhagen.parsely.server.storage

import akka.Done
import com.github.bzumhagen.parsely.{Record, Records}

import scala.collection.parallel.mutable
import scala.concurrent.{ExecutionContext, Future}

trait RecordStorage {
  def write(record: Record): Future[Done]
  def readAll(): Future[Records]
}

class InMemoryStorage(implicit ec: ExecutionContext) extends RecordStorage {
  private val storage = mutable.ParSet[Record]()

  override def write(record: Record): Future[Done] =
    Future.successful{
      storage + record
      Done
    }

  override def readAll(): Future[Records] =
    Future.successful(Records(storage.seq.toSet))
}
