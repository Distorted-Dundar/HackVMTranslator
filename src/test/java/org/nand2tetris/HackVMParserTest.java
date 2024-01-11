package org.nand2tetris;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.nand2tetris.Command.*;


/**
 * This Test file uses a Mocked Object (via the Mockito Framework) that simulates
 * what it would be to run the class with the following commands. <br>
 * <hr>
 * <p>
 * Testing naming Convention is as follows<br><br>
 * MethodName_ExpectedBehavior_underState() <br>
 * <b>
 * In other words the MethodName() returns or executes the ExpectedBehavior
 * when its underState (which is the mockedBehavior)
 * </b>
 */
class HackVMParserTest {
    static HackVMParser hackVMParser;

    @BeforeAll
    public static void setUpBeforeClass() {
        hackVMParser = Mockito.mock(HackVMParser.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    void commandType_CONSTANT_currentCommandIsPushConstant() {
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push constant 10");
        assertEquals(hackVMParser.commandType(), CONSTANT, "Push 10 is a constant operation");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push constant 0");
        assertEquals(hackVMParser.commandType(), CONSTANT, "Push 0 is a constant operation");
    }

    @Test
    void commandType_C_ARITHMETIC_currentCommandIsArithmeticOrLogical() {
//        Pure arithmetic commands are twos complement
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("add");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "add is Arithmetic");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("sub");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "subtraction is Arithmetic");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("neg");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "negation is Arithmetic");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("eq");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "equality is Arithmetic (comparison)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("gt");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "greater than is Arithmetic (comparison)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("lt");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "less than is Arithmetic (comparison)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("and");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "logical And is Arithmetic (Logical)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("or");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "logical or is Arithmetic (Logical)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("not");
        assertEquals(hackVMParser.commandType(), C_ARITHMETIC, "logical not is Arithmetic (Logical)");
    }

    @Test
    void commandType_C_PUSH_currentCommandIsAPushSegment() {
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push local 0");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push *(LCL_Base_address + 0)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push that 5");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push *(THAT_Base_address + 5)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push argument 1");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push *(ARG_Base_address + 1)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push this 6");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push *(THIS_Base_address + 6)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push temp 6");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push *(TEMP_Base_address + 6)");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push static 3");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push a static variable from 16-255, therefore is at 16+3");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push pointer 0");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push the virtual segment to access the THIS (0) pointer");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push pointer 1");
        assertEquals(hackVMParser.commandType(), C_PUSH, "push the virtual segment to access the That (1) pointer");
    }

    @Test
    void commandType_C_POP_currentCommandIsAPushSegment() {
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop local 0");
        assertEquals(hackVMParser.commandType(), C_POP, "RAM[(LCL_Base_address + 0)] <- RAM[SP--]");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop that 5");
        assertEquals(hackVMParser.commandType(), C_POP, "RAM[(THAT_Base_address + 5)] <- RAM[SP--]");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop argument 1");
        assertEquals(hackVMParser.commandType(), C_POP, "RAM[(ARG_Base_address + 1)] <- RAM[SP--]");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop this 6");
        assertEquals(hackVMParser.commandType(), C_POP, "RAM[(THIS_Base_address + 6)] <- RAM[SP--]");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop temp 6");
        assertEquals(hackVMParser.commandType(), C_POP, "RAM[(TEMP_Base_address + 6)] <- RAM[SP--]");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop static 3");
        assertEquals(hackVMParser.commandType(), C_POP, "RAM[16+3] <- RAM[SP--]");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop pointer 0");
        assertEquals(hackVMParser.commandType(), C_POP, "THIS: RAM[RAM[PTR+0]] <- RAM[SP--]");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop pointer 1");
        assertEquals(hackVMParser.commandType(), C_POP, "THAT: RAM[RAM[PTR+1]] <- RAM[SP--]");
    }

    @Test
    void arg1_ArithmeticOperation_commandTypeC_ARITHMETIC() {
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("add");
        assertEquals(hackVMParser.arg1(), "add", "returns first arg of add");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("sub");
        assertEquals(hackVMParser.arg1(), "sub", "returns first arg of sub");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("neg");
        assertEquals(hackVMParser.arg1(), "neg", "returns first arg of neg");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("eq");
        assertEquals(hackVMParser.arg1(), "eq", "returns first arg of eq");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("gt");
        assertEquals(hackVMParser.arg1(), "gt", "returns first arg of gt");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("lt");
        assertEquals(hackVMParser.arg1(), "lt", "returns first arg of lt");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("and");
        assertEquals(hackVMParser.arg1(), "and", "returns first arg of and");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("or");
        assertEquals(hackVMParser.arg1(), "or", "returns first arg of or");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("not");
        assertEquals(hackVMParser.arg1(), "not", "returns first arg of not");
    }

    @Test
    void arg1_MemorySegment_commandTypeC_PushOrC_POP() {
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push local 0");
        assertEquals(hackVMParser.arg1(), "local", "first arg is local second is 0");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop local 0");
        assertEquals(hackVMParser.arg1(), "local", "first arg is local second is 0");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push that 5");
        assertEquals(hackVMParser.arg1(), "that", "first arg is that second is 5");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop that 5");
        assertEquals(hackVMParser.arg1(), "that", "first arg is that second is 5");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push argument 1");
        assertEquals(hackVMParser.arg1(), "argument", "first arg is argument second is 1");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop argument 1");
        assertEquals(hackVMParser.arg1(), "argument", "first arg is argument second is 1");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push this 6");
        assertEquals(hackVMParser.arg1(), "this", "first arg is this second is 6");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop this 6");
        assertEquals(hackVMParser.arg1(), "this", "first arg is this second is 6");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push temp 6");
        assertEquals(hackVMParser.arg1(), "temp", "first arg is temp second is 6");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop temp 6");
        assertEquals(hackVMParser.arg1(), "temp", "first arg is temp second is 6");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push static 3");
        assertEquals(hackVMParser.arg1(), "static", "first arg is static second is 3");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop static 3");
        assertEquals(hackVMParser.arg1(), "static", "first arg is static second is 3");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push pointer 0");
        assertEquals(hackVMParser.arg1(), "pointer", "first arg is pointer second is 0");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop pointer 0");
        assertEquals(hackVMParser.arg1(), "pointer", "first arg is pointer second is 0");
    }

    @Test
    void arg1_ThrowsException_commandTypeC_RETURN() {
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("NULL");

        Mockito.when(hackVMParser.commandType()).thenReturn(C_RETURN);
        assertThrows(UnsupportedOperationException.class, () -> hackVMParser.arg1(), "arg1() should not be called on a commandType of C_RETURN");
    }

    @Test
    void arg2_offsetAddress_commandTypeC_PushOrC_POP(){
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push local 0");
        assertEquals(hackVMParser.arg2(), "0", "first is local second arg  is 0");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop local 0");
        assertEquals(hackVMParser.arg2(), "0", "first is local second arg  is 0");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push that 5");
        assertEquals(hackVMParser.arg2(), "5", "first is that second arg is 5");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop that 5");
        assertEquals(hackVMParser.arg2(), "5", "first is that second arg is 5");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push argument 1");
        assertEquals(hackVMParser.arg2(), "1", "first is argument second arg ond is 1");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop argument 1");
        assertEquals(hackVMParser.arg2(), "1", "first is argument second arg ond is 1");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push this 6");
        assertEquals(hackVMParser.arg2(), "6", "first is this second arg is 6");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop this 6");
        assertEquals(hackVMParser.arg2(), "6", "first is this second arg is 6");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push temp 6");
        assertEquals(hackVMParser.arg2(), "6", "first is temp second arg is 6");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop temp 6");
        assertEquals(hackVMParser.arg2(), "6", "first is temp second arg is 6");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push static 3");
        assertEquals(hackVMParser.arg2(), "3", "first is static second arg  is 3");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop static 3");
        assertEquals(hackVMParser.arg2(), "3", "first is static second arg  is 3");

        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("push pointer 0");
        assertEquals(hackVMParser.arg2(), "0", "first is pointer second arg  is 0");
        Mockito.when(hackVMParser.getCurrentCommand()).thenReturn("pop pointer 0");
        assertEquals(hackVMParser.arg2(), "0", "first is pointer second arg  is 0");
    }

}
