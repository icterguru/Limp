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
public class Pretty extends Types {
    
    public static void print(Lexeme pt) {
        switch(pt.type) {
            // single character tokens
        case OPAREN:
            System.out.print("(");
            print(pt.right);
            System.out.print(")");
        break;
        case OBRACE:
            break;
        case CBRACE:
            break;
        case COMMA:
            print(pt.left);
            System.out.print(",");
            print(pt.right);
            break;
        case SEMI:
            System.out.print(";");
            break;
        case PLUS:
            print(pt.left); // primary
            System.out.print(" + ");
            print(pt.right); // expression
            break;
        case MINUS:
            print(pt.left); // primary
            System.out.print(" - ");
            print(pt.right); // expression
            break;
        case TIMES:
            print(pt.left); // primary
            System.out.print(" * ");
            print(pt.right); // expression
            break;
        case DIVIDES:
            print(pt.left); // primary
            System.out.print(" / ");
            print(pt.right); // expression
            break;
        case MODULUS:
            print(pt.left); // primary
            System.out.print(" / ");
            print(pt.right); // expression
            break;
        case ASSIGN:
            print(pt.left); // primary
            System.out.print(" = ");
            print(pt.right); // expression
            break;
        case EQ:
            print(pt.left); // primary
            System.out.print(" == ");
            print(pt.right); // expression
            break;
        case GT:
            print(pt.left); // primary
            System.out.print(" > ");
            print(pt.right); // expression
            break;
        case GE:
            print(pt.left); // primary
            System.out.print(" >= ");
            print(pt.right); // expression
            break;
        case LT:
            print(pt.left); // primary
            System.out.print(" < ");
            print(pt.right); // expression
            break;
        case LE:
            print(pt.left); // primary
            System.out.print(" <= ");
            print(pt.right); // expression
            break;
        case NEWLINE:
            break;
        case INTEGER:
            System.out.print(pt.val);
            System.out.print(" ");
            break;
        case THIS:
            System.out.print("this ");
            System.out.print(" ");
            break;
        case STRING:
            System.out.print(pt.str);
            break;
        case VARIABLE:
            System.out.print(pt.str);
            break;
        // keywords
        case AND:
            print(pt.left); // primary
            System.out.print(" and ");
            print(pt.right); // expression
            break;
        case BREAK:
            System.out.print(" break ");
            break;
        case FOR:
            break;
        case FUNCTION:
            System.out.print("function ");
            break;
        case IF:
            System.out.print("if ");
            System.out.print("(");
            print(pt.left); // expression
            System.out.print(") ");
            print(pt.right); // block, or ifStatement
            System.out.print("end ");
            break;
        case LOCAL:
            System.out.print("local ");
            break;
        case OR:
            print(pt.left); // primary
            System.out.print(" or ");
            print(pt.right); // expression
            break;
        case NOT:   // ***************
            break;
        case PASS:
            System.out.print("pass ");
            print(pt.right);
            break;
        case REPEAT:
            System.out.print("repeat ");
            System.out.print("(");
            print(pt.left); // expression
            System.out.print(") ");
            print(pt.right); // block
            System.out.print("end ");
            break;
        case RETURN:    // ***********
            break;
        case WHILE:
            System.out.print("while ");
            System.out.print("(");
            print(pt.left); // expression
            System.out.print(") ");
            print(pt.right); // block
            System.out.print("end ");
            break;
        case STATEMENT_LIST:
            print(pt.left);
            if (pt.right != null)
                print(pt.right);
            break;
        case PROGRAM:
        case STATEMENT:
        case FUNC_CALL:
        case BLOCK:
            print(pt.left);
            print(pt.right);
            break;
                
        default:
            System.out.println("Bad expression!");
        break;
        }
    }
}
