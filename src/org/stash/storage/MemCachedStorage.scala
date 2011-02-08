package org.stash.storage

import net.rubyeye.xmemcached._

/**
 * @summary
 */
class MemCachedStorage(host:String, port:Int) extends StashStorage {
    private val client:MemcachedClient = new XMemcachedClient(host, port)

    override def get(key: String) : StashObject = client.get(key)
    override def put(el: StashObject) = client.set(el.key, el.expire, el.value)
    override def keys:List[String] = null//client.getKeyIterator()
    override def remove(key: String) = client.delete(key)
    override def size : Long = 0
    override def clear = null
    def this() = this("localhost", 12000)
}
