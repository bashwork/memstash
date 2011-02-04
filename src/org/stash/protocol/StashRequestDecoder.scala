package org.stash.protocol

import org.apache.mina
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter

/**
 * @summary
 */
class StashRequestDecoder extends MessageDecoderAdapter

    def decodable(session:IoSession, buffer:IoBuffer) = {
    }

    @throws(classOf[Exception])
    def decode(session:IoSession, request:ResponseMessage, output:ProtocolDecoderOutput) = {
        val buffer = IoBuffer.allocate(response.size)
        buffer.put(response.bytes)
        buffer.flip
        if (buffer.capacity > 0) out.write(buffer)
    }
}
