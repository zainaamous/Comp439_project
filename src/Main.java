import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Use JFileChooser to select a file
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Choose a MODULA-2 file");
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            java.io.File file = fileChooser.getSelectedFile();

            try {
                // Read the contents of the file
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                String contentString = content.toString();
//                Tokenizer tokenizer = new Tokenizer(contentString);

                // Tokenize the content (including handling whitespace)
                List<String> tokens = tokenize(content.toString());

                // Create an instance of the RecursiveDescent parser
                RecursiveDescent parser = new RecursiveDescent(tokens);
                System.out.println("lines" + lines);

                // Parse the input
                parser.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean reservedWords(String token) {
        String[] reservedWords = {"module", "begin", "end", "const", "var", "integer", "real", "char",
                "procedure", "mod", "div", "readint", "readreal", "readchar", "readln",
                "writeint", "writereal", "writechar", "writeln", "if", "elseif", "then",
                "else", "while", "do", "loop", "until", "exit", "call"};
        return Arrays.asList(reservedWords).contains(token);
    }
    static List<Integer> lines = new ArrayList<>();
     public static int getlines(int index){
         return lines.get(index);
     }


    public static List<String> tokenize(String input) {
        String ff = "\\|";
         int line =1;
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        int currentIndex = 0;


        while (currentIndex < input.length()) {
            char currentChar = input.charAt(currentIndex);
            if (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n')
                    line++;
                currentIndex++;
                continue;
            }
            //          Check for whitespace and skip
//            if (Character.isWhitespace(currentChar)) {
//                currentIndex++;
//                continue;
//            }

            // Check for special characters
            if (currentChar == ';' || currentChar == ':' || currentChar == ',' || currentChar == '=' ||
                    currentChar == '<' || currentChar == '>' || currentChar == '(' || currentChar == ')' ||
                    currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/'|| currentChar == '.'){
                if (currentToken.length() > 0) {
                    addToken(tokens, currentToken.toString());
                    addLine(line);
                    currentToken.setLength(0);
                }

                    if (currentIndex + 1 < input.length() && input.charAt(currentIndex + 1) == '=') {
                        addToken(tokens, currentChar + "=");
                        addLine(line);
                        currentIndex += 2;
                        continue;
                    }



                // Tokenize relational operators ("<", "<=", ">", ">=", "=")
                if (currentChar == '<' || currentChar == '>' ||currentChar == ':' ) {
                    if (currentIndex + 1 < input.length() && input.charAt(currentIndex + 1) == '=') {
                        addToken(tokens, currentChar + "=");
                        addLine(line);
                        currentIndex += 2;
                        continue;
                    }
                }

                if (String.valueOf(currentChar).equals(ff)) {
                    if (currentIndex + 1 < input.length() && input.charAt(currentIndex + 1) == '=') {
                        addToken(tokens, ff + "=");
                        addLine(line);
                        currentIndex += 2;
                        continue;
                    }
                }
                // Tokenize real numbers
                if (currentChar == '.') {
                    // Check if the dot is preceded and followed by digits
                    if (currentIndex - 1 >= 0 && Character.isDigit(input.charAt(currentIndex - 1)) &&
                            currentIndex + 1 < input.length() && Character.isDigit(input.charAt(currentIndex + 1))) {
                        currentToken.append(currentChar);
                        currentIndex++;

                        // Continue appending digits after the dot
                        while (currentIndex < input.length() && Character.isDigit(input.charAt(currentIndex))) {
                            currentToken.append(input.charAt(currentIndex));
                            currentIndex++;
                        }
                        addLine(line);
                        addToken(tokens, currentToken.toString());
                        currentToken.setLength(0);
                        continue;
                    }
                }

                // Other special characters
                addToken(tokens, String.valueOf(currentChar));
                addLine(line);
                currentIndex++;
                continue;
            }

            // Check for letters (identifiers or reserved words)
            if (Character.isLetter(currentChar)) {
                while (currentIndex < input.length() && (Character.isLetterOrDigit(input.charAt(currentIndex)) || input.charAt(currentIndex) == '_')) {
                    currentToken.append(input.charAt(currentIndex));
                    currentIndex++;
                }

                // Check for reserved words
                if (reservedWords(currentToken.toString())) {
                    addToken(tokens, currentToken.toString());
                    addLine(line);// Tokenize reserved words as they are
                } else {
                    for (char letter : currentToken.toString().toCharArray()) {
                        addToken(tokens, String.valueOf(letter));
                        addLine(line);
                    }

                }

                currentToken.setLength(0);
//                System.out.println(tokens);
                continue;
            }

            // Check for numbers
            if (Character.isDigit(currentChar)) {
                while (currentIndex < input.length() && (Character.isDigit(input.charAt(currentIndex)) || input.charAt(currentIndex) == '.')) {
                    currentToken.append(input.charAt(currentIndex));
                    currentIndex++;
                }
                for (char digit : currentToken.toString().toCharArray()) {
                    addToken(tokens, String.valueOf(digit));
                    addLine(line);
                }


//                addToken(tokens, currentToken.toString());
                currentToken.setLength(0);
                continue;
            }

            // Invalid character
            throw new RuntimeException("Invalid character: " + currentChar);
        }

        // Add the last token if any
        if (currentToken.length() > 0) {
            addToken(tokens, currentToken.toString());
            addLine(line);
        }



        return tokens;
    }


    public static void addToken(List<String> tokens, String tokenValue) {
        // Add the token to the list
        tokens.add(tokenValue);
    }
    public static void addLine(int line){
         lines.add(line);
    }


}
