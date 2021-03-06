package model.lexical_analyzer.nfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import model.lexical_analyzer.construction.LexicalRulesContainer;
import model.lexical_analyzer.graph.Graph;
import utilities.Constant;
import utilities.GraphUtility;
import utilities.NfaUtility;

public class Keyword {
    private HashMap<String, Graph> keywordNfa;

    public Keyword(LexicalRulesContainer rulesCont) {
        keywordNfa = new HashMap<String, Graph>();
        keywordToNfa(rulesCont);
    }

    private void keywordToNfa(LexicalRulesContainer lexicalRulesContainer) {

        for (int i = 0; i < lexicalRulesContainer.getKeywords().size(); i++) {
            String keyword = lexicalRulesContainer.getKeyword(i);
            String[] keywordCharacters = keyword.split("");
            ArrayList<String> characters = NfaUtility.addConcatSymbolToWords(keywordCharacters);
            ArrayList<String> postFixExpression = NfaUtility.infixToPostFix(characters);
            Graph nfa = createNfa(postFixExpression);
            keywordNfa.put(keyword, nfa);
            nfa.getDestination().setNodeTypes(keyword);

        }
    }

    private static Graph createNfa(ArrayList<String> expression) {
        // create a stack
        Stack<Graph> nfa = new Stack<Graph>();

        // Scan all characters one by one
        for (int i = 0; i < expression.size(); i++) {
            String currentExpression = expression.get(i);

            if (currentExpression.equals(Constant.CONCATENATE)) {
                Graph right = nfa.pop();
                Graph left = nfa.pop();
                nfa.push(GraphUtility.concatenate(left, right));
            } else {
                nfa.push(new Graph(currentExpression));
            }
        }
        return nfa.pop();
    }

    public HashMap<String, Graph> getKeywordNfa() {
        return keywordNfa;
    }

}
