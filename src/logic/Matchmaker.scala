package logic

import scala.collection.mutable.Buffer

class Matchmaker(personData: Array[Person]) {
  
  /**
   * Uses the people recorded in the matchmaker (personData) to create a map from each possible pairing
   * to the mutual matchmaking score of that pair.
   * 
   * When creating the map remember to take each pair of people *only once*, 
   * e.g. take only (Matt, Laura) or (Laura, Matt). If the person appears earlier in the list
   * you should put them as the first half o f the pair.
   */
  def matchMap: Map[(Person, Person), Int] = {
    var data = Map[(Person, Person), Int]() 
    
    for {
      first <- personData
      second <- personData
    } {
      // Clearing matches with oneself
      if(first != second) {
        // Ordering pairs alphabetically, first into array and the orderring
        var pair = Array(first,second)
        pair = pair.sortBy(personData.indexOf(_)) 
        val score = pair(0).bothMatch(pair(1))
        
        data += ((pair(0), pair(1)) -> score)  
        
        
      }
      
    }
    
    data
    
  }
  
}