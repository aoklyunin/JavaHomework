import java.io.IOException;
import java.util.function.Predicate;

import static java.lang.System.out;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

class ClassFinder {

    public static void main(String[] args) throws IOException {
//        String pattern = "'fb'";
//        String filename = "classes.txt";
//        args = new String[]{filename, pattern};

        Predicate<String> predicate = new ClassFinderPredicate(args[1].substring(1, args[1].length() - 1));
        readAllLines(get(args[0])).stream()
                .filter(predicate)
                .map(String::trim)
                .sorted()
                .forEach(out::println);
    }
}
