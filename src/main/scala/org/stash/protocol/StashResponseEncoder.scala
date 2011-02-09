package org.stash.protocol

import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolEncoderOutput
import org.apache.mina.filter.codec.demux.MessageEncoder

/**
 * @summary
 */
class StashResponseEncoder extends MessageEncoder[StashResponse] {

    @throws(classOf[Exception])
    def encode(session:IoSession, response:StashResponse, output:ProtocolEncoderOutput) = {
        val buffer = IoBuffer.allocate(response.size)
        buffer.put(response.bytes)
        buffer.flip
        if (buffer.capacity > 0) output.write(buffer)
    }
}
