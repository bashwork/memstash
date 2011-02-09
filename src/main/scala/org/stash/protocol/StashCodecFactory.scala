package org.stash.protocol

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory

/**
 */
class StashCodecFactory extends DemuxingProtocolCodecFactory {

    addMessageDecoder(classOf[StashRequestDecoder])
    addMessageEncoder(classOf[StashResponse], classOf[StashResponseEncoder])
}