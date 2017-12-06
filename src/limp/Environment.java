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
public class Environment extends Types {
    
    public void printEnv(Lexeme env) {
        while (env != null) {
            Lexeme tmp;
            Lexeme tmpVar = car(env);
            Lexeme tmpVal = cadr(env);
            
            // print the variable
            while (tmpVar != null && tmpVal != null) {
                if ((tmp = car(tmpVar)) != null) {
                    System.out.print("Variable: " + tmp.str + " | ");
                }
                if ((tmp = car(tmpVal)) != null) {
                    switch (tmp.type) {
                        case INTEGER:
                            System.out.print("Value: " + Integer.toString(tmp.val) + "\n");
                            break;
                        case STRING:
                            System.out.println("Value: " + tmp.str + "\n");
                            break;
                        case CLOSURE:
                            Lexeme pt = tmp.left;
                            if (pt.right.left != null)
                                System.out.println("Value: " + tmp.type + "\n");
                            else
                                System.out.println("Value: empty\n");
                            //printEnv()
                            break;
                        default:
                            break;
                    }
                } 
                tmpVar = tmpVar.right;
                tmpVal = tmpVal.right;
            }
            
            env = cddr(env); // move to the next environment
        }
        
        System.out.println("No more environments to explore.");
    }
    
    public Lexeme create() {
        return cons(ENV, null, cons(VALUES, null, null));
    }
    
    public Lexeme extend(Lexeme variables, Lexeme values, Lexeme env) {
        return cons(ENV, variables, cons(VALUES, values, env));
        //
        // A new environment is created, populated with the local parameters
        // and values, and finally pointed to the defining environment.
    }
    
    public Lexeme insert(Lexeme variable, Lexeme value, Lexeme env) {
        setCar(env, cons(JOIN, variable, car(env)));
        setCar(cdr(env), cons(JOIN, value, cadr(env)));
        return value;
    }
    
    public Lexeme lookup(Lexeme pt, Lexeme env) {
        String variable = pt.str;
        while (env != null) {
            Lexeme vars = car(env);
            Lexeme vals = cadr(env);
            while (vars != null) {
                if (sameVariable(variable, car(vars))) {
                    return car(vals);
                }
                vars = cdr(vars);
                vals = cdr(vals);
            }
            env = cddr(env);
        }
        
        System.out.println("Error: variable " + "\"" + variable + "\"" +  " is undefined");
        System.out.println("\t Error on line number: " + pt.lineError);
        System.exit(0);
        return null;
    }
    
    public Lexeme update(Lexeme pt, Lexeme elem, Lexeme env) {
        String variable = pt.str;
        while (env != null) {
            Lexeme vars = car(env);
            Lexeme vals = cadr(env);
            while (vars != null) {
                if (sameVariable(variable, car(vars))) {
                    return setCar(vals, elem); // set the left node
                    //
                    // returns the element inserted
                }
                vars = cdr(vars);
                vals = cdr(vals);
            }
            env = cddr(env);
        }
        
        System.out.println("variable " + "\"" + variable + "\"" + " is undefined and cannot be updated");
        System.exit(0);
        return null;
    }
    
    
    public boolean sameVariable(String variable, Lexeme varTree) {
        return variable.equals(varTree.str);
    }
    
    public Lexeme setCar(Lexeme env, Lexeme elem) {
        env.left = elem;
        return elem;
    }
    
    public Lexeme cons(String type, Lexeme variables, Lexeme values) {
        Lexeme elem = new Lexeme(type);
        elem.left = variables;
        elem.right = values;
        return elem;
    }
    
    public Lexeme car(Lexeme env) {
        return env.left;
    }
    
    public Lexeme cdr(Lexeme env) {
        return env.right;
    }
    
    public Lexeme cadr(Lexeme env) {
        return car(cdr(env));
    }
    
    public Lexeme cddr(Lexeme env) {
        return cdr(cdr(env));
    }
}
