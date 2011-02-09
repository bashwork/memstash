package org.stash.storage

import java.io.Serializable

/**
 * @summary
 */
abstract trait StashStorage extends Serializable {
    def get(key: String): StashObject
    def put(el: StashObject): Unit
    def keys:List[String]
    def remove(key: String): Unit
    def size: Long
    def clear: Unit
}
