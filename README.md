## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

-   `src`: the folder to maintain sources
-   `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

<!--  -->

```js

    parser.evaluar("25 * sin(23.6+e)- logB(10, sen(pi))");
    Expr -> Term E1
        E1 -> Expr | λ

    Term -> Fact T1
        T1 -> * Term | / Term | λ

    Fact -> ID | NUM | IDFunc(Expr, Expr) | - Term | + Term
```

```java
    float expresion() {
        float t = termino()
        float s = e1(t)
        return s
    }

    float e1(float a) {
        float s = 0
        if (analex.preanalisis().contains("id, num ,idfunc, - , +, *, /, λ")) {
            s = a + expresion()
        }

        return s
    }

    void termino() {
        float a = factor()
        float p = t1(a)
        return p
    }

    float t1(float a) {
        float b
        if (analex.preanalisis().contains("*")) {
            match("*")
            b = termino()
        } else if (analex.preanalisis().contains("/")) {
            match("/")
            b = 1/termino()
        } else {
            b = 1
            // nada
        }

        return a * b
    }

    float factor() {
        float factor
        if (analex.preanalisis().contains("ID")) {
            factor = analex.preanalisis().getValor()
            match("ID")
        } else if (analex.preanalisis().contains("NUM")) {
            factor = analex.preanalisis().getValor()
            match("NUM")
        } else if (analex.preanalisis().contains("IDFunc")) {
            String func = analex.preanalisis().getValor()
            match("IDFunc")
            match("(")
            float param1 = Expr()
            match(",")
            float param2 = Expr()
            match(")")

            factor = analex.preanalisis().getFunc(func ,param1, param2)
        } else if (analex.preanalisis().contains("-")) {
            match("-")
            factor = -Term()
        } else {
            match("+")
            factor = Term()
        }
        return factor
    }
```
