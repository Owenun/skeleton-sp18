package byog.Core;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class findRandom {

    public static final long seed = 19300;
    public static final Random RANDOM = new Random(seed);
//    public static void main(String[] args) {
//        int i = 0;
//        while (i < 50) {
//            System.out.println(RANDOM.(20));
//            System.out.print( RANDOM.nextInt(10));
//            System.out.print(RANDOM.ints());
//            System.out.println();
//            i++;
//        }
//    }

    public static void main(String[] args) {
        String regex = "^([nNlL])(\\d+)([sS]|:[qQ])$";
        String str = "n13385:k";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            String startLetter = matcher.group(1);
            String numbers = matcher.group(2);
            String afterNumbers = matcher.group(3);

            System.out.println("开头字母: " + startLetter);
            System.out.println("中间的数字: " + numbers);
            System.out.println("数字之后的部分: " + afterNumbers);
        }
    }
}
