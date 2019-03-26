import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class ClassFinderPredicate implements Predicate<String> {

    ClassFinderPredicate(String pattern) {
        String mPattern;
        if (pattern.equals(pattern.toLowerCase()))
            mPattern = pattern.toUpperCase();
        else
            mPattern = pattern;

        mLastSpaceCharacter = mPattern.charAt(mPattern.length() - 1) == ' ';
        if (mLastSpaceCharacter)
            mPattern = mPattern.substring(0, mPattern.length() - 1);

        mPatternWords = getWordList(mPattern);
    }

    @Override
    public boolean test(String s) {
        if (s.isEmpty())
            return false;

        // get class name without spaces
        String pureClassName = s.indexOf('.') == -1 ? s : s.substring(s.lastIndexOf('.') + 1);
        String trimmed = pureClassName.trim();
        if (trimmed.isEmpty())
            return false;

        // split class name in words
        ArrayList<String> classWords = getWordList(trimmed);

        // count of prefix words can not by larger then count of passed classname words
        if (mPatternWords.size() > classWords.size()) {
            return false;
        }

        // if last word in class name and last word in pattern are different
        // and pattern ends with space, then this classname does not fit
        if (mLastSpaceCharacter) {
            if (!doesTheWordStartWithAnother(classWords.get(classWords.size() - 1), mPatternWords.get(mPatternWords.size() - 1))) {
                return false;
            }
        }

        // check if pattern fits
        int classWordsCnt = mLastSpaceCharacter ? classWords.size() - 1 : classWords.size();
        int patternWordsCnt = mLastSpaceCharacter ? mPatternWords.size() - 1 : mPatternWords.size();

        for (int i = 0; i <= classWordsCnt - patternWordsCnt; i++) {
            boolean flgPatternFits = true;
            for (int j = 0; j < patternWordsCnt; j++) {
                if (!doesTheWordStartWithAnother(classWords.get(i + j), mPatternWords.get(j)))
                    flgPatternFits = false;
            }
            if (flgPatternFits)
                return true;
        }


        return false;
    }

    // split camelcase class name in words
    private static ArrayList<String> getWordList(String className) {
        StringBuilder sb = new StringBuilder();
        sb.append(className.charAt(0));
        ArrayList<String> classWords = new ArrayList<>();

        for (int i = 1; i < className.length(); i++) {
            if (Character.isUpperCase(className.charAt(i))) {
                classWords.add(sb.toString());
                sb.setLength(0);
                //lastUpperCaseLetterIndex = i;
            }
            sb.append(className.charAt(i));
        }
        classWords.add(sb.toString());
        return classWords;
    }

    // check if one word starts with another ('*' symbol acceptable)
    private static boolean doesTheWordStartWithAnother(String wordA, String wordB) {
        boolean flgAccept = true;
        for (int i = 0; i < wordB.length(); i++) {
            if (wordB.charAt(i) != '*' && wordA.charAt(i) != wordB.charAt(i)) {
                flgAccept = false;
                break;
            }
        }
        return flgAccept;
    }

    private boolean mLastSpaceCharacter;
    private List<String> mPatternWords;
}