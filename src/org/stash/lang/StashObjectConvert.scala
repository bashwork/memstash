package org.stash.lang

import org.stash.storage.StashObject
import org.stash.protocol.StashRequest

/**
 * @summary
 */
object StashObjectConvert {

    /**
     * @summary An implicit conversion from a StashRequest to a StashObject
     * @returns An initialized stash object
     */
    implicit def fromStashRequest(en: StashRequest) : StashObject = {
        val result = new StashObject(en.key, en.flags, en.expire, en.size)
        result.value = en.bytes
        result
    }
}
