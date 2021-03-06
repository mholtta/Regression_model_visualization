package regressionviz

import scala.swing._
import scala.swing.event.ButtonClicked

// Import for storing data
import breeze.linalg.DenseMatrix

// Import for setting insets in GridBagPanel
import java.awt.Insets

// Imports for charting
import org.jfree.chart.plot._
import org.jfree.data.xy._
import org.jfree.chart.renderer.xy._
import org.jfree.chart.axis._
import org.jfree.chart._

import java.io.File
import java.awt.Event._

object GUI extends SimpleSwingApplication {
  
  /*
   * 
   * Helper methods for updating GUI
   * 
   */
  
  
  // Helper method for displaying filechooser
  private def choosePlainFile(title: String = ""): Option[File] = {  
    val chooser = new FileChooser(new File("."))
    chooser.title = title
    val result = chooser.showOpenDialog(null)
    if (result == FileChooser.Result.Approve) {
      Some(chooser.selectedFile)
    } else None
  }
  
  // Helper method for updating graph after file chosen
  private def datasetUpdate(file: Option[File], scatterData: DefaultXYDataset, lineData: DefaultXYDataset,xAxis: NumberAxis, yAxis: NumberAxis, xMin: TextField,
      xMax: TextField, yMin: TextField, yMax: TextField, polynomial: TextField) : Option[DenseMatrix[Double]] = {
    for(content <- file) {
      val data = new Data(content.getCanonicalPath)
      data.loadFile()
      
      for(i <- data.getData) {
        // Creating new model with loaded data
        val model = new RegressionModel(i,1)
        
        // Splitting loaded data to x and y for scatter plot
        val xScatter = i(::,0).toArray
        val yScatter = i(::,1).toArray
        
        // Splitting predictions to x and y for line
        val xLine = model.getPredictions(::,0).toArray
        val yLine = model.getPredictions(::,1).toArray
        
        // Updating the dataseries given to function
        scatterData.addSeries("Original data", Array(xScatter,yScatter))
        lineData.addSeries("Fitted model", Array(xLine,yLine))
        
        // Resetting axis endpoints
        xAxis.setAutoRange(true)
        yAxis.setAutoRange(true)
        
        // Updating text fields visible in GUI to match new data
        xMin.text = xAxis.getLowerBound.toString()
        xMax.text = xAxis.getUpperBound.toString()
        yMin.text = yAxis.getLowerBound.toString()
        yMax.text = yAxis.getUpperBound.toString()
        polynomial.text = "1"
        
        // Updating axis labels to match file
        for(value <- data.getHeader){
          val xLabel = if(value.isDefinedAt(0)) value(0) else "X"
          val yLabel = if(value.isDefinedAt(1)) value(1) else "Y"
          
          xAxis.setLabel(xLabel)
          yAxis.setLabel(yLabel)
        }
        
        return Some(i)
      }
    }
    return None
  }
  
