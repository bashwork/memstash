package org.stash.storage

import org.scalatest.matchers.HavePropertyMatcher
import org.scalatest.matchers.HavePropertyMatchResult

trait StashObjectPropertyMatchers {

    class KeyMatcher(expected: String) extends HavePropertyMatcher[StashObject, String] {
        def apply(stash: StashObject) =
            new HavePropertyMatchResult(stash.key == expected, "key", expected, stash.key)
    }
    def key(expected:String) = new KeyMatcher(expected)

    class FlagsMatcher(expected: Int) extends HavePropertyMatcher[StashObject, Int] {
        def apply(stash: StashObject) =
            new HavePropertyMatchResult(stash.flags == expected, "flags", expected, stash.flags)
    }
    def flags(expected:Int) = new FlagsMatcher(expected)

    class ExpireMatcher(expected: Long) extends HavePropertyMatcher[StashObject, Long] {
        def apply(stash: StashObject) =
            new HavePropertyMatchResult(stash.expire == expected, "expire", expected, stash.expire)
    }
    def expire(expected:Long) = new ExpireMatcher(expected)

    class SizeMatcher(expected: Int) extends HavePropertyMatcher[StashObject, Int] {
        def apply(stash: StashObject) =
            new HavePropertyMatchResult(stash.size == expected, "size", expected, stash.size)
    }
    def size(expected:Int) = new SizeMatcher(expected)
}
