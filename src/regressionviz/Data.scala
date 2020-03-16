package regressionviz

// Importing data type fo be used
import breeze.linalg.{DenseMatrix, DenseVector}

// Importing file reading tools
import scala.io.Source

class Data (val file: String) {
  
  private var storedData: Option[DenseMatrix] = None
  private var dataHeader: Option[Array[String]] = None
  
  // Method calls either loadCSV or loadJSON based on filetype
  def loadFile = ???
  
  private def loadCSV(file: String) = {
    val loader = Source.fromFile(file).getLines()
    val header = loader.take(1).toString.split(";").map(_.trim)
    
    // Data from file to array of arrays
    // TODO add try and except here, even if file and data exists, toDouble can cause errors
    val content = loader.toArray
                         .map(_.split(";"))
                         .map(_.map(_.trim))
                         .map(_.map(_.toDouble))
                         
    val lenght = content.size
    
    val width = if(lenght > 0) content(1).size else 0
    
    val sameWidth = content.forall(_.size == width)
    
    if( !sameWidth || lenght == 0 || width == 0) {
      // TODO raise some exception
    }
    
    // Creating DenseMatrix
    val data = new DenseMatrix(lenght, width, content.flatten)
 
    
    
    // Returning headers and data in breeze dense matrix
    (header, data)
  }
  
  private def loadJSON = ???
  
  
  
  // Returns the data loaded
  def getData = this.storedData
  
  // Returns data header
  def getHeader = this.dataHeader
  
}