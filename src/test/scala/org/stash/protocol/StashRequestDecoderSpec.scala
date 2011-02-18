package org.stash.protocol

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class  StashRequestDecoderSpec extends FlatSpec
    with ShouldMatchers with StashRequestPropertyMatchers {

    behavior of "StashRequestDecoder"

    it should "know all commands to process" in {
        val commands = List(
            "SET", "ADD", "INCR", "DECR",
            "APPEND", "PREPEND", "REPLACE",
            "GET", "GETS", "STATS", "DELETE", "CAS",
            "QUIT", "VERSION", "ERROR", "FLUSH_ALL")
        StashRequestDecoder.commands should be (commands)
    }

    it should "process simple commands" in {
        StashRequestDecoder.parse("STATS") should have (command ("STATS"), reply (true))
        StashRequestDecoder.parse("VERSION") should have (command ("VERSION"), reply (true))
        StashRequestDecoder.parse("QUIT") should have (
            command ("QUIT"), reply (true))
        StashRequestDecoder.parse("QUIT noreply") should have (
            command ("QUIT"), reply (false))
        StashRequestDecoder.parse("FLUSH_ALL") should have (
            command ("FLUSH_ALL"), reply (true))
        StashRequestDecoder.parse("FLUSH_ALL noreply") should have (
            command ("FLUSH_ALL"), reply (false))
    }

    it should "fail for invalid commands" in {
        StashRequestDecoder.parse("ERROR") should have (command ("ERROR"))
        StashRequestDecoder.parse("Something") should have (command ("ERROR"))
        StashRequestDecoder.parse("") should have (command ("ERROR"))
    }

    it should "process DELETE command" in {
        StashRequestDecoder.parse("DELETE key") should have (
            command ("DELETE"), key ("key"), reply (true))
        StashRequestDecoder.parse("DELETE next noreply") should have (
            command ("DELETE"), key ("next"), reply (false))
    }

    it should "process GET command" in {
        StashRequestDecoder.parse("GET key") should have (
            command ("GET"), extra (List("key")), reply (true))
        StashRequestDecoder.parse("GET key1 key2 key3") should have (
            command ("GET"), extra (List("key1","key2","key3")), reply (true))
    }

    it should "process GETS command" in {
        StashRequestDecoder.parse("GETS key1 key2 key3") should have (
            command ("GETS"), extra (List("key1","key2","key3")), reply (true))
        StashRequestDecoder.parse("GETS key") should have (
            command ("GETS"), extra (List("key")), reply (true))
    }

    it should "process INCR command" in {
        StashRequestDecoder.parse("INCR key") should have (
            command ("INCR"), data ("1".getBytes), reply (true))
        StashRequestDecoder.parse("INCR key 22") should have (
            command ("INCR"), data ("22".getBytes), reply (true))
        StashRequestDecoder.parse("INCR key 1 noreply") should have (
            command ("INCR"), data ("1".getBytes), reply (false))
    }

    it should "process DECR command" in {
        StashRequestDecoder.parse("DECR key") should have (
            command ("DECR"), data ("1".getBytes), reply (true))
        StashRequestDecoder.parse("DECR key 22") should have (
            command ("DECR"), data ("22".getBytes), reply (true))
        StashRequestDecoder.parse("DECR key 1 noreply") should have (
            command ("DECR"), data ("1".getBytes), reply (false))
    }

    it should "process SET command" in {
        StashRequestDecoder.parse("SET key 1 1 1000") should have (
            command ("SET"), flags(1), expire(1), size (1000), reply (true))
        StashRequestDecoder.parse("SET key 1 1 1000 noreply") should have (
            command ("SET"), flags(1), expire(1), size (1000), reply (false))
    }

    it should "process ADD command" in {
        StashRequestDecoder.parse("ADD key 1 1 1000") should have (
            command ("ADD"), flags(1), expire(1), size (1000), reply (true))
        StashRequestDecoder.parse("ADD key 1 1 1000 noreply") should have (
            command ("ADD"), flags(1), expire(1), size (1000), reply (false))
    }

    it should "process REPLACE command" in {
        StashRequestDecoder.parse("REPLACE key 1 1 1000") should have (
            command ("REPLACE"), flags(1), expire(1), size (1000), reply (true))
        StashRequestDecoder.parse("REPLACE key 1 1 1000 noreply") should have (
            command ("REPLACE"), flags(1), expire(1), size (1000), reply (false))
    }

    it should "process PREPEND command" in {
        StashRequestDecoder.parse("PREPEND key 1 1 1000") should have (
            command ("PREPEND"), flags(1), expire(1), size (1000), reply (true))
        StashRequestDecoder.parse("PREPEND key 1 1 1000 noreply") should have (
            command ("PREPEND"), flags(1), expire(1), size (1000), reply (false))
    }

    it should "process APPEND command" in {
        StashRequestDecoder.parse("APPEND key 1 1 1000") should have (
            command ("APPEND"), flags(1), expire(1), size (1000), reply (true))
        StashRequestDecoder.parse("APPEND key 1 1 1000 noreply") should have (
            command ("APPEND"), flags(1), expire(1), size (1000), reply (false))
    }

    it should "process CAS command" in { // TODO options for cas
        StashRequestDecoder.parse("CAS key 1 1 1000") should have (
            command ("CAS"), flags(1), expire(1), size (1000), reply (true))
        StashRequestDecoder.parse("CAS key 1 1 1000 noreply") should have (
            command ("CAS"), flags(1), expire(1), size (1000), reply (false))
    }
}



