package org.stash.storage

import java.io.Serializable

class StashObject(var key:String, var flags:Int, var expire:Long, var size:Int)
    extends Serializable {

    var bytes: Array[Byte] = _

    def this(key: String) = this(key, 0,0,0)
}
