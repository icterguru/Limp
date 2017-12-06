/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package limp;

/**
 *
 * @author seyuu_000
 */
public class Parser extends Types {
    Lexer lexer;
    Lexeme current;
    
    public Parser(String filename) {
        lexer = new Lexer(filename);
        current = null;
    }
    
    public boolean check(String type) {
        return type.equals(current.type);
    }
    
    public Lexeme advance() {
        Lexeme old = current;
        current = lexer.lex();
        return old;
    }
    
    public Lexeme match(String type) {
        if (check(type)) {
            return advance();
        }
        System.out.println("Parse error: looking for " + type + " found " + "\"" + current.type + "\"" + " insead.");
        System.out.println("\t Error on line number: " + Integer.toString(current.lineError));
        System.exit(0);
        return null;
    }
    
    public Lexeme parse() {
        Lexeme tree;
        advance();
        tree = program();
        match(ENDofFILE);
        return tree;
    }
    /*
        Program
    *
    *
    *
    */
    public Lexeme program() {
        Lexeme tree = new Lexeme(LIST);
        optNewline();
        tree.left = globalDefineList();
        Lexeme tmp = new Lexeme(LIST);
        tmp.left = funcCallStart();
        optNewline();
        tree.right = tmp;
        return tree;
    }
    
    public Lexeme globalDefine() {
        Lexeme tree;
        if (defineVariablePending()) {
            tree = defineVariable();
        }
        else if (defineObjectPending()) {
            tree = defineObject();
        }
        else {
            tree = defineFunction();
        }
        return tree;
    }
    
    public Lexeme globalDefineList() {
        Lexeme tree = new Lexeme(LIST);
        tree.left = globalDefine();
        //
        //
        //
        newline();
        if (globalDefinePending()) {
            tree.right = globalDefineList();
        }
        return tree;
    }
    
    public Lexeme newline() {
        match(NEWLINE);
        if (newlinePending()) {
            newline();
        }
        return null;
    }
    
    public Lexeme optNewline() {
        if (newlinePending()) {
            newline();
        }
        return null;
    }
    
    /*
        Primary
    *
    *
    *
    */
    public Lexeme primary() {
        Lexeme tree;
        if (literalPending()) {
            tree = literal();
        }
        else if (varExpressionPending()) {
            tree = varExpression();
        } else if (powStatementPending()) {
            tree = powStatement();
        } else if (getChrStatementPending()) {
            tree = getChrStatement();
        } else if (strimStatementPending()) {
            tree = strimStatement();
        } else if (toIntStatementPending()) {
            tree = toIntStatement();
        } else if (toStringStatementPending()) {
            tree = toStringStatement();
        } else if (isDigitStatementPending()) {
            tree = isDigitStatement();
        } else if (lengthStatementPending()) {
            tree = lengthStatement();
        }
        else if (check(OPAREN)) {
            tree = match(OPAREN);
            tree.left = null;
            tree.right = expression();
            match(CPAREN);
        }
        else {  // unary minus
            tree = match(MINUS);
            tree.type = UMINUS;
            tree.left = null;
            tree.right = primary();
        }
        
        return tree;
    }
    
    public Lexeme literal() {
        Lexeme tree;
        tree = advance(); 
        return tree;
        //
        // must be a literal pending or we would not have gotten here
        // use advance instead of match since we don't know exactly
        // which literal is pending
    }
    
    /*
        Lists
    *
    *
    *
    */
    public Lexeme paramList() {
        Lexeme tree = new Lexeme(LIST);
        tree.left = match(VARIABLE);
        if (check(COMMA)) {
            match(COMMA);
            if (paramListPending()) {
                tree.right = paramList();
            }
        }
        return tree;
    }
    
    public Lexeme optParamList() {
        Lexeme tree = null;
        if (paramListPending()) {
            tree = paramList();
        }
        return tree;
    }
    
    public Lexeme expressionList() {
        Lexeme tree = new Lexeme(LIST);
        tree.left = expression();
        if (check(COMMA)) {
            match(COMMA);
            if (expressionListPending()) {
                tree.right = expressionList();
            }
        }
        return tree;
    }
    
