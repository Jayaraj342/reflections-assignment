package reflections.assignment;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Application {
    private static final List<String> userOperations = List.of(
            "Methods", "Class", "Subclasses", "Parent classes", "Constructors", "Data Members");

    public static void main(String[] args) throws ClassNotFoundException {
        final Scanner sc = new Scanner(System.in);

        System.out.println("Please enter the class with package name :");
        Class classEntered;
        try {
            classEntered = Class.forName(sc.next());
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Please enter correct class name with package!");
        }

        boolean exit = false;
        while (!exit) {
            char option = askUserForOperationToPerform(sc);
            if (option == 'x') {
                exit = true;
            } else {
                switch (option) {
                    case 'a':
                        System.out.println(Arrays.toString(classEntered.getMethods()));
                        break;
                    case 'b':
                        System.out.println(classEntered);
                        break;
                    case 'c':
                        printSubClasses(classEntered);
                        break;
                    case 'd':
                        System.out.println(classEntered.getSuperclass());
                        break;
                    case 'e':
                        printConstructors(classEntered);
                        break;
                    case 'f':
                        printDataMembers(classEntered);
                        break;
                    default:
                        break;
                }
            }
            System.out.println();
        }
        System.out.println("Exited successfully");
    }

    private static void printDataMembers(Class classEntered) {
        Field[] fields = classEntered.getFields();
        System.out.println(fields.length + " fields for '" + classEntered + "' as follows :-");
        for (Field field : fields) {
            System.out.println(field);
        }
    }

    private static void printConstructors(Class classEntered) {
        Constructor[] constructors = classEntered.getConstructors();
        System.out.println(constructors.length + " constructors for '" + classEntered + "' as follows :-");
        for (Constructor constructor : constructors) {
            System.out.println(constructor);
        }
    }

    private static void printSubClasses(Class classEntered) throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(classEntered));

        Set<BeanDefinition> components = provider.findCandidateComponents(classEntered.getPackageName());
        for (BeanDefinition component : components) {
            Class cls = Class.forName(component.getBeanClassName());
            if (classEntered.isAssignableFrom(cls) && classEntered != cls) {
                System.out.println(cls);
            }
        }
    }

    private static char askUserForOperationToPerform(Scanner sc) {
        boolean isValidOptionEntered = false;
        char option = 'a';

        System.out.println("Enter the option that you want to perform i.e. b/n 'a' and 'f' :");
        while (!isValidOptionEntered) {
            userOperations.forEach(operation ->
                    System.out.println((char) (userOperations.indexOf(operation) + 97) + ") " + operation)
            );
            System.out.println("x) Exit");
            System.out.println("-----------------------------------------");
            option = sc.next().charAt(0);
            if (option != 'x' && (option < 'a' || option > 'f')) {
                System.out.println("Please enter valid option!");
            } else {
                isValidOptionEntered = true;
            }
        }

        return option;
    }
}
