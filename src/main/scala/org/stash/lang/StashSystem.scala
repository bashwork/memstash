package org.stash.lang

import java.lang.management.ManagementFactory

object StashSystem {
    def cpus:Int    = Runtime.getRuntime().availableProcessors
    def pid:String  = ManagementFactory.getRuntimeMXBean.getName.split('@').head
    def time:Long   = System.currentTimeMillis/1000
    def bits:String = System.getProperty("sun.arch.data.model")
}

