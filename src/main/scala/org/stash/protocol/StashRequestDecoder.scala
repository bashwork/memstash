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

    def parse(line:String) : StashRequest = {
        var request = new StashRequest()
        val pieces  = line.split(" ").map { _.trim }

        pieces.head.toUpperCase match {
            case cmd @ ("SET" | "CAS" | "PREPEND" | "APPEND" | "ADD" | "REPLACE") => {
                request.command = cmd
                request.key     = pieces(1)
                request.flags   = pieces(2).toInt
                request.expire  = pieces(3).toLong
                request.bytes   = pieces(4).getBytes
                request.reply   = pieces.contains("noreply")
            }
            case cmd @ ("INCR" | "DECR") => {
                request.command = cmd
                request.key     = pieces(1)
                request.bytes   = pieces.lift(2).getOrElse("1").getBytes
                request.reply   = shouldReply(pieces)
            }
            case cmd @ ("GET" | "GETS") => {
                request.command = cmd
                request.options = pieces.tail
            }
            case cmd @ ("DELETE") => {
                request.command = cmd
                request.key     = pieces(1)
                request.reply   = shouldReply(pieces)
            }
            case cmd @ _ => {
                request.command = if (!commands.contains(cmd))
                    "ERROR" else cmd
                request.reply   = shouldReply(pieces)
            }
        }

        return request
    }

    private def shouldReply(commands:Array[String]) =
        commands.last.toLowerCase.equals("noreply")
}
