package org.nand2tetris;

public interface VMCodeWriter {

    /**
     * Returns assembly commands that realize
     * the given arithmetic logical command. <br>
     * The commands that are realized by this methods
     * are add, sub, neg, and, or , not.
     */
    String writeArithmetic(String vmCommand);

    /**
     * Returns assembly commands that realize
     * the given arithmetic logical command. <br>
     * The overloaded operator indicates that the
     * commands need a unique identifier as they
     * use goto's. these are for comparison commands
     * e.g. lt,gt,eq
     */
    String writeArithmetic(String vmCommand, int identifier);

    /**
     * Returns assembly commands that realize the given
     * push or pop command
     */
    String writePushPop(Command cmdType, String segment, int index);

    /**
     * Implements a <code>Push constant x</code> command
     * @param value An integer value as a string
     * @return The specific command that places the value into the stack
     */
    String writeConstant(String value);

    /**
     * Creates a infinite loop to avoid malware inkjection
     * @return Writes a ending loop
     */
    String writeEndLoop();

}
