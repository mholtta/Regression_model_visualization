

package test

import org.scalatest._
import regressionviz._
import breeze.linalg.DenseMatrix


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
  
  def assertEqualsOptions[T] (message: String, valueA: Option[T], valueB: Option[T]) = {
    
    (valueA, valueB) match {
      case (None, None) => assert(true, message)
      case (Some(a: DenseMatrix[Double]), Some(b: DenseMatrix[Double])) =>
        assert(breeze.linalg.all(a :== b), message)
      case (Some(a: Array[String]), Some(b: Array[String])) =>
        assert(a sameElements b, message)
      case _ => assert(false, message)
      
    }
    
    
  }
  
  /*
   * Person objects for testing.
   */
  
  
  /*
   * Data object.
   */
  val data1 = new Data("Test1.csv")  
  /*
   * Target results.
   */
  val denseMatrix1Start = None
  val header1Start = None
  val denseMatrix2 = Option(DenseMatrix((1.0,2.0),(2.0,4.0),(3.0,6.0)))
  val header2 = Option(Array("X","Y"))
  
  
  
  
  
  "Data1 header and data prior to loading data" should "match to test values" in { 
    assertEqualsOptions(s"The header should be $header1Start before data is loaded into data1 instance.", data1.getHeader, header1Start)
    assertEqualsOptions(s"The data should be $denseMatrix1Start before data is loaded into data1 instance.", data1.getData, denseMatrix1Start)
  }
  
  val data2 = new Data("Test1.csv")
  data2.loadFile()
  
 
  
  "Data2 header and data after loading data" should "match to test values" in { 
    assertEqualsOptions(s"The header should be $header2.get before data is loaded into data1 instance.", data2.getHeader, header2)
    assertEqualsOptions(s"The data should be $denseMatrix2 before data is loaded into data1 instance.", data2.getData, denseMatrix2)
  }  
  
  
  
  
}
