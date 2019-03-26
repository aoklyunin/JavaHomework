import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ClassFinderPredicateTest {


    @org.junit.jupiter.api.Test
    void test1() {
        Predicate<String> predicate = new ClassFinderPredicate("FB");
        testClassFinding(predicate, new String[]{"a.b.FooBarBaz", "c.d.FooBar"});

        predicate = new ClassFinderPredicate("FoBa");
        testClassFinding(predicate, new String[]{"a.b.FooBarBaz", "c.d.FooBar"});

        predicate = new ClassFinderPredicate("FBar");
        testClassFinding(predicate, new String[]{"a.b.FooBarBaz", "c.d.FooBar"});

        predicate = new ClassFinderPredicate("BF");
        testClassFinding(predicate, new String[]{});


        predicate = new ClassFinderPredicate("fbb");
        testClassFinding(predicate, new String[]{"a.b.FooBarBaz"});

        predicate = new ClassFinderPredicate("fBb");
        testClassFinding(predicate, new String[]{});

        predicate = new ClassFinderPredicate("FBar ");
        testClassFinding(predicate, new String[]{"c.d.FooBar"});


        predicate = new ClassFinderPredicate("B*rBaz");
        testClassFinding(predicate, new String[]{"a.b.FooBarBaz"});


        predicate = new ClassFinderPredicate("B*rB*z ");
        testClassFinding(predicate, new String[]{"a.b.FooBarBaz"});

        predicate = new ClassFinderPredicate("Y*u");
        testClassFinding(predicate, new String[]{
                "       YoureLeavingUsHere",
                "   YouveComeToThisPoint",
                "YourEyesAreSpinningInTheirSockets"
        });

        predicate = new ClassFinderPredicate("Sp*nI");
        testClassFinding(predicate, new String[]{
                "YourEyesAreSpinningInTheirSockets"
        });

        predicate = new ClassFinderPredicate("So ");
        testClassFinding(predicate, new String[]{
                "YourEyesAreSpinningInTheirSockets"
        });
    }

    @org.junit.jupiter.api.Test
    void testGetWordList() throws InvocationTargetException, IllegalAccessException {

        Method method = null;
        try {
            method = ClassFinderPredicate.class.getDeclaredMethod("getWordList", String.class);
        } catch (NoSuchMethodException e) {
            System.out.println("error getting doesTheWordStartWithAnother method");
        }

        assert method != null;

        method.setAccessible(true);

        Object ret = method.invoke(null, "FoBa");
        assertArrayEquals(((ArrayList<String>) ret).toArray(), new String[]{"Fo", "Ba"});

        ret = method.invoke(null, "GF");
        assertArrayEquals(((ArrayList<String>) ret).toArray(), new String[]{"G", "F"});

        ret = method.invoke(null, "GfdF");
        assertArrayEquals(((ArrayList<String>) ret).toArray(), new String[]{"Gfd", "F"});

    }


    @org.junit.jupiter.api.Test
    void testDoesTheWordStartWithAnother() throws InvocationTargetException, IllegalAccessException {
        Method method = null;
        try {
            method = ClassFinderPredicate.class.getDeclaredMethod("doesTheWordStartWithAnother", String.class, String.class);
        } catch (NoSuchMethodException e) {
            System.out.println("error getting doesTheWordStartWithAnother method");
        }

        assert method != null;

        method.setAccessible(true);

        Object ret = method.invoke(null, "Foo", "F");
        assertEquals(true, ret);

        ret = method.invoke(null, "Footr", "Fo");
        assertEquals(true, ret);

        ret = method.invoke(null, "Footr", "F*o");
        assertEquals(true, ret);

        ret = method.invoke(null, "Foo", "F*");
        assertEquals(true, ret);

        ret = method.invoke(null, "Foo", "ffo");
        assertEquals(false, ret);
    }

    private void testClassFinding(Predicate<String> predicate, String[] arr) {
        //System.out.println(Arrays.toString(Arrays.stream(classNames).filter(predicate).toArray(String[]::new)));
        assertArrayEquals(Arrays.stream(classNames).filter(predicate).toArray(String[]::new), arr);
    }

    private final String[] classNames = new String[]{
            "a.b.FooBarBaz",
            "c.d.FooBar",
            "",
            "codeborne.WishMaker",
            "codeborne.MindReader",
            "",
            "TelephoneOperator",
            "ScubaArgentineOperator",
            "       YoureLeavingUsHere",
            "   YouveComeToThisPoint",
            "YourEyesAreSpinningInTheirSockets"
    };

}