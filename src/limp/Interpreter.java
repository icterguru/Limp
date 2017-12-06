/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limp;

/**
 *
 * @author seyuu_000
 */
public class Interpreter extends Environment {
    
    public Lexeme evalList(Lexeme tree, Lexeme env) {
        Lexeme result = null;
        //
        // Should return 'explicit' return values, however, for now simply
        // return the last statement evaluated.
        while (tree != null) {
            result = eval(tree.left, env);
            tree = tree.right;
            if (result != null) {
                if (result.type.equals(RETURN)) {
                    break;
                }
            }
        }
        
        return result;
    }
    
    public Lexeme evalOr(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        //
        // if the left hand side returns 'true', then return 'true'
        //
        if (left.type.equals(INTEGER) && left.val > 0) {
            return new Lexeme(INTEGER, 1);
        }
        
        Lexeme right = eval(tree.right, env);
        //
        // else, evaluate the right hand side
        //
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            int result;
            result = (right.val > 0) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else {
            System.out.println("Error: Cannot determine " + left.type + " OR " + right.type + "");
            System.out.println("\t Error found at line number: " + left.lineError);
            System.exit(0);
        }
        
        return null;
    }
    
    public Lexeme evalAnd(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        //
        // if the left hand side returns 'true', then return 'true'
        //
        if (left.type.equals(INTEGER) && left.val <= 0) {
            return new Lexeme(INTEGER, 0);
        }
        
        Lexeme right = eval(tree.right, env);
        //
        // else, evaluate the right hand side
        //
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            int result;
            result = (right.val > 0) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else {
            System.out.println("Error: Cannot determine " + left.type + " AND " + right.type + "");
            System.out.println("\t Error found at line number: " + left.lineError);
            System.exit(0);
        }
        
        return null;
    }
    
    public Lexeme evalAssign(Lexeme tree, Lexeme env) {
        // eval the right hand side
        //Lexeme value = eval(tree.right, env);
        //return update(tree.left, value, env);
        Lexeme value;
        value = eval(tree.right, env);
        if (tree.left.type.equals(DOT)) {
            Lexeme object = eval(tree.left.left, env);
            update(tree.left.right, value, object);
        }
        else {
            update(tree.left, value, env);
        }
        
        return value;
    }
    
    public Lexeme evalLocalDefinition(Lexeme tree, Lexeme env) {
        return insert(tree.left, eval(tree.right, env), env);
    }
    
    public Lexeme evalBlock(Lexeme tree, Lexeme env) {
        Lexeme result = null;
        //
        // Should return 'explicit' return values, however, for now simply
        // return the last expression evaluated.
        while (tree != null) {
            result = eval(tree.left, env);
            tree = tree.right;
            if (result != null) {
                if (result.type.equals(RETURN)) {
                    break;
                }
            }
        }
            
        return result;
    }
    
    public Lexeme getFuncDefName(Lexeme tree) {
        Lexeme name = car(tree);
        return name;
    }
    
    public Lexeme getFuncCallName(Lexeme tree) {
        Lexeme name = car(tree);
        return name;
    } 
    
    public Lexeme getFuncCallArgs(Lexeme tree) {
        Lexeme args = cdr(tree);
        //args = cons(JOIN, args, null);
        //
        //
        //
        return args;
    }
    
    public Lexeme getClosureParams(Lexeme closure) {
        Lexeme pt = car(closure);
        Lexeme params = cadr(pt);
        //params = cons(JOIN, params, null);
        //
        //
        //
        return params;
    }
    
    public Lexeme getClosureBody(Lexeme closure) {
        
        //Lexeme closure = cons(CLOSURE, tree, env);
        //return insert(getFuncDefName(tree), closure, env);
        
        Lexeme pt = car(closure);
        Lexeme body = cddr(pt);
        return body;
    }
    
    public Lexeme getClosureEnvironment(Lexeme closure) {
        Lexeme closureEnv = cdr(closure);
        return closureEnv;
    }
    
