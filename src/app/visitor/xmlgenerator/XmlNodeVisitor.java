package app.visitor.xmlgenerator;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;
import app.visitor.INodeVisitor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XmlNodeVisitor implements INodeVisitor {

	private Document document;

	public XmlNodeVisitor() throws ParserConfigurationException {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}

	public void saveOnFile(String filepath) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(document), new StreamResult(new File(filepath)));
	}

	@Override
	public Object visitProgramOP(ProgramOP programOP) {
		// <VarDecls>
		Element valDeclsElem = null;
		if (programOP.varDecls != null) {
			valDeclsElem = document.createElement("VarDecls");
			for (VarDeclOP varDecl : programOP.varDecls) {
				Element varDeclOpElem = (Element) varDecl.accept(this);
				valDeclsElem.appendChild(varDeclOpElem);
			}
		}
		// <Procs>
		Element procsElem = document.createElement("Procs");
		for (ProcOP proc : programOP.procs) {
			Element procElem = (Element) proc.accept(this);
			procsElem.appendChild(procElem);
		}
		// <ProgramOp>
		Element programOpElem = document.createElement("ProgramOp");
		if (programOP.varDecls != null) {
			programOpElem.appendChild(valDeclsElem);
		}
		programOpElem.appendChild(procsElem);
		document.appendChild(programOpElem);
		return valDeclsElem;
	}

	@Override
	public Object visitVarDeclOP(VarDeclOP varDeclOP) {
		Element varDeclOpElem = document.createElement("VarDeclOp");
		// <VarDeclOp>
		Element typeNodeElem = (Element) varDeclOP.type.accept(this);
		varDeclOpElem.appendChild(typeNodeElem);
		// <IdInitOps>
		Element idInitOpsElem = document.createElement("IdInitOps");
		for (IdInitOP idInit : varDeclOP.idInits) {
			Element idInitElem = (Element) idInit.accept(this);
			idInitOpsElem.appendChild(idInitElem);
		}
		varDeclOpElem.appendChild(idInitOpsElem);
		return varDeclOpElem;
	}

	@Override
	public Object visitTypeNode(TypeNode typeNode) {
		// <Type type="...">
		Element typeElem = document.createElement("Type");
		typeElem.setAttribute("type", typeNode.getStringType());
		return typeElem;
	}

	@Override
	public Object visitIdInitOP(IdInitOP idInitOP) {
		Element idElem = (Element) idInitOP.id.accept(this);
		Element exprElem = null;
		if (idInitOP.expr != null) {
			exprElem = (Element) idInitOP.expr.accept(this);
		}
		// <IdInitOp>
		Element idInitOpElem = document.createElement("IdInitOp");
		idInitOpElem.appendChild(idElem);
		if (exprElem != null) {
			idInitOpElem.appendChild(exprElem);
		}
		return idInitOpElem;
	}

	@Override
	public Object visitProcOP(ProcOP procOP) {
		Element idElem = (Element) procOP.id.accept(this);
		// <ParDecls>
		Element parDeclsElem = null;
		if (procOP.parDecls != null) {
			parDeclsElem = document.createElement("ParDecls");
			for (ParDeclOP parDecl : procOP.parDecls) {
				Element parDeclElem = (Element) parDecl.accept(this);
				parDeclsElem.appendChild(parDeclElem);
			}
		}
		Element returnTypesElem = null;
		if (procOP.returnTypes != null) {
			// <ReturnTypes>
			returnTypesElem = document.createElement("ReturnTypes");
			for (TypeNode returnType : procOP.returnTypes) {
				Element returnTypeElem = (Element) returnType.accept(this);
				returnTypesElem.appendChild(returnTypeElem);
			}
		}
		Element procBodyElem = (Element) procOP.procBody.accept(this);
		// <ProcOP>
		Element procOpElem = document.createElement("ProcOp");
		procOpElem.appendChild(idElem);
		if (parDeclsElem != null) {
			procOpElem.appendChild(parDeclsElem);
		}
		if (returnTypesElem != null) {
			procOpElem.appendChild(returnTypesElem);
		}
		if (procBodyElem != null) {
			procOpElem.appendChild(procBodyElem);
		}
		return procOpElem;
	}

	@Override
	public Object visitParDeclOP(ParDeclOP parDeclOP) {
		Element typeElem = (Element) parDeclOP.type.accept(this);
		Element idsElem = getIdsElem(parDeclOP.ids);
		// <ParDeclOp>
		Element parDeclOpElem = document.createElement("ParDeclOp");
		parDeclOpElem.appendChild(typeElem);
		parDeclOpElem.appendChild(idsElem);
		return parDeclOpElem;
	}

	@Override
	public Object visitProcBodyOP(ProcBodyOP procBodyOP) {
		// <VarDecls>
		Element varDeclsElem = null;
		if (procBodyOP.varDecls != null) {
			varDeclsElem = document.createElement("VarDecls");
			for (VarDeclOP varDecl : procBodyOP.varDecls) {
				Element varDeclElem = (Element) varDecl.accept(this);
				varDeclsElem.appendChild(varDeclElem);
			}
		}
		// <ReturnExprList>
		Element returnExprListElem = null;
		if (procBodyOP.returnExpressions != null) {
			returnExprListElem = document.createElement("ReturnExprList");
			for (ExprNode retExpr : procBodyOP.returnExpressions) {
				Element retExprElem = (Element) retExpr.accept(this);
				returnExprListElem.appendChild(retExprElem);
			}
		}
		Element bodyElem = null;
		if (procBodyOP.body != null) {
			bodyElem = (Element) procBodyOP.body.accept(this);
		}
		// <ProcBodyOp>
		Element procBodyOpElem;
		if (varDeclsElem != null || returnExprListElem != null || bodyElem != null) {
			procBodyOpElem = document.createElement("ProcBodyOp");
			if (varDeclsElem != null) {
				procBodyOpElem.appendChild(varDeclsElem);
			}
			if (returnExprListElem != null) {
				procBodyOpElem.appendChild(returnExprListElem);
			}
			if (bodyElem != null) {
				procBodyOpElem.appendChild(bodyElem);
			}
		} else {
			procBodyOpElem = null;
		}
		return procBodyOpElem;
	}

	@Override
	public Object visitBodyOP(BodyOP bodyOP) {
		// <BodyOp>
		Element bodyOpElem = document.createElement("BodyOp");
		for (StatNode stmt : bodyOP.stmts) {
			Element stmtElem = (Element) stmt.accept(this);
			bodyOpElem.appendChild(stmtElem);
		}
		return bodyOpElem;
	}

	@Override
	public Object visitIfOP(IfOP ifOp) {
		Element exprElem = (Element) ifOp.expr.accept(this);
		Element ifBodyOpElem = (Element) ifOp.ifBody.accept(this);
		// <Then>
		Element thenElem = document.createElement("Then");
		thenElem.appendChild(ifBodyOpElem);
		// <Elifs>
		Element elifsElem = null;
		if (ifOp.elifs != null) {
			elifsElem = document.createElement("Elifs");
			for (ElifOP elif : ifOp.elifs) {
				Element elifElem = (Element) elif.accept(this);
				elifsElem.appendChild(elifElem);
			}
		}
		// <Else>
		Element elseElem = null;
		if (ifOp.elseBody != null) {
			Element elseBodyOpElem = (Element) ifOp.elseBody.accept(this);
			elseElem = document.createElement("Else");
			elseElem.appendChild(elseBodyOpElem);
		}
		// <IfOp>
		Element ifOpElem = document.createElement("IfOp");
		ifOpElem.appendChild(exprElem);
		ifOpElem.appendChild(thenElem);
		if (elifsElem != null) {
			ifOpElem.appendChild(elifsElem);
		}
		if (elseElem != null) {
			ifOpElem.appendChild(elseElem);
		}
		return ifOpElem;
	}

	@Override
	public Object visitElifOP(ElifOP elifOp) {
		Element exprElem = (Element) elifOp.expr.accept(this);
		Element bodyOpElem = (Element) elifOp.body.accept(this);
		// <ElifOp>
		Element elifOpElem = document.createElement("ElifOp");
		elifOpElem.appendChild(exprElem);
		elifOpElem.appendChild(bodyOpElem);
		return elifOpElem;
	}

	@Override
	public Object visitWhileOP(WhileOP whileOp) {
		// <PreStmt>
		Element preStmtsElem = null;
		if (whileOp.preStmts != null) {
			Element preStmtBodyOpElem = (Element) whileOp.preStmts.accept(this);
			preStmtsElem = document.createElement("PreStmts");
			preStmtsElem.appendChild(preStmtBodyOpElem);
		}
		Element exprElem = (Element) whileOp.expr.accept(this);
		// <IterStmt>
		Element iterStmtsElem = document.createElement("IterStmts");
		iterStmtsElem.appendChild((Element) whileOp.iterStmts.accept(this));
		// <WhileOp>
		Element whileOpElem = document.createElement("WhileOp");
		if (preStmtsElem != null) {
			whileOpElem.appendChild(preStmtsElem);
		}
		whileOpElem.appendChild(exprElem);
		whileOpElem.appendChild(iterStmtsElem);
		return whileOpElem;
	}

	@Override
	public Object visitAssignOP(AssignOP assignOp) {
		// <AssignOp>
		Element assignOpElem = document.createElement("AssignOp");
		assignOpElem.appendChild(getIdsElem(assignOp.ids));
		assignOpElem.appendChild(getExprListElem(assignOp.exprs));
		return assignOpElem;
	}

	@Override
	public Object visitReadlnOP(ReadlnOP readlnOp) {
		// ReadlnOp
		Element readlnOpElem = document.createElement("ReadlnOp");
		readlnOpElem.appendChild(getIdsElem(readlnOp.ids));
		return readlnOpElem;
	}

	@Override
	public Object visitWriteOP(WriteOP writeOp) {
		// <WriteOp>
		Element writeOpElem = document.createElement("WriteOp");
		writeOpElem.appendChild(getExprListElem(writeOp.exprs));
		return writeOpElem;
	}

	@Override
	public Object visitId(Id id) {
		// <Id name="...">
		Element idElem = document.createElement("Id");
		idElem.setAttribute("name", id.name);
		return idElem;
	}

	@Override
	public Object visitNull(Null aNull) {
		// <Null>
		return document.createElement("Null");
	}

	@Override
	public Object visitTrue(True aTrue) {
		// <True>
		return document.createElement("True");
	}

	@Override
	public Object visitFalse(False aFalse) {
		// <False>
		return document.createElement("False");
	}

	@Override
	public Object visitIntConst(IntConst intConst) {
		// <IntConst const="...">
		Element intConstElem = document.createElement("IntConst");
		intConstElem.setAttribute("const", "" + intConst.getValue());
		return intConstElem;
	}

	@Override
	public Object visitFloatConst(FloatConst floatConst) {
		// <FloatConst const="...">
		Element intConstElem = document.createElement("FloatConst");
		intConstElem.setAttribute("const", "" + floatConst.getValue());
		return intConstElem;
	}

	@Override
	public Object visitStringConst(StringConst stringConst) {
		// <StringConst const="...">
		Element intConstElem = document.createElement("StringConst");
		intConstElem.setAttribute("const", "" + stringConst.getStr());
		return intConstElem;
	}

	@Override
	public Object visitNotOP(NotOP notOp) {
		Element exprElem = (Element) notOp.expr.accept(this);
		// <NotOp>
		Element notOpElem = document.createElement("NotOp");
		notOpElem.appendChild(exprElem);
		return notOpElem;
	}

	@Override
	public Object visitUMinusOP(UMinusOP uMinusOp) {
		Element exprElem = (Element) uMinusOp.expr.accept(this);
		// <UMinusOp>
		Element uMinusOpElem = document.createElement("UMinusOp");
		uMinusOpElem.appendChild(exprElem);
		return uMinusOpElem;
	}

	@Override
	public Object visitCallProcOP(CallProcOP callProcOp) {
		Element idElem = (Element) callProcOp.procId.accept(this);
		Element exprListElem = null;
		if (callProcOp.exprs != null) {
			exprListElem = getExprListElem(callProcOp.exprs);
		}
		// <CallProcOp>
		Element procOpElem = document.createElement("CallProcOp");
		procOpElem.appendChild(idElem);
		if (exprListElem != null) {
			procOpElem.appendChild(exprListElem);
		}
		return procOpElem;
	}

	@Override
	public Object visitLTOP(LTOP ltop) {
		return printBinaryOperationNode(ltop);
	}

	@Override
	public Object visitLEOP(LEOP leop) {
		return printBinaryOperationNode(leop);
	}

	@Override
	public Object visitGTOP(GTOP gtop) {
		return printBinaryOperationNode(gtop);
	}

	@Override
	public Object visitGEOP(GEOP geop) {
		return printBinaryOperationNode(geop);
	}

	@Override
	public Object visitEQOP(EQOP eqop) {
		return printBinaryOperationNode(eqop);
	}

	@Override
	public Object visitAndOP(AndOP andOp) {
		return printBinaryOperationNode(andOp);
	}

	@Override
	public Object visitOrOP(OrOP orOp) {
		return printBinaryOperationNode(orOp);
	}

	@Override
	public Object visitTimesOP(TimesOP timesOp) {
		return printBinaryOperationNode(timesOp);
	}

	@Override
	public Object visitDivOP(DivOP divOp) {
		return printBinaryOperationNode(divOp);
	}

	@Override
	public Object visitPlusOP(PlusOP plusOp) {
		return printBinaryOperationNode(plusOp);
	}

	@Override
	public Object visitMinusOP(MinusOP minusOp) {
		return printBinaryOperationNode(minusOp);
	}

	private Element printBinaryOperationNode(BinaryOperationNode binaryOperationNode) {
		Element aElem = (Element) binaryOperationNode.a.accept(this);
		Element bElem = (Element) binaryOperationNode.b.accept(this);
		String className = binaryOperationNode.getClass().getSimpleName();
		if (className.endsWith("OP")) {
			className = className.substring(0, className.length() - 1) + "p";
		}
		Element binOpElem = document.createElement(className);
		binOpElem.appendChild(aElem);
		binOpElem.appendChild(bElem);
		return binOpElem;
	}

	private Element getIdsElem(List<Id> ids) {
		// <Ids>
		Element idsElem = document.createElement("Ids");
		for (Id id : ids) {
			Element idElem = (Element) id.accept(this);
			idsElem.appendChild(idElem);
		}
		return idsElem;
	}

	private Element getExprListElem(List<ExprNode> exprs) {
		// <ExprList>
		Element exprListElem = document.createElement("ExprList");
		for (ExprNode expr : exprs) {
			Element exprElem = (Element) expr.accept(this);
			exprListElem.appendChild(exprElem);
		}
		return exprListElem;
	}

}
