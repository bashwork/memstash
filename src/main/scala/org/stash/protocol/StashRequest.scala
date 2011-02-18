package org.stash.protocol

import java.io.Serializable

class StashRequest extends Serializable {

    var command:String     = "ERROR"
    var more:Boolean       = false
    var reply:Boolean      = true
    var flags:Int          = 0x00
    var key:String         = _
    var expire:Long        = 0
    var size:Int           = 0
    var extra:List[String] = _
    var data:Array[Byte]   = _
}
