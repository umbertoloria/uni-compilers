import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class Driver {

	public static void main(String[] args) throws Exception {
		System.out.println("Type in program, hit Return, then Cmd-D (in MacOs) o Ctrl-D (in Windows)");
		Reader keyboard = new BufferedReader(new InputStreamReader(System.in));
		parser p = new parser(new Yylex(keyboard));
		java_cup.runtime.Symbol res = p.parse();
	}

}
