package org.nand2tetris;


import org.jetbrains.annotations.NotNull;

public class HackVMCodeWriter implements VMCodeWriter {
    @Override
    public String writeArithmetic(@NotNull String vmCommand) {
        StringBuilder result = new StringBuilder();
        result.append(moveSignature(vmCommand));

        if (vmCommand.contains("and")) {
            result.append(storeLastEntryOfStackInRegisterD());
            result.append(storePastStackEntryInMRegister());
            result.append("""
                    M=D&M
                                        
                    """);

        } else if (vmCommand.contains("or")) {
            result.append(storeLastEntryOfStackInRegisterD());
            result.append(storePastStackEntryInMRegister());
            result.append("""
                    M=D|M
                                        
                    """);
        } else if (vmCommand.contains("not")) {
            result.append(storePastStackEntryInMRegister());
            result.append("""
                    M=!M
                                        
                    """);
        } else if (vmCommand.contains("add")) {
            result.append(storeLastEntryOfStackInRegisterD());
            result.append(storePastStackEntryInMRegister());
            result.append("""
                    D=M+D
                    M=D // R[SP - 1] = R[SP - 1] + R[A]
                                        
                    """);
        } else if (vmCommand.contains("sub")) {
            result.append(storeLastEntryOfStackInRegisterD());
            result.append(storePastStackEntryInMRegister());
            result.append("""
                    D=M-D
                    M=D // R[SP - 1] = R[SP - 1] - R[A]
                                        
                    """);
        } else if (vmCommand.contains("neg")) {
            result.append(storePastStackEntryInMRegister());
            result.append("""
                    M=-M
                                        
                    """);
        } else {
            throw new UnsupportedOperationException("Cant resolve" + vmCommand + " command");
        }

        return result.toString();
    }

    private String moveSignature(String procedure) {
        return String.format("//  ================= %s ================= \n", procedure.toUpperCase());
    }

    /**
     * returns
     * <pre>
     *     """
     *     @ SP
     *     A=M-1 // A = SP--
     *     """
     *  </pre>
     */
    private String storePastStackEntryInMRegister() {
        return """
                @SP
                A=M-1 // A = SP-1
                """;
    }

    /**
     * returns
     * <pre>
     *    """
     *    @ SP
     *    AM=M-1 // A = SP--
     *    D=M
     *
     *    """;
     *  </pre>
     */
    private String storeLastEntryOfStackInRegisterD() {
        return """
                @SP
                AM=M-1 // A = SP--
                D=M

                """;
    }


    /**
     * used when doing a logical command lt,gt,eq
     * @return The setup for using the 2 past stack entries in the D and M
     */
    private @NotNull String setSubtractionResultOfPast2StackEntriesInDRegister() {
        return storeLastEntryOfStackInRegisterD() + storePastStackEntryInMRegister() + "D=M-D\n\n";
    }


    /**
     * Realizes the logic assuming that the topmost Entry is stored in the D register and the second to topmost
     * entry in the D register. <br>
     * <p>
     * It stores the boolean result in the first argument address this means that the original previous topmost
     * value is considered free and is thus considered the next SP position.
     *
     * @implNote A unique identifier is required as goto statements are exclusive meaning using the same label will
     * route to the first label that was declared.
     **/
    private @NotNull String jumpProcedureToSetBooleanResultToLastEntryInSP(String procedure, int uniqueIdentifier) {
        procedure = procedure.equalsIgnoreCase("eq") ? "JEQ" : procedure;
        return """
                @%1$s_START_%2$d
                D;%1$s
                @SP
                A=M-1
                M=0 // false
                @%1$s_END_%2$d
                0;JMP
                              
                (%1$s_START_%2$d)
                @SP
                A=M-1
                M=-1 // true
                               
                (%1$s_END_%2$d) // End procedure %1$s
                                
                """.formatted(procedure.toUpperCase(), uniqueIdentifier);
    }

    public static void main(String[] args) {
        System.out.println(new HackVMCodeWriter().writeArithmetic("gt", 1));
        System.out.println(new HackVMCodeWriter().writeArithmetic("lt", 2));
        System.out.println(new HackVMCodeWriter().writeArithmetic("eq", 3));
    }

    @Override
    public String writeArithmetic(String vmCommand, int identifier) {
        StringBuilder result = new StringBuilder();
        result.append(moveSignature(vmCommand));

        if (vmCommand.matches("(.*)(eq|lt|gt)(.*)")) {
            result.append(setSubtractionResultOfPast2StackEntriesInDRegister());
            result.append(jumpProcedureToSetBooleanResultToLastEntryInSP(vmCommand, identifier));
        } else {
            throw new UnsupportedOperationException("Cant resolve" + vmCommand + " command");
        }

        return result.toString();
    }

    @Override
    public String writePushPop(Command cmdType, String segment, int index) {
        return null;
    }

    /**
     * Writes the infinite loop that is needed at every program.
     */
    public String writeEndLoop() {
        return """
                (END)
                @END
                0;JMP
                 """;
    }
}
