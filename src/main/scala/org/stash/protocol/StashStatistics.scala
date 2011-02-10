package org.stash.protocol

import scala.collection.mutable.{Map => MutableMap}
import java.io.Serializable
import java.util.concurrent.atomic.AtomicLong

/**
 * A wrapper around the statistics that are managed for
 * the memstach server instance.
 */
class StashStatistics extends Serializable {

    private val statistics = MutableMap[String, AtomicLong](
        "uptime" -> new AtomicLong(0),
        "curr_items" -> new AtomicLong(0),
        "total_items" -> new AtomicLong(0),
        "bytes" -> new AtomicLong(0),
        "curr_connections" -> new AtomicLong(0),
        "total_connections" -> new AtomicLong(0),
        "cmd_get" -> new AtomicLong(0),
        "cmd_set" -> new AtomicLong(0),
        "cmd_flush" -> new AtomicLong(0),
        "get_hits" -> new AtomicLong(0),
        "get_misses" -> new AtomicLong(0),
        "delete_hits" -> new AtomicLong(0),
        "delete_misses" -> new AtomicLong(0),
        "bytes_read" -> new AtomicLong(0),
        "bytes_written" -> new AtomicLong(0)
    )

    def all(): Iterator[(String, Long)]   = statistics.toIterator.map { case(k,v) => (k,v.get) }
    def get(key:String):Long              = guard(key) { _.get() }
    def set(key:String, value:Long)       = guard(key) { _.getAndSet(value) }
    def increment(key:String, value:Long) = guard(key) { _.addAndGet(value) } 
    def decrement(key:String, value:Long) = guard(key) { _.addAndGet(-value) }

    private def guard(key:String)(action:AtomicLong => Long):Long = {
        statistics.get(key).map { action } getOrElse { -1 }
    }
}

