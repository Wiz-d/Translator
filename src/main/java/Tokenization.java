import Tokens.*;

import java.util.ArrayList;
import java.util.List;

public class Tokenization {

    private List<Token> tokenList = new ArrayList<Token>();

    private final String keywords = " or div mod and not Read Write ";

    private final String specialSymbols = "();+_*";

    private int row = 0;
    private int col = 0;
    private char ch = Main.inputLines.get(0).charAt(0);
    private boolean isReading = true;
    private StringBuilder strBuilder = new StringBuilder();

    private void readNextChar(){
        col++;
        if(col == Main.inputLines.get(row).length()){
            row++;
            col = 0;
        }
        if(row >= Main.inputLines.size()){
            isReading = false;
            ch = ' ';
            tokenList.add(new EOFToken("eof"));
            return;
        }
        ch = Main.inputLines.get(row).charAt(col);
    }

    public void tokenCheck(){
        while(isReading){

            if (ch>='a' && ch<='z' || ch>='A' && ch<='Z' || ch=='_'){

                while(ch>='a' && ch<='z' || ch>='A' && ch<='Z' || ch=='_' || ch>='0' && ch<='9'){
                    strBuilder.append(ch);
                    readNextChar();
                }

                String id = strBuilder.toString();

                if(keywords.contains(" "+id+" ")){
                    Token tok = new KeywordToken(strBuilder.toString());
                    tokenList.add(tok);
                    strBuilder.setLength(0);
                }else{
                    Token tok = new IdentToken(strBuilder.toString());
                    tokenList.add(tok);
                    strBuilder.setLength(0);
                }


            } else if(ch>='0' && ch<='9'){
                while(ch>='0' && ch<='9'){
                    strBuilder.append(ch);
                    readNextChar();
                }
                Token tok = new NumberToken(strBuilder.toString());
                tokenList.add(tok);
                strBuilder.setLength(0);

            }
            else if(ch == ' '){
                readNextChar();
            }
            else if(ch == ':'){
                strBuilder.append(ch);
                readNextChar();
                if(ch == '='){
                    strBuilder.append(ch);
                    Token tok = new SpecSymbolToken(strBuilder.toString());
                    tokenList.add(tok);
                    strBuilder.setLength(0);
                    readNextChar();
                }
            }
            if(specialSymbols.contains(Character.toString(ch))){
                strBuilder.append(ch);
                Token tok = new SpecSymbolToken(strBuilder.toString());
                tokenList.add(tok);
                strBuilder.setLength(0);
                readNextChar();
            }

        }

        for(Token token : tokenList){
            System.out.print(token);
        }

    }

    public List<Token> getTokenList() {
        return tokenList;
    }

}
