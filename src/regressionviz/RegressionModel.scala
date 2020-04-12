package regressionviz

import breeze.linalg.{DenseMatrix, DenseVector, max, linspace}

import breeze.linalg.NumericOps._

class RegressionModel(inputData: DenseMatrix[Double], private val polynomial: Int) {
  
  // Storing input to separate DenseVectors
  private val x = inputData(::,0)
  private val y = inputData(::,1)
    
  // Finding range of input
  private val maxValue = max(x)
  private val minValue = x.toArray.min
  
  // Linearly spaced DenseVector to for showing predictions line
  private val predictSpace = linspace(minValue,maxValue)
  
  
  // Fitted model coefficients
  private val modelCoefficients = fitModel
  
  // Private method for fitting model
  private def fitModel = {
    val X = this.exponentiate(x, polynomial)
    val coefficients = X \ y
    
    coefficients
    
  }
  
  // Private method  for producing DenseMatrix where 0th column ones, second data and then higher powers of data
  private def exponentiate(data: DenseVector[Double], exponential: Int) = {
    val matrix = DenseMatrix.zeros[Double](data.length, exponential +1)

       // Adding powers of x to original
    for(p <- 0 to polynomial) {
      val power = data ^:^ p.toDouble
      matrix(::,p) := power
    }
    matrix
  }
  
  // Private method for generating predictions
  private def predict(x: DenseVector[Double], coefficients: DenseVector[Double]) = {
    val X = exponentiate(x, polynomial)
    val result = DenseVector[Double].ones(x.length)
    
    for(i <- 0 until x.length) {
      result(i) = X(i,::) * coefficients
    }
    
    // Mapping each row as a dot product of row and coefficients
    val result = X(*,::).map(dv => dv * coefficients)
  }
  
  
}