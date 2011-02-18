package org.stash.protocol

import org.scalatest.matchers.HavePropertyMatcher
import org.scalatest.matchers.HavePropertyMatchResult

trait StashRequestPropertyMatchers {

    class CommandMatcher(expected: String) extends HavePropertyMatcher[StashRequest, String] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.command == expected, "command", expected, request.command)
    }
    def command(expected:String) = new CommandMatcher(expected)

    class KeyMatcher(expected: String) extends HavePropertyMatcher[StashRequest, String] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.key == expected, "key", expected, request.key)
    }
    def key(expected:String) = new KeyMatcher(expected)

    class FlagsMatcher(expected: Int) extends HavePropertyMatcher[StashRequest, Int] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.flags == expected, "flags", expected, request.flags)
    }
    def flags(expected:Int) = new FlagsMatcher(expected)

    class ReplyMatcher(expected: Boolean) extends HavePropertyMatcher[StashRequest, Boolean] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.reply == expected, "reply", expected, request.reply)
    }
    def reply(expected:Boolean) = new ReplyMatcher(expected)

    class ExpireMatcher(expected: Long) extends HavePropertyMatcher[StashRequest, Long] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.expire == expected, "expire", expected, request.expire)
    }
    def expire(expected:Long) = new ExpireMatcher(expected)

    class ExtraMatcher(expected: Any) extends HavePropertyMatcher[StashRequest, Any] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.extra == expected, "extra", expected, request.extra)
    }
    def extra(expected:Any) = new ExtraMatcher(expected)

    class SizeMatcher(expected: Int) extends HavePropertyMatcher[StashRequest, Int] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.size == expected, "size", expected, request.size)
    }
    def size(expected:Int) = new SizeMatcher(expected)

    class DataMatcher(expected: Array[Byte]) extends HavePropertyMatcher[StashRequest, Array[Byte]] {
        def apply(request: StashRequest) =
            new HavePropertyMatchResult(request.data.toList == expected.toList, "data", expected, request.data)
    }
    def data(expected:Array[Byte]) = new DataMatcher(expected)
}
