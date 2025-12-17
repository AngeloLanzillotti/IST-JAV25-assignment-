# IST-JAV25-assignment

## 1. Project Overview
This project is a standalone Java application implementing a core Turn-Based Role-Playing Game (RPG) combat and character management system.
The application allows users to create and manage player profiles, select diverse character classes, engage in simulated combat scenarios, manage equipment, and track progress through an experience system.

## 2. Key features
__1. Character & Combat System (Model)__
The game features a rich set of character entities, each defined by core stats and distinct abilities.
  - Diverse Character Classes: Includes playable classes (Warrior, Archer, Wizard) and enemy types (Dragon, Goblin, Orc). All characters inherit from the CharacterModel base class, ensuring consistent behavior across all entities.
  -  Stats and Progression: Characters are defined by Stats and progress using an ExperienceProgressBar tied to an internal experience system.
  -   Combat Logic: The GameController manages the turn-based combat flow, determining actions, damage calculations, and win/loss conditions.

__2. Equipment and Items System (Model)__
A complete system for handling inventory and character equipment, supporting both consumption and permanent enhancements.
  -	Equippable Items: Defined by the Equipable class, items like Sword, Crossbow, Armor, and Shield modify character stats. The system is managed via the EquipmentController.
  -	Consumables: Includes various Potion types (HealthPotion, PowerfulPotion, SpeedPotion) that can be used during gameplay.

__3. User Management & Persistence (Model/Users)__
The application includes a system to manage user accounts and application state across sessions.
  - User Profiles: Users are handled by the User class and managed via the Singleton UserManager.
  - Session Management: The UserManager handles login, user creation, experience updates, and data persistence by writing user data to a dedicated file (users.txt), ensuring user progress is saved.

__4. Graphical User Interface (GUI - View)__
The entire application runs on a dedicated, custom-built Graphical User Interface (GUI), providing an intuitive experience.
  - Dedicated Windows: The game flow is driven by dedicated windows, including the IntroductionWindow, CharacterSelection, GameWindow, and EquipmentWindow.
  - Visual Feedback: Components like HealthProgressBar and ExperienceProgressBar provide real-time visual feedback on character status.
  - Interactive Components: Uses custom components (RoundButton, ItemLabel) and listeners (DropListener, EquipmentEventListener) for interactive gameplay.
![Game Preview](resources/images/GameWindowScreenshot.png)

## 3. Project Structure
```
Game/
└── resources/
|   └── data/
|   |   └── users.txt
|   └── images/
|       └── *.png
|
└── src/
    └── Main.java
    └── controller/
    |   └── eventListeners/
    |   |   └── CharacterEventListener.java
    |   |       DropListener.java
    |   |       EquipmentEventListener.java
    |   └── gameFlow/
    |   |   └── CharacterSelectionController.java
    |   |       GameController.java
    |   |       IntroductionController.java
    |   └── system/
    |       └── EquipmentController.java
    |           MessageController.java
    |
    └── model/
    |  └── core/
    |  |   └── CharacterType.java
    |  |       Difficulty.java
    |  |       ItemType.java
    |  |       Stats.java
    |  └── entity/
    |  |   └── base/
    |  |   |   └── CharacterModel.java
    |  |   |       Enemy.java
    |  |   |       PlayerCharacter.java
    |  |   └── concrete/
    |  |       └── Archer.java
    |  |           Dragon.java
    |  |           Goblin.java
    |  |           Orc.java
    |  |           Warrior.java
    |  |           Wizard.java
    |  └── items/
    |  |   └── base/
    |  |   |   └── Equipable.java
    |  |   |       Item.java
    |  |   |       Potion.java
    |  |   └── concrete/
    |  |       └── Armor.java
    |  |           Crossbow.java
    |  |           HealthPotion.java
    |  |           PowerfulPotion.java
    |  |           Shield.java
    |  |           SpeedPotion.java
    |  |           Sword.java
    |  └── users/
    |      └── Session.java
    |          User.java
    |          UserManager.java
    |
    └── utility/
    |   └── engine/
    |       └── DelayTimer.java
    |
    └── view/
        └── assets/
        |   └── Images.java
        └── components/
        |   └── ExperienceProgressBar.java
        |       HealthProgressBar.java
        |       ItemLabel.java
        |       RoundButton.java
        └── panels/
        |   └── CharacterSelection.java
        |       CharacterSelectionInterface.java
        |       EnemySelection.java
        |       EquipmentPanel.java
        └── windows/
            └── EquipmentWindow.java
                GameWindow.java
                IntroductionWindow.java
                MyFrame.java
                UserWindow.java
```
### Documentation
La documentazione tecnica completa è generata tramite Javadoc. 
È possibile esplorare la gerarchia delle classi, i metodi e le logiche di sistema qui: 
**[Esplora la Javadoc del Progetto](https://angelolanzillotti.github.io/IST-JAV25-assignment-/docs/index.html)**
