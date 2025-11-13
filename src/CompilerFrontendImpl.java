public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    private Automaton makeSingleCharToken(char c) {
        AutomatonImpl a = new AutomatonImpl();
        a.addState(0, true, false);
        a.addState(1, false, true);
        a.addTransition(0, c, 1);
        return a;
    }
    @Override
    protected void init_lexer() {
        // TODO Auto-generated method stub
        lex = new LexerImpl();
        
        Automaton numAutomaton = new AutomatonImpl();
        // Define states
        numAutomaton.addState(0, true, false); // start state
        numAutomaton.addState(1, false, false);
        numAutomaton.addState(2, false, true); // accept state

        // Define transitions for NUM: [0-9]*\.[0-9]+
        for (char c = '0'; c <= '9'; c++) {
            numAutomaton.addTransition(0, c, 0); // loop on digits before
            numAutomaton.addTransition(1, c, 2); // digits after decimal
            numAutomaton.addTransition(2, c, 2); // loop on digits after decimal
        }
        numAutomaton.addTransition(0, '.', 1); // transition on decimal point
        lex.add_automaton(TokenType.NUM, numAutomaton);
        lex.add_automaton(TokenType.PLUS, makeSingleCharToken('+'));
        lex.add_automaton(TokenType.MINUS, makeSingleCharToken('-'));
        lex.add_automaton(TokenType.TIMES, makeSingleCharToken('*'));
        lex.add_automaton(TokenType.DIV, makeSingleCharToken('/'));
        lex.add_automaton(TokenType.LPAREN, makeSingleCharToken('('));
        lex.add_automaton(TokenType.RPAREN, makeSingleCharToken(')'));

        AutomatonImpl whiteSpace = new AutomatonImpl();
        whiteSpace.addState(0, true, true); // start and accept state
        for (char c : new char[] {' ', '\n', '\r', '\t'}) {
            whiteSpace.addTransition(0, c, 0); // loop on whitespace characters
        }
        lex.add_automaton(TokenType.WHITE_SPACE, whiteSpace);
        
        //throw new UnsupportedOperationException("Unimplemented method 'init_lexer'");
    }

}
