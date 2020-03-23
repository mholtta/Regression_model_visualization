package regressionviz

import scala.swing._

/*
class UI extends MainFrame {
  title = "GUI Program #3"
  contents = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Look at me!")
    contents += Button("Press me, please") { println("Thank you") }
    contents += Button("Close") { sys.exit(0) }
  }
}


object GuiProgramThree {
  def main(args: Array[String]) {
    val ui = new UI
    ui.visible = true
  }
}

*/



object GUI extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title     = "Regression model visualization"
    resizable = true
    
    val width      = 200
    val height     = 600
    val fullHeight = 810
    
    // The component declares here the minimum, maximum and preferred sizes, which Layout Manager 
    // possibly can follow when positioning components on the screen.
    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)
    
    // Creating a Box on left hand side of the window
    val box = new BoxPanel(Orientation.Vertical)
    
    
    // Creating buttons to add to box
    val loadData = new Button("Load data")
    loadData.border = Swing.BeveledBorder(Swing.Raised)
    
    
    val help = new Button("Help")
    help.border = Swing.BeveledBorder(Swing.Raised)
    
    // Creating combo box for selecting regression model
    val modelSelector = new ComboBox(Seq("Model 1","Model 2"))
    
    // Creating input fields for x and y min and max
    val xMin = new TextField(5)
    val xMax = new TextField(5)
    
    val xLimits = new FlowPanel()
    xLimits.contents += xMin
    xLimits.contents += xMax
    
    val yLimits = new TextField(2)
    
    
    // Adding contents to box
    box.contents += loadData
    box.contents += Swing.VStrut(10)
    box.contents += help
    box.contents += Swing.VStrut(50)
    box.contents += Swing.Glue
    box.contents += new Label("Select regression model")
    box.contents += Swing.VStrut(10)
    box.contents += new BorderPanel {
      add(modelSelector, BorderPanel.Position.Center)
    }
    
    box.contents += new Label("Set x-min and x-max")
    box.contents += Swing.VStrut(10)
    box.contents += xLimits
    box.contents += new Label("Set y-min and y-max")
    box.contents += Swing.VStrut(10)
    box.contents += yLimits
    box.border = Swing.EmptyBorder(10, 10, 10, 10)
    
    
    visible = true
    
    contents = box
  }
  
  
  
}