  /*
   * 
   * Code for GUI
   * 
   */
  
  
  def top = new MainFrame {
    title     = "Regression model visualization"
    resizable = false
    

    // A variable storing the data instance currently in use
    var data : Option[DenseMatrix[Double]] = None
    
    
    // Creating button to open file chooser and load data
    val loadData = new Button("Load data")
    loadData.border = Swing.BeveledBorder(Swing.Raised)
    
    listenTo(loadData)
    reactions += {
      case ButtonClicked(b) if b == loadData => {
        try {
          // Running helper function to update data in use and graphs
          data = datasetUpdate(choosePlainFile(), collection1, collection2, domain1, range1, xMin, xMax, yMin, yMax, modelSelector)
        } catch {
          // Catching exceptions and displaying errors
          case e: UnknownFileType => Dialog.showMessage(contents.head, "Unknown file type, currently only CSV and XLSX -files supported.", title="Error")
          case e: FileFormatError => Dialog.showMessage(contents.head, "Error in file format. Please check that there is data in the file, each row has two columns and separator is ';'.", title="Error")
          case e: NumberFormatException => Dialog.showMessage(contents.head, "Error in file format. Please check that there are only numerical values, '.' is decimal separator and ';' separates the columns.", title="Error")
          case e: XLSXFileMissingData => Dialog.showMessage(contents.head, "Please check there are no empty cells in columns A or B in rows with data.", title="Error")
          case e: XLSXNoContent => Dialog.showMessage(contents.head, "Please check that the XLSX file contains data in addition to header row.", title="Error")
          case e: IllegalStateException => Dialog.showMessage(contents.head, "Please check that the XLSX file does not have any non-numeric data aside from first row.", title="Error")
        }
        
      }
    }
    
    // Help button
    val help = new Button("Help")
    help.border = Swing.BeveledBorder(Swing.Raised)
    
    // Listener for button and a message
    listenTo(help)
    
    reactions += {
      // Text behind help button
      case ButtonClicked(b) if b == help => Dialog.showMessage(contents.head, "This program fits a polynomial regression with OLS method to an X-Y dataset stored in a CSV or XLSX -file. \n \n" 
         + "Use button 'Load data' to select a CSV or XLSX-file. CSV-file needs to be in following format:\n"
         + "- two columns, first colum is the independent variable visualized on the horizontal axis, second column is the dependent variable\n"
         + "- file should have a header row, the header row is shown as axis labels allowing one to check columns were in right order\n"
         + "- only numerical values are allowed below the header row, no spaces\n"
         + "- only ';' is allowed as a column separator and '.' as decimal separator \n \n"
         + "XLSX-file needs to be in following format:\n"
         + "- data is stored in the first sheet of the file\n"
         + "- column A should contain the independent variable visualized on the horizontal axis, column B should contain the dependent variable\n"
         + "- sheet should have header on the first row\n"
         + "- header should be in text format and other rows in numerical format \n\n"
         + "The program should yield hopefully helpful error messages in case not all file criteria is met.\n\n"
         + "The program allows one to set the degree of polynomial fit to data\n"
         + "by inputting a positive integer to text field below 'Set degree of polynomial'\n"
         + "and pressing 'Update degree pf polynomial'.\n"
         + "Input 0 means a constant, 1 regular linear regression, 2 quadratic regression and so on.\n"
         + "The program will by default fit linear regression to data when new data is loaded.\n\n"
         + "In sections 'Set x-min and x-max' and 'Set y-min and y-max' one can set the axis endpoints.\n"
         + "The endpoints are updated after pressing 'Update axis endpoints'.\n"
         + "Pressing 'Reset axis endpoints' allows one to return to default endpoints where original data is visible.\n"
         + "When new data is loaded, the endpoints are automatically reset.", title="Help")
    }
    

    
    // Creating a text field for entering degree of polynomial
    val modelSelector = new TextField(5)
    modelSelector.border = Swing.BeveledBorder(Swing.Raised)
    modelSelector.text = "1"
    // Text field to flow panel for consistent look
    val modelPanel = new FlowPanel()
    modelPanel.contents += modelSelector
    
    
    // A button for updating polynomial degree, adding listener and reactions to it 
    val updatePolynomial = new Button("Update degree of polynomial")
    updatePolynomial.border = Swing.BeveledBorder(Swing.Raised)
    listenTo(updatePolynomial)
    reactions += {
      case ButtonClicked(b) if b == updatePolynomial=> {
        // After update polynomial clicked, try to create a new model, throw exceptions if no data or not an integer value
        try{
          if(data.isEmpty) {
            throw new DataNotFound
           } else {
             val degree = modelSelector.peer.getText.toInt
             // If negative, throw and exception
             if(degree < 0) throw new NegativeValue
             
             // Fit the model with new polynomial degree
             val model = new RegressionModel(data.get, degree)
             // Separate x and y from predictions matrix
             val xLine = model.getPredictions(::,0).toArray
             val yLine = model.getPredictions(::,1).toArray
             
             // Update dataset being graphed to update graph
             collection2.addSeries("Fitted model", Array(xLine,yLine))
           }
        } catch {
            // Show error messages in case of exceptions
            case e: DataNotFound => Dialog.showMessage(contents.head, "Please load data first.", title="Error")
            case e: NegativeValue => Dialog.showMessage(contents.head, "Please check input, only positive values allowed.", title="Error")
            case e: NumberFormatException => Dialog.showMessage(contents.head, "Please check input, only positive integers allowed.", title="Error")
        }
        
      }
    }
    
    // Creating button that updates axis endpoints
    val updateEndpoints = new Button("Update axis endpoints")
    updateEndpoints.border = Swing.BeveledBorder(Swing.Raised)
    
    // Adding a listener that changes endpoints
    listenTo(updateEndpoints)
    reactions += {
      case ButtonClicked(b) if b == updateEndpoints => {
        try{
            // Setting x and y axis upper and lower bounds
            domain1.setLowerBound(xMin.peer.getText.toDouble)
            domain1.setUpperBound(xMax.peer.getText.toDouble)
            
            range1.setLowerBound(yMin.peer.getText.toDouble)
            range1.setUpperBound(yMax.peer.getText.toDouble)
            
        } catch {
          // Catch the exception if string not convertible to int
          case e: NumberFormatException => Dialog.showMessage(contents.head, "Please check your axis endpoints input. Only numerical values allowed. Please use '.' as decimal separator. ", title="Error")
        }
      }
    }
    
    // Adding another button that allows returning to default axis endpoints and a listener for it
    val resetEndpoints = new Button("Reset axis endpoints")
    resetEndpoints.border = Swing.BeveledBorder(Swing.Raised)
    listenTo(resetEndpoints)
    
    reactions += {
      case ButtonClicked(b) if b == resetEndpoints => {
        // Setting auto endpoints for both axes
        domain1.setAutoRange(true)
        range1.setAutoRange(true)
        
        // Updating text fields visible in GUI to match new data
        xMin.text = domain1.getLowerBound.toString()
        xMax.text = domain1.getUpperBound.toString()
        yMin.text = range1.getLowerBound.toString()
        yMax.text = range1.getUpperBound.toString()
      }
    }

    // Creating input fields for x and y min and max
    val xMin = new TextField(10)
    val xMax = new TextField(10) 

    // Adding input fields to FlowPanel to show them side by side
    val xLimits = new FlowPanel()
    xLimits.contents += xMin
    xLimits.contents += xMax
    
    val yLimits = new FlowPanel()
    val yMin = new TextField(10)
    val yMax = new TextField(10)
    yLimits.contents += yMin
    yLimits.contents += yMax
    
    
    // Generating plot
    val plot = new XYPlot()
 
    /*  
     * 
     * Setting up line for the chart
     * 
     * */

    // Create the line data, renderer, and axis
    val collection2 = new DefaultXYDataset

    
    val renderer2 = new XYLineAndShapeRenderer(true, false)	// Lines only
    
    // Set the line data, renderer, and axis into plot
    plot.setDataset(0, collection2)
    plot.setRenderer(0, renderer2)

    
    // Map the line to the second Domain and second Range
    plot.mapDatasetToDomainAxis(1, 0)
    plot.mapDatasetToRangeAxis(1, 0)

    
    /*  
     *  
     * Setting up scatter for chart
     * 
     * */
    
    // Create the scatter data, renderer, and axis
    val collection1 = new DefaultXYDataset
    
    // Generating rendered and axes
    val renderer1 = new XYLineAndShapeRenderer(false, true) // Shapes only
    val domain1 = new NumberAxis("X")
    val range1 = new NumberAxis("Y")
    
    // Setting auto-range to true in axes
    domain1.setAutoRange(true)
    range1.setAutoRange(true)
    

    
    // Setting upper and lower bounds to textfields
    xMin.text = domain1.getLowerBound.toString()
    xMax.text = domain1.getUpperBound.toString()
    yMin.text = range1.getLowerBound.toString()
    yMax.text = range1.getUpperBound.toString()
    
    
    // Set the scatter data, renderer, and axis into plot
    plot.setDataset(1, collection1)
    plot.setRenderer(1, renderer1)
    plot.setDomainAxis(0, domain1)
    plot.setRangeAxis(0, range1)
    
    // Map the scatter to the first Domain and first Range
    plot.mapDatasetToDomainAxis(0, 0)
    plot.mapDatasetToRangeAxis(0, 0)
    
    
    
        
    // Create the chart with the plot and a legend
    val chart = new JFreeChart("Visualizing polynomial regression data fit", JFreeChart.DEFAULT_TITLE_FONT, plot, true)
    
    // Warpping the chart to first Java Swing component and then Scala swing component to allow displaying the chart in Swing UI
    val wrappedChart = Component.wrap(new ChartPanel(chart))
    
    
    // Combining all items to GridBagPanel
    val panel = new GridBagPanel {
      // Helper function for setting constraints in GridBagPanel
      def constraints(x: Int, y: Int, gridwidth: Int = 1, gridheight: Int = 1,
		    weightx: Double = 0.0, weighty: Double = 0.0, fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.Horizontal, 
		    anchor: GridBagPanel.Anchor.Value = GridBagPanel.Anchor.NorthWest, inset: Insets = new Insets(10,10,0,10)) : Constraints = {
        val c = new Constraints()
        c.gridx = x
        c.gridy = y
        c.gridwidth = gridwidth
        c.gridheight = gridheight
        c.weightx = weightx
        c.weighty = weighty
        c.fill = fill
        c.anchor = anchor
        c.insets = inset
        c
      }
      // Adding all components to panel
      add(loadData, constraints(0,0))
      add(help,constraints(0,1))
      add(updatePolynomial, constraints(0,2, inset = new Insets(75,10,0,10)))
      add(new Label("Set degree of polynomial"), constraints(0,3))
      add(modelPanel, constraints(0,4))
      add(updateEndpoints, constraints(0,5, inset = new Insets(40,10,0,10)))
      add(resetEndpoints, constraints(0,6))
      add(new Label("Set x-min and x-max"), constraints(0,7))
      add(xLimits, constraints(0,8))
      add(new Label("Set y-min and y-max"), constraints(0,9))
      add(yLimits, constraints(0,10))
      add(wrappedChart,constraints(1,0, gridheight = 11))
      
    }
    
    visible = true
    
    // Adding panel to MainFrame contents
    contents = panel
  }
 
}

