package org.stash.storage

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConversions._

/**
 * @summary
 */
class DatabaseStorage extends StashStorage {
    private var store:ConcurrentHashMap[String, StashObject] = new ConcurrentHashMap()

    override def get(key: String) : StashObject = store.get(key)
    override def put(el: StashObject) = store.put(el.key, el)
    override def keys:List[String] = store.keys.toList
    override def remove(key: String) = store.remove(key)
    override def size : Long = store.size
    override def clear = { store = new ConcurrentHashMap() }
}

/*
 * import com.twitter.querulous.evaluator._
 * import com.twitter.querulous.query._
 * import com.twitter.querulous.database._
 *
 * val queryFactory = new SqlQueryFactory
 * val apachePoolingDatabaseFactory = new apachePoolingDatabaseFactory(
 *   minOpenConnections:                 Int,      // minimum number of open/active connections at all times
 *   maxOpenConnections:                 Int,      // minimum number of open/active connections at all times
 *   checkConnectionHealthWhenIdleFor:   Duration, // asynchronously check the health of open connections every `checkConnectionHealthWhenIdleFor` amount of time
 *   maxWaitForConnectionReservation:    Duration, // maximum amount of time you're willing to wait to reserve a connection from the pool; throw an exception otherwise
 *   checkConnectionHealthOnReservation: Boolean,  // check connection health when reserving the connection from the pool
 *   evictConnectionIfIdleFor:           Duration  // destroy connections if they are idle for longer than `evictConnectionIfIdleFor` amount of time
 * )
 * val queryEvaluatorFactory = new StandardQueryEvaluatorFactory(apachePoolingDatabaseFactory, queryFactory)
 * val queryEvaluator = queryEvaluatorFactory(List("primaryhost", "fallbackhost1", "fallbackhost2", ...), "username", "password")
 *
 * val users = queryEvaluator.select("SELECT * FROM users WHERE id IN (?) OR name = ?", List(1,2,3), "Jacques") { row =>
 *   new User(row.getInt("id"), row.getString("name"))
 * }
 *
 * queryEvaluator.transaction { transaction =>
 *   transaction.select("SELECT ... FOR UPDATE", ...)
 *   transaction.execute("INSERT INTO users VALUES (?, ?)", 1, "Jacques")
 *   transaction.execute("INSERT INTO users VALUES (?, ?)", 2, "Luc")
 * }
 */
