package org.nand2tetris;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.nand2tetris.Command.*;

public class HackVMParser implements VMParser {

    public String currentCommand;
    private final Scanner reader;
    /**
     * Is used to differentiate commands that use gotos. <br>
     * E.G (Less than LT, Greater Than GT, Equal Eq)
     */
    private int currentLine;

    public HackVMParser(String fileLocation) {
        try {
            File file = new File(fileLocation);
            this.reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new UnsupportedOperationException("Could not find or open the file" + fileLocation);
        }
        currentLine = 0;
        this.currentCommand = null;
    }

    public String getCurrentCommand() {
        return this.currentCommand;
    }

    @Override
    public boolean hasMoreLines() {
        return reader.hasNextLine();
    }

    @Override
    public void advance() {
        closeFileWhenNoMoreLinesArePresent();
        String cmd = "";

        while (hasMoreLines()) {
            String str = this.reader.nextLine();

            if (str.isEmpty() || str.startsWith("//")) {
                continue;
            }
//            You found a string worth storing
            cmd = str;
            break;
        }

//      Decides what to do if no command is found
        if (cmd.isEmpty()) {
            return;
        }

        currentCommand = cmd;
        currentLine++;
    }

    private void closeFileWhenNoMoreLinesArePresent() {
        if (!hasMoreLines())
            this.reader.close();
    }

    @Override
    public Command commandType() {
        String cmd = getCurrentCommand();
        if (cmd.matches("(.*)(local|that|this|argument|temp|static|pointer)(.*)")) {
            if (cmd.contains("push"))
                return C_PUSH;
            else
                return C_POP;
        } else if (cmd.contains("constant"))
            return CONSTANT;
        else if (cmd.matches("add|sub|neg|eq|gt|lt|and|or|not"))
            return C_ARITHMETIC;

        return null;
    }

    @Override
    public String arg1() {
        String[] cmdLexemes = getCurrentCommand().split(" ");
        switch (commandType()) {
            case C_ARITHMETIC -> {
                return getCurrentCommand().strip();
            }
            case C_PUSH, C_POP -> {
                return cmdLexemes[1];
            }
            case C_RETURN -> throw new UnsupportedOperationException("Cant capture first argument of C_RETURN command Type");
        }
        return null;
    }

    @Override
    public String arg2() {
        return null;
    }


}
