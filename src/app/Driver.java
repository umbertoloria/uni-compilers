package app;

import app.gen.Yylex;
import app.gen.parser;
import app.visitor.scoping.ScopingVisitor;
import app.visitor.typechecking.TypeCheckingVisitor;
import app.visitor.xmlgenerator.XmlNodeVisitor;
import app.visitor.scoping.ScopingTable;

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
		java_cup.runtime.Symbol res = p.parse();
		Node root = (Node) res.value;
		XmlNodeVisitor xmlNodeVisitor = new XmlNodeVisitor();
		root.accept(xmlNodeVisitor);

		ScopingVisitor scopingVisitor = new ScopingVisitor();
		ScopingTable rootScopingTable = (ScopingTable) root.accept(scopingVisitor);

		TypeCheckingVisitor typeCheckingVisitor = new TypeCheckingVisitor(rootScopingTable);
		root.accept(typeCheckingVisitor);


		String astFilePath;
		if (args.length == 1) {
			astFilePath = args[0];
			if (astFilePath.contains(".")) {
				astFilePath = astFilePath.substring(0, astFilePath.lastIndexOf("."));
			}
			astFilePath += ".xml";
		} else {
			System.out.println("Insert destination path of the XML AST: ");
			Scanner sc = new Scanner(System.in);
			astFilePath = sc.nextLine();
			sc.close();
		}
		xmlNodeVisitor.saveOnFile(astFilePath);
	}

}
