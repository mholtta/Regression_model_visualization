package regressionviz

import scala.swing._

object GUI extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title     = "Regression model visualization"
    resizable = false
    
    val width      = 200
    val height     = 200
    val fullHeight = 210
    
    // The component declares here the minimum, maximum and preferred sizes, which Layout Manager 
    // possibly can follow when positioning components on the screen.
    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)
    
    val box = new BoxPanel(Orientation.Horizontal)
    box.contents += new Label("Load data")
    box.contents += new Label("Help")
    
    contents = box
  }
  
}