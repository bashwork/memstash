package org.stash.protocol

import java.io.Serializable
import java.io.ByteArrayOutputStream

class StashRequest(val command:String, val key: String, val flags:Int, val expire: Int)
    extends Serializable {
    private val buffer:ByteArrayOutputStream = new ByteArrayOutputStream()

    def this(command: String, key: String) = this(command, key, 0, 0)


    def bytes:Array[Byte] = buffer.toByteArray
    def size:Int = buffer.size

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
