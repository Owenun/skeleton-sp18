import java.util.SplittableRandom;

public class Palindrome {

    /* @param: word
    * return the Deque where the characters appear in the same order */
    public Deque<Character> wordToDeque(String word){
        Deque<Character> wordDeque = new LinkedListDeque<>();
        int length = word.length();
        int index = 0;
        while (index < length) {
            wordDeque.addLast(word.charAt(index));
            index += 1;
        }
        return wordDeque;
    }

    /* return true if word is a palindrome */
    public boolean isPalindrome(String word) {
        Deque wordDeque = wordToDeque(word);
        while (wordDeque.size() > 1) {
            if (!isEqualFLHelper(wordDeque)) return false;
        }
        return true;
    }

    /* the recursive version to implement isPalindrome */
    public boolean isPalindromeRecursion(String word) {
        Deque wordDeque = wordToDeque(word);
        return isPalindromeRecursionHelper(wordDeque);
    }

    private boolean isPalindromeRecursionHelper(Deque wordDeque) {
        if (wordDeque.size() <= 1) return true;
        return wordDeque.removeLast() == wordDeque.removeFirst() && isPalindromeRecursionHelper(wordDeque);

    }

    /* helper func which judge if wordDeque font equal to last */
    private boolean isEqualFLHelper(Deque wordDeque) {
        Character front = (Character) wordDeque.removeFirst();
        Character last = (Character) wordDeque.removeLast();
        return front == last;

    }

    /* return true if word is palindrome according the rule of cc */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque wordDeque = wordToDeque(word);
        while (wordDeque.size() > 1) {
            char front= (char) wordDeque.removeFirst();
            char last = (char) wordDeque.removeLast();
            if (!cc.equalChars(front, last)) return false;
        }
        return true;
    }
}
