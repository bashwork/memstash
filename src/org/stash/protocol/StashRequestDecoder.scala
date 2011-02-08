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
    def decode(session:IoSession, request:StashResponse, output:ProtocolDecoderOutput)
        : MessageDecoderResult = {

        val buffer = IoBuffer.allocate(request.size)
        buffer.put(request.bytes)
        buffer.flip
        if (buffer.capacity > 0) output.write(buffer)
    }
}
