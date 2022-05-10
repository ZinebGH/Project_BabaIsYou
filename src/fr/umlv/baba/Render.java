//package fr.umlv.baba;
//
//
//import java.awt.*;
//import java.util.Objects;
//
//public class Render {
//	private final Grid grid;
//    private final int blockSize = 24;
//    private Image image;
//
//    public Render(Grid grid) {
//    	this.grid = Objects.requireNonNull(grid);
//    }
//    private void renderGame(Graphics2D graphics2D, int width, int height) {
//        grid.renderGrid(graphics2D, width, height);
//    }
//
//    public void renderGrid(Graphics2D graphics2D, int width, int height) {
//        for (var i = 0; i < cols; i++) {
//            for (var j = 0; j < rows; j++) {
//                var block = blocks[i][j];
//                int x = (i * blockSize) + (width - blockSize * cols) / 4;
//                int y = (j * blockSize) + (height - blockSize * rows) / 4;
//                block.renderBlock(graphics2D, x, y);
//            }
//        }
//    }
//
//
//    public void renderBlock(Graphics2D graphics2D, int x, int y) {
//        if (isType(WordTypes.BLANK)) {
//            return;
//        }
//        if (image == null) {
//            if (controllable) {
//                image = Toolkit.getDefaultToolkit().getImage("res/images/" + word.name() + "_0.gif");
//            } else {
//                image = Toolkit.getDefaultToolkit().getImage("res/images/Text_" + word.name() + "_0.gif");
//            }
//        }
//        if (secondNoun != null) {
//            Image image2;
//            image2 = Toolkit.getDefaultToolkit().getImage("res/images/" + secondNoun.word.name() + "_0.gif");
//            graphics2D.drawImage(image2 ,x , y, null);
//        }
//        graphics2D.drawImage(image ,x , y, null);
//    }
//
//}
