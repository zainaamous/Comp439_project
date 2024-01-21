
import javax.swing.event.MenuKeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class RecursiveDescent {
    //to store input tokens
    private List<String> tokens;
    private int curr;
//    private int lineNumber; // to keep track of the token we are dealing with
//    private int index; // to keep track of the index of the token we are dealing with

    // constructor that takes a list of tokens

    public RecursiveDescent(List<String> tokens) {
        this.tokens = tokens;
        this.curr = 0;
//        this.lineNumber = 1;

        System.out.println("tokens: "+tokens );
    }


//    since all non terminals should be converted into terminals which are the tokens
//    this means that the list of tokens size should equal the number of tokens in the code
//    since we add tokens to the list through the match() method


    public void parse() {
        try {
//            curr--;
            moduleDecl();
//            System.out.println(lineNumber + "line number");
            if (curr < tokens.size()) {
//                System.out.println("Error: Unexpected tokens at the end of input: " + tokens.subList(curr, tokens.size()));
//                System.exit(1);
            } else {
                System.out.println("Parsing is done successfully.");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    public void nextToken() {
        if (curr < tokens.size()) {
            curr++;


        }
    }


//    this match method is going to be used for terminals!!!
//    note that each non terminal is going to have a recursive method

    public void match(String str) {
//        str is the token that we are expecting to appear otherwise error
//        so first we need to check that our index tracker didnt exceed the size of the tokens list cause otherwise through error
//        then we check the token in the code equals the expected token because if it doesnt then syntax error
        if (curr < tokens.size() && tokens.get(curr).equals(str)) {
//            move to next token if no errors
            nextToken();
        }
//            this indicates that there is more tokens than expected
//        else if (curr < tokens.size()) {
//            System.out.println("Error: unexpected token: " + tokens.get(curr) + " at the end of input");
////            nextToken();
//        }
//wrong token
        else {
            System.out.println("Error: found: " + tokens.get(curr) + " expected: " + str);
//            nextToken();
        }


    }




//    module-decl  module-heading    declarations   procedure-decl   block    name  .


    private void moduleDecl() {

        moduleHeading();

            declarations();
            procedureDecl();
            block();
            name();
            match(".");


    }

    //    module-heading    module        name      ;
    private void moduleHeading() {

        if (tokens.get(curr).equals("module")) {
            match("module");
            name();
            if (tokens.get(curr).equals(";"))
                match(";");
            else
                System.out.println("Error: expected ';' but got: " + tokens.get(curr) +"at line:"+ Main.getlines(curr));
//            match(";");

        }
        else
        {
            System.out.println("Error: expected 'module' but got: " + tokens.get(curr) +"at line:"+ Main.getlines(curr));
        }
//        nextToken();

    }

    //block    begin        stmt-list         end
    private void block() {

        if(tokens.get(curr).equals("begin")){
            match("begin");
            stmtList();
            match("end");
        }
        else
        {
            System.out.println("Error: expected 'begin' but got: " + tokens.get(curr) +"at line:"+ Main.getlines(curr));
        }

//        nextToken();
    }

    //declarations     const-decl    var-decl
    private void declarations() {


        constDecl();
        varDecl();
//        nextToken();
    }

    // NOTE LAMBDA!!!!!
//
//const-decl   const    const-list     |       lammbda
    private void constDecl() {


        if (tokens.get(curr).equals("const")) {
            match("const");
            constList();
//            nextToken();

        } else {
//
        }

    }

    //const-list       ( name      =    value   ;  )*
//    notice how for each terminal we use the match method and for each non terminal we call the recursive method for it
    private void constList() {


       while (tokens.get(curr).matches("[a-zA-Z]"))
       {
           name();
           match("=");
           value();
           match(";");
//           nextToken();
       }

    }
//    var-decl   var    var-list     |      lambda

    private void varDecl() {


        if (tokens.get(curr).equals("var")) {
            match("var");
            varList();
//            nextToken();
        }
        else {
//            nextToken();
            System.out.println("Error: expected 'var' but got: " + tokens.get(curr) +"at line:"+ Main.getlines(curr) );

        }

    }


// GET BACK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    //    var-list     ( var-item     ;  )*
    private void varList() {

//        letter is the first(name) where name is first (nameList) where namelist is
 while (tokens.get(curr).matches("[a-zA-Z]"))

        {

        varItem();
        match(";");
//       nextToken();

      }

    }

    //    var-item     name-list     :     data-type
    public void varItem() {


        nameList();
        match(":");
        dataType();
//        nextToken();
    }

//    name-list     name    ( ,   name )*

    private void nameList() {

        name();
        while (tokens.get(curr).equals(",")) {

            match(",");
            name();
//            nextToken();

        }
//nextToken();
    }

    //    data-type     integer    |    real   |     char
    private void dataType() {

//        since integer, real , char are just terminals
        if ((tokens.get(curr).equals("integer")) ||
                (tokens.get(curr).equals("real")) || (tokens.get(curr).equals("char"))) {

            match(tokens.get(curr));
//            nextToken();
        }

        else{
            System.out.println("Error: data type expected, found: " + tokens.get(curr) +"at line:"+ Main.getlines(curr));

//             nextToken();

        }


    }

    //    procedure-decl     procedure-heading        declarations        block       name  ;
    private void procedureDecl() {

//        if (tokens.get(curr).equals("procedure")) {
//            match("procedure");
            procedureHeading();
            declarations();
            block();
            name();
            match(";");

    }

    //    procedure-heading    procedure        name      ;
//we have already checked if procedure exist in the procedureDecl method
    private void procedureHeading() {

        if(tokens.get(curr).equals("procedure")){
            match("procedure");
            Proname();
            match(";");
        }
        else
        {
            System.out.println("Error: expected 'procedure' but got: " + tokens.get(curr));
        }

//        nextToken();
    }

    //    stmt-list      statement    ( ;   statement )*
    private void stmtList() {

        statement();
        while (tokens.get(curr).equals(";")) {
            match(";");
            statement();
        }
//        nextToken();
    }
//    statement   ass-stmt   |    read-stmt    |    write-stmt    |      if-stmt
//                                  |  while-stmt    |     repeat-stmt  |   exit-stmt   |   call-stmt    |   lambda

    private void statement() {

        if (tokens.get(curr).matches("[a-zA-Z]"))
            assStmt();
        else if (tokens.get(curr).equals("readint")||tokens.get(curr).equals("readreal")||
                tokens.get(curr).equals("readchar")|| tokens.get(curr).equals("readln")|| tokens.get(curr).equals("writeln")
        || tokens.get(curr).equals("writeint")||tokens.get(curr).equals("writereal")||
                tokens.get(curr).equals("writechar") )
            ioStmt();
        else if (tokens.get(curr).equals("begin"))
            block();
        else if (tokens.get(curr).equals("if"))
            ifStmt();
        else if (tokens.get(curr).equals("while"))
            whileStmt();
        else if (tokens.get(curr).equals("loop"))
            repeatStmt();
        else if (tokens.get(curr).equals("exit"))
            exitStmt();
        else if (tokens.get(curr).equals("call"))
            callStmt();
        else{
//            System.out.println("';' or 'end' or 'else' exprcted after the statment"+"STATMENT METHOD");
//            nextToken();

        }




    }


//    ass-stmt  name     :=      exp
//    since name was already checked in the previous method
    private void assStmt()
    {

        name();
        match(":=");
        exp();
//        nextToken();
    }

//    exp  term     (  add-oper    term  )*
    private void exp()
    {

        term();
//        in this while the + and - are just the first of the addoper();
        while (tokens.get(curr).equals("+") || tokens.get(curr).equals("-"))
        {
         addOper();
         term();
        }
//        nextToken();
    }
//    term  factor   ( mul-oper   factor )*
    private void term()
    {

        factor();
        while (tokens.get(curr).equals("*") || tokens.get(curr).equals("/") ||
                tokens.get(curr).equals("mod")|| tokens.get(curr).equals("div"))
        {
            mulOper();
            factor();
        }

    }
//    factor   “(“     exp     “)”     |     name      |      value
    private void factor()
    {

        if(tokens.get(curr).equals("(")){
            match("(");
            exp();
            match(")");
        }
        else if (tokens.get(curr).matches("[a-zA-Z]"))
            name();
        else if (tokens.get(curr).matches("\\d+"))
            value();
        else
        {
            System.out.println("Error: expected: 'name' or 'value' or '(', but found :"  + tokens.get(curr) +"at line:"+ Main.getlines(curr));




        }


    }

//    add-oper   +    |     -
    private void addOper()
    {

        if (tokens.get(curr).equals("+")|| tokens.get(curr).equals("-"))
        {
            match(tokens.get(curr));
        }
        else {
            System.out.println("Error: expected operation '-' or '+' , but found:" + tokens.get(curr) +"at line:"+ Main.getlines(curr));

        }

    }
//    mul-oper  *     |     /       |      mod     |    div
    private void mulOper()
    {

        if(tokens.get(curr).equals("*")||tokens.get(curr).equals("/")|| tokens.get(curr).equals("mod")
        || tokens.get(curr).equals("div"))
            match(tokens.get(curr));
        else {
            System.out.println("Error: expected: '*', '/', 'mod' or 'div' , but found: " + tokens.get(curr) +"at line:"+ Main.getlines(curr));

        }

    }

//    read-stmt readint   “(“    name-list “)”     |  readreal   “(“    name-list  “)”
//                              |     readchar    “(“    name-list    “)”    |    readln
//  	write-stmt writeint  “(“  write-list “)”   |  writereal   “(“  write-list    “)”
//                               writechar  “(“    write-list “)”     |    writeln
//    SINCE THESE TWO PRODUCTION RULES SHARE THE SAME SYNTAX BUT DIFFERENT OPERANDS (READ, WRITE)
//    WE CAN WRITE THESE TWO PRODUCTION RULES IN ONE METHOD, LETS CALL IT IOSTMT SINCE WE ARE DEALING WITH I/O
    private void ioStmt()
    {

        if(tokens.get(curr).equals("readint")||tokens.get(curr).equals("readreal")||
                tokens.get(curr).equals("readchar"))
        {
            match(tokens.get(curr));
            match("(");
            nameList();
            match(")");
        }
        else if (tokens.get(curr).equals("readln"))
        {
            match("readln");
        }
        else if (tokens.get(curr).equals("writeint")||tokens.get(curr).equals("writereal")||
                tokens.get(curr).equals("writechar"))
        {
            match(tokens.get(curr));

            match("(");
            writeList();
            match(")");
        }
        else if (tokens.get(curr).equals("writeln"))
        {
            match("writeln");
        }
        else
        {
            System.out.println("Error: Expected: 'readint' , 'readreal', 'readchar', 'readln'" +
                    " 'writeint', 'writereal', 'writechar', 'writeln', but got: " + tokens.get(curr) +"at line:"+ Main.getlines(curr));


        }

    }
//    write-list     write-item    ( ,   write-item )*

    private void writeList()
    {

        writeItem();
        while (tokens.get(curr).equals(","))
        {
            match(",");
            writeItem();
        }

    }
//    write-item     name   |    value
    private void writeItem()
    {

        if(tokens.get(curr).matches("[a-zA-Z]"))
            name();
        else if (tokens.get(curr).matches("\\d+"))
            value();

    }
//    if-stmt  if  condition   then   stmt-list   elseif-part   else-part    end
    private void ifStmt()
    {

        if(tokens.get(curr).equals("if"))
    {
        match("if");
        condition();
        if(tokens.get(curr).equals("then"))
        {
            match("then");
            stmtList();
            elseifPart();
            elsePart();
            if(tokens.get(curr).equals("end"))
            match("end");
            else
            {
                System.out.println("Error: expected 'end' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));}
        }
        else {
            System.out.println("Error: expected 'then' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));}

    }
    else
    {
        System.out.println("Error: expected 'if' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));
    }

    }


//    elseif-part  ( elseif  condition   then   stmt-list  )*

    private void elseifPart()
    {

        while (tokens.get(curr).equals("elseif"))
        {
            match("elseif");
            condition();
            if(tokens.get(curr).equals("then") )
            match("then");
            else System.out.println("Error: expected 'then' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));

            stmtList();
        }

    }



//    else-part    else     stmt-list     |    lambda

    private void elsePart()
    {

        if(tokens.get(curr).equals("else")){
            match("else");
            stmtList();
        }
        else
          {
        }


    }
//    while-stmt  while      condition       do      stmt-list   end

    private void whileStmt()
    {

        if (tokens.get(curr).equals("while"))
        {
            match("while");
            condition();
            if(tokens.get(curr).equals("do"))
            match("do");
            else System.out.println("Error: expected 'do' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));
            stmtList();
            if(tokens.get(curr).equals("end"))
            match("end");
            else System.out.println("Error: expected 'end' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));

        }

    }

//    repeat-stmt     loop      stmt-list       until        condition


    private void repeatStmt()
    {

        if(tokens.get(curr).equals("loop")){
        match("loop");
        stmtList();
        if(tokens.get(curr).equals("until"))
        match("until");
        else System.out.println("Error: expected 'until' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));
        condition();

    }


    }
//    exit-stmt     exit
    private void exitStmt()
    {

        if(tokens.get(curr).equals("exit"))
            match("exit");
        else
            System.out.println("Error: expected 'exit' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));

//        nextToken();
    }

    //    call-stmt     call name          (*  This is a procedure name   *)
    private void callStmt() {

        String checkName="";
    if(tokens.get(curr).equals("call")) {
        match("call");

        String s = Return_name(checkName);// to check if the name is in the procedure list

        if(!Proname.contains(s))
        {
//            System.out.println("name after the call"+ s);
            System.out.println("Error: procedure name not found: "+tokens.get(curr) +"at line:"+ Main.getlines(curr));
        }
    }


    else
    {
        System.out.println("Error: expected 'call' but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));
    }



    }

//    condition    name-value       relational-oper        name-value

    private void condition()
    {

        nameValue();
        relationalOp();
        nameValue();
//        nextToken();

    }
//    name-value   name    |      value

    private void nameValue()
    {

        if(tokens.get(curr).matches("[a-zA-Z]"))
            name();
        else if (tokens.get(curr).matches("\\d+"))
            value();
        else {
            System.out.println("Error: expected name or value but found: "+tokens.get(curr)+"at line:"+ Main.getlines(curr));

        }



    }
//    relational-oper   =      |     |=    |    <     |       <=     |     >     |     >=

    private void relationalOp()
    {

        if(tokens.get(curr).equals("=")|| tokens.get(curr).equals("|=")|| tokens.get(curr).equals("<")||
                tokens.get(curr).equals("<=")|| tokens.get(curr).equals(">")||tokens.get(curr).equals(">="))
        {
            match(tokens.get(curr));
        }
            else
        {
            System.out.println("Error: Expected relational operator but found: " + tokens.get(curr)+"at line:"+ Main.getlines(curr));


        }


    }
//    name  letter ( letter | digit )*


    private void name() {

        String name="";
        if (tokens.get(curr).matches("[a-zA-Z]")) {
            name+=tokens.get(curr);
            nextToken();

            while (tokens.get(curr).matches("[a-zA-Z]") || tokens.get(curr).matches("\\d+")) {
                name+=tokens.get(curr);
                nextToken();
            }

        }else if (!(isValidName(name)))
        {
            System.out.println("Error: names cant be named same as reserved words " +"at line:"+ Main.getlines(curr));
        }
        else {
            System.out.println("Error: expected a name, found: " + tokens.get(curr)+"at line:"+ Main.getlines(curr));


        }

    }


    private String Return_name(String name) {

        if (tokens.get(curr).matches("[a-zA-Z]")) {
            name+=tokens.get(curr);
            nextToken();

            while (tokens.get(curr).matches("[a-zA-Z]") || tokens.get(curr).matches("\\d+")) {
                name+=tokens.get(curr);
                nextToken();
            }
//            match(tokens.get(curr));
        }else if (!(isValidName(name)))
        {
            System.out.println("Error: names cant be named same as reserved words " +"at line:"+ Main.getlines(curr));
        }
        else {
            System.out.println("Error: expected a name, found: " + tokens.get(curr)+"at line:"+ Main.getlines(curr));

//            curr++;//new
        }
        return name;
//        nextToken();
    }


    //    value  integer-value   |   real-value
    private void value()
    {

        if(tokens.get(curr).matches("\\d+"))
        integerValue();
        else if (tokens.get(curr).matches("\\d+"))
            realValue();
        else{
            System.out.println("Error: expected a value but got this instead: "+ tokens.get(curr)+"at line:"+ Main.getlines(curr));
//curr++;//new
        }
//nextToken();
    }
//    integer-value  digit ( digit )*
    private void integerValue()
    {


        if(tokens.get(curr).matches("\\d+")){
            match(tokens.get(curr));
            while (tokens.get(curr).matches("\\d+"))
                match(tokens.get(curr));
        }
        else{
            System.out.println("Error: expected an integer value but got this instead: "+ tokens.get(curr)+"at line:"+ Main.getlines(curr));
        }



//            matchDigit();
//        nextToken();
    }


//    real-value  digit ( digit )*. digit ( digit )*
    private void realValue()
    {

        if(tokens.get(curr).matches("\\d+")) {
            match(tokens.get(curr));
            while (tokens.get(curr).matches("\\d+"))
                match(tokens.get(curr));

            if (tokens.get(curr).equals(".")) {
                match(".");
                if (tokens.get(curr).matches("\\d+"))
                    match(tokens.get(curr));
                while (tokens.get(curr).matches("\\d+"))
                    match(tokens.get(curr));
            }
        }
        else{
            System.out.println("Error: expected a real value but got this instead: "+ tokens.get(curr)+"at line:"+ Main.getlines(curr));
        }



//nextToken();
    }
    private boolean isValidName(String name) {
        // Check if the name is not equal to any reserved words
        return !Main.reservedWords(name);
    }
//    this list is to store the names of the procedures
    List<String> Proname = new ArrayList<>();
    private void Proname() {

        String name="";
        if (tokens.get(curr).matches("[a-zA-Z]")) {
            name+=tokens.get(curr);
            nextToken();

            while (tokens.get(curr).matches("[a-zA-Z]") || tokens.get(curr).matches("\\d+")) {
                name+=tokens.get(curr);
                nextToken();
            }
        Proname.add(name);
//            System.out.println("Proname: "+Proname);
//            match(tokens.get(curr));
        }else if (!(isValidName(name)))
        {
            System.out.println("Error: names cant be named same as reserved words " +"at line:"+ Main.getlines(curr));
        }
        else {
            System.out.println("Error: expected a name, found: " + tokens.get(curr) +"at line:"+ Main.getlines(curr));


        }

    }



}