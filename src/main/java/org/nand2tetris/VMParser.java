package org.nand2tetris;

public interface VMParser {
    /**
     * Returns whether you can read more lines in the file.
     */
    boolean hasMoreLines();

    /**
     * Reads the next command from the input and makes it the current Command.
     * <p>
     * The Method should be called only if {@link #hasMoreLines()} returns true.
     * <p>
     * Initially there is no current Command
     */
    void advance();

    /**
     * Returns a constant representing the type of the current command.
     * If the current command is an arithmetic-logical command, returns <code>C_ARITHMETIC</code>.
     */
    Command commandType();

    /**
     * Returns the first argument of the current command.
     * In the case of C _ARITHMETIC, the command itself (add, sub, etc.) is returned.
     * Should not be called if the current command is <code>C_RETURN</code>.
     */
    String arg1();

    /**
     * Returns the second argument of the current command.
     * Should be called only if the current command is <code>C_PUSH, C_POP, C_FUNCTION,</code> or <code>C_CALL.</code>
     */
    String arg2();
}
