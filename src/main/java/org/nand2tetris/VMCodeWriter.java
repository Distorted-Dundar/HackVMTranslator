package org.nand2tetris;

public interface VMCodeWriter {

    /**
     * Writes to the output file the assembly code that implements
     * the given arithmetic logical command.
     */
    void writeArithmetic(String command);

    /**
     * Writes to the output file the assembly code that implements the given
     * push or pop command
     */
    void writePushPop(Command cmdType, String segment, int index);

}
