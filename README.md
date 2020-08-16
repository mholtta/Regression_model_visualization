# Regression model visualisation tool

This the course project for CS-C2120 Programming studio 2: Project. In the course we were given a list of projects to choose from. I chose fitting and visualizing a regression model as my topic. The application is simple and could be improved in multiple ways. However, it met all the criteria set in the project brief and earned a grade 5/5 (maximum grade).

**The project brief for completing at advanced level was the following:**
- Build a program that fits a regression model to a dataset consisting of x,y -pairs and visualises the datapoints and the regression line.
- The basic y = a + b * x regression needs to be included. Furthermore, the user needs to be able to select other type of regression as well.
- The endpoints of the graph visualizing the data and regression line need to be user selectable.
- User needs to be able to select the file containing the data and two file types need to be supported.
- Application needs to have a graphical user interface.
- The creation of program should not be made trivial by extensive use of libraries.

I implemented the application relaying heavily on Scala Breeze linear algebra library for data handling and computations and on Scala Swing and JFreeChart on the graphical user interface. The linear regression could have been implemented in a more concise manner with libraries built for it, but that would have made it too trivial for the purpose of this course.

The repository is in the Eclipse-format as that was the recommended format for the course. Importing the project to Eclipse and running it yields the following simple graphical user interface. The code itself is in src-folder.

**User interface:**
![User interface](https://raw.githubusercontent.com/mholtta/CS-C2120_Programming_studio2-project/master/readMeImages/GUI.png?token=AOTNNCCE5GSVKABSHU7Y7NC7HED7E)

### The best sides of the project:
-	The unit tests cover large part of the application’s functionality (data loading, regression model fitting and obtaining predictions).
-	The application does not crash from incorrect inputs or user actions, but instead yield meaningful error messages.
-	The application scales well to larger that typically seen data in course projects (millions of datapoints).
-	User can select the degree of polynomial (however, floating point error reduces the usefulness of higher order polynomials).

### The areas of improvement in the project:
-	User interface is unnecessarily tightly coupled with the rest of the application. This is due to using a variety of different kinds of exceptions to convey messages to user interface, where each type of exception has been separately handled. It would have been better to instead use one kind of exception that would carry a string containing the reason for exception.
-	The event listeners are not used in an ideal manner in the user interface. After a user action, the application was explicitly told to update elements visible to user, separately for each possible user action. A better way to it would have been to tell certain elements simply to follow values in other elements and update accordingly, irrespective of which caused the values in the followed elements to change.
-	User interface is not very aesthetical, nor does it provide any information on regression model coefficients or goodness of fit measures.

### Program structure 
The goal was to keep the application structure as simple as possible, but extendable. In initial plans the structure was more complicated, such as with the graph as its own class. During implementation, a natural place for the graph turned out to be within user interface. Please see a UML class diagram and a description of the main classes below.

**Description of main classes:**
![Description of main classes](https://raw.githubusercontent.com/mholtta/CS-C2120_Programming_studio2-project/master/readMeImages/Main_classes.png?token=AOTNNCDRFS5IZRQKSNSVP7K7HEEHY)

**UML class diagram:**

![UML class diagram](https://raw.githubusercontent.com/mholtta/CS-C2120_Programming_studio2-project/master/readMeImages/UML_class_diagram.png?token=AOTNNCHREESKXPOWXQPSU427HEEKY)
