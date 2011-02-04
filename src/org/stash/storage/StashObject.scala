package org.stash.storage

import java.io.Serializable

class StashObject(key:String, flags:Int, expire:Int, length:Int) extends Serializable {
    var value: Array[Byte] = _

    def this(key: String) = this(key, 0,0,0)
}
