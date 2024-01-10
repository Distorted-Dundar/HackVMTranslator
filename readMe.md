# Translator for VM

[![LinkedIn][linkedin-shield]][linkedin-url]

- [PROJECT SPECS](https://www.nand2tetris.org/project07)

<!-- ABOUT THE PROJECT -->
## About The Project

The HackVMTranslator generates assembly code according to VM commands.
This project is meant to gain a higher understanding of the two tier compilation.
After code is generated, it is executed on a custom-made virtual Computer (The hack Computer). 
See [N2T part 1](https://www.nand2tetris.org). Testing is done to ensure that the code is complete.

### Built With / Tools Used

* Java
* VM Emulator (Provided Nand2Tetris VMEmulator for understanding the objectives and behavior)
* CPU emulator (Provided Nand2Tetris CPU emulator for running assembly programs)

### Educational goals
- Applying Dependency Inversion Principle
- Applying Interface Separation Principle
- Reducing complicated code into functions (applying Clean Code advice)
- Making Code readable (applying Clean Code advice)
- Applying Regex's
- Code Generation
- Test Driven Development
- Mockito for Mocking Objects

<!-- GETTING STARTED -->
## Getting Started

You first need to assert that the hack folder is outside the src code. 
You can debug what directory java is on by doing the following print statement

``` java
System.out.println(System.getProperty("user.dir"));
```

### Prerequisites / Steps

knowledge of editing configurations is crucial.
1. Edit the configurations of **HackVMTranslator.java**
2. The program arguments should be somewhere along the lines of **path\*.vm**
3. When running the program you will find the assembly hack code within that same directory.
4. Use the CPU Emulator to run the program and use its corresponding script to assert code generation is complete


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/bflo/
