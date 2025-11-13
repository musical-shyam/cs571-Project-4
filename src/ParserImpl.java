
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        // TODO Auto-generated method stub
        // Skip leading whitespace
        skipWhiteSpace();
        Expr result = parseT();

        // Skip trailing whitespace
        skipWhiteSpace();

        if (tokens != null) {
            throw new Exception("Unexpected token after expression: " + tokens.elem.lexeme);
        }

        return result;
        //throw new UnsupportedOperationException("Unimplemented method 'do_parse'");
    }
    private Expr parseT() throws Exception {
        // TODO Auto-generated method stub
        skipWhiteSpace();
        Expr left = parseF();
        skipWhiteSpace();

        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Token op = consume(tokens.elem.ty);
            Expr right = parseT();
            if (op.ty == TokenType.PLUS) {
                return new PlusExpr(left, right);
            } 
            else {
                return new MinusExpr(left, right);
            }
        }
        return left;
        //throw new UnsupportedOperationException("Unimplemented method 'parseT'");
    }
    private Expr parseF() throws Exception {
        // TODO Auto-generated method stub
        skipWhiteSpace();
        Expr left = parseLit();
        skipWhiteSpace();

        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Token op = consume(tokens.elem.ty);
            Expr right = parseF();
            if (op.ty == TokenType.TIMES) {
                return new TimesExpr(left, right);
            }
            else {
                return new DivExpr(left, right);
            }
        }
        return left;
        //throw new UnsupportedOperationException("Unimplemented method 'parseF'");
    }
    private Expr parseLit() throws Exception {
        // TODO Auto-generated method stub

        skipWhiteSpace();
        if (tokens == null) {
            throw new Exception("Unexpected end of input while parsing literal");
        }
        if (peek(TokenType.NUM, 0)) {
            Token numToken = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(numToken.lexeme));
        }
        else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr innerExpr = parseT();
            consume(TokenType.RPAREN);
            return innerExpr;
        }
        else {
            throw new Exception("Unexpected token while parsing literal: " + tokens.elem.lexeme);
        }

        //throw new UnsupportedOperationException("Unimplemented method 'parseLit'");
    }
    private void skipWhiteSpace() {
        while (tokens != null && tokens.elem.ty == TokenType.WHITE_SPACE) {
            tokens = tokens.rest;
        }
    }
}



