package org.stash.storage

import java.util.concurrent.ConcurrentHashMap

/**
 * @summary
 */
class ECachedStorage extends StashStorage {
    private val store:ConcurrentHashMap[String, StashObject] = new ConcurrentHashMap()

    override def get(key: String) : StashObject = store.get(key)
    override def put(el: StashObject) = store.put(el.key, el)
    override def keys:List[String] = store.keys
    override def remove(key: string) = store.remove(key)
    override def size : Long = store.size
    override def clear = { store = new ConcurrentHashMap() }
}