import java.util.*;
import java.util.Scanner;


public class Main {

    private static final String NUMBER_REGEX = "[7-8]9[0-9]{9}";
    private static final String NAME_REGEX = "[А-яЁёA-z]{1,}[\\w\\s]*";
    private static final String LIST_COMMAND = "LIST";
    private static final String EXIT_COMMAND = "EXIT";

    private static Map<String, Set<String>> namesToNumbers = new TreeMap<>();
    public static void main(String[] args) {
        while (true) {
            String input = getInput();
            if (isValidNumber(input))
                handleInputNumber(input);
            else if (input.matches(LIST_COMMAND))
                printAll(namesToNumbers);
            else if (input.matches(EXIT_COMMAND))
                return;
            else if (input.matches(NAME_REGEX))
                handleInputName(input);
            else
                System.out.println("Неверный формат ввода");
        }
    }

    public static String getInput(){
        System.out.println("\nВведите номер телефона, имя контакта, команду LIST или EXIT");
        return new Scanner(System.in).nextLine().trim();
    }

    public static void handleInputName(String name){
        if(!namesToNumbers.containsKey(name)) {
            System.out.println("Такого имени в телефонной книге еще нет.\nВведите номер телефона для абонента \"" + name +"\": ");
            Set<String> numbers = new HashSet<>();
            do {
                String input = new Scanner(System.in).nextLine().trim();
                if (isValidNumber(input)) {
                    numbers.add(formatNumber(input));
                    namesToNumbers.put(name, numbers);
                    System.out.println("Контакт сохранен!");
                    return;
                }
                System.out.println("Это не номер телефона, введите правильный номер: ");
            }while(true);
        }
       for (String number : namesToNumbers.get(name))
         System.out.println(number);
    }

    public static void handleInputNumber(String number){
        String formattedNumber = formatNumber(number);
        boolean isContained = false;
        String name = "";
        for (Map.Entry<String, Set<String>> entry : namesToNumbers.entrySet())
            for (String value : entry.getValue()){
                if (value.equals(formattedNumber)){
                    isContained = true;
                    name = entry.getKey();
                    break;
                }
            }
        if (isContained) {
            System.out.println(name);
            return;
        }

        System.out.println("Такого номера в телефонной книге еще нет.\nВведите имя абонента для номера \"" + formattedNumber +"\": ");
        do {
            String newNumberName = new Scanner(System.in).nextLine().trim();
            Set<String> numbers;
            if (newNumberName.matches(NAME_REGEX)) {
                if (namesToNumbers.containsKey(newNumberName))
                    numbers = namesToNumbers.get(newNumberName);
                else
                    numbers = new HashSet<>();
                numbers.add(formattedNumber);
                namesToNumbers.put(newNumberName, numbers);
                System.out.println("Контакт сохранен!");
                return;
            }
            System.out.println("Ввод не соответствует формату имени, используйте, пожалуйста, только русский и англйиский алфавит и цифры 0-9 :");
        } while(true);
    }

    public static boolean isValidNumber(String number){
        String result = number.replaceAll("\\D+", "");
        if (result.length()==10)
            result = "7"+ result;
        return result.matches(NUMBER_REGEX);
    }

    public static String formatNumber(String number){
        String result = number.replaceAll("\\D+", "");
        if (result.length()==10)
            result = "7"+  result;
        if (result.startsWith("8"))
            result = "7" + result.substring(1);
        return result;
    }

    public static void printAll(Map<String, Set<String>> phonebook){
        if (phonebook.isEmpty())
            System.out.println("Справочник пуст!");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : phonebook.entrySet()){
           sb.append("\n" + entry.getKey() + " - ");
            for (String value : entry.getValue()) {
                sb.append(value + ", ");
            }
            sb.delete(sb.length()-2,sb.length());
        }
        System.out.print(sb.toString());
    }
}