package org.stash.protocol

import java.io.Serializable

class StashRequest(val key: String, val flags:Int, val expire: Int, val value:Array[Byte])
    extends Serializable {

    def this(key: String, value:Array[Byte]) = this(key, 0, 0, value)
}
