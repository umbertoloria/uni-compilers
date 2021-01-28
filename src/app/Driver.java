package app;

import app.gen.Yylex;
import app.gen.parser;
import app.node.ProgramOP;
import app.visitor.ConstraintsVisitor;
import app.visitor.clanggenerator.ClangVisitor;
import app.visitor.scoping.ScopingTable;
import app.visitor.scoping.ScopingVisitor;
import app.visitor.typechecking.TypeCheckingVisitor;
import app.visitor.xmlgenerator.XmlNodeVisitor;
import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		for (String arg : args) {
			try {
				compile(arg);
				System.out.println("Compiled '" + arg + "'");
			} catch (RuntimeException e) {
				System.out.print("Semantic error in '" + arg + "' source file: ");
			} catch (Exception e) {
				System.out.println("Error in compiling '" + arg + "' source file");
				System.err.println(e.getLocalizedMessage());
			}
		}
	}

	private static void compile(String sourcePath) throws Exception {
		String astXmlPath = sourcePath.substring(0, sourcePath.lastIndexOf(".toy")) + ".xml";
		String targetPath = sourcePath.substring(0, sourcePath.lastIndexOf(".toy")) + ".c";
		String outPath = sourcePath.substring(0, sourcePath.lastIndexOf(".toy")) + ".exe";
		// Parsing
		ProgramOP root = parse(sourcePath);
		// Semantic: Scoping checks
		ScopingVisitor scopingVisitor = new ScopingVisitor();
		ScopingTable rootScopingTable = (ScopingTable) root.accept(scopingVisitor);
		// Semantic: type checking
		TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor(rootScopingTable);
		root.accept(typeCheckingVisitor);
		// Semantic: constraints
		ConstraintsVisitor constraintsVsitor = new ConstraintsVisitor(rootScopingTable);
		root.accept(constraintsVsitor);
		// Generation: XML representation
		XmlNodeVisitor xmlNodeVisitor1 = new XmlNodeVisitor();
		root.accept(xmlNodeVisitor1);
		xmlNodeVisitor1.saveOnFile(astXmlPath);
		// Generation: C code
		ClangVisitor clangVisitor = new ClangVisitor(rootScopingTable.getAllNames());
		root.accept(clangVisitor);
		clangVisitor.saveOnFile(targetPath);
		// Generation: C compilation
		runGcc(targetPath, outPath);
	}

	private static ProgramOP parse(String sourcePath) throws Exception {
		Yylex lexer;
//		System.out.println("Type in program, hit Return, then Cmd-D (in MacOs) o Ctrl-D (in Windows)");
//		Reader keyboard = new BufferedReader(new InputStreamReader(System.in));
//		lexer = new Yylex(keyboard);
//		while (!lexer.yyatEOF()) {
//			Symbol token = lexer.next_token();
//			System.out.print(sym.terminalNames[token.sym]);
//			if (token.value != null)
//				System.out.println(" -> " + token.value);
//			else
//				System.out.println();
//		}
		FileReader fileReader = new FileReader(sourcePath);
		lexer = new Yylex(fileReader);
		parser p = new parser(lexer);
		Symbol res = p.parse();
		return (ProgramOP) res.value;
	}

	private static void runGcc(String toySourceFile, String outFile) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(
				"gcc", "-pthread", "-lm", "-o", outFile, toySourceFile);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		Scanner sc = new Scanner(p.getInputStream());
		if (sc.hasNextLine()) {
			System.out.println("C compiler output...\n");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				System.out.println(line);
			}
		}
	}

}
