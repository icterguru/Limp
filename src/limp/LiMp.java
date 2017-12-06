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
public class LiMp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length != 0) {
            String filename = args[0];
            
            Parser parser = new Parser(filename);
            Lexeme pt = parser.parse();
            
            Environment env = new Environment();
            Interpreter intr = new Interpreter();
            
            Lexeme nenv = env.create();
            
            intr.eval(pt, nenv);
            //env.printEnv(nenv);
            
        } else {
            System.out.println("File name not included in argument list.");
        }
    }
    
}
