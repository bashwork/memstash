package org.stash.storage

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class HashMapStorageSpec extends FlatSpec with ShouldMatchers {

   behavior of "A HashMapStorage"

   val storage = new HashMapStorage()

   it should "initially be empty" in {
       storage.size should be (0)
       storage.keys should be (List[String]())
   }

   it should "accept new values" in {
       (1 to 10).foreach { (key:Int) =>
           storage.put(new StashObject(key.toString))
       }
       storage.size should be (10)
   }

   it should "get existing values" in {
       (1 to 10).foreach { (key:Int) =>
           storage.get(key.toString).key should be (key.toString)
       }
   }

   it should "remove existing values" in {
       (1 to 10).foreach { (key:Int) =>
           storage.remove(key.toString)
       }
       storage.size should be (0)
       storage.keys should be (List[String]())
   }

   it should "easily clear" in {
       (1 to 10).foreach { (key:Int) =>
           storage.put(new StashObject(key.toString))
       }
       storage.clear
   }
}



