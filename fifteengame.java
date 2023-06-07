import java.util.ArrayList;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents an individual tile 
class Tile { 
  
  int value;
  
  Tile(int value) { 
    this.value = value;
  }
  
  // draw a tile on top of the given background
  WorldImage drawAt(int col, int row, WorldImage background) { 
    //return new OverlayImage(new TextImage(String.valueOf(this.value), 18, Color.BLACK), 
    //    background);
    if (this.value == 16) { 
      return new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY);
    }
    
    WorldImage tileDraw = new OverlayImage(new TextImage(String.valueOf(this.value), 
        18, Color.BLACK), background);
    return tileDraw;
  }
  
  // checks to see whether the given value is the same as this value
  boolean checkValue(int count) { 
    return this.value == count;
  }
  
}

// to represent abstracting all duplicate code to work on tiles
class Tiles {
  
  ArrayList<ArrayList<Tile>> lot;
  
  Tiles(ArrayList<ArrayList<Tile>> lot) {
    this.lot = lot;
  }
  
  // randomize the positions of the tiles
  ArrayList<ArrayList<Tile>> randomizeTile() {
    for (int tile = 0; tile < this.lot.size(); tile++) { 
      for (int t = 0; t < this.lot.get(tile).size(); t++) { 
        int swapRandRow = new Random().nextInt(4); 
        int swapRandCol = new Random().nextInt(4); 
        
        Tile temp = this.lot.get(swapRandRow).get(swapRandCol); 
        this.lot.get(swapRandRow).set(swapRandCol, 
            this.lot.get(tile).get(t));
        this.lot.get(tile).set(t, temp);
      }
    }
    
    return this.lot;
   
  }
  
}


// Represents the world scene
class FifteenGame extends World { 
  
  ArrayList<ArrayList<Tile>> tiles;
  
  FifteenGame(ArrayList<ArrayList<Tile>> tiles) { 
    this.tiles = tiles;
  }
  
  // draws the game
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(300, 300);
    WorldImage background = new RectangleImage(300, 300, OutlineMode.OUTLINE, 
        Color.BLACK);
    ws.placeImageXY(background, 150, 150);
    
