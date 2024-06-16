package org.nand2tetris;


import java.io.FileWriter;
import java.io.IOException;

public class VMTranslator {
    public static void main(String[] args) {
        Command currentCommandType;
        final String fileLocation = args[0];
        final String[] fileLocationAsArray = fileLocation.split("/");
        final String programName = fileLocationAsArray[2].substring(0, fileLocationAsArray[2].length() - 3);

        VMParser hackVMParser = new HackVMParser(fileLocation);
        VMCodeWriter hackCodeWriter = new HackVMCodeWriter(programName);


        System.out.printf("Parsing file with path: %s%n", fileLocation);
        String pathTemplate = fileLocation.substring(0, fileLocation.length() - 3);
        String writePath = pathTemplate + ".asm";
        int identifier = 1;

        try (FileWriter hackWriter = new FileWriter(writePath)) {

            while (hackVMParser.hasMoreLines()) {
                String assembledCommand = "";
                hackVMParser.advance();
                currentCommandType = hackVMParser.commandType();

                final String arg1 = hackVMParser.arg1();
                switch (currentCommandType) {
                    case CONSTANT -> {
                        assembledCommand = hackCodeWriter.writeConstant(arg1);
                    }
                    case C_ARITHMETIC -> {
                        boolean commandIsComparison = arg1.matches("(.*)(eq|lt|gt)(.*)");

                        if (commandIsComparison) {
                            assembledCommand = hackCodeWriter.writeArithmetic(arg1, identifier);
                            identifier++;
                        } else {
                            assembledCommand = hackCodeWriter.writeArithmetic(arg1);
                        }

                    }
                    case C_POP, C_PUSH -> {
                        int arg2 = Integer.parseInt(hackVMParser.arg2());
                        assembledCommand = hackCodeWriter
                                .writePushPop(currentCommandType, arg1, arg2);
                    }
                }
                hackWriter.write(assembledCommand);
            }

            hackWriter.write(hackCodeWriter.writeEndLoop());

        } catch (IOException e) {
            throw new RuntimeException("Could not write " + writePath);
        }
        System.out.printf("Finished translating %s%n", writePath);
    }


}
