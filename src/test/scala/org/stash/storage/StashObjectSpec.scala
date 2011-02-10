package org.stash.storage

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class StashObjectSpec extends FlatSpec
    with ShouldMatchers with StashObjectPropertyMatchers {

    "The StashObject" should "be constructed correctly" in {
        val storage = new StashObject("key")
        storage should have (key("key"), flags(0), expire(0), size(0))
    }
}



