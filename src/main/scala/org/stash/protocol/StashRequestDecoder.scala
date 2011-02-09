package org.stash.protocol

import org.apache.mina.core.session.IoSession
import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.filter.codec.ProtocolDecoderOutput
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter
import org.apache.mina.filter.codec.demux.MessageDecoderResult

/**
 * @summary
 */
class StashRequestDecoder extends MessageDecoderAdapter {

    private val identifier = "stash-session-data"

    def decodable(session:IoSession, buffer:IoBuffer)
        : MessageDecoderResult = {

        val request = session.getAttribute(identifier).asInstanceOf[StashRequest]
        if (request != null && buffer.remaining <= request.size)
            MessageDecoderResult.NEED_DATA else MessageDecoderResult.OK
    }

    @throws(classOf[Exception])
    def decode(session:IoSession, buffer:IoBuffer, output:ProtocolDecoderOutput)
        : MessageDecoderResult = {

        var result = MessageDecoderResult.NEED_DATA

        session.getAttribute(identifier) match {
            case request:StashRequest => {
                result = MessageDecoderResult.OK
            }
            case null => {
                result = MessageDecoderResult.OK
            }
        }
       
        buffer.reset 
        result
    }
}

object StashRequestDecoder {
    val commands = List(
        "SET", "ADD", "INCR", "DECR",
        "APPEND", "PREPEND", "REPLACE",
        "GET", "GETS", "STATS", "DELETE", "CAS",
        "QUIT", "VERSION", "ERROR", "FLUSH_ALL")

    def parse(line:String):StashRequest = {
        return new StashRequest("fake", "fake")
    }
}
