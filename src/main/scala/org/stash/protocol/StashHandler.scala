package org.stash.protocol

import java.util.Date
import java.io.ByteArrayOutputStream
import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IoSession
import org.slf4j.{Logger, LoggerFactory}

import org.stash.Application
import org.stash.storage.{StashStorage, StashObject}
import org.stash.lang.StashSystem
import org.stash.lang.StashObjectConvert._

/**
 * The main handler implementation for the memcached protocol
 *
 * @param storage The storage backend to use for data persistance
 */
class StashHandler(val storage:StashStorage) extends IoHandlerAdapter {

    private val logger = LoggerFactory.getLogger(this.getClass)
    private val statistic = new StashStatistics()

    @throws(classOf[Exception])
    override def exceptionCaught(session:IoSession, cause:Throwable) = {
        logger.error(cause.toString())
        session.close(true)
    }

    @throws(classOf[Exception])
    override def messageReceived(session:IoSession, message:Object) = {
        val request = message.asInstanceOf[StashRequest]
        val response = new StashResponse()

        request.command match {
            case "FLUSH_ALL"   => command_flush(response)
            case "ERROR"       => response.write("ERROR")
            case "VERSION"     => command_version(response)
            case "SET"|"ADD"   => command_set(request, response)
            case "REPLACE"|"APPEND"|"PREPEND" => command_alter(request, response)
            case "GET"|"GETS"  => command_get(request, response)
            case "INCR"|"DECR" => command_increment(request, response)
            case "DELETE"      => command_delete(request, response)
            case "STATS"       => command_statistic(response)
            case "QUIT"        => session.close(true)
        }

        session.write(response)
    }

    @throws(classOf[Exception])
    override def sessionClosed(session:IoSession) = {
        logger.info("Session Closed", session.getRemoteAddress)
        statistic.decrement("curr_connections", 1)
    }

    @throws(classOf[Exception])
    override def sessionOpened(session:IoSession) = {
        logger.info("Session Opened", session.getRemoteAddress)
        statistic.increment("curr_connections", 1)
        statistic.increment("total_connections", 1)
    }

    /**
     * Handler for the memcache version command
     *
     * @param response The response to build
     */
    private def command_version(response: StashResponse) = {
        response.write("VERSION " + Application.Version)
    }

    /**
     * Handler for the memcache flush command
     *
     * @param response The response to build
     */
    private def command_flush(response: StashResponse) = {
        storage.clear
        statistic.increment("cmd_flush", 1)
        response.write("OK")
    }

    /**
     * Handler for the memcache get keys command
     *
     * @param request The request to process
     * @param response The response to build
     */
    private def command_get(request:StashRequest, response: StashResponse) = {
        statistic.increment("cmd_get", 1)
        request.extra.foreach { key:String =>
            storage.get(key) match {
                case el:StashObject => {
                    statistic.increment("get_hits", 1)
                    statistic.increment("bytes_read", el.size)
                    // TODO CAS
                    response.write("VALUE " + el.key + " " + el.flags + " " + el.size)
                    response.write(el.bytes)
                }
                case null => statistic.increment("get_misses", 1)
            }
        }
        response.write("END")
    }

    /**
     * Handler for the memcache key set command
     *
     * @param request The request to process
     * @param response The response to build
     */
    private def command_set(request:StashRequest, response: StashResponse) = {
        var stored = true
        statistic.increment("cmd_set", 1)
        request.command match {
            case "SET" => storage.put(request)
            case "ADD" => {
                storage.get(request.key) match {
                    case el:StashObject => { stored = false }
                    case null => storage.put(request)
                }
            }
        }
        stored match {
            case true => {
                statistic.increment("set_hits", 1)
                statistic.increment("bytes_written", request.size)
                response.write("STORED")
            }
            case false => {
                statistic.increment("set_misses", 1)
                response.write("NOT_STORED")
            }
        }
    }

    /**
     * Handler for the memcache key alter command
     *
     * @param request The request to process
     * @param response The response to build
     */
    private def command_alter(request:StashRequest, response: StashResponse) = {
        statistic.increment("cmd_set", 1)
        storage.get(request.key) match {
            case el:StashObject => {
                val stream = new ByteArrayOutputStream(request.size + el.size)
                request.command match {
                    case "REPLACE" => { stream.write(request.bytes) } 
                    case "APPEND"  => {
                        stream.write(el.bytes)
                        stream.write(request.bytes)
                    } 
                    case "PREPEND" => {
                        stream.write(request.bytes)
                        stream.write(el.bytes)
                    } 
                }
                el.bytes = stream.toByteArray
                el.size = el.bytes.length
                storage.put(el)

                statistic.increment("set_hits", 1)
                statistic.increment("bytes_written", el.size)
                response.write("STORED")
            }
            case null => {
                statistic.increment("set_misses", 1)
                response.write("NOT_FOUND")
            }
        }
    }

    /**
     * Handler for the memcache delete command
     *
     * @param request The request to process
     * @param response The response to build
     */
    private def command_delete(request:StashRequest, response: StashResponse) = {
        storage.get(request.key) match {
            case el:StashObject => {
                statistic.increment("delete_hits", 1)
                storage.remove(request.key)
                if (request.reply) response.write("DELETED")
            }
            case null => {
                statistic.increment("delete_misses", 1)
                if (request.reply) response.write("NOT_FOUND")
            }
        }
    }

    /**
     * Handler for the memcache key increment/decrement commands
     *
     * @param request The request to process
     * @param response The response to build
     */
    private def command_increment(request:StashRequest, response: StashResponse) = {
        def int(array:Array[Byte]) = new String(array).toInt
        implicit def _2array(input:Int) = input.toString.getBytes

        statistic.increment("cmd_set", 1)
        storage.get(request.key) match {
            case el:StashObject => {
                request.command match {
                    case "INCR" => el.bytes = int(el.bytes) + int(request.bytes)
                    case "DECR" => el.bytes = math.min(0, int(el.bytes) - int(request.bytes))
                }
                storage.put(el)
                response.write(el.bytes)
            }
            case null => response.write("NOT_FOUND")
        }
    }

    /**
     * Handler for the memcache get statistics command
     *
     * @param response The response to build
     */
    private def command_statistic(response: StashResponse) = {
        def stat(key:String, value:Any) {
            response.write("STAT " + key + " " + value)
        }
        stat("pid", StashSystem.pid)
        stat("uptime", StashSystem.time - statistic.get("uptime"))
        stat("time", StashSystem.time)
        stat("version", Application.Version)
        stat("pointer_size", StashSystem.bits)
        statistic.all.foreach { case (k,v) => if (k != "uptime") stat(k, v) }
        response.write("END")
    }
}
