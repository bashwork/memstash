package org.stash.protocol

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory

/**
 * Factory used to generate a deumxuing protocol for the memstash
 * server.
 *
 * @param isClient A flag indicating if this is a client or a server
 */
class StashCodecFactory(val isClient:Boolean) extends DemuxingProtocolCodecFactory {

    if (isClient) {
        addMessageDecoder(classOf[StashResponseDecoder])
        addMessageEncoder(classOf[StashRequest], classOf[StashRequestEncoder])
    } else {
        addMessageDecoder(classOf[StashRequestDecoder])
        addMessageEncoder(classOf[StashResponse], classOf[StashResponseEncoder])
    }
}
