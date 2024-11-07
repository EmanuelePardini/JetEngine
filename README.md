# Jet Game Engine

## Overview
This documentation is designed for anyone interested in understanding the fundamental principles behind the development of a 2D Game Engine. It will guide you through the concepts of game engine architecture, resource management, rendering, and physics using Java. No prior experience in graphics programming or low-level development is required, although basic knowledge of object-oriented programming will be helpful for understanding the technical aspects.

The engine will be written entirely in Java to take advantage of automatic memory management (garbage collection). This allows the focus to be on graphic technologies and gameplay mechanics rather than on low-level memory management, such as dealing with memory leaks or dangling pointers.

By building a 2D engine, we eliminate the complexities associated with 3D graphics, making it easier to focus on core game mechanics and optimizing the engine’s performance. This approach also simplifies the management of resources and collisions, allowing a more straightforward and intuitive development process.

The goal of this documentation is to provide an accessible and practical guide for creating a 2D game engine in Java, covering key concepts like engine architecture, resource management, the use of libraries like OpenGL, and essential performance optimizations.

The content is divided into two parts: theoretical knowledge with practical examples in the first part, followed by practical engine development in the second part.

## Description
A beginner-friendly 2D Game Engine built in Java to simplify game engine development. This engine focuses on core systems like rendering, physics, and input management while abstracting away complex memory management concerns.

## Features
- **Rendering System**: Efficient 2D rendering pipeline with support for graphical assets.
- **Physics Engine**: Simple yet effective 2D physics simulation.
- **Input Handling**: Support for keyboard, mouse, and gamepad inputs.
- **Audio**: Basic sound system integration for 2D games.
- **Resource Management**: Load and manage game assets such as textures, sounds, and fonts.
- **Entity-Component-System (ECS)**: Flexible architecture to manage game objects and their behaviors.

## How to Use

### Step 1: Create a Java Project
To use this engine, simply start by setting up a new Java project. The engine’s core components will be provided as source code files, which you can import into your project.

### Step 2: Copy Source Classes and Import HUD Plugin
- Download and add the source classes from this repository to your Java project.
- Integrate the provided HUD (Heads-Up Display) plugin for displaying UI elements like scores, health, and other game stats.

### Step 3: Setup Your Game in the Engine
Once the classes are integrated into your project, you'll need to set up the engine for your game. This involves configuring the rendering loop, initializing input and physics systems, and loading game assets. You can modify the engine's features to suit the needs of your game.

### Step 4: Build and Run Your Game
After setup, you can start building and testing your game using the engine. The engine is flexible enough for you to extend and customize, but provides a solid foundation to begin development without needing to worry about low-level engine features.

## Demonstration:
- [Demo](https://www.youtube.com/watch?v=gK3r-UttiK8)

## Technologies Used:
- **Java** for the core engine development.
- **OpenGL** for rendering 2D graphics.
- **OpenAL** for 2D audio support.
- **LWJGL3** for providing bindings to OpenGL, OpenAL, and OpenCL in Java.

This game was created by [Emanuele Pardini](http://emanuelepardini.altervista.org/). Enjoy!

## Additional Information
This game engine is tailored for those who want to learn the fundamentals of game engine development in a simplified 2D context. The goal is to provide an accessible starting point for understanding how game engines work internally, without the complexity of 3D or low-level memory management issues.
