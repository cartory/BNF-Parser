package Expr;

public class Parser {
    private final PError error;
    private final Cinta cinta;
    private final Analex analex;

    public Parser() {
        error = new PError();
        cinta = new Cinta();
        analex = new Analex(cinta, error);
    }

    public boolean hayError() {
        return error.hayError();
    }

    public String getErrorMsj() {
        return error.getErrorMsj();
    }

    public float evaluar(String expresion) {
        error.init();
        cinta.init(expresion);
        analex.init();
        if (analex.preNom() == Token.FIN)
            return 0; // La expresion está vacía.

        return expresion(); // Llamar al símbolo inicial.
    }

    /**
     * Expr -> Term Expr1
     * 
     * @return float
     */
    private float expresion() {
        float t = termino();
        return Expr1(t);
    }

    /**
     * Expr1 -> expresion | λ
     * 
     * @return float
     */
    private final int[] CF_Expr1 = {
            Token.NUM, Token.IDFUNC, Token.MENOS, Token.MAS, Token.POR, Token.DIV,
    };

    boolean CF_Expr1_contains(int token) {
        for (int cf_token : CF_Expr1) {
            if (cf_token == token) {
                return true;
            }
        }
        return false;
    }

    private float Expr1(float a) {
        if (CF_Expr1_contains(analex.Preanalisis().getNom())) {
            float e = expresion();
            return a + e;
        }

        return a;
    }

    /**
     * termino -> factor term1
     * 
     * @return float
     */
    private float termino() {
        float a = factor();
        return term1(a);
    }

    /**
     * term1 -> * termino | / termino | λ
     * 
     * @return float
     */
    private float term1(float a) {
        float b;
        switch (analex.Preanalisis().getNom()) {
            case Token.POR:
                match(Token.POR);
                b = termino();
                break;
            case Token.DIV:
                match(Token.DIV);
                b = 1 / termino();
                break;
            default:
                b = 1;
                break;
        }

        return a * b;
    }

    /**
     * factor -> ID | NUM | IDFunc(Expr, Expr) | - termino | + termino
     * 
     * @return
     */

    private float getFunc(String funct, float num, float base) {
        if (funct.equals("sen")) {
            return (float) Math.sin(Math.PI / 180 * num);
        }

        if (funct.equals("cos")) {
            return (float) Math.cos(Math.PI / 180 * num);
        }

        if (funct.equals("sqrt")) {
            return (float) Math.sqrt(num);
        }

        return (float) (Math.log(num) / Math.log(base));
    }

    private float factor() {
        float f;
        switch (analex.Preanalisis().getNom()) {
            case Token.NUM:
                f = analex.Preanalisis().getAtr();
                match(Token.NUM);
                break;
            case Token.IDFUNC:
                String func = analex.lexema();

                match(Token.IDFUNC);
                match(Token.PA);

                float p1 = expresion();

                if (func.equals("logb")) {
                    match(Token.COMA);
                    float p2 = expresion();
                    f = getFunc(func, p1, p2);
                } else {
                    f = getFunc(func, p1, 0);
                }
                match(Token.PC);
                break;
            case Token.MENOS:
                match(Token.MENOS);
                f = -termino();
                break;
            default:
                match(Token.MAS);
                f = termino();
                break;
        }

        return f;
    }

    private void match(int nomToken) {
        match(nomToken, "Error de Sintaxis");
    }

    private void match(int nomToken, String msj) {
        if (analex.Preanalisis().getNom() == nomToken)
            analex.avanzar();
        else
            setError(msj);
    }

    private void setError(String msj) {
        error.setError(msj, analex.getPosLexema(), analex.lexema());
    }

    // ----------------- Para resaltar el Error en el Form
    // --------------------------
    public void comunicarError() {
        if (hayError())
            comunicarError(getErrorMsj(), error.getPosLexema(), error.getLexema());
    }

    public void comunicarError(String errorMsj, int pos, String lexema) {
        // Overridable.
    }
}
