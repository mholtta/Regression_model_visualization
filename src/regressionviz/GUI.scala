package regressionviz

import scala.swing._

import java.awt.Insets

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
    
    /*
    val width      = 200
    val height     = 600
    val fullHeight = 810
    
    // The component declares here the minimum, maximum and preferred sizes, which Layout Manager 
    // possibly can follow when positioning components on the screen.
    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)
    
    */
    
    // Creating a Box on left hand side of the window
    //val box = new BoxPanel(Orientation.Vertical)
    
    
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
    
    val yLimits = new FlowPanel()
    val yMin = new TextField(5)
    val yMax = new TextField(5)
    yLimits.contents += yMin
    yLimits.contents += yMax
    
    val graph = new TextArea(25,45)
    graph.text = "Placeholder, graph will replace this"
    
    
    val panel = new GridBagPanel {
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
  
      add(loadData, constraints(0,0))
      add(help,constraints(0,1))
      add(new Label("Select regression model"), constraints(0,2, inset = new Insets(100,10,0,0)))
      add(modelSelector, constraints(0,3))
      add(new Label("Set x-min and x-max"), constraints(0,4))
      add(xLimits, constraints(0,5))
      add(new Label("Set y-min and y-max"), constraints(0,6))
      add(yLimits, constraints(0,7))
      add(graph,constraints(1,0, gridheight = 8))
      
    }
    
    
    /*
    // Adding contents to box
    box.contents += loadData
    box.contents += Swing.VStrut(10)
    box.contents += help
    box.contents += Swing.VStrut(50)
    box.contents += Swing.Glue
    box.contents += new Label("Select regression model")
    box.contents += Swing.VStrut(10)
    box.contents += new FlowPanel {
      contents += modelSelector
      
    }
    
    box.contents += new Label("Set x-min and x-max")
    box.contents += Swing.VStrut(10)
    box.contents += xLimits
    box.contents += new Label("Set y-min and y-max")
    box.contents += Swing.VStrut(10)
    box.contents += yLimits
    box.border = Swing.EmptyBorder(10, 10, 10, 10)
    */
    
    visible = true
    
    contents = panel
  }
  
  
  
  
}

