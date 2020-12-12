package app;

import app.gen.Yylex;
import app.gen.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

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
		parser p = new parser(lexer);
		java_cup.runtime.Symbol res = p.parse();
//		p.debug_parse();
		Node root = (Node) res.value;
		visit(root, 0);
	}

	public static void visit(Object obj, int level) {
		if (obj instanceof Node) {
			Node n = (Node) obj;
			n.visit(level);
		} else if (obj instanceof List) {
			List l = (List) obj;
			int i = 0;
			for (Object o : l) {
				System.out.println("    ".repeat(level) + "[" + i++ + "]");
				visit(o, level + 1);
			}
		} else { // if (obj instanceof String) {
			System.out.print("    ".repeat(level));
			System.out.println(obj);
		}
	}

}
