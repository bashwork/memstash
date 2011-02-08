package org.stash.lang

object StashSystem {
    def version:String = "0.1.0"
    def cpus:Int    = Runtime.getRuntime().availableProcessors
    def pid:Int     = 1
    def time:Long   = System.currentTimeMillis
    def bits:String = System.getProperty("sun.arch.data.model")
}

