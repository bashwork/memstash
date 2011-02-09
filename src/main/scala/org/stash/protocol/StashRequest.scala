package org.stash.protocol

import java.io.Serializable
import java.io.ByteArrayOutputStream

class StashRequest extends Serializable {

    private val buffer:ByteArrayOutputStream = new ByteArrayOutputStream()

    var command:String = "ERROR"
    var reply:Boolean  = true
    var flags:Int      = 0x00
    var key:String     = _
    var expire:Long    = 0
    var options:Any    = _
    var bytes:Array[Byte] = _


    //def bytes:Array[Byte] = buffer.toByteArray
    def size:Int = bytes.size

    def write(message:Array[Byte]) = {
        buffer.write(message)
        buffer.write("\r\n" getBytes)
    }

    def write(message:String) = {
        buffer.write(message getBytes)
        if (!message.contains("\r\n"))
            buffer.write("\r\n" getBytes)
    }
}
