package org.stash.protocol

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class  StashStatisticsSpec extends FlatSpec with ShouldMatchers {

    behavior of "StashStatistics Manager"

    val statistics = new StashStatistics()
    val keys = List[String]("uptime", "curr_items", "total_items", "bytes", "curr_connections",
        "total_connections", "cmd_get", "cmd_set", "cmd_flush", "get_hits",
        "get_misses", "delete_hits", "delete_misses", "bytes_read", "bytes_written");

    it should "increment keys it knows" in {
        keys.foreach { statistics.get(_) should be (0) }
        keys.foreach { statistics.increment(_,1) should be (1) }
    }

    it should "decrement keys it knows" in {
        keys.foreach { statistics.get(_) should be (1) }
        keys.foreach { statistics.decrement(_,1) should be (0) }
    }

    it should "get and set keys it knows" in {
        val key = "uptime"
        statistics.set(key, 100) should be (0)
        statistics.get(key) should be (100)
    }

    it should "ignore keys it does not know" in {
        val key = "something"
        statistics.set(key, 100) should be (-1)
        statistics.get(key) should be (-1)
        statistics.decrement(key, 10) should be (-1)
        statistics.increment(key, 10) should be (-1)
    }

    it should "retrieve all keys" in {
        statistics.all.map { _._1 } foreach { keys should contain (_) }
    }
}



