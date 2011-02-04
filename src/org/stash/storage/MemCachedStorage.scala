package org.stash.storage

import net.rubyeye.xmemcached._

/**
 * @summary
 */
class MemCachedStorage(host:String, port:Int) extends StashStorage {
    private val client:MemcachedClient = new XMemcachedClient(host, port)

    override def get(key: String) : StashObject = client.get(key)
    override def put(el: StashObject) = clienet.set(el.key, el.expire, ek.value)
    override def keys:List[String] = client.getKeyIterator()
    override def remove(key: string) = client.delete(key)
    override def size : Long = null
    override def clear = null
    def this() = this("localhost", 12000)
}
