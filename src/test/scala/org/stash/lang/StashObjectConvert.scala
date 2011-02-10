package org.stash.lang

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import org.stash.protocol.StashRequest
import org.stash.storage.{StashObject, StashObjectPropertyMatchers}

class  StashObjectConvertSpec extends FlatSpec
    with ShouldMatchers with StashObjectPropertyMatchers {

    behavior of "The StashObjectConvert utility"

    val request = new StashRequest()
    request.key = "key"

    it should "convert a StashRequest to a StashObject" in {
        val storage = StashObjectConvert.fromStashRequest(request)
        storage should have (key("key"), flags(0), expire(0), size(0))
    }
}



