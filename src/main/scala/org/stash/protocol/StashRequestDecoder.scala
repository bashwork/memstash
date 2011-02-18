package org.stash.protocol

import java.io.ByteArrayOutputStream
import org.apache.mina.core.session.IoSession
import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.filter.codec.ProtocolDecoderOutput
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter
import org.apache.mina.filter.codec.demux.MessageDecoderResult

/**
 * Mina message decoder implementation for the memcached
 * protocol.
 */
class StashRequestDecoder extends MessageDecoderAdapter {

    /**
     * The unique identifier used to retrieve session data
     */
    private val identifier = "stash-session-data"

    /**
     * Mina implementation to see if the request is decodeable
     *
     * @param session The current context for this session
     * @param buffer The current receieve buffer for this session
     * @return The MessageDecoderResult.{NEED_DATA or OK}
     */
    def decodable(session:IoSession, buffer:IoBuffer)
        : MessageDecoderResult = {

        val request = session.getAttribute(identifier).asInstanceOf[StashRequest]
        if (request != null && buffer.remaining <= request.size)
            MessageDecoderResult.NEED_DATA else MessageDecoderResult.OK
    }

    /**
     * Mina implementation to decode the request
     *
     * @param session The current context for this session
     * @param buffer The current receieve buffer for this session
     * @param output The buffer to send the output result
     * @throws Exception
     * @return The MessageDecoderResult.{NEED_DATA or OK}
     */
    @throws(classOf[Exception])
    def decode(session:IoSession, buffer:IoBuffer, output:ProtocolDecoderOutput)
        : MessageDecoderResult = {

        val stream = new ByteArrayOutputStream(50)

        session.getAttribute(identifier) match {
            case request:StashRequest => {
                if (buffer.remaining > request.size) {
                    request.more = false // no more data to get
                    request.data = new Array[Byte](request.size)
                    buffer.get(request.data)
                    session.setAttribute(identifier, null)
                    output.write(request)
                    return MessageDecoderResult.OK
                }
            }
            case null => {
                buffer.mark // set current buffer position
                do {
                    val c = buffer.get.asInstanceOf[Char]
                    stream.write(c)
                    if (c == '\r' && buffer.hasRemaining && buffer.get == '\n') {
                        stream.close
                        val request = StashRequestDecoder.parse(stream.toString)
                        if (request.more) session.setAttribute(identifier, request)
                        else output.write(request)
                        return MessageDecoderResult.OK
                    }
                } while (buffer.hasRemaining)
            }
        }
       
        buffer.reset // force re-read of buffer
        MessageDecoderResult.NEED_DATA
    }
}

/**
 * Factory used to decode the memcached commands into
 * valid StashRequest instances.
 */
object StashRequestDecoder {

    /**
     * The list of currently supported commands
     */
    val commands = List(
        "SET", "ADD", "INCR", "DECR",
        "APPEND", "PREPEND", "REPLACE",
        "GET", "GETS", "STATS", "DELETE", "CAS",
        "QUIT", "VERSION", "ERROR", "FLUSH_ALL")

    /**
     * Used to parse the incoming memcached request
     *
     * @param line The line to decode
     * @return The decoded StashRequest
     */
    def parse(line:String) : StashRequest = {
        var request = new StashRequest()
        val pieces  = line.split(" ").map { _.trim }

        pieces.head.toUpperCase match {
            case cmd @ ("SET" | "CAS" | "PREPEND" | "APPEND" | "ADD" | "REPLACE") => {
                request.command = cmd
                request.key     = pieces(1)
                request.flags   = pieces(2).toInt
                request.expire  = pieces(3).toLong
                request.size    = pieces(4).toInt
                request.reply   = shouldReply(pieces)
                request.more    = true
            }
            case cmd @ ("INCR" | "DECR") => {
                request.command = cmd
                request.key     = pieces(1)
                request.data    = pieces.lift(2).getOrElse("1").getBytes
                request.reply   = shouldReply(pieces)
            }
            case cmd @ ("GET" | "GETS") => {
                request.command = cmd
                request.extra   = pieces.tail.toList
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

    /**
     * Helper method used to determine if a command should reply
     *
     * @param commands Commands to search for the noreply flag
     * @return true if we should reply, false otherwise
     */
    private def shouldReply(commands:Array[String]) =
        ! commands.last.toLowerCase.equals("noreply")
}