    public Lexeme optExpressionList() {
        Lexeme tree = null;
        if (expressionListPending()) {
            tree = expressionList();
        }
        return tree;
    }
    /*
        Variables
    *
    *
    *
    */
    public Lexeme varExpression() {
        Lexeme tree;
        tree = match(VARIABLE);
        if (check(OPAREN)) {
            Lexeme tmp = funcCall();
            tmp.left = tree;
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme defineStatement() {
        Lexeme tree;
        if (defineVariablePending()) {
            tree = defineVariable();
        }
        else {
            tree = defineFunction();
        }
        return tree;
    }
    
    public Lexeme defineVariable() {
        Lexeme tree = new Lexeme(VAR_DEF);
        match(LOCAL);
        tree.left = match(VARIABLE);
        match(ASSIGN);
        tree.right = expression();
        return tree;
    }
    
    public Lexeme defineVariableList() {
        Lexeme tree = new Lexeme(LIST);
        tree.left = defineVariable();
        newline();
        if (defineVariablePending()) {
            tree.right = defineVariableList();
        }
        return tree;
    }
    
    /*
        Operators
    *
    *
    *
    */
    public Lexeme operator() {
        Lexeme tree;
        tree = advance();
        return tree;
    }
    
    /*
        Expressions
    *
    *
    *
    */
    //public Lexeme expression() {
    //    Lexeme tree;
    //    tree = expression1();
    //    while (operatorCommaPending()) {
    //        Lexeme tmp = operator();
    //        tmp.left = tree;
    //        tmp.right = expression();
    //        tree = tmp;
    //    }
    //    return tree;
    //}
    
    public Lexeme expression() {
        Lexeme tree;
        tree = expression1();
        if (operatorAssignmentPending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression1() {
        Lexeme tree;
        tree = expression2();
        while (operatorLogicalOrPending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression1();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression2() {
        Lexeme tree;
        tree = expression3();
        while (operatorLogicalAndPending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression2();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression3() {
        Lexeme tree;
        tree = expression4();
        while (operatorEqualityPending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression3();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression4() {
        Lexeme tree;
        tree = expression5();
        while (operatorRelationalPending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression4();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression5() {
        Lexeme tree;
        tree = expression6();
        while (operatorAdditivePending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression5();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression6() {
        Lexeme tree;
        tree = expression7();
        while (operatorMultiplicativePending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression6();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression7() {
        Lexeme tree;
        tree = expression8();
        if (operatorUnaryPending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = expression7();
            tree = tmp;
        }
        return tree;
    }
    
    public Lexeme expression8() {
        Lexeme tree;
        tree = primary();
        while (operatorPrimaryPending()){
            Lexeme tmp = operator();
            tmp.left = tree;
            tmp.right = primary();
            tree = tmp;
        }
        return tree;
    }
    
    /*
        Statements
    *
    *
    *
    */
    public Lexeme block() {
        Lexeme tree;
        newline();
        tree = statementList();
        return tree;
    }
    
    public Lexeme statement() { // *************
        Lexeme tree = null;
        //if (varExpressionPending()) {
        //    tree = varExpression();
        if (expressionPending()) {
            tree = expression();
        } else if (ifStatementPending()) {
            tree = ifStatement();
        } else if (iterStatementPending()) {
            tree = loopStatement();
        } else if (definitionPending()){
            tree = defineStatement();
        } else if (flowStatementPending()) {
            tree = flowStatement();
        } else if (printStatementPending()) {
            tree = printStatement();
        }
        return tree;
    }
    
    public Lexeme statementList() { // *************
        Lexeme tree = new Lexeme(LIST);
        tree.left = statement();
        newline();
        if (statementPending()) {
            tree.right = statementList();
        }
        //
        // right side of tree will be null
        //
        return tree;
    }
    
    public Lexeme flowStatement() {
        Lexeme tree;
        if (returnStatementPending()) {
            tree = match(RETURN);
            tree.right = expression();
        }
        else {
            tree = advance();
        }
        return tree;
    }
    
    public Lexeme printStatement() {
        Lexeme tree;
        tree = match(PRINT);
        match(OPAREN);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme powStatement() {
        Lexeme tree;
        tree = match(POWER);
        match(OPAREN);
        tree.left = expression();
        match(COMMA);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme getChrStatement() {
        Lexeme tree;
        tree = match(GETCHR);
        match(OPAREN);
        tree.left = expression();
        match(COMMA);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme strimStatement() {
        Lexeme tree;
        tree = match(STRIM);
        match(OPAREN);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme toIntStatement() {
        Lexeme tree;
        tree = match(TOINT);
        match(OPAREN);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme toStringStatement() {
        Lexeme tree;
        tree = match(TOSTRING);
        match(OPAREN);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme isDigitStatement() {
        Lexeme tree;
        tree = match(ISDIGIT);
        match(OPAREN);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme lengthStatement() {
        Lexeme tree;
        tree = match(LENGTH);
        match(OPAREN);
        tree.right = expression();
        match(CPAREN);
        return tree;
    }
    
    /*
        Conditionals
    *
    *
    *
    */
    public Lexeme ifStatement() {
        Lexeme tree;
        tree = match(IF);
        match(OPAREN);
        tree.left = expression();
        match(CPAREN);
        
        Lexeme block = new Lexeme(BLOCK);
        block.left = block();
        if (elseStatementPending()) {
            match(ELSE);
            if (ifStatementPending()) {
                block.right = ifStatement();
                newline();
            }
            else {
                Lexeme block2 = new Lexeme(BLOCK);
                block2.left = block();
                block.right = block2;
            }
        }
        tree.right = block;
        match(END);
        return tree;
    }
    
    /*
        Iteration
    *
    *
    *
    */
    public Lexeme loopStatement() {
        Lexeme tree;
        if (whileStatementPending()) {
            tree = whileStatement();  
        }
        else {
            tree = repeatStatement();
        }
        
        return tree;
    }
    
    public Lexeme whileStatement() {
        Lexeme tree;
        tree = match(WHILE);
        match(OPAREN);
        tree.left = expression();
        match(CPAREN);
        Lexeme block = new Lexeme(BLOCK);
        block.left = block();
        tree.right = block;
        match(END);
        
        return tree;
    }
    
    public Lexeme repeatStatement() {
        Lexeme tree;
        tree = match(REPEAT);
        match(OPAREN);    
        tree.left = expression();
        match(CPAREN);
        Lexeme block = new Lexeme(BLOCK);
        block.left = block();
        tree.right = block;
        match(END);
        
        return tree;
    }
    
    /*
        Functions
    *
    *
    *
    */
    public Lexeme defineFunction() {
        Lexeme tree = new Lexeme(FUNC_DEF);
        match(FUNCTION);
        tree.left = match(VARIABLE);
        match(OPAREN);
        Lexeme tmp1 = new Lexeme(LIST);
        Lexeme tmp2 = new Lexeme(BLOCK);
        tmp1.left = optParamList();
        match(CPAREN);
        tmp2.left = block();
        tmp1.right = tmp2;
        tree.right = tmp1;
        match(END);
        return tree;      
    }
    
    public Lexeme defineFunctionList() {
        Lexeme tree = new Lexeme(LIST);
        tree.left = defineFunction();
        newline();
        if (check(FUNCTION)) {
            tree.right = defineFunctionList();
        }
        return tree;
    }
    
    public Lexeme funcCall() {
        Lexeme tree = new Lexeme(FUNC_CALL);
        //
        // Needs to be fixed, the final call to a function requires a variable
        // name but is not given in this function as the function varExpression
        // takes care of that instead.
        //
        match(OPAREN);
        tree.right = optExpressionList();
        match(CPAREN);
        return tree;
    }
    
    public Lexeme funcCallStart() {
        Lexeme tree = new Lexeme(FUNC_CALL);
        tree.left = match(VARIABLE);
        match(OPAREN);
        tree.right = optExpressionList();
        match(CPAREN);
        return tree;
    }
    
    /*
        Objects
    *
    *
    *
    */ 
    public Lexeme defineObject() {
        Lexeme tree = new Lexeme(FUNC_DEF);
        match(OBJECT);
        tree.left = match(VARIABLE);
        match(OPAREN);
        Lexeme tmp1 = new Lexeme(LIST);
        tmp1.left = optParamList();
        match(CPAREN);
        Lexeme tmp2 = new Lexeme(BLOCK);
        
        //
        // need a newline, because not a block
        //
        newline();
        
        //
        // object fields (optional)
        if (defineVariablePending()) {
            tmp2.left = defineVariableList();
        }
        Lexeme tmp3 = new Lexeme(BLOCK);
        //
        // object methods (mandatory)
        tmp3.left = defineFunctionList();
        //
        //
        //
        Lexeme tmp4 = new Lexeme(LIST);
        tmp4.left = match(THIS);
        
        tmp1.right = tmp2;
        tmp2.right = tmp3;
        tmp3.right = tmp4;
        tree.right = tmp1;
        //
        //
        //
        newline();
        
        match(END);
        return tree;
    }
    
    public Lexeme defineObjectList() {
        Lexeme tree = new Lexeme(LIST);
        tree.left = defineObject();
        newline();
        if (defineObjectPending()) {
            tree.right = defineObjectList();
        }
        return tree;
    }
    
    // pending functions
    public boolean globalDefinePending() {
        return check(LOCAL) || check(FUNCTION) || check(OBJECT);
    }
    public boolean newlinePending() {
        return check(NEWLINE);
    }
    
    public boolean operatorPending() {
        return check(PLUS) || check(MINUS) || check(UMINUS) || check(TIMES) 
                || check(DIVIDES) || check(MODULUS) || check(EQ) || check(NEQ) 
                || check(GT) || check(LT) || check(GE) || check(LE) || check(ASSIGN) 
                || check(AND) || check(OR) || check(NOT) || check(DOT);
    }
    
    public boolean literalPending() {
        return check(INTEGER) || check(STRING) || check(NULL);
    }
    
    
    public boolean primaryPending() {
        return literalPending() || varExpressionPending() || getChrStatementPending()
                || strimStatementPending() || toIntStatementPending()
                || toStringStatementPending() || isDigitStatementPending() || lengthStatementPending()
                || powStatementPending() || check(OPAREN) || check(MINUS);
    }
    
    public boolean expressionPending() {
        return primaryPending();
    }
    
    public boolean statementPending() {
        return varExpressionPending() || ifStatementPending() 
            || iterStatementPending() || definitionPending() || flowStatementPending()
            || printStatementPending();
    }
    
    public boolean statementListPending() {
        return statementPending();
    }
    
    public boolean ifStatementPending() {
        return check(IF);
    }
    
    public boolean iterStatementPending() {
        return check(WHILE) || check(REPEAT);
    }
    
    public boolean whileStatementPending() {
        return check(WHILE);
    }
    
    public boolean definitionPending() {
        return check(LOCAL) || check(FUNCTION);
    }
    
    //
    // Object
    //
    public boolean defineObjectPending() {
        return check(OBJECT);
    }

    public boolean defineVariablePending() {
        return check(LOCAL);
    }
    
    public boolean varExpressionPending() {
        return check(VARIABLE);
    }

    public boolean paramListPending() {
        return check(VARIABLE);
    }

    public boolean defineFunctionPending() {
        return check(FUNCTION);
    }

    public boolean expressionListPending() {
        return expressionPending();
    }

    public boolean funcCallPending() {
        return check(OPAREN);
    }
    
    public boolean returnStatementPending() {
        return check(RETURN);
    }
    
    public boolean operatorPrimaryPending() {
        return check(DOT); //
                           // Parenthesis operators cause an error when added here
                           // if (a) -> causes an error
    }
    
    public boolean operatorUnaryPending() {
        return check(UMINUS);
    }

    public boolean operatorMultiplicativePending() {
        return check(TIMES) || check(DIVIDES) || check(MODULUS);
    }

    public boolean operatorAdditivePending() {
        return check(PLUS) || check(MINUS);
    }

    public boolean operatorRelationalPending() {
        return check(LT) || check(GT) || check(LE) || check(GE);
    }

    public boolean operatorEqualityPending() {
        return check(EQ) || check(NEQ);
    }

    public boolean operatorLogicalAndPending() {
        return check(AND);
    }

    public boolean operatorLogicalOrPending() {
        return check(OR);
    }
    
    public boolean operatorAssignmentPending() {
        return check(ASSIGN);
    }
    
    public boolean flowStatementPending() {
        return check(PASS) || check(BREAK) || check(RETURN);
    }
    
    public boolean printStatementPending() {
        return check(PRINT);
    }
    
    public boolean getChrStatementPending() {
        return check(GETCHR);
    }
    
    public boolean strimStatementPending() {
        return check(STRIM);
    }
    
    public boolean toIntStatementPending() {
        return check(TOINT);
    }
    
    public boolean toStringStatementPending() {
        return check(TOSTRING);
    }
    
    public boolean isDigitStatementPending() {
        return check(ISDIGIT);
    }
    
    public boolean lengthStatementPending() {
        return check(LENGTH);
    }
    
    public boolean powStatementPending() {
        return check(POWER);
    }
    
    public boolean elseStatementPending() {
        return check(ELSE);
    }
}
