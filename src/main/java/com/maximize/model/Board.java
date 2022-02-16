package com.maximize.model;

import com.maximize.util.MaximizeDeque;

import java.util.Random;

public class Board {
    public static final int MAX_DIM = 50;
    private Integer id;
    private Cell[][] matrix;
    private int rows, columns;
    private MaximizeDeque<Cell[][]> changes;

    public Board(int rows, int columns){ this(rows, columns, null, null); }

    public Board(int id, String matrixTex){ this(0, 0, id, matrixTex); }

    private Board(int rows, int columns, Integer id, String matrixTex){
        this.rows = Math.min(rows, MAX_DIM);
        this.columns = Math.min(columns, MAX_DIM);
        this.id = id;
        this.changes = new MaximizeDeque<>();
        if(matrixTex==null){
            this.matrix = new Cell[rows][columns];
            generate();
        }
        else{
            unwrapper(matrixTex);
        }
    }

    private void unwrapper(String matrixTex) {
        String[] wrows = matrixTex.split(String.valueOf(CellWrapper.SPLIT.getValue()));
        this.rows = wrows.length;
        for (int i = 0; i < wrows.length; i++) {
            String[] wcolumns = wrows[i].split(String.valueOf(CellWrapper.COLUMN.getValue()));
            this.columns = wcolumns.length;
            for (int j = 0; j < wcolumns.length; j++) {
                String wcell = wcolumns[j];
                char status = wcell.charAt(0);
                char type = wcell.charAt(1);
                Cell cell = new Cell(i, j);
                if (CellWrapper.SHOW.getValue() == status) {
                    cell.setHidden(false);
                }
                if (CellType.POINT.getValue() == type) {
                    cell.setType(CellType.POINT);
                } else if (CellType.STOP.getValue() == type) {
                    cell.setType(CellType.STOP);
                } else if (CellType.DUPLEX.getValue() == type) {
                    cell.setType(CellType.DUPLEX);
                } else if (CellType.ZERO.getValue() == type) {
                    cell.setType(CellType.ZERO);
                } else if (CellType.PLUS.getValue() == type) {
                    cell.setType(CellType.PLUS);
                }

                this.matrix[i][j] = cell;
            }
        }
    }

    public String wrapper() {
        StringBuilder wmatrix = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            if(i>0) wmatrix.append(CellWrapper.SPLIT.getValue());
            for (int j = 0; j < columns; j++) {
                wmatrix.append(CellWrapper.COLUMN.getValue());
                Cell cell = this.matrix[i][j];
                if(cell.isHidden()) wmatrix.append(CellWrapper.HIDDEN.getValue());
                else wmatrix.append(CellWrapper.SHOW.getValue());
                wmatrix.append(cell.getType().getValue());
            }
        }

        return wmatrix.toString();
    }

    private void generate() {
        Random rand = new Random();
        int refnumber = rows*columns;
        int points = refnumber*30/100;
        int duplex = refnumber*10/100;
        int stops = refnumber*7/100;
        int plus = refnumber*10/100;
        int empty = refnumber - (points+duplex+stops+plus+2);

        int[] cells = new int[refnumber];
        for (int i = 0; i < refnumber; i++) {
            cells[i] = 1;
        }

        int[] density = {empty/8, points/3, 0, 2, 0, duplex, 0, 0, 0, stops, 0, plus, 0, 0, 0};
        empty = empty - density[0];
        points = points - density[1];
        density[2] = empty/7;
        empty = empty - density[2];
        density[4] = empty/6;
        empty = empty - density[4];
        density[6] = empty/5;
        empty = empty - density[6];
        density[7] = points/2;
        density[8] = empty/4;
        empty = empty - density[8];
        density[10] = empty/3;
        empty = empty - density[10];
        density[12] = empty/2;
        density[13] = points - density[7];
        density[14] = empty - density[12];

        CellType[] types = {CellType.EMPTY, CellType.POINT, CellType.EMPTY, CellType.ZERO,
                CellType.EMPTY, CellType.DUPLEX, CellType.EMPTY, CellType.POINT, CellType.EMPTY,
                CellType.STOP, CellType.EMPTY, CellType.PLUS, CellType.EMPTY, CellType.POINT, CellType.EMPTY };

        for (int i = 0; i < refnumber; i++) {
            int dindex, cindex;
            do {
                cindex = (int) (Math.random()*refnumber); //rand.nextInt(cells.length);
                dindex = (int) (Math.random()*density.length); //rand.nextInt(density.length);
            }
            while(density[dindex]==0 || cells[cindex]==0);
            int r = cindex/rows;
            int c = cindex%columns;
            Cell cell = new Cell(r, c);
            cell.setType(types[dindex]);
            density[dindex] = density[dindex] - 1;
            cells[cindex] = 0;
            matrix[r][c] = cell;
        }
    }

    public void print(boolean showAll) {
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = this.matrix[i][j];
                if(showAll || !cell.isHidden()) System.out.print(cell.getType().getValue()+" ");
                else System.out.print(CellWrapper.HIDDEN.getValue()+" ");
            }
            System.out.println();
        }

    }

    public void play(Move move){
        changes.push(this.matrix);
        //Procesar la jugada
    }

    public void reverse(){
        if(!changes.empty()) { this.matrix = changes.pop();}
    }
}