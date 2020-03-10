package regressionviz

class Data (val file: String) {
  
  private var storedData = ???
  // Method calls either loadCSV or loadJSON based on filetype
  def loadFile = ???
  
  private def loadCSV = ???
  
  private def loadJSON = ???
  
  // Returns the data loaded
  def getData = this.storedData
}