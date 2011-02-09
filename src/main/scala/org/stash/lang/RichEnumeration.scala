package org.stash.lang
import java.util.Enumeration

/**
 * @deprecated This is supplied in scala 2.8
 */
object RichEnumeration {

    /**
     * An implicit conversion from java.util.Enumeration to a RichEnumeration
     *
     * @return A scala iterator wrapper
     */
    implicit def fromEnumeration[T](en: Enumeration[T]) = new RichEnumeration(en)

    /**
     * A wrapper around java.util.Enumeration to implement the scala Iterator
     * of the supplied type.
     */
    class RichEnumeration[T] (en: Enumeration[T]) extends Iterator[T] {
        def next: T = en.nextElement
        def hasNext: Boolean = en.hasMoreElements
    }
}
