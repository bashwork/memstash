package org.stash

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class  ApplicationSpec extends FlatSpec with ShouldMatchers {

    "The Application" should "report its current version" in {
        Application.Version should be ("1.0.0")
    }
}



