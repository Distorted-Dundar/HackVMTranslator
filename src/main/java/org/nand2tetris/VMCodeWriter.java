package org.nand2tetris;

public interface VMCodeWriter {

    /**
     * Returns assembly commands that realize
     * the given arithmetic logical command.
     */
    String writeArithmetic(String command);

    /**
     * Returns assembly commands that realize the given
     * push or pop command
     */
    String writePushPop(Command cmdType, String segment, int index);

}
