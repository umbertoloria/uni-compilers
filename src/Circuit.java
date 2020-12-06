import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class Circuit {

	public static void main(String[] args) throws Exception {
		System.out.println("Type in circuit, hit Return, then Cmd-D (in MacOs) o Ctrl-D (in Windows)");
		InputStreamReader inp = new InputStreamReader(System.in);
		Reader keyboard = new BufferedReader(inp);
		parser p = new parser(new Yylex(keyboard));
		java_cup.runtime.Symbol res = p.parse();
//		System.out.println(res);
		System.out.println("Arrivederci!");
//        System.out.println("Resistance is "+ p.parse().value); // l'uso di p.debug_parse() al posto di p.parse()
//        produce tutte le azioni del parser durante il riconoscimento
	}

}
