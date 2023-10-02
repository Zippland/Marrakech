## Code Review

Reviewed by: <Xinyue Fei>, <u7615192>

Reviewing code written by: <Zihan Jian> <u7174903>

Component:
https://gitlab.cecs.anu.edu.au/u7174903/comp1110-ass2/src/Assam
https://gitlab.cecs.anu.edu.au/u7174903/comp1110-ass2/src/Board.java#27
### Comments 
****
Encapsulation: The Assam class encapsulates the state of an entity, i.e. its x and y coordinates and its orientation. This state is declared private and can only be accessed through the public methods (getter methods) defined in the class, not directly. This protects the internal state of the class and prevents external code from modifying it directly.

Simplicity: The Assam class is designed to be very simple and clear, containing only the necessary state and methods. This makes the class very easy to understand and use.

Immutability: Once an Assam object has been created, its state (coordinates and orientation) cannot be changed because no methods (setter methods) are provided to change these states. This is a design pattern known as "immutability", which adds security and predictability to your code because you know that once the object is created, its state will not change.

Clear interfaces: This class provides clear public interfaces, i.e., getX(), getY(), and getDirection() methods, making it easy for external code to get the state of the Assam object.

Good code style: This code follows good code style, including sensible indentation, spaces and naming conventions, making the code easy to read and understand.


