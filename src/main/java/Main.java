import Tokens.EOFToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static List<String> inputLines = new ArrayList<>();

    public static void getInput(){
        Scanner scanner = new Scanner(System.in);
        String newLine;

        while (scanner.hasNextLine()) {
            newLine = scanner.nextLine();
            if (newLine.isEmpty()) {
                break;
            }
            inputLines.add(newLine);
        }
    }

    public static void main(String[] args) throws Exception {
        getInput();
        Tokenization tokenizer = new Tokenization();
        tokenizer.tokenCheck();
        Parser parser = new Parser(tokenizer.getTokenList());

    }

}
