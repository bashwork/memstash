package org.stash.protocol

import org.apache.mina.filter.codec.demux.DumxingProtocolCodecFactory

class StashCodecFactory extends DemuxingProtocolCodecFactory {
    addMessageDecoder(classOf[StashRequest],  classOf[StashDecoder])
    addMessageEncoder(classOf[StashResponse], classOf[StashEncoder])
}
