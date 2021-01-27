package app;

import app.gen.Yylex;
import app.gen.parser;
import app.node.Node;
import app.visitor.ConstraintsVisitor;
import app.visitor.clanggenerator.ClangVisitor;
import app.visitor.scoping.ScopingTable;
import app.visitor.scoping.ScopingVisitor;
import app.visitor.typechecking.TypeCheckingVisitor;
import app.visitor.xmlgenerator.XmlNodeVisitor;
import java_cup.runtime.Symbol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws Exception {
		Yylex lexer;
		if (args.length == 1) {
			FileReader fileReader = new FileReader(args[0]);
			lexer = new Yylex(fileReader);
		} else {
			System.out.println("Type in program, hit Return, then Cmd-D (in MacOs) o Ctrl-D (in Windows)");
			Reader keyboard = new BufferedReader(new InputStreamReader(System.in));
			lexer = new Yylex(keyboard);
		}
		/*while (!lexer.yyatEOF()) {
			Symbol token = lexer.next_token();
			System.out.print(sym.terminalNames[token.sym]);
			if (token.value != null)
				System.out.println(" -> " + token.value);
			else
				System.out.println();
		}*/
		parser p = new parser(lexer);
		Symbol res = p.parse();
		Node root = (Node) res.value;

		ScopingVisitor scopingVisitor = new ScopingVisitor();
		ScopingTable rootScopingTable = (ScopingTable) root.accept(scopingVisitor);

		TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor(rootScopingTable);
		root.accept(typeCheckingVisitor);

		ConstraintsVisitor constraintsVsitor = new ConstraintsVisitor(rootScopingTable);
		root.accept(constraintsVsitor);

		String astFilePath, genFilePath;
		if (args.length == 1) {
			astFilePath = args[0];
			if (astFilePath.contains(".")) {
				astFilePath = astFilePath.substring(0, astFilePath.lastIndexOf("."));
			}
			genFilePath = astFilePath + ".c";
			astFilePath += ".xml";
		} else {
			System.out.println("Insert destination path of the XML AST: ");
			Scanner sc = new Scanner(System.in);
			astFilePath = sc.nextLine();
			System.out.println("Insert destination path of the C code: ");
			genFilePath = sc.nextLine();
			sc.close();
		}
		XmlNodeVisitor xmlNodeVisitor1 = new XmlNodeVisitor();
		root.accept(xmlNodeVisitor1);
		xmlNodeVisitor1.saveOnFile(astFilePath);

		ClangVisitor clangVisitor = new ClangVisitor(rootScopingTable.getAllNames());
		root.accept(clangVisitor);
		clangVisitor.saveOnFile(genFilePath);
	}

}
