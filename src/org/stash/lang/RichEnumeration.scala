/**
 * @note This is supplied in scala 2.8
 */
package org.stash.lang
import java.util.Enumeration

/**
 * @summary
 */
object RichEnumeration {

    /**
     * @summary An implicit conversion from java.util.Enumeration to a RichEnumeration
     * @returns A scala iterator wrapper
     */
    implicit def fromEnumeration[T](en: Enumeration[T]) = new RichEnumeration(en)

    /**
     * @summary A wrapper around java.util.Enumeration to implement Iterator
     */
    class RichEnumeration[T] (en: Enumeration[T]) extends Iterator[T] {
        def next: T = en.nextElement
        def hasNext: Boolean = en.hasMoreElements
    }
}
