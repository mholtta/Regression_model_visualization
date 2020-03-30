

package test

import org.scalatest._
import logic._


class UnitTest extends FlatSpec {
  /*
   * Helpermethods for testing.
   */
  def assertTrue (message: String, valueToCheck: Boolean) = assert(valueToCheck, message)
  def assertFalse(message: String, valueToCheck: Boolean) = assert(!valueToCheck, message)
  def assertEquals[T] (message: String, valueA: T, valueB: T) = assert(valueA == valueB, message)
  def assertNotNone[T] (message: String, valueToCheck: Option[T]) = assert(valueToCheck.isDefined, message)
  def assertOption[T] (message: String, valueToCheck: Option[T], expectedContents:T) = {
    assert(valueToCheck.isDefined, message)
    assert(valueToCheck.get == expectedContents, message)
  }  
  
  /*
   * Person objects for testing.
   */
  val ruby = Hobbyist("Ruby", Red, "Jogging", "Developer")
  val julia = Hobbyist("Julia", Blue, "Jogging", "Developer")
  val hobbyist1 = Hobbyist("Hobbyist1", Green, "Hiking", "Designer")
  val linus = ColorNeutral("Linus", "Hiking", "Developer")
  val cecilia = ColorNeutral("Cecilia", "Jogging", "Designer")
  val django = Perfectionist("Django", Blue, "Jogging", "Developer")
  val go = Perfectionist("Go", Blue, "Jogging", "Developer")
  val rust = Perfectionist("Rust", Red, "Jogging", "Entrepreneur")
  
  /*
   * Matchmaker object.
   */
  val matcher1 = new Matchmaker(Array(ruby, julia, django, linus, cecilia, go))
  
  /*
   * Target results.
   */
  val res100 = 100
  val res95 = 95
  val res90 = 90
  val res85 = 85
  val res80 = 80
  val res60 = 60
  val res0 = 0
  
  val resMap: Map[(Person, Person), Int]  = Map(((Hobbyist("Julia", Blue, "Jogging", "Developer"), Perfectionist("Go", Blue, "Jogging", "Developer")) -> 90), ((Perfectionist("Django", Blue, "Jogging", "Developer"), ColorNeutral("Cecilia", "Jogging", "Designer")) -> 45), ((Hobbyist("Ruby", Red, "Jogging", "Developer"), Hobbyist("Julia", Blue, "Jogging", "Developer")) -> 100), ((Hobbyist("Ruby", Red, "Jogging", "Developer"), Perfectionist("Go", Blue, "Jogging", "Developer")) -> 45), ((ColorNeutral("Linus", "Hiking", "Developer"), Perfectionist("Go", Blue, "Jogging", "Developer")) -> 45), ((Hobbyist("Ruby", Red, "Jogging", "Developer"), Perfectionist("Django", Blue, "Jogging", "Developer")) -> 45), ((Hobbyist("Julia", Blue, "Jogging", "Developer"), ColorNeutral("Cecilia", "Jogging", "Designer")) -> 85), ((Hobbyist("Julia", Blue, "Jogging", "Developer"), ColorNeutral("Linus", "Hiking", "Developer")) -> 45), ((Perfectionist("Django", Blue, "Jogging", "Developer"), Perfectionist("Go", Blue, "Jogging", "Developer")) -> 100), ((Hobbyist("Julia", Blue, "Jogging", "Developer"), Perfectionist("Django", Blue, "Jogging", "Developer")) -> 90), ((Hobbyist("Ruby", Red, "Jogging", "Developer"), ColorNeutral("Linus", "Hiking", "Developer")) -> 45), ((Hobbyist("Ruby", Red, "Jogging", "Developer"), ColorNeutral("Cecilia", "Jogging", "Designer")) -> 85), ((Perfectionist("Django", Blue, "Jogging", "Developer"), ColorNeutral("Linus", "Hiking", "Developer")) -> 45), ((ColorNeutral("Linus", "Hiking", "Developer"), ColorNeutral("Cecilia", "Jogging", "Designer")) -> 100), ((ColorNeutral("Cecilia", "Jogging", "Designer"), Perfectionist("Go", Blue, "Jogging", "Developer")) -> 45),
)
  
  
  "Hobbyist calculateMatch" should "give correct scores" in { 
    assertEquals(s"The score should be $res100 for two Hobbyists with the same hobby.", ruby.calculateMatch(julia), res100)
    assertEquals(s"The score should be $res60 for two hobbyists with different hobby", ruby.calculateMatch(hobbyist1), res60)
    assertEquals(s"The score should be $res90 for another person (not a hobbyist) with the same hobby and profession.", ruby.calculateMatch(django), res90)
    assertEquals(s"The score should be $res90 for another person (not a hobbyist) with the same hobby and favorite color.", ruby.calculateMatch(rust), res90)
    assertEquals(s"The score should be $res80 for another person (not a hobbyist) with the same hobby, but no other mutual interesests.", ruby.calculateMatch(cecilia), res80)
    assertEquals(s"The score should be $res0 for another person with a different hobby.", ruby.calculateMatch(linus), res0)
  }
  
  "Perfectionist calculateMatch" should "give correct scores" in {
    assertEquals(s"The score should be $res100 for two perfectionists with identical interests.", django.calculateMatch(go), res100)
    assertEquals(s"The score should be $res90 for another person (not Perfectionist) with identical interests.", django.calculateMatch(julia), res90)
    assertEquals(s"The score should be $res0 for another person with at least one different interest.", django.calculateMatch(ruby), res0)
  
  }
  
  "ColorNeutral calculateMatch" should s"give correct scores for a ColorNeutral person" in {
    assertEquals(s"The score should be $res100 for two ColorNeutral people.", linus.calculateMatch(cecilia), res100)
    assertEquals("The score should be $res90 for another person with the same hobby.", linus.calculateMatch(hobbyist1), res90)
    assertEquals("The score should be $res90 for another person with the same occupation.", linus.calculateMatch(ruby), res90)
    assertEquals("The score should be $res0 for another person with no mutual interests.", linus.calculateMatch(rust), res0)
  }
  
  "bothMatch" should "produce correct match scores" in  {
    assertResult(res100)( ruby.bothMatch(julia) )
    assertResult(85)( ruby.bothMatch(cecilia) )
    assertResult(60)(ruby.bothMatch(hobbyist1))
  }
  
  
  "Matchmaker matchMap1" should "have no duplicates" in {
    assertTrue("", {  
      val pairs = matcher1.matchMap.toArray.map(_._1)
      
      val res = (for {
        i <- 0 until pairs.size
      } yield (pairs.drop(i + 1).forall(z => z._1 != pairs(i)._2 && z._2 != pairs(i)._1)) )
      res.reduce(_||_)
    })
   }
  
  "Matchmaker matchMap2" should "include every pair" in {
    val pairs = matcher1.matchMap.toArray.map(_._1)
      
    val res = (for {
      i <- 0 until pairs.size
    } yield (pairs.drop(i + 1).forall(z => z != pairs(i))) )
    res.reduce(_&&_)
  }
  
  "Matchmaker matchMap3" should "have correct scores for each pair" in {
    val ans = matcher1.matchMap
    //Get all the keys from ans
    // Go through all scores from ans and resMap with the keys from part 1
    assertTrue( "", ans.keys.forall(k => ans(k) == resMap(k)) )
    
    print(ans)
    
    
    
  }
  
  val matcherTest = new Matchmaker(Array(ruby, go))
  
  val testi =  matcherTest.matchMap
  
}
