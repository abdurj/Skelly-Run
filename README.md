# Skelly-Run
Skelly Run is a 5-Stage 2D Platformer game developed using LibGDX and the Box2D library in Kotlin. The player can progress through all 5 levels increasing in difficulty by simply playing through the levels and defeating all enemies before walking into the teleporter at the end of the level. The game also features a secret speed run mode, where the player can try to finish the game as fast as possible by simply shooting the portal at the end of the level instead of walking into it, bypassing the requirement to defeat all the enemies to progress.

# Main Features

## 5 Unique Stages
  Each stage is given a unique theme: Forest, Ice, Lava, Industrial, and Cyberpunk and all elements in the stage follow the theme
![stages](https://github.com/abdurj/Skelly-Run/blob/master/imgs/ezgif.com-gif-maker.gif?raw=true)

## Enemy AI and Health
  Each enemy has a simple AI component, they will patrol an area near them and when the player is seen they will lock on and start shooting fireballs at the player. Each fireball itself does not do much damage, however as the level progresses the damage accumulates and the player is forced to either dodge each attack or defeat the enemy before progressing
  ![enemies](https://github.com/abdurj/Skelly-Run/blob/master/imgs/2021-01-06_12-05-34.gif?raw=true)
 
## Chargeable Fireballs
  Pressing spacebar creates a fireball for the player to shoot, holding it down increases the size of the fireball, slowing its speed, however dealing more damage
  ![fireball](https://i.imgur.com/ToyDTt0.gif)

## Parallax Background
  As the user plays through the level, different elements of the background move at different rates to give a more immersive effect
  ![parallax](https://github.com/abdurj/Skelly-Run/blob/master/imgs/2021-01-06_12-18-51.gif?raw=true)
  
# Usage
  To play the game it is required to install the Java Runtime Environment and a .jar file containing the game can be found in [desktop/build/libs](https://github.com/abdurj/Skelly-Run/tree/master/desktop/build/libs)
