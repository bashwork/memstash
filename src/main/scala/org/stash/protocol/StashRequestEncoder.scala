package org.stash.protocol

import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.ProtocolEncoderOutput
import org.apache.mina.filter.codec.demux.MessageEncoder

/**
 * @summary
 */
class StashRequestEncoder extends MessageEncoder[StashRequest] {

    /**
     * @summary
     * @param session
     * @param request
     * @param output
     */
    @throws(classOf[Exception])
    def encode(session:IoSession, request:StashRequest, output:ProtocolEncoderOutput) = {
        val buffer = IoBuffer.allocate(request.size)
        buffer.put(request.data)
        buffer.flip
        if (buffer.capacity > 0) output.write(buffer)
    }
}
