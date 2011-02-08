package org.stash.storage

import java.io.Serializable

class StashObject(var key:String, var flags:Int, var expire:Int, var length:Int)
    extends Serializable {

    var value: Array[Byte] = _

    def this(key: String) = this(key, 0,0,0)
}
