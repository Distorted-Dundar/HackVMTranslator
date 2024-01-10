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
 *  when its underState (which is the mockedBehavior)
 * </b>
 */
class HackVMParserTest {
    static HackVMParser hackVMParser;

    @BeforeAll
    public static void setUpBeforeClass(){
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
    void arg1() {
    }

    @Test
    void arg2() {
    }

    @Test
    void currentCommand() {
    }
}