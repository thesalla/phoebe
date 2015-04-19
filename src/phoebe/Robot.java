package phoebe;

import java.util.List;
import java.util.Random;

public class Robot implements GameObject {

    public static boolean randomStatus;

    public int idx;

    private Cell currentCell = new Cell();

    private Cell destinationCell = null;

    private Map map;

    private Random rand = new Random();

    private Cell initialPosition;

    public Robot(Map map, Cell initialPosition, int id) {
        this.idx = id;
        this.map = map;
        this.initialPosition = initialPosition;
        this.currentCell = this.initialPosition;
        this.currentCell.setGameObject(this);
    }

    @Override
    public void interact(Player player) {
        Cell cell = currentCell;

        OilStain oilStain = new OilStain();
        oilStain.setCell(cell);
        cell.setGameObject(oilStain);
        System.out.println(cell.toString());
        int newPosX = this.randomStatus ? this.rand.nextInt(map.getSize()[0])
                : this.initialPosition.getX();
        int newPosY = this.randomStatus ? this.rand.nextInt(map.getSize()[1])
                : this.initialPosition.getY();

        currentCell = map.getCell(newPosX, newPosY);
    }

    @Override
    public void onTurnStart() {

        if (destinationCell == null) {
            // Választunk új cellát
            List<Cell> destinationCells = map.getCellsWithStain();

            if (destinationCells.size() > 0) {
                destinationCell = destinationCells.get(0);

                for (Cell cell : destinationCells) {
                    if (getDistance(cell) < getDistance(destinationCell)) {
                        destinationCell = cell;
                    }
                }
            }
        }

        if (destinationCell.equals(currentCell)) {
            destinationCell = null;
            System.out.println(currentCell.toString());

            return;
        }
      
        Cell cell = this.currentCell;

        if (this.randomStatus) { // Ha az automatikus mozgás be van kapcsolva,
                                 // mozgunk
            // Útkeresés
            int nextPosX = 0;
            int nextPosY = 0;

            if (destinationCell.getX() != currentCell.getX()) {

                if (destinationCell.getX() > currentCell.getX()) {
                    nextPosX = currentCell.getX() + 1;
                } else {
                    nextPosX = currentCell.getX() - 1;
                }
                cell = map.getCell(nextPosX, currentCell.getY());

            } else if (destinationCell.getY() != currentCell.getY()) {

                if (destinationCell.getY() > currentCell.getY()) {
                    nextPosY = currentCell.getY() + 1;
                } else {
                    nextPosY = currentCell.getY() - 1;
                }
                cell = map.getCell(currentCell.getX(), nextPosY);
            }
        }

        if ((cell.getGameObject() != null
                && cell.getGameObject() instanceof Robot && !cell
                .getGameObject().equals(this)) || (cell.getPlayer() != null)) {
            currentCell.setGameObject(null);

            if (this.randomStatus) {
                List<Cell> neighbours = map.getNeighbours(currentCell, 1);
                currentCell = neighbours.get(this.rand.nextInt(neighbours
                        .size()));
            } else {
                currentCell = this.initialPosition;
                System.out
                        .println(String
                                .format("Hardworking-little-robot %d: Collision; New Position: Cell(%d, %d)",
                                        this.idx, this.currentCell.getX(),
                                        this.currentCell.getY()));
            }
        } else {
            currentCell.setGameObject(null);
            currentCell = cell;
            currentCell.setGameObject(this);
        }

        /*
         * if (destinationCell == null) {
         * 
         * List<Cell> destinationCells = map.getCellsWithStain();
         * 
         * if (destinationCells.size() > 0) { destinationCell =
         * destinationCells.get(0);
         * 
         * for (Cell cell : destinationCells) { if (getDistance(cell) <
         * getDistance(destinationCell)) { destinationCell = cell; } } }
         * 
         * } else if (this.randomStatus) { int nextPosX = 0; int nextPosY = 0;
         * Cell cell = new Cell();
         * 
         * if (destinationCell.equals(currentCell)) { destinationCell = null;
         * System.out.println(currentCell.toString());
         * 
         * return; } else { if (destinationCell.getX() != currentCell.getX()) {
         * 
         * if (destinationCell.getX() > currentCell.getX()) { nextPosX =
         * currentCell.getX() + 1; } else { nextPosX = currentCell.getX() - 1; }
         * cell = map.getCell(nextPosX, currentCell.getY());
         * 
         * } else if (destinationCell.getY() != currentCell.getY()) {
         * 
         * if (destinationCell.getY() > currentCell.getY()) { nextPosY =
         * currentCell.getY() + 1; } else { nextPosY = currentCell.getY() - 1; }
         * cell = map.getCell(currentCell.getX(), nextPosY); } }
         * 
         * if ((cell.getGameObject()!=null &&
         * cell.getGameObject().toString().equals("Hardworking-little-robot"))
         * || (cell.getPlayer()!=null)) { List<Cell> neighbours =
         * map.getNeighbours(currentCell, 1); currentCell.setGameObject(null);
         * 
         * if (this.randomStatus) { currentCell =
         * neighbours.get(this.rand.nextInt(neighbours.size())); } else {
         * currentCell = this.map.getCell(this.idx, this.idx);
         * System.out.println(String.format(
         * "Hardworking-little-robot %d: Collision; New Position: Cell(%d, %d)",
         * this.idx, this.currentCell.getX(), this.currentCell.getY())); } }
         * else { currentCell.setGameObject(null); currentCell = cell;
         * currentCell.setGameObject(this); } }
         */
    }

    public void move(Cell cell) {
        GameObject currentObj = cell.getGameObject();

        if ((currentObj instanceof Robot && !currentObj.equals(this)) || cell.getPlayer() != null) {
            // Ütközés!
            cell = this.initialPosition;
            System.out
                    .println(String
                            .format("Hardworking-little-robot %d: Collision; New Position: Cell(%d, %d)",
                                    this.idx, this.currentCell.getX(),
                                    this.currentCell.getY()));
        }
        
        
        if (currentObj instanceof Stain) {
            currentCell.setGameObject(null);
            System.out.println(currentCell.toString());
        }

        currentCell = cell;
        currentCell.setGameObject(this);
    }

    public int getDistance(Cell cell) {
        return Math.abs(this.currentCell.getX() - cell.getX())
                + Math.abs(this.currentCell.getY() - cell.getY());
    }

    @Override
    public void onTurnEnd() {
    }

    @Override
    public String toString() {
        return "Hardworking-little-robot";
    }

    @Override
    public void setCell(Cell cell) {
        this.currentCell = cell;
    }
}
