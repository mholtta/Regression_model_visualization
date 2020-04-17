package regressionviz

// Importing data type fo be used
import breeze.linalg.{DenseMatrix, DenseVector}

// Importing file reading tools
import scala.io.Source
import java.io.File

// Import XLSX reading tools
import org.apache.poi.ss.usermodel.{WorkbookFactory,Row, DataFormatter}

// For iterating over POI sheet
import collection.JavaConversions._

class Data (val file: String) {
  
  private var storedData: Option[DenseMatrix[Double]] = None
  private var dataHeader: Option[Array[String]] = None
  
  
  // Method calls either loadCSV or loadXLSX based on filetype
  def loadFile() = {
    // If to select right filetype
    
    if(file.takeRight(3).toLowerCase() == "csv") {
      val (header, data) = this.loadCSV(file)
      this.dataHeader = header
      this.storedData = data
      
      
    } else if (file.takeRight(4).toLowerCase() == "xlsx") {
      val (header, data) = this.loadXLSX(file)
      this.dataHeader = header
      this.storedData = data
            
    } else {
      throw new UnknownFileType
    }
    
    
  }
  
  
  // Helper method for loading XLSX files
  private def loadXLSX(file: String) = {
    // Creating file
    val f = new File(file)
    // Creating the workbook and loading first sheet from it
    val workbook = WorkbookFactory.create(f)
    val sheet = workbook.getSheetAt(0)
    
    // Creating a formatter that converts the cell reference to the value
    val formatter = new DataFormatter()
    
    // Getting all cell references where row has some content to array of arrays for first two columns
    // Getting also cases where only another cell has data (return blanks as null), wrapping to Option
    val allContentOption = sheet.toArray
                          .map(row => Array( Option(row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)), Option(row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)) ) )
    
    // Checking are there any None's in content and throwing an exception if there are
    if(allContentOption.flatten.exists(_ == None)){
      throw new XLSXFileMissingData
    }
                          
    // No None's at this point, safe to remove Options, and get values
    val allContent = allContentOption.map(_.flatten).map(_.map( x => formatter.formatCellValue(x)))
    
    // Getting header
    val header = allContent.head
    
    // Getting data, toDouble may create an exception, 
    val content = allContent.drop(1)
                         .map(_.map(_.trim))
                         .map(_.map(_.toDouble))
    
    
    // Lenght for creating DenseMatrix
    val lenght = content.size
    
    // If file has no content, throw an exception
    if(lenght == 0) throw new XLSXNoContent
    
    // Creating DenseMatrix, needs to be transposed in the end
    val data = new DenseMatrix(2, lenght, content.flatten)
    
    // Returning headers and data in breeze dense matrix
    (Option(header), Option(data.t))
    
  }

  
  // Helper method for loading CSV-files
  private def loadCSV(file: String) = {
    val loader = Source.fromFile(file).getLines()
    // Getting the header from file
    val header = loader
                       .take(1)
                       .mkString
                       .split(";")
                       .map(_.trim)
    
    // Data from file to array of arrays (DenseMatrix requires input in Array). ToDouble will create exceptions, handled in GUI
    val content = loader.toArray
                         .map(_.split(";"))
                         .map(_.map(_.trim))
                         .map(_.map(_.toDouble))
    
    
    // Lenght for creating DenseMatrix
    val lenght = content.size
    
    // Width for creating DenseMatrix purposes
    val width = if(lenght > 0) content(1).size else 0
    
    // Checking that all columns have same width
    val sameWidth = content.forall(_.size == width)
    
    // If not all criteria met, throw an exception
    if( !sameWidth || lenght == 0 || width != 2) {
      throw new FileFormatError
    }
    
    // Creating DenseMatrix, needs to be transposed in the end
    val data = new DenseMatrix(width, lenght, content.flatten)
 
    
    
    // Returning headers and data in breeze dense matrix
    (Option(header), Option(data.t))
  }
  
    

  
  
  // Returns the data loaded
  def getData = this.storedData
  
  // Returns data header
  def getHeader = this.dataHeader
    
}