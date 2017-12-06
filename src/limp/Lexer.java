/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

/**
 *
 * @author Michael Walker
 */
public class Lexer extends Types {
    PushbackReader pbr;
    int current_line = 1;
	//Lexeme prevLexeme;
    public Lexer (String filename) {
        try {
            pbr = new PushbackReader(new FileReader(filename));
        }
        catch (FileNotFoundException e) {
            System.out.println("Cannot find file " + filename);
        }
    }
    
    public Lexeme lex() {
        int ch;
        Lexeme n;
        skipWhitespace();
	ch = readCharacter();
        // return end of input once end of file is found
	if ((ch == -1) || (ch >= 65535)) {
            n = new Lexeme(ENDofFILE);
            n.lineError = current_line;
            return n;
        }
		
	 // set the previous char
        switch((char) ch) {
            // single character tokens
            case '(':
                n = new Lexeme(OPAREN);
                n.lineError = current_line;
                return n;
            case ')':
                n = new Lexeme(CPAREN);
                n.lineError = current_line;
                return n;
            case '{':
                n = new Lexeme(OBRACE);
                n.lineError = current_line;
                return n;
            case '}':
                n = new Lexeme(CBRACE);
                n.lineError = current_line;
                return n;
            case ',':
                n = new Lexeme(COMMA);
                n.lineError = current_line;
                return n;
            //case ';':
            //    n = new Lexeme(SEMI);
            //    n.lineError = current_line;
            //    return n;
            case '.':
                n = new Lexeme(DOT);
                n.lineError = current_line;
                return n;   
            case '+':
                n = new Lexeme(PLUS);
                n.lineError = current_line;
                return n;
            case '-':
                n = new Lexeme(MINUS);
                n.lineError = current_line;
                return n;
            case '*':
                n = new Lexeme(TIMES);
                n.lineError = current_line;
                return n;
            case '/':
                n = new Lexeme(DIVIDES);
                n.lineError = current_line;
                return n;
            case '%':
                n = new Lexeme(MODULUS);
                n.lineError = current_line;
                return n;
            case '!':
                ch = readCharacter();
                if (ch == '=') {
                    n = new Lexeme(NEQ);
                    n.lineError = current_line;
                    return n;
                }
                else {
                    unreadCharacter(ch);
                    n = new Lexeme(UNKNOWN);
                    n.lineError = current_line;
                    return n;
                }
            case '=':
                ch = readCharacter();
                if (ch == '=') {
                    n = new Lexeme(EQ);
                    n.lineError = current_line;
                    return n;
                } else {
                    unreadCharacter(ch);
                    n = new Lexeme(ASSIGN);
                    n.lineError = current_line;
                    return n;
		}
            case '>':
                ch = readCharacter();
                if (ch == '=') {
                    n = new Lexeme(GE);
                    n.lineError = current_line;
                    return n;
                } else {
                    unreadCharacter(ch);
                    n = new Lexeme(GT);
                    n.lineError = current_line;
                    return n;
		}
            case '<':
                ch = readCharacter();
                if (ch == '=') {
                    n = new Lexeme(LE);
                    n.lineError = current_line;
                    return n;
                } else { 
                    unreadCharacter(ch);
                    n = new Lexeme(LT);
                    n.lineError = current_line;
                    return n;
		}
            case '\n':
                n = new Lexeme(NEWLINE);
                n.lineError = current_line;
                current_line++;
                return n;
            default:
                // integers, variables/keywords, and strings
                if (Character.isDigit(ch)) {
                    unreadCharacter(ch);
                    return lexInteger();
                } else if (Character.isLetter(ch)) {
                    unreadCharacter(ch);
                    return lexIdentifier();
                } else if (ch == '\"') {
                    return lexString();
                } else {
                    n = new Lexeme(UNKNOWN);
                    n.lineError = current_line;
                    return n;
                }
        }
    }
    
    // return integer lexeme
    public Lexeme lexInteger() {
        int ch;
        String token = "";
        
        //if ((char) ch == '-') { // negative numbers
        //    token = token + (char) ch;
        //} else {
        //    unreadCharacter(ch);
        //}
        while (Character.isDigit((char) (ch = readCharacter()))) { // check if follows grammar
            token = token + (char) ch;
        }
        unreadCharacter(ch);
        Lexeme n = new Lexeme(INTEGER, Integer.parseInt(token));
        n.lineError = current_line;
        return n;
    }
    
