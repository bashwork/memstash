package org.stash.lang

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class  StashSystemSpec extends FlatSpec with ShouldMatchers {

    behavior of "The StashSystem utility"

    it should "report system values" in {
        StashSystem.cpus should not be (0)
        StashSystem.time should not be (0)
        StashSystem.pid  should not be (0)
        StashSystem.bits should not be (null)
    }
}



