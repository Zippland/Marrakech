# Test Plan

## List of Classes

### 1. Assam
* **Methods to be tested in isolation:**
  * `Assam(int x, int y, char direction)`
  * `getX()`
  * `getY()`
  * `getDirection()`

### 2. Player
* **Methods to be tested in isolation:**
  * `Player(char color, int dirhams, int rugs, boolean inGame)`
  * `getColor()`
  * `getDirhams()`
  * `getRugs()`
  * `isInGame()`

### 3. Rug
* **Methods to be tested in isolation:**
  * `Rug(char color, int id, int x1, int y1, int x2, int y2)`
  * `getColor()`
  * `getId()`
  * `getX1()`
  * `getY1()`
  * `getX2()`
  * `getY2()`

### 4. Viewer
* **Methods to be tested in isolation:**
  * `parseAssam(String assamString)`

## Conditions for Testing

* For the `Assam` class, we can test the constructor and getter methods in isolation. We should ensure that the values returned by the getter methods match the values provided to the constructor.
* For the `Player` class, we can also test the constructor and getter methods in isolation. We should ensure that the values returned by the getter methods match the values provided to the constructor.
* For the `Rug` class, we can test the constructor and getter methods in isolation. We should ensure that the values returned by the getter methods match the values provided to the constructor.
* For the `Viewer` class, we can test the `parseAssam` method in isolation. We should ensure that the Assam object returned by the method has the correct property values based on the input string.

Please note that the `Marrakech` class and predefined static methods for which unit tests have already been provided are not included in this test plan.