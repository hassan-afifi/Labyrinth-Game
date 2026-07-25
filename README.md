# Labyrinth Game

A Java desktop maze game where players navigate randomly selected labyrinths while avoiding a roaming dragon. The game features limited visibility, persistent high scores stored in MySQL, randomized levels, and progressively replayable gameplay.

## Overview

Players must escape procedurally selected labyrinths by reaching the exit while avoiding a moving dragon. Each completed level loads a new random labyrinth, allowing continuous gameplay and score progression.

The project demonstrates object-oriented game architecture, event-driven programming, file-based level loading, and database integration.

## Features

- Random labyrinth selection
- Fog-of-war visibility system
- Dragon enemy AI
- Keyboard movement (WASD / Arrow Keys)
- Multiple handcrafted levels
- High score system
- MySQL database integration
- Timer and score tracking
- Restart functionality
- Menu system

## Technologies

- Java
- Java Swing
- Java2D
- JDBC
- MySQL
- File I/O

## Project Structure

```text
Labyrinth-Game/
│
├── LabyrinthGame.java
├── GamePanel.java
├── Player.java
├── Dragon.java
├── LevelManager.java
├── DatabaseManager.java
├── HighScore.java
├── levels/
├── README.md
├── LICENSE
└── .gitignore
```

## Gameplay

- Navigate the maze
- Avoid the dragon
- Reach the exit
- Complete increasingly more labyrinths
- Record your score in the leaderboard

## Game Mechanics

### Fog of War

Only nearby tiles are visible, requiring exploration rather than full-map knowledge.

### Dragon AI

The dragon patrols the labyrinth autonomously and defeats the player if it reaches them.

### Random Levels

A random labyrinth is loaded each round until all available levels have been played.

### High Scores

Scores are stored in a MySQL database and displayed through an in-game leaderboard.

## Learning Objectives

This project demonstrates:

- Object-oriented programming
- Event-driven programming
- Game loops
- Java Swing development
- Collision detection
- File handling
- Database programming with JDBC
- Simple game AI

## License

This project is licensed under the **Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License**.

https://creativecommons.org/licenses/by-nc-nd/4.0/