    public Lexeme evalArguments(Lexeme args, Lexeme env) {
        Lexeme nenv = create();
        Lexeme result;
        while(args != null) {
            result = eval(args.left, env);
            insert(result, null, nenv);
            args = args.right;
        }
        return car(nenv);
    }
    
    public Lexeme evalParameters(Lexeme params, Lexeme env) {
        Lexeme nenv = create();
        Lexeme result;
        while (params != null) {
            result = params.left;
            insert(result, null, nenv);
            params = params.right;
        }
        return car(nenv);
    }
    
    public Lexeme evalFuncDefinition(Lexeme tree, Lexeme env) {
        Lexeme closure = cons(CLOSURE, tree, env);
        return insert(getFuncDefName(tree), closure, env);
    }
    
    public Lexeme evalFuncCall(Lexeme tree, Lexeme env) {
        Lexeme closure = eval(getFuncCallName(tree), env);
        Lexeme args = getFuncCallArgs(tree);
        Lexeme params = getClosureParams(closure);
        Lexeme body = getClosureBody(closure);
        Lexeme senv = getClosureEnvironment(closure);
        Lexeme eargs = evalArguments(args, env);
        params = evalParameters(params, env);
        //
        // eval parameters does not actually evaluate the parameters, it
        // simply puts the parameters into the "JOIN" format
        //
        Lexeme xenv = extend(params, eargs, senv);
        //
        // Fix: Parameter count and argument count must match!
        //
        Lexeme result = eval(body, xenv);
        if (result != null) {
            if (result.type.equals(RETURN)) {
                result = result.right;
            }
        }
        return result;//eval(body, xenv);
    }
    
    public Lexeme evalIfStatement(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        //
        // Error:
        //
        //
        if (left == null) {
            System.out.println("Error: Expression could not be evaluated.");
            System.out.println("\t Error on line number: " + tree.lineError);
            System.exit(0);
        }
        
        if (left.type.equals(INTEGER)) { // type mismatch
            if (left.val == 1) {
                return eval(cadr(tree), env);
            } else {
                return eval(cddr(tree), env); // evaluate the optional else
            }
        } else {
            System.out.println("Error: expression is a " + left.type + " should be an INTEGER");
            System.out.println("\t Error found at line number: " + tree.lineError);
            System.exit(0);
        }
        
        return null;
    }
    
    public Lexeme evalWhileStatement(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme result;
        
        if (left == null) {
            System.out.println("Error: Expression could not be evaluated.");
            System.out.println("\t Error on line number: " + tree.lineError);
            System.exit(0);
        }
        
        if (left.type.equals(INTEGER)) { // type mismatch
            if (left.val == 1) {
                result = eval(tree.right, env); //
                                                // evaluate the block
                if (evalWhileStatement(tree, env) == null) {
                    return result;
                }      
            } 
        } else {
            System.out.println("Error: expression is a " + left.type + " should be an INTEGER");
            System.out.println("\t Error found at line number: " + left.lineError);
            System.exit(0);
        }
        
        return null; 
    }
    
    public Lexeme evalRepeatStatement(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme result;
        
        if (left == null) {
            System.out.println("Error: Expression could not be evaluated.");
            System.out.println("\t Error on line number: " + tree.lineError);
            System.exit(0);
        }
        
        if (left.type.equals(INTEGER)) { // type mismatch
            if (left.val > 0) {
                result = eval(tree.right, env); //
                                                // evaluate the block
                //
                // Udpate the expression
                //
                // The expression must be decremented
                //
                Lexeme expression = new Lexeme(INTEGER, left.val - 1);
                setCar(tree, expression);
                if (evalRepeatStatement(tree, env) == null) {
                    return result;
                }      
            } 
        } else {
            System.out.println("Error: expression is a " + left.type + " should be an INTEGER");
            System.out.println("\t Error found at line number: " + left.lineError);
            System.exit(0);
        }
        
        return null; 
    }
    
