package org.nand2tetris;


import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class HackVMCodeWriter implements VMCodeWriter {

    private final String programName;

    private interface CommandHandler extends BiFunction<String, Integer, String> {};

    private final Map<String, CommandHandler> popHandler = new HashMap<>();
    private final Map<String, CommandHandler> pushHandler = new HashMap<>();

    public HackVMCodeWriter(String programName) {
        this.programName = programName;
        popHandler.put("pointer", this::pointerPopSequence);
        popHandler.put("static", this::staticPopSequence);
        popHandler.put("default", this::defaultPopSequence);

        pushHandler.put("pointer", this::pointerPushSequence);
        pushHandler.put("static", this::staticPushSequence);
        pushHandler.put("default", this::defaultPushSequence);
    }

    private String staticPushSequence(String s, Integer i) {
        if (i > 240 || i < 1) {
            throw new UnsupportedOperationException("Cant pop static " + i + " only options are in the range [1, 240]");
        }
        StringBuilder result = new StringBuilder();

        result.append("""
                @%s.%d
                D = M
                                
                """.formatted(this.programName.toUpperCase(), i));

        result.append(placeDRegisterOnStack());
        result.append(increaseStackAddress());

        return result.toString();
    }

    private String staticPopSequence(String s, Integer i) {
        if (i > 240 || i < 1) {
            throw new UnsupportedOperationException("Cant pop static " + i + " only options are in the range [1, 240]");
        }
        StringBuilder result = new StringBuilder();

        result.append(storeLastEntryOfStackInRegisterDAndDecreaseStack());

        result.append("""
                @%s.%d
                M = D
                                
                """.formatted(this.programName.toUpperCase(), i));

        return result.toString();
    }

    private String pointerPushSequence(String s, Integer i) {
        if (i > 1 || i < 0) {
            throw new UnsupportedOperationException("Cant pop pointer " + i + " only options are 0 and 1");
        }

        StringBuilder result = new StringBuilder();

        String targetSegment = i == 0 ? "THIS" : "THAT";

        result.append("""
                @%s                     
                D = M                
                """.formatted(targetSegment));

        result.append(placeDRegisterOnStack());
        result.append(increaseStackAddress());

        return result.toString();
    }

    private String placeDRegisterOnStack() {
        return """
                @SP
                A = M
                M = D
                """;
    }

    private String pointerPopSequence(String s, int i) {
        if (i > 1 || i < 0) {
            throw new UnsupportedOperationException("Cant pop pointer " + i + " only options are 0 and 1");
        }

        StringBuilder result = new StringBuilder();

        result.append(storeLastEntryOfStackInRegisterDAndDecreaseStack());

        String targetSegment = i == 0 ? "THIS" : "THAT";

        result.append("""
                @%s
                M = D
                
                """.formatted(targetSegment));

        return result.toString();
    }

    @Override
    public String writeArithmetic(@NotNull String vmCommand) {
        StringBuilder result = new StringBuilder();
        result.append(moveSignature(vmCommand));

        if (vmCommand.contains("and")) {
            result.append(storeLastEntryOfStackInRegisterDAndDecreaseStack());
            result.append(gotoLastEntryAddressOnStack());
            result.append("""
                    M=D&M
                                        
                    """);

        } else if (vmCommand.contains("or")) {
            result.append(storeLastEntryOfStackInRegisterDAndDecreaseStack());
            result.append(gotoLastEntryAddressOnStack());
            result.append("""
                    M=D|M
                                        
                    """);
        } else if (vmCommand.contains("not")) {
            result.append(gotoLastEntryAddressOnStack());
            result.append("""
                    M=!M
                                        
                    """);
        } else if (vmCommand.contains("add")) {
            result.append(storeLastEntryOfStackInRegisterDAndDecreaseStack());
            result.append(gotoLastEntryAddressOnStack());
            result.append("""
                    D=M+D
                    M=D // R[SP - 1] = R[SP - 1] + R[A]
                                        
                    """);
        } else if (vmCommand.contains("sub")) {
            result.append(storeLastEntryOfStackInRegisterDAndDecreaseStack());
            result.append(gotoLastEntryAddressOnStack());
            result.append("""
                    D=M-D
                    M=D // R[SP - 1] = R[SP - 1] - R[A]
                                        
                    """);
        } else if (vmCommand.contains("neg")) {
            result.append(gotoLastEntryAddressOnStack());
            result.append("""
                    M=-M
                                        
                    """);
        } else {
            throw new UnsupportedOperationException("Cant resolve " + vmCommand + " command");
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
    private String gotoLastEntryAddressOnStack() {
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
    private String storeLastEntryOfStackInRegisterDAndDecreaseStack() {
        return """
                @SP
                AM=M-1 // A = SP--
                D=M

                """;
    }


    /**
     * used when doing a logical command lt,gt,eq
     *
     * @return The setup for using the 2 past stack entries in the D and M
     */
    private @NotNull String setSubtractionResultOfPast2StackEntriesInDRegister() {
        return storeLastEntryOfStackInRegisterDAndDecreaseStack() + gotoLastEntryAddressOnStack() + "D=M-D\n\n";
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
        procedure = findAssemblyJump(procedure);
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

    private String findAssemblyJump(String procedure) {
        switch (procedure) {
            case "gt" -> {
                return "JGT";
            }
            case "lt" -> {
                return "JLT";
            }
            case "eq" -> {
                return "JEQ";
            }
            default -> {
                return null;
            }
        }
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
        StringBuilder result = new StringBuilder();
        result.append(moveSignature(cmdType + " - " + segment + " " + index));
        segment = findAssemblySegment(segment);

        CommandHandler handler;

        switch (cmdType) {
            case C_POP -> {
                handler = popHandler.getOrDefault(segment, popHandler.get("default"));
            }
            case C_PUSH -> {
                handler = pushHandler.getOrDefault(segment, pushHandler.get("default"));
            }

            default -> throw new UnsupportedOperationException("Unfamiliar " + cmdType + " command");
        }

        result.append(handler.apply(segment, index));
        return result.toString();
    }

    private String defaultPushSequence(String segment, int index) {
        StringBuilder res = new StringBuilder();

        res.append(setupOffsetAddress(segment, index));
        res.append(placeDataOnTopOfStack());
        res.append(increaseStackAddress());

        return res.toString();
    }

    private String defaultPopSequence(String segment, int index) {
        StringBuilder res = new StringBuilder();

        res.append(setupOffsetAddress(segment, index));
        res.append(storeLastEntryOfStackInRegisterDAndDecreaseStack());
        res.append(setSegmentDataToStackData());

        return res.toString();
    }


    private String setSegmentDataToStackData() {
        return """
                // *addr = *sp
                @offset
                A = M
                M = D
                                
                """;
    }

    private String placeDataOnTopOfStack() {
        return """
                // *SP = *ADDR
                @offset
                A = M
                D = M
                                        
                @SP
                A = M
                M = D
                """;
    }

    private String increaseStackAddress() {
        return """
                // SP ++
                @SP
                M = M + 1
                                
                """;
    }

    private String setupOffsetAddress(String segment, int index) {
        boolean isInteger = isInteger(segment);

        String calculation = isInteger ? "D = A + D" : "D = M + D";

        return """
                // addr = SEG + i
                @%d
                D = A
                            
                @%s
                %s
                            
                @offset
                M = D
                            
                """.formatted(index, segment, calculation);
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String findAssemblySegment(String segment) {
        switch (segment) {
            case "local" -> {
                return "LCL";
            }
            case "argument" -> {
                return "ARG";
            }
            case "this", "that" -> {
                return segment.toUpperCase();
            }
            case "temp" -> {
                return "5";
            }
            case "pointer", "static" -> {
                return segment;
            }

            default -> {
                return null;
            }
        }
    }

    @Override
    public String writeConstant(String value) {
        StringBuilder result = new StringBuilder();
        result.append(moveSignature("PUSH" + " - " + value));

        try {
            Integer.parseInt(value);
            result.append("@" + value + "\n");
            result.append("""
                    D = A // State the constant
                                        
                    // SP always points to the open slot
                    @SP
                    A = M
                    M = D // RAM[SP] = value
                                        
                    // SP++
                    @SP
                    AM = M + 1
                                            
                    """);
        } catch (NumberFormatException e) {
            throw new UnsupportedOperationException("Could not place" + value + " A supposed constant onto the stack");
        }

        return result.toString();
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
