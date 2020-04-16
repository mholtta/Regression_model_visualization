package regressionviz

// Importing data type fo be used
import breeze.linalg.{DenseMatrix, DenseVector}

// Importing file reading tools
import scala.io.Source

class Data (val file: String) {
  
  private var storedData: Option[DenseMatrix[Double]] = None
  private var dataHeader: Option[Array[String]] = None
  
  
  // Method calls either loadCSV or loadJSON based on filetype
  def loadFile() = {
    // If to select right filetype
    
    if(file.takeRight(3).toLowerCase() == "csv") {
      val (header, data) = this.loadCSV(file)
      this.dataHeader = header
      this.storedData = data
      
      
    } else {
      throw new UnknownFileType
    }
    
    
  }
  
  private def loadCSV(file: String) = {
    val loader = Source.fromFile(file).getLines()
    val header = loader
                       .take(1)
                       .mkString
                       .split(";")
                       .map(_.trim)
    
    // Data from file to array of arrays
    val content = loader.toArray
                         .map(_.split(";"))
                         .map(_.map(_.trim))
                         .map(_.map(_.toDouble))
                         
    val lenght = content.size
    
    val width = if(lenght > 0) content(1).size else 0
    
    val sameWidth = content.forall(_.size == width)
    
    if( !sameWidth || lenght == 0 || width != 2) {
      throw new FileFormatError
    }
    
    // Creating DenseMatrix, needs to be transposed in the end
    val data = new DenseMatrix(width, lenght, content.flatten)
 
    
    
    // Returning headers and data in breeze dense matrix
    (Option(header), Option(data.t))
  }
  
  private def loadJSON = ???
  

  
  
  // Returns the data loaded
  def getData = this.storedData
  
  // Returns data header
  def getHeader = this.dataHeader
    
}