    public Lexeme evalOperator(Lexeme tree, Lexeme env) {
        if (tree.type.equals(PLUS)) return evalPlus(tree, env);
        if (tree.type.equals(MINUS)) return evalMinus(tree, env);
        if (tree.type.equals(TIMES)) return evalMultiply(tree, env);
        if (tree.type.equals(DIVIDES)) return evalDivide(tree, env);
        if (tree.type.equals(UMINUS)) return evalNegate(tree, env);
        if (tree.type.equals(GT)) return evalGT(tree, env);
        if (tree.type.equals(GE)) return evalGE(tree, env);
        if (tree.type.equals(LT)) return evalLT(tree, env);
        if (tree.type.equals(LE)) return evalLE(tree, env);
        if (tree.type.equals(EQ)) return evalEQ(tree, env);
        if (tree.type.equals(NEQ)) return evalNEQ(tree, env);
        if (tree.type.equals(AND)) return evalAnd(tree, env);
        if (tree.type.equals(OR)) return evalOr(tree, env);
        return null;
    }
    
    public Lexeme evalPlus(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) { // add integers
            return new Lexeme(INTEGER, left.val + right.val);
        }
        else if (left.type.equals(INTEGER) && right.type.equals(STRING)) {
            return new Lexeme(STRING, Integer.toString(left.val).concat(right.str));
        }
        else if (left.type.equals(STRING) && right.type.equals(INTEGER)) {
            return new Lexeme(STRING, left.str.concat(Integer.toString(right.val)));
        }
        else if (left.type.equals(STRING) && right.type.equals(STRING)) { // concatenate strings
            return new Lexeme(STRING, left.str.concat(right.str));
        }
        else {
            System.out.println("Error: " + left.type + " + " + right.type + " cannot be done");
            System.out.println("\t Error found at line number: " + left.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme evalMinus(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) // subtract integers
            return new Lexeme(INTEGER, left.val - right.val);
        System.out.println("Error: " + left.type + " - " + right.type + " cannot be done");
        System.out.println("\t Error found at line number: " + left.lineError);
        System.exit(0);
        return null;
    }
    
    public Lexeme evalMultiply(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) // multiply integers
            return new Lexeme(INTEGER, left.val * right.val);
        System.out.println("Error: " + left.type + " * " + right.type + " cannot be done");
        System.out.println("\t Error found at line number: " + left.lineError);
        System.exit(0);
        return null;
    }
    