    // draws the tile
    for (int tile = 0; tile < this.tiles.size(); tile++) { 
      for (int t = 0; t < this.tiles.get(tile).size(); t++) { 
        ws.placeImageXY(this.tiles.get(tile).get(t).drawAt(t, tile, 
            new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY)), 
            (tile + 2) * 40 , (t + 2) * 40);
      } 
    }
    
    return ws;
    
  }
   
  // handles the keystroke
  //move the tile to the right of the hole left to fill the hole
  public void onKeyEvent(String k) { 
    // needs to handle up, down, left, right to move hole
    // extra: handle "u" to undo moves
    int emptyIndexRow = -1; 
    int emptyIndexCol = -1;
    
    for (int tile = 0; tile < tiles.size(); tile++) { 
      for (int t = 0; t < tiles.get(tile).size(); t++) { 
        if (this.tiles.get(tile).get(t).checkValue(16)) { 
          emptyIndexRow = tile; 
          emptyIndexCol = t;
        }
      }
    }
    
    if (k.equals("up") && emptyIndexCol != this.tiles.get(emptyIndexRow).size() - 1) { 
      Tile emptyTile = this.tiles.get(emptyIndexRow).get(emptyIndexCol);
      this.tiles.get(emptyIndexRow).set(emptyIndexCol,
          this.tiles.get(emptyIndexRow).get(emptyIndexCol + 1));
      this.tiles.get(emptyIndexRow).set(emptyIndexCol + 1, 
          emptyTile);
    }
    
    if (k.equals("down") && emptyIndexCol != 0) { 
      Tile emptyTile = this.tiles.get(emptyIndexRow).get(emptyIndexCol);
      this.tiles.get(emptyIndexRow).set(emptyIndexCol,
          this.tiles.get(emptyIndexRow).get(emptyIndexCol - 1));
      this.tiles.get(emptyIndexRow).set(emptyIndexCol - 1, 
          emptyTile);
    }
    
    if (k.equals("left") && emptyIndexRow != this.tiles.get(emptyIndexRow).size() - 1) { 
      Tile emptyTile = this.tiles.get(emptyIndexRow).get(emptyIndexCol);
      this.tiles.get(emptyIndexRow).set(emptyIndexCol,
          this.tiles.get(emptyIndexRow + 1).get(emptyIndexCol));
      this.tiles.get(emptyIndexRow + 1).set(emptyIndexCol, 
          emptyTile);
    }
    
    if (k.equals("right") && emptyIndexRow != 0) { 
      Tile emptyTile = this.tiles.get(emptyIndexRow).get(emptyIndexCol);
      this.tiles.get(emptyIndexRow).set(emptyIndexCol,
          this.tiles.get(emptyIndexRow - 1).get(emptyIndexCol));
      this.tiles.get(emptyIndexRow - 1).set(emptyIndexCol, 
          emptyTile);
    }
    
  }
  
  // determines if the game has been won
  public WorldEnd worldEnds() { 
    boolean inOrder = true; 
    int counter = 1;
    for (int n = 0; n < this.tiles.size(); n++) { 
      for (int i = 0; i < this.tiles.get(n).size(); i++) {
        if (this.tiles.get(n).get(i).checkValue(16)) { 
          continue;
        }
        if (!this.tiles.get(n).get(i).checkValue(counter)) { 
          inOrder = false;
        }
        counter += 1;
      }
    }
    
    if (inOrder) { 
      return new WorldEnd(true, this.lastScene("You have won"));
    } else { 
      return new WorldEnd(false, this.makeScene());
    }
   
  }
  
  // displays the final world scene
  public WorldScene lastScene(String msg) {
    WorldScene ws = new WorldScene(300, 300);
    ws.placeImageXY(new TextImage((msg), 24, FontStyle.BOLD, Color.RED), 
        150, 150);
    return ws;
  }
  
}

// Represents examples FifteenGame
class ExampleFifteenGame {
  
  // to test the game
  void testGame(Tester t) {
    ArrayList<Tile> arr1tile1 = new ArrayList<Tile>();
    arr1tile1.add(new Tile(1));
    arr1tile1.add(new Tile(2));
    arr1tile1.add(new Tile(3));
    arr1tile1.add(new Tile(4));
    
    ArrayList<Tile> arr1tile2 = new ArrayList<Tile>();
    arr1tile2.add(new Tile(5));
    arr1tile2.add(new Tile(6));
    arr1tile2.add(new Tile(7));
    arr1tile2.add(new Tile(8));
    
    ArrayList<Tile> arr1tile3 = new ArrayList<Tile>();
    arr1tile3.add(new Tile(9));
    arr1tile3.add(new Tile(10));
    arr1tile3.add(new Tile(11));
    arr1tile3.add(new Tile(12));
    
    ArrayList<Tile> arr1tile4 = new ArrayList<Tile>();
    arr1tile4.add(new Tile(13));
    arr1tile4.add(new Tile(14));
    arr1tile4.add(new Tile(15));
    arr1tile4.add(new Tile(16));
    
    ArrayList<ArrayList<Tile>> arr1 = new ArrayList<ArrayList<Tile>>();
    arr1.add(arr1tile1);
    arr1.add(arr1tile2);
    arr1.add(arr1tile3);
    arr1.add(arr1tile4);
    
    FifteenGame g = new FifteenGame(new Tiles(arr1).randomizeTile());
    g.bigBang(300, 300);
    
  }
  
  // to test drawAt
  void testDrawAt(Tester t) {
    WorldImage background = new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY);
    
    t.checkExpect(new Tile(5).drawAt(1, 3, background), 
        new OverlayImage(new TextImage(String.valueOf(5), 
            18, Color.BLACK), background));
    
    t.checkExpect(new Tile(7).drawAt(1, 3, background), 
        new OverlayImage(new TextImage(String.valueOf(7), 
            18, Color.BLACK), background));
    
