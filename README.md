SnakeHandTracking
=================

What is this?
-------------

A twist on the classic snake game where you control the snake's movement through hand gestures!

How do you play the game?
-------------------------

See set-up before running the game

Two windows, one containing your webcam and one with a stationary green pixel will appear. Once you rapidly move your hand in a singular direction (Up, down, left, right), the snake will start moving and the game will start. Just like the original snake game, fruit will randomly appear and the snake will get longer as more is eaten. The game will end when your snake collides with the border or itself.

How does the game recognize hand movement?
------------------------------------------

The mediapipe library was utilized in getting the pixel positions of each digit of the hand relative to the window. The average change in position of all these points between frames yielded its average velocity. If this velocity was above a certain threshold, the game registers it as one of the four directions.

Set-up
------

First, clone this repo:
```git clone https://github.com/kylerod22/SnakeHandTracking.git```

Then, install these libraries via pip:
```pip install mediapipe opencv-contrib-python numpy```

There is also a config file you can edit (```config.properties```) to change the settings of the game:
- ```CamId```: If your webcam is the only camera connected to your computer, set this to 0. Otherwise, keep on modifying it until the id represents your webcam.
- ```CamInverted```: Some webcams may be inverted (i.e. moving your hand left may be shown as moving right on the camera), so change this accordingly so that your camera essentially acts as a mirror.
- ```Width and Height```: Controls the width and height of the game window.
- ```Scale```: The larger the scale, the larger the window, and vice versa.
- ```DelayMillis```: The delay (in milliseconds) between each frame of snake movement.

Finally, run ```Game.java```

Demo
----
