package org.stash;

import org.apache.commons.cli._
import org.slf4j.{Logger, LoggerFactory}

import org.stash.lang.StashSystem
import org.stash.storage.HashMapStorage
import org.stash.protocol.{StashCodecFactory, StashHandler}

/**
 * The main launcher script for the service. This parses the following
 * command line options:
 *
 * - h | help    : prints this help text
 * - v | version : prints the version of the server
 * - p | port    : sets the port to listen on
 * - a | address : sets the address to listen on
 * - t | threads : sets the number of threads to use
 */
object Application {

    val Version = "1.0.0"
    private val logger = LoggerFactory.getLogger(this.getClass)

    /**
     * Main program start
     */
    def main(args: Array[String]) = {
        var defaults = createDefaults
        val options  = createOptions
        val parser   = new PosixParser()
        val results  = parser.parse(options, args)

        results.getOptions.foreach { o:Option =>
          o.getOpt match {
              case "u" | "udp"      => defaults += ("udp"     -> true)
              case "t" | "threads"  => defaults += ("threads" -> o.getValue())
              case "p" | "port"     => defaults += ("port"    -> o.getValue())
              case "a" | "address"  => defaults += ("address" -> o.getValue())
              case "v" | "version"  => printVersion
              case "h" | "help" | _ => printHelp(options)
          }
        }
        implicit def _atos(a:Any) = a.asInstanceOf[String]
        implicit def _atoi(a:Any) = a.toString.toInt

        val memcached = new MemStashAscii(defaults("address"),
            defaults("port"), defaults("threads"), new HashMapStorage())
        logger.info("Starting memstash on {}:{}", defaults("address"), defaults("port"))
        memcached.startBlocking
    }

    /**
     * Helper method to create the option parser set
     *
     * @return The populated option parser set
     */
    private def createOptions() : Options = {
        val options = new Options()
        options.addOption("h", "help", false, "print this help text")
        options.addOption("v", "version", false, "print the version of the server")
        options.addOption("p", "port", true, "set the port to listen on")
        options.addOption("a", "address", true, "set the address to listen on")
        options.addOption("t", "threads", true, "set the number of threads to use")
        options.addOption("u", "udp", true, "set to enable the udp interface")
        options.addOption("b", "binding", true, "set the default protocol type")
    }

    /**
     * Helper method to create the default options
     *
     * @return The default options map
     */
    private def createDefaults() = Map[String,Any](
        "address" -> null,
        "port"    -> "11211",
        "udp"     -> false,
        "threads" -> (StashSystem.cpus + 1),
        "binding" -> 'binary
    )

    /**
     * Helper method to print the current version and exit
     */
    private def printVersion() = {
        println("Memstash Version " + Application.Version)
        exit
    }

    /**
     * Helper method to print the option help and exit
     */
    private def printHelp(options: Options) = {
        val format = new HelpFormatter()
        format.printHelp("java -jar memstash.jar", options)
        exit
    }
}

