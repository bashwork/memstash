package org.stash.protocol

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory

/**
 * Factory used to generate a deumxuing protocol for the memstash
 * server.
 */
class StashCodecFactory extends DemuxingProtocolCodecFactory {

    addMessageDecoder(classOf[StashRequestDecoder])
    addMessageEncoder(classOf[StashResponse], classOf[StashResponseEncoder])
}
