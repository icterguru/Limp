/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * keyword : "and" | "break" | "else" | "end" | "#f" | "for" | "function"
           | "if" | "local" | "not" | "or" | "pass" | "return" | "#t" | "while"
 */
package limp;

/**
 *
 * @author Michael Walker
 */
public class Keywords {
    public static boolean isKeyword(String str) {
        String[] keywords = {"and", "break", "else", "end", "#f", "for", "function",
                             "if", "local", "not", "null", "or", "pass", "print", "repeat", 
                             "return", "#t", "while"};
        for (String keyword : keywords)
            if (keyword.equals(str)) return true;
        return false;
    }
}