    // return identifier lexeme
    public Lexeme lexIdentifier() {
        int ch = readCharacter();
        String token = "";
        
        while (Character.isLetter((char) ch) || Character.isDigit((char) ch)) {
            token = token + (char) ch;
            ch = readCharacter();
        }
        unreadCharacter(ch);
        
        Lexeme n;
        switch(token) {
            case AND:
                n = new Lexeme(AND);
                n.lineError = current_line;
                return n;
            case BREAK:
                n = new Lexeme(BREAK);
                n.lineError = current_line;
                return n;
            case ELSE:
                n = new Lexeme(ELSE);
                n.lineError = current_line;
                return n;
            case END:
                n = new Lexeme(END);
                n.lineError = current_line;
                return n;
            case POWER:
                n = new Lexeme(POWER);
                n.lineError = current_line;
                return n;
            case FOR:
                n = new Lexeme(FOR);
                n.lineError = current_line;
                return n;
            case FUNCTION:
                n = new Lexeme(FUNCTION);
                n.lineError = current_line;
                return n;
            case GETCHR:
                n = new Lexeme(GETCHR);
                n.lineError = current_line;
                return n;
            case IF:
                n = new Lexeme(IF);
                n.lineError = current_line;
                return n;
            case ISDIGIT:
                n = new Lexeme(ISDIGIT);
                n.lineError = current_line;
                return n;
            case LOCAL:
                n = new Lexeme(LOCAL);
                n.lineError = current_line;
                return n;
            case LENGTH:
                n = new Lexeme(LENGTH);
                n.lineError = current_line;
                return n;
            case NOT:
                n = new Lexeme(NOT);
                n.lineError = current_line;
                return n;
            case NULL:
                n = new Lexeme(NULL);
                n.lineError = current_line;
                return n;
            case OR:
                n = new Lexeme(OR);
                n.lineError = current_line;
                return n;
            case OBJECT:
                n = new Lexeme(OBJECT);
                n.lineError = current_line;
                return n;
            case PASS:
                n = new Lexeme(PASS);
                n.lineError = current_line;
                return n;
            case PRINT:
                n = new Lexeme(PRINT);
                n.lineError = current_line;
                return n;
            case STRIM:
                n = new Lexeme(STRIM);
                n.lineError = current_line;
                return n;
            case REPEAT:
                n = new Lexeme(REPEAT);
                n.lineError = current_line;
                return n;
            case RETURN:
                n = new Lexeme(RETURN);
                n.lineError = current_line;
                return n;
            case THIS:
                n = new Lexeme(THIS);
                n.lineError = current_line;
                return n;
            case TOINT:
                n = new Lexeme(TOINT);
                n.lineError = current_line;
                return n;
            case TOSTRING:
                n = new Lexeme(TOSTRING);
                n.lineError = current_line;
                return n;
            case WHILE:
                n = new Lexeme(WHILE);
                n.lineError = current_line;
                return n;
        }
        
        n = new Lexeme(VARIABLE, token);
        n.lineError = current_line;
        return n;
    }
    
    // return string lexeme
    public Lexeme lexString() {
        int ch = readCharacter();
        //String token = "\"";
        String token = "";
        
        while (ch != '\"') {
            token = token + (char) ch;
            ch = readCharacter();
        }
        //token = token + (char) ch; // obtain the closing quote
        Lexeme n = new Lexeme(STRING, token);
        n.lineError = current_line;
        return n;
    }
    // skip the whitespace from the source file (excluding newline)
    public void skipWhitespace() {
        // ascii representations of whitespace
        final int TAB = 9;
        final int NL = 10;
        final int CR = 13;
        final int ETB = 23;
        final int SPACE = 32;
        final int COMMENT = 59;
        boolean readcomment = false;
            
        int ch = readCharacter();
        while ((ch == TAB) || (ch == CR) || (ch == ETB) || (ch == SPACE) || (ch == COMMENT) || readcomment) {
            if (ch == COMMENT && readcomment == false)
                readcomment = true;
            
            ch = readCharacter();
            
            if (ch == NL) 
                readcomment = false;
        }
        unreadCharacter(ch);
    }
    
    // pulls a single character from the stream
    public int readCharacter() {
        try {
            int ch = pbr.read();
            return ch;
        }
        catch (IOException e) {
        }
        return -1;
    }
    
    // push the previously read character back onto the stream
    public void unreadCharacter(int ch) {
        try {
            pbr.unread((char) ch);
        }
        catch (IOException e) {
        }
    }
}
