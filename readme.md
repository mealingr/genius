# Genius Square #

## Requirements ##

- Java 8+
- Maven 3+

## Building ##

In the project directory (where the `pom.xml` file is) run:

```
mvn clean install
```

## Running ##

Run the game via
```
java -jar genius-1.0-SNAPSHOT.jar
```
where `genius-1.0-SNAPSHOT.jar` is produced in the `target` directory after a successful build.

## Game ##

Before playing, select the peg piece using number `0`, and add 7 pegs to the board.

The goal of the game is to place all of your pieces onto the board after the pegs have been placed.

To select a piece, use the numbers `1-9`. You can also cycle through pieces using either `q` (backward) and `e`
(forward) or mouse wheel up (backward) and mouse wheel down (forward).

To rotate the selected piece, press `r`.

To flip the selected piece vertically, press `f`.

To place a piece, left click on the grid where you want the piece to be placed. If the placement is valid, then the
piece will be put there.

## Automatically Solving ##

At any point you can press `Enter` to have the computer automatically try to place the remaining pieces
(ignoring unplaced pegs).

## Web Version ##

On Windows, the community edition of [CheerpJ](https://www.leaningtech.com/pages/cheerpj.html), specifically
[version 2.1](https://d3415aa6bfa4.leaningtech.com/cheerpj_win_2.1.zip), was used to convert
`genius-1.0-SNAPSHOT.jar` into `genius-1.0-SNAPSHOT.jar.js` via
```
py cheerpjfy.py .\genius-1.0-SNAPSHOT.jar
```
The corresponding `index.html` is used to host this JavaScript version of the game, and is based on
[this example](https://github.com/leaningtech/cheerpj-meta/wiki/Getting-Started).
