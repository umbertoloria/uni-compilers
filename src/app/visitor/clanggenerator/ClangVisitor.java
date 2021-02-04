package app.visitor.clanggenerator;

import app.node.*;
import app.node.expr.Id;
import app.node.stat.*;
import app.visitor.ConstraintsVisitor;
import app.visitor.ExclusiveNodeVisitor;
import app.visitor.clanggenerator.exprvisitor.CExprGeneratorVisitor;
import app.visitor.clanggenerator.exprvisitor.ImmediateExprVisitor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ClangVisitor extends ExclusiveNodeVisitor<Object> {

	private TypeCodifier typeCodifier = new TypeCodifier();
	private ImmediateExprVisitor immediateExprVisitor = new ImmediateExprVisitor();
	private EasyNamesManager easyNamesManager;
	private ClangCodeEditor clangCodeEditor;
	private CExprGeneratorVisitor cExprGeneratorVisitor;
	private List<VarDeclOP> globalVarDecls;
	private Set<String> declaredStructs = new HashSet<>();

	public ClangVisitor(Set<String> namesToExclude) {
		easyNamesManager = new EasyNamesManager(namesToExclude);
		clangCodeEditor = new ClangCodeEditor(easyNamesManager);
		cExprGeneratorVisitor = new CExprGeneratorVisitor(clangCodeEditor, easyNamesManager);
	}

	private String getCLikeReturnType(List<TypeNode> typeNodes) {
		if (typeNodes != null) {
			if (typeNodes.size() > 1) {
				// 'int,bool,string' diventa 'int_bool_string', ossia il tipo (struct) che conterrà i valori
				// di ritorno (plurale) della procedura
				return typeNodes.stream().map(TypeNode::getStringType).collect(Collectors.joining("_"));
			} else {
				// 'string' diventa 'char*', ossia il tipo dell'unico valore restituito dalla procedura in questione
				return typeCodifier.codify(typeNodes.get(0));
			}
		} else {
			return TypeCodifier.VOID;
		}
	}

	@Override
	public Object visitProgramOP(ProgramOP programOP) {
		// Strutture: prima tutte le struct necessarie alle invocazioni che restituiscono più espressioni
		for (ProcOP proc : programOP.procs) {
			if (!proc.id.name.equals(ConstraintsVisitor.MAIN_NAME)) {
				publicStruct(proc);
			}
		}
		// Interfacce: poi vengono le interfacce delle procedure, così da permettere invocazioni prima di dichiarazioni
		for (ProcOP proc : programOP.procs) {
			if (!proc.id.name.equals(ConstraintsVisitor.MAIN_NAME)) {
				publicInterface(proc);
			}
		}
		// Variabili globali: si dichiarano tutte, si inizializzano solo quelle con espressioni immediate
		if (programOP.varDecls != null) {
			globalVarDecls = programOP.varDecls;
			for (VarDeclOP varDecl : programOP.varDecls) {
				visit_VarDeclOP(varDecl, false);
			}
		}
		// Procedure: alla fine si definiscono tutte le procedure con i loro comportamenti
		for (ProcOP proc : programOP.procs) {
			proc.accept(this);
		}
		return null;
	}

	private void publicStruct(ProcOP proc) {
		if (proc.returnTypes != null && proc.returnTypes.size() > 1) {
			String structName = getCLikeReturnType(proc.returnTypes);
			if (!declaredStructs.contains(structName)) {
				declaredStructs.add(structName);
				easyNamesManager.addReservedWord(structName);
				List<String> cTypes = new LinkedList<>();
				for (TypeNode returnType : proc.returnTypes) {
					cTypes.add(typeCodifier.codify(returnType));
				}
				clangCodeEditor.putStruct(structName, cTypes);
			}
		}

	}

	private void publicInterface(ProcOP proc) {
		String cRetType = getCLikeReturnType(proc.returnTypes);
		List<String> cParamTypes = new LinkedList<>();
		if (proc.parDecls != null) {
			for (ParDeclOP parDecl : proc.parDecls) {
				String cParamType = typeCodifier.codify(parDecl.type);
				for (Id ignored : parDecl.ids) {
					cParamTypes.add(cParamType);
				}
			}
		}
		String usableName = easyNamesManager.safeNameUsage(proc.id.name);
		clangCodeEditor.putInterface(cRetType, usableName, cParamTypes);
	}

	@Override
	public Object visitProcOP(ProcOP procOP) {
		List<String> cParamTypes = new LinkedList<>();
		List<String> paramNames = new LinkedList<>();
		if (procOP.parDecls != null) {
			for (ParDeclOP parDecl : procOP.parDecls) {
				String cParamType = typeCodifier.codify(parDecl.type);
				for (Id id : parDecl.ids) {
					cParamTypes.add(cParamType);
					String usableName = easyNamesManager.safeNameUsage(id.name);
					paramNames.add(usableName);
				}
			}
		}
		String cRetType = getCLikeReturnType(procOP.returnTypes);
		String usableProcName = easyNamesManager.safeNameUsage(procOP.id.name);
		clangCodeEditor.openDeclarationBlock(cRetType, usableProcName, cParamTypes, paramNames);
		if (procOP.id.name.equals(ConstraintsVisitor.MAIN_NAME)) {
			// Le assegnazioni di espressioni non-immediate sono vietate nello scope globale di C
			if (globalVarDecls != null) {
				for (VarDeclOP main_sVarDecl : globalVarDecls) {
					for (IdInitOP idInit : main_sVarDecl.idInits) {
						if (idInit.expr != null && !idInit.expr.accept(immediateExprVisitor)) {
							// Questa lista in realtà contiene una sola espressione C
							List<String> cNotImmExprs = idInit.expr.accept(cExprGeneratorVisitor);
							String cNotImmExpr = cNotImmExprs.get(0);
							String usableName = easyNamesManager.safeNameUsage(idInit.id.name);
							clangCodeEditor.assign(usableName, cNotImmExpr);
						}
					}
				}
			}
		}
		if (procOP.procBody.varDecls != null) {
			for (VarDeclOP varDecl : procOP.procBody.varDecls) {
				visit_VarDeclOP(varDecl, true);
			}
		}
		if (procOP.procBody.body != null) {
			procOP.procBody.body.accept(this);
		}
		if (procOP.procBody.returnExpressions != null) {
			singleOrMultipleReturn(procOP);
		}
		clangCodeEditor.closeBlock();
		return null;
	}

	public void visit_VarDeclOP(VarDeclOP varDeclOP, boolean inProcOP) {
		String cType = typeCodifier.codify(varDeclOP.type);
		for (IdInitOP idInit : varDeclOP.idInits) {
			// L'inizializzazione con espressioni non-immediate (che dipendono da variabili o invocazioni) non è valida
			// se ci si trova nello scope globale di C. In questi casi le variabili globali vengono dichiarate nello
			// scope globale e poi assegnate all'inizio del main.
			if (idInit.expr != null && (inProcOP || idInit.expr.accept(immediateExprVisitor))) {
				// Se c'è una espressione da assegnare, allora si inizializza direttamente se:
				// * siamo in una funzione, oppure;
				// * siamo nello scope globale ma l'espressione è immediata.
				String usableName = easyNamesManager.safeNameUsage(idInit.id.name);
				// Questa lista in realtà contiene una sola espressione C
				List<String> cExprs = idInit.expr.accept(cExprGeneratorVisitor);
				String cExpr = cExprs.get(0);
				clangCodeEditor.putVarInitialization(cType, usableName, cExpr);
			} else {
				String usableName = easyNamesManager.safeNameUsage(idInit.id.name);
				clangCodeEditor.putVarDeclaration(cType, usableName);
			}
		}
	}

	public void singleOrMultipleReturn(ProcOP procOP) {
		List<String> cExprs = new LinkedList<>();
		for (ExprNode returnExpression : procOP.procBody.returnExpressions) {
			List<String> cParExprs = returnExpression.accept(cExprGeneratorVisitor);
			cExprs.addAll(cParExprs);
		}
		if (cExprs.size() == 1) {
			clangCodeEditor.putSingleReturn(cExprs.get(0));
		} else {
			String cRetType = getCLikeReturnType(procOP.returnTypes);
			clangCodeEditor.putMultipleReturn(cRetType, cExprs);
		}
	}

	@Override
	public Object visitBodyOP(BodyOP bodyOP) {
		for (StatNode stmt : bodyOP.stmts) {
			stmt.accept(this);
		}
		return null;
	}

	@Override
	public Object visitIfOP(IfOP ifOP) {
		String cIfExpr = ifOP.expr.accept(cExprGeneratorVisitor).get(0);
		List<String> cElifsExprs = new LinkedList<>();
		if (ifOP.elifs != null) {
			for (ElifOP elif : ifOP.elifs) {
				String cElifExpr = elif.expr.accept(cExprGeneratorVisitor).get(0);
				cElifsExprs.add(cElifExpr);
			}
		}
		clangCodeEditor.openIfBlock(cIfExpr);
		ifOP.ifBody.accept(this);
		clangCodeEditor.closeBlock();
		if (ifOP.elifs != null) {
			Iterator<String> cElifsExprsIt = cElifsExprs.iterator();
			for (ElifOP elif : ifOP.elifs) {
				String cElifExpr = cElifsExprsIt.next();
				clangCodeEditor.openElseIfBlock(cElifExpr);
				elif.body.accept(this);
				clangCodeEditor.closeBlock();
			}
		}
		if (ifOP.elseBody != null) {
			clangCodeEditor.openElseBlock();
			ifOP.elseBody.accept(this);
			clangCodeEditor.closeBlock();
		}
		return null;
	}

	@Override
	public Object visitWhileOP(WhileOP whileOP) {
		if (whileOP.preStmts != null) {
			whileOP.preStmts.accept(this);
		}
		String cWhileExpr = whileOP.expr.accept(cExprGeneratorVisitor).get(0);
		clangCodeEditor.openWhileBlock(cWhileExpr);
		whileOP.iterStmts.accept(this);
		if (whileOP.preStmts != null) {
			whileOP.preStmts.accept(this);
		}
		clangCodeEditor.closeBlock();
		return null;
	}

	@Override
	public Object visitRepeatOP(RepeatOP repeatOP) {
		clangCodeEditor.openDoWhileBlock();
		for (VarDeclOP varDecl : repeatOP.varDecls) {
			visit_VarDeclOP(varDecl, false);
		}
		repeatOP.stmts.accept(this);
		String cWhileExpr = repeatOP.expr.accept(cExprGeneratorVisitor).get(0);
		clangCodeEditor.closeDoWhileBlock(cWhileExpr);
		return null;
	}

	@Override
	public Object visitAssignOP(AssignOP assignOP) {
		Iterator<Id> idIt = assignOP.ids.iterator();
		for (ExprNode expr : assignOP.exprs) {
			List<String> cExprs = expr.accept(cExprGeneratorVisitor);
			for (String cExpr : cExprs) {
				String varName = idIt.next().name;
				String usableName = easyNamesManager.safeNameUsage(varName);
				clangCodeEditor.assign(usableName, cExpr);
			}
		}
		return null;
	}

	@Override
	public Object visitReadlnOP(ReadlnOP readlnOP) {
		for (Id id : readlnOP.ids) {
			String usableName = easyNamesManager.safeNameUsage(id.name);
			clangCodeEditor.scanf(usableName, id.type);
		}
		return null;
	}

	@Override
	public Object visitWriteOP(WriteOP writeOP) {
		for (ExprNode expr : writeOP.exprs) {
			List<String> cExprs = expr.accept(cExprGeneratorVisitor);
			String[] types = expr.type.split(",");
			int i = 0;
			for (String cExpr : cExprs) {
				clangCodeEditor.printf(cExpr, types[i++]);
			}
		}
		return null;
	}

	@Override
	public Object visitCallProcStatOP(CallProcStatOP callProcStatOP) {
		List<String> cExprs = callProcStatOP.callProcOP.accept(cExprGeneratorVisitor);
		// Chiarimento contestuale
		// 'cExprs' può essere:
		// * ['add(1, 5)']                                      (se è una C-like expression)
		// * ['_add_0.t0', '_add_0.t1', '_add_0.t2']            (se era necessario codice di servizio per invocarla)
		//
		// Scrivo nel codice l'espressione solo se è un'invocazione (e non una sequenza di valori di ritorno inutili),
		// quindi solo quando 'callProcStatOP.callProcOP.type' è un singolo tipo, ossia:
		// if (callProcStatOP.callProcOP.type.split(",").length == 1) {...}
		if (cExprs.size() == 1) { // condizione alternativa intercambiabile
			clangCodeEditor.putCLikeExpr(cExprs.get(0) + ";");
		}
		return null;
	}

	public void saveOnFile(String filepath) {
		try {
			FileWriter myWriter = new FileWriter(filepath);
			myWriter.write(clangCodeEditor.getCode().replace("\n", "\r\n"));
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred while saving the C output code file.");
			e.printStackTrace();
		}
	}

}
