# Programming studio 2-regression viz

Project brief:

Fitting and Visualization of a Regression Model

Theme: statistics

Build a program which can be used to fit a regression model to a dataset and visualize the results.

Regression means estimating the relationships between variables. Perhaps the simplest, yet very useful method for doing this is simple linear regression. It can be used when the data has two variables (x, y) and we can reasonably claim that they are proportional to each other, i.e.

    y = a + b * x

for some a and b – finding appropriate values for them is the ”fitting” part of this project.

The formula can be useful even if the variables are directly proportional, because it is often possible to find a function which transforms the x variables so that they are. After doing this, the formula becomes

    y = a + b * f(x)

For example, if the true shape of the data is a parabola instead of a straight line, we could take the square root of x before fitting the model.



Requirements
Intermediate

        The user of the program should be able to load the dataset from a file of their choosing. The file may contain even a large amount of (x, y) coordinate pairs.
        Implement simple linear regression. (Only the first equation. Since this is a programming course, not a math course, it is not necessary to show why your method produces a good fit – if you have not taken MS-A050X First course in probability and statistics or have equivalent knowledge, you may study the subject online or ask an assistant).
        The program should produce a plot of the original datapoints and the fitted model. The appearance of the graph, such as the endpoints of the axes, can be configured by the user.
        The implementation should be done with extendability in mind; when designing your program, you should consider how you could make it as easy as possible if you were to implement the advanced level requirements in the future.

Advanced

        All the requirements of the intermediate level.
        Support for loading at least two different file formats.
        Implement at least one regression method in addition to simple linear regression. (Either both the first and second equations, or the first equation and a method of your choice).

