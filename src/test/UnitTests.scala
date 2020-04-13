

package test

import org.scalatest._
import regressionviz._
import breeze.linalg.{DenseMatrix, DenseVector}


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
   * Data object.
   */
  val data1 = new Data("Test1.csv") 
  
  val regressionTest = new Data("Test2_third_power.csv") // For testing regression model class
  
  /*
   * Target results.
   */
  val denseMatrix1Start = None
  val header1Start = None
  val denseMatrix2 = Option(DenseMatrix((1.0,2.0),(2.0,4.0),(3.0,6.0)))
  val header2 = Option(Array("X","Y"))
  
  
  val xCSV2 = DenseVector(-30.0,-25.0,-20.0,-15.0,-10.0,-5.0,0.0,5.0,10.0,15.0,20.0,25.0,30.0)
  val yCSV2 = DenseVector(133114.0,76804.0,39144.0,16384.0,4774.0,564.0,4.0,-656.0,-5166.0,-17276.0,-40736.0,-79296.0,-136706.0)
  val model = DenseVector(4.0,3.0,-2.0,-5.0)
  val CSV2matrix = DenseMatrix.zeros[Double](xCSV2.length,2)
  CSV2matrix(::,0) := xCSV2
  CSV2matrix(::,1) := yCSV2
  
  
  
  
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
  
  regressionTest.loadFile()
  
  "RegressionTest header and data after loading data" should "match to test values" in { 
    assertEqualsOptions(s"The header should be $header2.get before data is loaded into data1 instance.", data2.getHeader, header2)
    assertEqualsOptions(s"The data should be $CSV2matrix before data is loaded into data1 instance.", regressionTest.getData, Option(CSV2matrix))
  }
  
  
}
