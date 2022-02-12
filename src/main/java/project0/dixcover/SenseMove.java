package project0.dixcover;

public enum SenseMove {
    FORWARD('F'), BACKWARD('K'), BOTH('B');

    private char value;

    SenseMove(char value){
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}