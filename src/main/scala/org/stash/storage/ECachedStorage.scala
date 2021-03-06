package org.stash.storage

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConversions._

/**
 * @summary
 */
class ECachedStorage extends StashStorage {
    private var store:ConcurrentHashMap[String, StashObject] = new ConcurrentHashMap()

    override def get(key: String) : StashObject = store.get(key)
    override def put(el: StashObject) = store.put(el.key, el)
    override def keys:List[String] = store.keys.toList
    override def remove(key: String) = store.remove(key)
    override def size : Long = store.size
    override def clear = { store = new ConcurrentHashMap() }
}