    t.checkExpect(new Tile(9).drawAt(1, 3, background), 
        new OverlayImage(new TextImage(String.valueOf(9), 
            18, Color.BLACK), background));
    
    t.checkExpect(new Tile(10).drawAt(1, 3, background), 
        new OverlayImage(new TextImage(String.valueOf(10), 
            18, Color.BLACK), background));
    
    t.checkExpect(new Tile(16).drawAt(1, 3, background), 
        new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY));
    
  }
  
  // to test makeScene
  void testMakeScene(Tester t) { 
    WorldScene ws = new WorldScene(300, 300);
    
    ArrayList<Tile> arr1tile1 = new ArrayList<Tile>();
    Tile tile1 = new Tile(1);
    arr1tile1.add(tile1);
    
    ArrayList<Tile> arr1tile2 = new ArrayList<Tile>();
    Tile tile2 = new Tile(5);
    arr1tile2.add(tile2);
    
    ArrayList<Tile> arr1tile3 = new ArrayList<Tile>();
    Tile tile3 = new Tile(9);
    arr1tile3.add(tile3);
    
    ArrayList<ArrayList<Tile>> arr1 = new ArrayList<ArrayList<Tile>>();
    arr1.add(arr1tile1);
    arr1.add(arr1tile2);
    arr1.add(arr1tile3);
    
    ws.placeImageXY(tile1.drawAt(0, 0, 
        new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY)), 
        (2) * 40, (2) * 40);
    ws.placeImageXY(tile2.drawAt(1, 0,
        new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY)),
        (3) * 40, (2) * 40);
    ws.placeImageXY(tile3.drawAt(2, 0, 
        new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY)), 
        (4) * 40 , (2) * 40);
    
    t.checkExpect(new FifteenGame(arr1).makeScene(), ws);
    
    Tile tile4 = new Tile(8);
    arr1tile3.add(tile4);
    
    ws.placeImageXY(tile4.drawAt(2, 1, 
        new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY)), 
        (4) * 40 , (3) * 40);
    
    t.checkExpect(new FifteenGame(arr1).makeScene(), ws);
    
    Tile tile5 = new Tile(16);
    arr1tile3.add(tile5);
    
    ws.placeImageXY(tile5.drawAt(2, 2, 
        new RectangleImage(50, 50, OutlineMode.SOLID, Color.GRAY)), 
        (4) * 40 , (4) * 40);
    
    t.checkExpect(new FifteenGame(arr1).makeScene(), ws);
    
  }
  
  // to test onkeyEvent
  void testOnKeyEvent(Tester t)  {
    
    // array list to be passed through FifteenGame and for
    // invoke onkeyEvent
    ArrayList<Tile> arr1tile1 = new ArrayList<Tile>();
    arr1tile1.add(new Tile(1));
    arr1tile1.add(new Tile(2));
    arr1tile1.add(new Tile(3));
    arr1tile1.add(new Tile(4));
    
    ArrayList<Tile> arr1tile2 = new ArrayList<Tile>();
    arr1tile2.add(new Tile(5));
    arr1tile2.add(new Tile(6));
    arr1tile2.add(new Tile(7));
    arr1tile2.add(new Tile(8));
    
    ArrayList<Tile> arr1tile3 = new ArrayList<Tile>();
    arr1tile3.add(new Tile(9));
    arr1tile3.add(new Tile(10));
    arr1tile3.add(new Tile(11));
    arr1tile3.add(new Tile(12));
    
    ArrayList<Tile> arr1tile4 = new ArrayList<Tile>();
    arr1tile4.add(new Tile(13));
    arr1tile4.add(new Tile(14));
    arr1tile4.add(new Tile(15));
    arr1tile4.add(new Tile(16));
    
    ArrayList<ArrayList<Tile>> arr1 = new ArrayList<ArrayList<Tile>>();
    arr1.add(arr1tile1);
    arr1.add(arr1tile2);
    arr1.add(arr1tile3);
    arr1.add(arr1tile4);
    
    // creating new ArrayList to compare to the array list that 
    // has been modified by the on key event
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);
    Tile tile3 = new Tile(3);
    Tile tile4 = new Tile(4);
    Tile tile5 = new Tile(5);
    Tile tile6 = new Tile(6);
    Tile tile7 = new Tile(7);
    Tile tile8 = new Tile(8);
    Tile tile9 = new Tile(9);
    Tile tile10 = new Tile(10);
    Tile tile11 = new Tile(11);
    Tile tile12 = new Tile(12);
    Tile tile13 = new Tile(13);
    Tile tile14 = new Tile(14);
    Tile tile15 = new Tile(15);
    Tile tile16 = new Tile(16);
    
    
    ArrayList<Tile> arr1t1 = new ArrayList<Tile>();
    arr1t1.add(tile1);
    arr1t1.add(tile2);
    arr1t1.add(tile3);
    arr1t1.add(tile4);
    
    ArrayList<Tile> arr1t2 = new ArrayList<Tile>();
    arr1t2.add(tile5);
    arr1t2.add(tile6);
    arr1t2.add(tile7);
    arr1t2.add(tile8);
    
    ArrayList<Tile> arr1t3 = new ArrayList<Tile>();
    arr1t3.add(tile9);
    arr1t3.add(tile10);
    arr1t3.add(tile11);
    arr1t3.add(tile12);
    
    ArrayList<Tile> arr1t4 = new ArrayList<Tile>();
    arr1t4.add(tile13);
    arr1t4.add(tile14);
    arr1t4.add(tile15);
    arr1t4.add(tile16);
    
    ArrayList<ArrayList<Tile>> arrT1 = new ArrayList<ArrayList<Tile>>();
    arrT1.add(arr1t1);
    arrT1.add(arr1t2);
    arrT1.add(arr1t3);
    arrT1.add(arr1t4);
    
    // switches the elements of the array -> 
    // by calling on key event
    new FifteenGame(arr1).onKeyEvent("down");
    
    arrT1.get(3).set(2, tile16);
    arrT1.get(3).set(3, tile15);
    
    t.checkExpect(arr1, arrT1);
    
    new FifteenGame(arr1).onKeyEvent("left");
    
    t.checkExpect(arr1, arrT1);
    
    new FifteenGame(arr1).onKeyEvent("right");
    
    arrT1.get(2).set(2, tile16);
    arrT1.get(3).set(2, tile11);
    
    t.checkExpect(arr1, arrT1);
    
    new FifteenGame(arr1).onKeyEvent("up");
    
    arrT1.get(2).set(2, tile12);
    arrT1.get(2).set(3, tile16);
    
    t.checkExpect(arr1, arrT1);
    
  }
  
  // to test worldEnds
  void testWorldEnds(Tester t) { 
    ArrayList<Tile> arr1tile1 = new ArrayList<Tile>();
    arr1tile1.add(new Tile(1));
    arr1tile1.add(new Tile(2));
    arr1tile1.add(new Tile(3));
    arr1tile1.add(new Tile(4));
    
    ArrayList<Tile> arr1tile2 = new ArrayList<Tile>();
    arr1tile2.add(new Tile(5));
    arr1tile2.add(new Tile(6));
    arr1tile2.add(new Tile(7));
    arr1tile2.add(new Tile(8));
    
    ArrayList<Tile> arr1tile3 = new ArrayList<Tile>();
    arr1tile3.add(new Tile(9));
    arr1tile3.add(new Tile(10));
    arr1tile3.add(new Tile(11));
    arr1tile3.add(new Tile(12));
    
    ArrayList<Tile> arr1tile4 = new ArrayList<Tile>();
    arr1tile4.add(new Tile(13));
    arr1tile4.add(new Tile(14));
    arr1tile4.add(new Tile(15));
    arr1tile4.add(new Tile(16));
    
    ArrayList<ArrayList<Tile>> arr1 = new ArrayList<ArrayList<Tile>>();
    arr1.add(arr1tile1);
    arr1.add(arr1tile2);
    arr1.add(arr1tile3);
    arr1.add(arr1tile4);
    
    WorldScene ws = new WorldScene(300, 300);
    ws.placeImageXY(new TextImage(("You have won"), 24, FontStyle.BOLD, Color.RED), 
        150, 150);
    
    t.checkExpect(new FifteenGame(arr1).worldEnds(), 
        new WorldEnd(true, ws));
    
    ArrayList<Tile> arr2tile1 = new ArrayList<Tile>();
    arr2tile1.add(new Tile(1));
    arr2tile1.add(new Tile(2));
    arr2tile1.add(new Tile(3));
    arr2tile1.add(new Tile(4));
    
    ArrayList<Tile> arr2tile2 = new ArrayList<Tile>();
    arr2tile2.add(new Tile(5));
    arr2tile2.add(new Tile(6));
    arr2tile2.add(new Tile(7));
    arr2tile2.add(new Tile(8));
    
    ArrayList<Tile> arr2tile3 = new ArrayList<Tile>();
    arr2tile3.add(new Tile(9));
    arr2tile3.add(new Tile(10));
    arr2tile3.add(new Tile(16));
    arr2tile3.add(new Tile(11));
    
    ArrayList<Tile> arr2tile4 = new ArrayList<Tile>();
    arr2tile4.add(new Tile(12));
    arr2tile4.add(new Tile(13));
    arr2tile4.add(new Tile(14));
    arr2tile4.add(new Tile(15));
    
    ArrayList<ArrayList<Tile>> arr2 = new ArrayList<ArrayList<Tile>>();
    arr2.add(arr2tile1);
    arr2.add(arr2tile2);
    arr2.add(arr2tile3);
    arr2.add(arr2tile4);
    
    t.checkExpect(new FifteenGame(arr2).worldEnds(), 
        new WorldEnd(true, ws));
    
    new FifteenGame(arr2).onKeyEvent("left");
    
    t.checkExpect(new FifteenGame(arr2).worldEnds(), 
        new WorldEnd(false, new FifteenGame(arr2).makeScene()));

  }
  
  // to test lastScene
  void testLastScene(Tester t) { 
    ArrayList<Tile> arr1tile1 = new ArrayList<Tile>();
    arr1tile1.add(new Tile(1));
    arr1tile1.add(new Tile(2));
    arr1tile1.add(new Tile(3));
    arr1tile1.add(new Tile(4));
    
    ArrayList<Tile> arr1tile2 = new ArrayList<Tile>();
    arr1tile2.add(new Tile(5));
    arr1tile2.add(new Tile(6));
    arr1tile2.add(new Tile(7));
    arr1tile2.add(new Tile(8));
    
    ArrayList<Tile> arr1tile3 = new ArrayList<Tile>();
    arr1tile3.add(new Tile(9));
    arr1tile3.add(new Tile(10));
    arr1tile3.add(new Tile(11));
    arr1tile3.add(new Tile(12));
    
    ArrayList<Tile> arr1tile4 = new ArrayList<Tile>();
    arr1tile4.add(new Tile(13));
    arr1tile4.add(new Tile(14));
    arr1tile4.add(new Tile(15));
    arr1tile4.add(new Tile(16));
    
    ArrayList<ArrayList<Tile>> arr1 = new ArrayList<ArrayList<Tile>>();
    arr1.add(arr1tile1);
    arr1.add(arr1tile2);
    arr1.add(arr1tile3);
    arr1.add(arr1tile4);
    
    // only one test written because the only way that lastScene will be invoked
    // is if the game is won -> only one condition to test
    WorldScene ws = new WorldScene(300, 300);
    ws.placeImageXY(new TextImage(("you have won"), 24, FontStyle.BOLD, Color.RED), 
        150, 150);
    
    t.checkExpect(new FifteenGame(arr1).lastScene("you have won"), ws);
    
  }
  
  
}
