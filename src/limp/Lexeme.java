/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limp;

/**
 *
 * @author Michael Walker
 */
public class Lexeme extends Types {
    String type;
    String str;
    int val;
    
    Lexeme left = null;
    Lexeme right = null;
    
    int lineError;
    
    // empty lexemes
    public Lexeme (String type) {
        this.type = type;
    }
    
    // single-character lexemes
    public Lexeme (String type, int val) {
        this.type = type;
        this.val = val;
    }
    
    // multi-character lexemes
    public Lexeme (String type, String val) {
        this.type = type;
        this.str = val;
    }
}
