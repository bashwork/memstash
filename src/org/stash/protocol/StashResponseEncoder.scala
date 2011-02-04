package org.stash.protocol

import org.apache.mina

/**
 * @summary
 */
class StashResponseEncoder extends MessageEncoder[ResponseMessage]

    @throws(classOf[Exception])
    def encode(session:IoSession, response:ResponseMessage, output:ProtocolEncoderOutput) = {
        val buffer = IoBuffer.allocate(response.size)
        buffer.put(response.bytes)
        buffer.flip
        if (buffer.capacity > 0) out.write(buffer)
    }
}
