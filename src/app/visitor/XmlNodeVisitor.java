package app.visitor;

import app.node.*;
import app.node.binop.*;
import app.node.expr.*;
import app.node.stat.*;
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
		if (programOP.getVarDecls() != null) {
			valDeclsElem = document.createElement("VarDecls");
			for (VarDeclOP varDecl : programOP.getVarDecls()) {
				Element varDeclOpElem = (Element) varDecl.accept(this);
				valDeclsElem.appendChild(varDeclOpElem);
			}
		}
		// <Procs>
		Element procsElem = document.createElement("Procs");
		for (ProcOP proc : programOP.getProcs()) {
			Element procElem = (Element) proc.accept(this);
			procsElem.appendChild(procElem);
		}
		// <ProgramOp>
		Element programOpElem = document.createElement("ProgramOp");
		if (programOP.getVarDecls() != null) {
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
		TypeNode typeNode = varDeclOP.getType();
		Element typeNodeElem = (Element) typeNode.accept(this);
		varDeclOpElem.appendChild(typeNodeElem);
		// <IdInitOps>
		Element idInitOpsElem = document.createElement("IdInitOps");
		for (IdInitOP idInit : varDeclOP.getIdInits()) {
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
		Element idElem = (Element) idInitOP.getId().accept(this);
		Element exprElem = null;
		if (idInitOP.getExpr() != null) {
			exprElem = (Element) idInitOP.getExpr().accept(this);
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
		Element idElem = (Element) procOP.getId().accept(this);
		// <ParDecls>
		Element parDeclsElem = null;
		if (procOP.getParDecls() != null) {
			parDeclsElem = document.createElement("ParDecls");
			for (ParDeclOP parDecl : procOP.getParDecls()) {
				Element parDeclElem = (Element) parDecl.accept(this);
				parDeclsElem.appendChild(parDeclElem);
			}
		}
		// <ReturnTypes>
		Element returnTypesElem = document.createElement("ReturnTypes");
		for (ResultTypeNode returnType : procOP.getReturnTypes()) {
			Element returnTypeElem = (Element) returnType.accept(this);
			returnTypesElem.appendChild(returnTypeElem);
		}
		Element procBodyElem = (Element) procOP.getProcBody().accept(this);
		// <ProcOP>
		Element procOpElem = document.createElement("ProcOp");
		procOpElem.appendChild(idElem);
		if (parDeclsElem != null) {
			procOpElem.appendChild(parDeclsElem);
		}
		procOpElem.appendChild(returnTypesElem);
		procOpElem.appendChild(procBodyElem);
		return procOpElem;
	}

	@Override
	public Object visitParDeclOP(ParDeclOP parDeclOP) {
		Element typeElem = (Element) parDeclOP.getType().accept(this);
		Element idsElem = getIdsElem(parDeclOP.getIds());
		// <ParDeclOp>
		Element parDeclOpElem = document.createElement("ParDeclOp");
		parDeclOpElem.appendChild(typeElem);
		parDeclOpElem.appendChild(idsElem);
		return parDeclOpElem;
	}

	@Override
	public Object visitResultTypeNode(ResultTypeNode resultTypeNode) {
		// <ReturnType type="...">
		Element returnTypeElem = document.createElement("ReturnType");
		returnTypeElem.setAttribute("type", resultTypeNode.getStringType());
		return returnTypeElem;
	}

	@Override
	public Object visitProcBodyOP(ProcBodyOP procBodyOP) {
		// <VarDecls>
		Element varDeclsElem = null;
		if (procBodyOP.getVarDecls() != null) {
			varDeclsElem = document.createElement("VarDecls");
			for (VarDeclOP varDecl : procBodyOP.getVarDecls()) {
				Element varDeclElem = (Element) varDecl.accept(this);
				varDeclsElem.appendChild(varDeclElem);
			}
		}
		// <ReturnExprList>
		Element returnExprListElem = null;
		if (procBodyOP.getReturnExpressions() != null) {
			returnExprListElem = document.createElement("ReturnExprList");
			for (ExprNode retExpr : procBodyOP.getReturnExpressions()) {
				Element retExprElem = (Element) retExpr.accept(this);
				returnExprListElem.appendChild(retExprElem);
			}
		}
		Element bodyElem = (Element) procBodyOP.getBody().accept(this);
		// <ProcBodyOp>
		Element procBodyOpElem = document.createElement("ProcBodyOp");
		if (varDeclsElem != null) {
			procBodyOpElem.appendChild(varDeclsElem);
		}
		if (returnExprListElem != null) {
			procBodyOpElem.appendChild(returnExprListElem);
		}
		procBodyOpElem.appendChild(bodyElem);
		return procBodyOpElem;
	}

	@Override
	public Object visitBodyOP(BodyOP bodyOP) {
		// <BodyOp>
		Element bodyOpElem = document.createElement("BodyOp");
		for (StatNode stmt : bodyOP.getStmts()) {
			Element stmtElem = (Element) stmt.accept(this);
			bodyOpElem.appendChild(stmtElem);
		}
		return bodyOpElem;
	}

	@Override
	public Object visitIfOP(IfOP ifOp) {
		Element exprElem = (Element) ifOp.getExpr().accept(this);
		Element ifBodyOpElem = (Element) ifOp.getIfBody().accept(this);
		// <Elifs>
		Element elifsElem = null;
		if (ifOp.getElifs() != null) {
			elifsElem = document.createElement("Elifs");
			for (ElifOP elif : ifOp.getElifs()) {
				Element elifElem = (Element) elif.accept(this);
				elifsElem.appendChild(elifElem);
			}
		}
		Element elseBodyOpElem = null;
		if (ifOp.getElseBody() != null) {
			elseBodyOpElem = (Element) ifOp.getElseBody().accept(this);
		}
		// <IfOp>
		Element ifOpElem = document.createElement("IfOp");
		ifOpElem.appendChild(exprElem);
		ifOpElem.appendChild(ifBodyOpElem);
		if (elifsElem != null) {
			ifOpElem.appendChild(elifsElem);
		}
		if (elseBodyOpElem != null) {
			ifOpElem.appendChild(elseBodyOpElem);
		}
		return ifOpElem;
	}

	@Override
	public Object visitElifOP(ElifOP elifOp) {
		Element exprElem = (Element) elifOp.getExpr().accept(this);
		Element bodyOpElem = (Element) elifOp.getBody().accept(this);
		// <ElifOp>
		Element elifOpElem = document.createElement("ElifOp");
		elifOpElem.appendChild(exprElem);
		elifOpElem.appendChild(bodyOpElem);
		return elifOpElem;
	}

	@Override
	public Object visitWhileOP(WhileOP whileOp) {
		// <PreStmt>
		Element preStmtBodyOpElem = null;
		if (whileOp.getPreStmts() != null) {
			preStmtBodyOpElem = (Element) whileOp.getPreStmts().accept(this);
		}
		Element exprElem = (Element) whileOp.getExpr().accept(this);
		// <IterStmt>
		Element iterStmtBodyOpElem = null;
		if (whileOp.getIterStmts() != null) {
			iterStmtBodyOpElem = (Element) whileOp.getIterStmts().accept(this);
		}
		// <WhileOp>
		Element whileOpElem = document.createElement("WhileOp");
		if (preStmtBodyOpElem != null) {
			whileOpElem.appendChild(preStmtBodyOpElem);
		}
		whileOpElem.appendChild(exprElem);
		if (iterStmtBodyOpElem != null) {
			whileOpElem.appendChild(iterStmtBodyOpElem);
		}
		return whileOpElem;
	}

	@Override
	public Object visitAssignOP(AssignOP assignOp) {
		// <AssignOp>
		Element assignOpElem = document.createElement("AssignOp");
		assignOpElem.appendChild(getIdsElem(assignOp.getIds()));
		assignOpElem.appendChild(getExprListElem(assignOp.getExprs()));
		return assignOpElem;
	}

	@Override
	public Object visitReadlnOP(ReadlnOP readlnOp) {
		// ReadlnOp
		Element readlnOpElem = document.createElement("ReadlnOp");
		readlnOpElem.appendChild(getIdsElem(readlnOp.getIds()));
		return readlnOpElem;
	}

	@Override
	public Object visitWriteOP(WriteOP writeOp) {
		// <WriteOp>
		Element writeOpElem = document.createElement("WriteOp");
		writeOpElem.appendChild(getExprListElem(writeOp.getExprs()));
		return writeOpElem;
	}

	@Override
	public Object visitId(Id id) {
		// <Id name="...">
		Element idElem = document.createElement("Id");
		idElem.setAttribute("name", id.getName());
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
		Element exprElem = (Element) notOp.getExpr().accept(this);
		// <NotOp>
		Element notOpElem = document.createElement("NotOp");
		notOpElem.appendChild(exprElem);
		return notOpElem;
	}

	@Override
	public Object visitUMinusOP(UMinusOP uMinusOp) {
		Element exprElem = (Element) uMinusOp.getExpr().accept(this);
		// <UMinusOp>
		Element uMinusOpElem = document.createElement("UMinusOp");
		uMinusOpElem.appendChild(exprElem);
		return uMinusOpElem;
	}

	@Override
	public Object visitCallProcOP(CallProcOP callProcOp) {
		Element idElem = (Element) callProcOp.getProcId().accept(this);
		Element exprListElem = null;
		if (callProcOp.getExprs() != null) {
			exprListElem = getExprListElem(callProcOp.getExprs());
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
		Element aElem = (Element) binaryOperationNode.getA().accept(this);
		Element bElem = (Element) binaryOperationNode.getB().accept(this);
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