    public Lexeme evalDivide(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) // divide integers
            return new Lexeme(INTEGER, left.val / right.val);
        System.out.println("Error: " + left.type + " / " + right.type + " cannot be done");
        System.out.println("\t Error found at line number: " + left.lineError);
        System.exit(0);
        return null;
    }
    
    public Lexeme evalNegate(Lexeme tree, Lexeme env) {
        Lexeme right = eval(tree.right, env);
        if (right.type.equals(INTEGER)) {
            return new Lexeme(INTEGER, -1 * right.val);
        }
        System.out.println("Error: " + "-" + right.type + " cannot be done");
        System.out.println("\t Error found at line number: " + right.lineError);
        System.exit(0);
        return null;
    }
    
    public Lexeme evalGT(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        int result;
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            result = (left.val > right.val) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(STRING) && right.type.equals(STRING)) {
            result = (left.str.length() > right.str.length()) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        return null;
    }
    
    public Lexeme evalGE(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        int result;
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            result = (left.val >= right.val) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(STRING) && right.type.equals(STRING)) {
            result = (left.str.length() >= right.str.length()) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        return null;
    }
    
    public Lexeme evalLT(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        int result;
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            result = (left.val < right.val) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(STRING) && right.type.equals(STRING)) {
            result = (left.str.length() < right.str.length()) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        return null;
    }
    
    public Lexeme evalLE(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        int result;
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            result = (left.val <= right.val) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(STRING) && right.type.equals(STRING)) {
            result = (left.str.length() <= right.str.length()) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        return null;
    }
    
    public Lexeme evalEQ(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        int result;
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            result = (left.val == right.val) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(STRING) && right.type.equals(STRING)) {
            result = (left.str.equals(right.str)) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(ENV) && right.type.equals(NULL)) {
            result = (left.type.equals(right.type)) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(NULL) && right.type.equals(NULL)) {
            result = (left.type.equals(right.type)) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        return null;
    }
    
    public Lexeme evalNEQ(Lexeme tree, Lexeme env) {
        // eval the left and right hand sides
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        int result;
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            result = (left.val != right.val) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(STRING) && right.type.equals(STRING)) {
            result = (!left.str.equals(right.str)) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(ENV) && right.type.equals(NULL)) {
            result = (!left.type.equals(right.type)) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        else if (left.type.equals(NULL) && right.type.equals(NULL)) {
            result = (!left.type.equals(right.type)) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        }
        return null;
    }
    
    public Lexeme evalReturn(Lexeme tree, Lexeme env) {
        Lexeme rtn = eval(tree.right, env);
        tree.right = rtn;
        return tree;
    }
    
    public Lexeme evalDot(Lexeme tree, Lexeme env) {
        Lexeme object = eval(tree.left, env);
        //
        //
        // This is a vital error: The bug is when trying to call
        // an object's function with an argument local to the calling environment.
        // This causes an error as we see below, the environment of the object 
        // is used instead. Therefore, variables cannot be used as arguments for objects
        //
        
        return eval(tree.right, object);
    }
    
    public Lexeme evalThis(Lexeme tree, Lexeme env) {
        return env;
    }
    
    public Lexeme evalPrint(Lexeme tree, Lexeme env) {
        Lexeme right = eval(tree.right, env);
        switch (right.type) {
            case INTEGER:
                System.out.print(Integer.toString(right.val));
                return tree;
            case STRING:
                if (right.str.equals("\\n"))
                    System.out.println("");
                else
                    System.out.print(right.str);
                return tree;
            default:
                System.out.println("Error: Cannot print a" + right.type + ".");
                System.out.println("\t Error found at line number: " + right.lineError);
                System.exit(0);
        }
        return tree;
    }
    
    public Lexeme evalGetChr(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        
        if (left.type.equals(STRING) && right.type.equals(INTEGER)) {
            String str = left.str;
            int index = right.val;
            if (index < 0 || index >= str.length()) {
                System.out.println("Error: Tried to access out of ranged index [" + Integer.toString(index) + "]");
                System.out.println("\t Error found at line number: " + left.lineError);
                System.exit(0);
                return null;
            } else {
                return new Lexeme(STRING, Character.toString(str.charAt(index)));
            }
        } else {
            System.out.println("Error: Cannot incorrect parameters, getchr( STRING , INTEGER ) ");
            System.out.println("\t Error found at line number: " + left.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme evalStringTrim(Lexeme tree, Lexeme env) {
        Lexeme right = eval(tree.right, env);
        
        if (right.type.equals(STRING)) {
            String str = right.str.substring(1);
            return new Lexeme(STRING, str);
        } else {
            System.out.println("Error: Cannot trim, incorrect parameters, strim( STRING ) ");
            System.out.println("\t Error found at line number: " + right.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme evalToInt(Lexeme tree, Lexeme env) {
        Lexeme right = eval(tree.right, env);
        if (right.type.equals(STRING)) {
            return new Lexeme(INTEGER, Integer.parseInt(right.str));
        } else {
            System.out.println("Error: Cannot convert " + right.type + " to Integer.");
            System.out.println("\t Error found at line number: " + right.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme evalToString(Lexeme tree, Lexeme env) {
        Lexeme right = eval(tree.right, env);
        if (right.type.equals(INTEGER)) {
            return new Lexeme(STRING, Integer.toString(right.val));
        } else {
            System.out.println("Error: Cannot convert " + right.type + " to String.");
            System.out.println("\t Error found at line number: " + right.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme evalIsDigit(Lexeme tree, Lexeme env) {
        Lexeme right = eval(tree.right, env);
        if (right.type.equals(STRING)) {
            if (right.str.equals(" "))
                return new Lexeme(INTEGER, 0);
            boolean test = Character.isDigit(right.str.charAt(0));
            int result = (test) ? 1 : 0;
            return new Lexeme(INTEGER, result);
        } else {
            System.out.println("Error: Cannot compare, incorrect parameters, isdigit( STRING ) " + " not " + right.type + ".");
            System.out.println("\t Error found at line number: " + right.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme evalLength(Lexeme tree, Lexeme env) {
        Lexeme right = eval(tree.right, env);
        if (right.type.equals(STRING)) {
            return new Lexeme(INTEGER, right.str.length());
        } else {
            System.out.println("Error: Cannot compare, incorrect parameters, length( STRING ) " + " not " + right.type + ".");
            System.out.println("\t Error found at line number: " + right.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme evalPower(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.right, env);
        
        if (left.type.equals(INTEGER) && right.type.equals(INTEGER)) {
            return new Lexeme(INTEGER, (int) Math.pow(left.val, right.val));
        } else {
            System.out.println("Error: Cannot compare, incorrect parameters, pow( INTEGER, INTEGER )");
            System.out.println("\t Error found at line number: " + right.lineError);
            System.exit(0);
            return null;
        }
    }
    
    public Lexeme eval(Lexeme tree, Lexeme env) {
        if (tree == null) return null;
        switch(tree.type) {
        // self evaluating
        case ENV:
            return tree;
        case INTEGER:
            return tree;
        case STRING:
            return tree;
        case FUNCTION:
            return tree;
        case LOCAL:
            return tree;
        case OBJECT:
            return tree;
        case PASS:
            return tree;
        case BREAK:
            return tree;
        case RETURN:
            //System.out.println("Eval the return");
            //return eval(tree.right, env);
            return evalReturn(tree, env);
        case THIS:
            return env;
        case PRINT:
            return evalPrint(tree, env);
        case POWER:
            return evalPower(tree, env);
        case GETCHR:
            return evalGetChr(tree, env);
        case STRIM:
            return evalStringTrim(tree, env);
        case TOINT:
            return evalToInt(tree, env);
        case TOSTRING:
            return evalToString(tree, env);
        case ISDIGIT:
            return evalIsDigit(tree, env);
        case LENGTH:
            return evalLength(tree, env);
        // find values of variables in the environment
        case VARIABLE:
            return lookup(tree, env);
        case DOT:
            return evalDot(tree, env);
        // parenthesized expression
        case OPAREN:
            return eval(tree.right, env);
        // operators (both sides evaluated)
        case PLUS:
        case MINUS:
        case TIMES:
        case DIVIDES:
        case UMINUS:
        case GT:
        case GE:
        case LT:
        case LE:
        case EQ:
        case NEQ:
        case AND:
        case OR:
            return evalOperator(tree, env);
        // assign operator evals rhs for sure
        //    lhs is a variable or a dot operation
        case ASSIGN:
            return evalAssign(tree, env);
        //case NEWLINE:
        //    return tree;
        case NULL:
            return tree;
        case IF:
            return evalIfStatement(tree, env);
        //case FOR:
        //    break;
        case BLOCK:
            return evalBlock(tree, env);
        case VAR_DEF:
            return evalLocalDefinition(tree, env);
        case FUNC_DEF:
            return evalFuncDefinition(tree, env);
        case FUNC_CALL:
            return evalFuncCall(tree, env);
        //case NOT:
        //    break;
        case REPEAT:
            return evalRepeatStatement(tree, env);
        //case TRUE:
        //    break;
        case WHILE:
            return evalWhileStatement(tree, env);
        case LIST:
            return evalList(tree, env);
        case PROGRAM:
            //
            // Do not evaluate the newline condition
            //
            return evalList(tree, env);
        default:
            System.out.println("Bad expression!");
            System.exit(0);
            return null;
        }
    }
}
